package org.fao.sws.model.configuration;

import static java.util.Collections.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import org.fao.sws.model.Dataset;
import org.fao.sws.model.Dimension;
import org.fao.sws.model.Domain;
import org.fao.sws.model.Flag;
import org.fao.sws.model.common.Group;
import org.fao.sws.model.configuration.Adapters.ContactsAdapter;
import org.fao.sws.model.configuration.Validators.Global;
import org.fao.sws.model.configuration.Validators.NotEmptyGroup;


public interface Configuration {

	
	Group<Dimension> dimensions();

	Group<Domain> domains();
	
	Group<Flag> flags();
	
	Group<Dataset> datasets();
	
	Configuration contact(String contact);
	
	Configuration with(Domain ... domains);
	
	Configuration with(Dimension ... dimensions);
	
	Configuration with(Flag ... flags);
	
	
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	@XmlRootElement(name="databaseConfiguration") 
	@EqualsAndHashCode @ToString
	class Default implements Configuration {
	
		@XmlAttribute(name="mailTo") @XmlJavaTypeAdapter(ContactsAdapter.class)
		@Getter 
		Set<String> contacts = new HashSet<>();
	
		
		@XmlElement
		@Valid @NotEmptyGroup(groups=Global.class,message="{no_shared_dimensions}")
		@Getter
		Group<Dimension> dimensions = new Group<>();
		
		
		@XmlElement
		@Valid @NotEmptyGroup(groups=Global.class,message="{no_shared_flags}")
		@Getter
		Group<Flag> flags = new Group<>();

		
		@XmlElement 
		@NotEmptyGroup(groups=Global.class,message="{no_shared_domains}") //no @Valid, validation boundary crossed explicitly in code
		@Getter
		Group<Domain> domains = new Group<>();
		
		
		
		/**
		 * All known datasets in a read-only group.
		 */
		@Override
		public Group<Dataset> datasets() {
			
			List<Dataset> datasets = unmodifiableList(new ArrayList<Dataset>());
			
			for (Domain domain : domains.all())
				datasets.addAll(domain.datasets().all());
			
			return new Group<>(datasets);
		}
		

		@Override
		public Configuration contact(String contact) {
			contacts.add(contact);
			return this;
		}
		
		/////////////////////////// //pro-fluency delegates: keeps up the builder pattern for inlined constructions

		@Override
		public Configuration with(Domain ... domains) {
			domains().with(domains);
			return this;
		}
		
		@Override
		public Configuration with(Dimension ... dimensions) {
			dimensions().with(dimensions);
			return this;
		}
		
		@Override
		public Configuration with(Flag ... flags) {
			flags().with(flags);
			return this;
		}
		
	}

}

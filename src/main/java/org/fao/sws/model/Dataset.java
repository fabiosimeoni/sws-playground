package org.fao.sws.model;

import java.util.Collection;

import javax.validation.Valid;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

import org.fao.sws.model.common.Entity;
import org.fao.sws.model.common.Group;
import org.fao.sws.model.configuration.Adapters.RefAdapter;


@XmlRootElement(name="dataSet") 
@NoArgsConstructor @EqualsAndHashCode(callSuper=true, exclude="boundDims") @ToString
public class Dataset extends Entity {

	public Dataset(String id) {
		super(id);
		sdmxCode = id; //default
	}
	
	
	@XmlAttribute(name="defaultEmptyRowsVisible")
	@Getter @Setter
	private boolean emptyRowsVisibile = false;
	
	@XmlAttribute
	@Getter @Setter
	private String sdmxCode;
	
	
	///////////////////////////////////////////////////////////////////   dimensions
	
	@Getter 
	private Group<Dimension> dimensions = new Group<>();
	
	
	//pro-fluency delegates: keeps up the builder pattern for inlined constructions
	public Dataset with(@NonNull Dimension ... dims) {
		
		dimensions.with(dims);
		
		return this;
	}
	
	
	
	///////////////////////////////////////////////////////////////////   observations
	
	@Valid @Getter @XmlElement(name="observation")
	private Observations observations;
	
	public Dataset with(@NonNull Observations observations) {
	
		this.observations = observations;
		
		return this;
	}
	
	
	// legacy format support via JAXB callback.
	// copies on binding to match both legacy format _and_ grouping facilities
	// you'd remove it entirely when/if legacy could be phased out.
	
	@XmlElement(name="dimension") @XmlJavaTypeAdapter(RefAdapter.class)
	private Collection<Dimension> boundDims;
	
	
	boolean beforeMarshal(Marshaller _) {
		boundDims = this.dimensions.all();
		return true;
	}
 
	void afterMmarshal(Marshaller _) {
		this.boundDims = null;
	}
	
	void afterUnmarshal(Unmarshaller _, Object __) {
		 this.dimensions = new Group<>(boundDims);
	}
	
}

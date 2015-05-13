package org.fao.sws.model;

import static lombok.AccessLevel.*;

import java.util.Collection;

import javax.validation.Valid;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import org.fao.sws.model.common.Group;
import org.fao.sws.model.configuration.Validators.NotEmptyGroup;


@XmlRootElement @NoArgsConstructor 

@Data
@EqualsAndHashCode(exclude="boundFlags") 
public class Observations {

	@Valid @NotEmptyGroup
	@Setter(NONE) 
	private Group<FlagRef> flags = new Group<>();
	
	public Observations with(@NonNull FlagRef ... flags) {
		
		this.flags.with(flags);
		
		return this;
	}
	
	// legacy format support via JAXB callback.
	// copies on binding to match both legacy format _and_ grouping facilities
	// you'd remove it entirely when/if legacy could be phased out.
	
	@XmlElementRef
	private Collection<FlagRef> boundFlags;
	
	
	boolean beforeMarshal(Marshaller _) {
		boundFlags = this.flags.all();
		return true;
	}
 
	void afterMmarshal(Marshaller _) {
		this.boundFlags = null;
	}
	
	void afterUnmarshal(Unmarshaller _, Object __) {
		 this.flags = new Group<>(boundFlags);
	}
}

package org.fao.sws.model;

import java.util.Collection;

import javax.validation.Valid;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import org.fao.sws.model.common.Entity;
import org.fao.sws.model.common.Group;


@XmlRootElement 
@NoArgsConstructor 
@EqualsAndHashCode(callSuper=true, exclude="bound") 
@ToString(callSuper=true)
public class Domain extends Entity<Domain> {
	
	@Valid @Getter 
	private Group<Dataset> datasets = new Group<>();
	
	public Domain(String id) {
		super(id);
	}
	
	
	//pro-fluency delegate: keeps up the builder pattern
	public Domain with(Dataset ... datasets) {
		this.datasets.with(datasets);
		return this;
	}
	
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	// legacy format support via JAXB callback.
	// copies on binding to match both legacy format _and_ grouping facilities
	// you'd remove it entirely when/if legacy could be phased out.
	
	@XmlElementRef
	private Collection<Dataset> bound;
	
	
	boolean beforeMarshal(Marshaller ignore) {
		bound = this.datasets.all();
		return true;
	}
 
	void afterMmarshal(Marshaller ignore) {
		this.datasets = null;
	}
	
	void afterUnmarshal(Unmarshaller ignore, Object also_ignore) {
		this.datasets = new Group<>(bound);
	}
	
	
	
}

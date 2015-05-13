package org.fao.sws.model.common;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlSeeAlso;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

import org.fao.sws.model.Dataset;
import org.fao.sws.model.Dimension;
import org.fao.sws.model.Domain;
import org.fao.sws.model.Flag;
import org.hibernate.validator.constraints.NotEmpty;

//rules of the game across model classes:

// - no-args constructor and no-finals to please for JAXB deserialisation
// - private fields with three layers of annotations on classe and/or fields: JAXB directives, validation, and lombok. in that order
// - JAXB directives adapt to legacy config naming...

@XmlSeeAlso({Domain.class, Dimension.class, Flag.class, Dataset.class})

@NoArgsConstructor @EqualsAndHashCode @ToString
public abstract class Entity {

	@XmlAttribute(name="code") @XmlID  
	
	@NotEmpty(message="{no_entity_id}") 
	@NonNull @Getter 
	private String id;

	
	protected Entity(String id) {
		
		this.id = id;
		this.labelKey = id;	
	}
	
	@XmlAttribute(name="displayNameKey") 
	@Setter
	private String labelKey;
	
	public String labelKey() {
		return labelKey == null ? id :labelKey;
	}
}

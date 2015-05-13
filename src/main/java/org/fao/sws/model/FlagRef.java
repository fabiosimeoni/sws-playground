package org.fao.sws.model;

import static lombok.AccessLevel.*;
import static org.fao.sws.common.Utils.*;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import org.fao.sws.model.common.Identified;
import org.hibernate.validator.constraints.NotEmpty;


@XmlRootElement @NoArgsConstructor

@Data
public class FlagRef implements Identified {

	@XmlAttribute(name="refCode") @XmlIDREF 
	@NonNull @Setter(NONE) 
	private Flag target;
	
	@XmlAttribute
	@NotEmpty
	private String sdmxCode;
	
	@XmlAttribute
	@NotEmpty(message="{joincol.required}")
	private String joinColumn;

	
	protected FlagRef(Flag target) {
		
		this.target = target;
		
		//defaults
		
		this.sdmxCode = id();
		this.joinColumn=dbfy(id());
	}
	
	@Override
	public String id() {
		return target==null?null:target.id();
	}
		
}

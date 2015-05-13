package org.fao.sws.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import org.fao.sws.model.common.Entity;
import org.hibernate.validator.constraints.NotEmpty;


@XmlRootElement @NoArgsConstructor
@EqualsAndHashCode(callSuper=true) @ToString
public class Flag extends Entity {

	public Flag(String id) {
		super(id);
	}
		
	@XmlAttribute(name="tableName")
	@NotEmpty(message="{table.required}")
	@Getter @Setter
	private String table;
}

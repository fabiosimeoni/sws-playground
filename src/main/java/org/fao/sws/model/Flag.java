package org.fao.sws.model;

import static org.fao.sws.common.Constants.*;
import static org.fao.sws.common.Utils.*;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import org.fao.sws.model.common.Entity;
import org.hibernate.validator.constraints.NotEmpty;


@XmlRootElement @NoArgsConstructor

@Data 
@EqualsAndHashCode(callSuper=true,exclude="length") 
@ToString(callSuper=true)
public class Flag extends Entity<Flag> {

	public Flag(String id) {
		super(id);
		this.table=dbfy(refdata_prefix,"flag_"+id());
	}
		
	@XmlAttribute(name="tableName")
	@NotEmpty(message="{table.required}")
	private String table;
	
	private int length=3;
	
	public FlagRef ref() {
		return new FlagRef(this);
	}
}

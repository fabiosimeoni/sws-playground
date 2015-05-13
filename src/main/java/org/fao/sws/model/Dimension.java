package org.fao.sws.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import org.fao.sws.model.common.Entity;
import org.hibernate.validator.constraints.NotEmpty;


@NoArgsConstructor @EqualsAndHashCode(callSuper=true) @ToString
@XmlSeeAlso({Dimension.Standard.class,Dimension.Time.class,Dimension.Measure.class})
public abstract class Dimension extends Entity {

	public Dimension(String id) {
		super(id);
		
		this.sdmxCode = id; //default
	}
	
	////// specialisations
	
	@XmlRootElement(name="dimension")
	@NoArgsConstructor @EqualsAndHashCode(callSuper=true) @ToString
	public static class Standard extends Dimension {

		
		public Standard(String id) {
			super(id);
		}
			
	}
	
	@XmlRootElement(name="timeDimension") 
	@NoArgsConstructor @EqualsAndHashCode(callSuper=true) @ToString
	public static class Time extends Dimension {

		
		public Time(String id) {
			super(id);
		}
			
	}
	
	@XmlRootElement(name="measurementUnitDimension") 
	@NoArgsConstructor @EqualsAndHashCode(callSuper=true) @ToString
	public static class Measure extends Dimension {

		
		public Measure(String id) {
			super(id);
		}
			
	}
	
	
	
	
	
	
	
	@XmlAttribute(name="tableName")
	@NotEmpty(message="{table.required}")
	@Getter @Setter
	private String table;
	
	@XmlAttribute(name="selectionTableName")
	@NotEmpty(message="{table.required}")
	@Getter @Setter
	private String selectionTable;
	
	@XmlAttribute(name="hierarchyTableName")
	@NotEmpty(message="{table.required}")
	@Getter @Setter
	private String hierarchyTable;
	
	@XmlAttribute
	@NotEmpty
	@Getter @Setter
	private String parent = "PARENT";
	
	@XmlAttribute
	@NotEmpty
	@Getter @Setter
	private String child = "CHILD";
	
	
	@XmlAttribute
	@Getter @Setter
	private String sdmxCode;
}

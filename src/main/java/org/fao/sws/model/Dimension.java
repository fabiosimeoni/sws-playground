package org.fao.sws.model;

import static org.fao.sws.common.Constants.*;
import static org.fao.sws.common.Utils.*;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import org.fao.sws.model.DimensionRef.StandardRef;
import org.fao.sws.model.DimensionRef.TimeRef;
import org.fao.sws.model.common.Entity;
import org.hibernate.validator.constraints.NotEmpty;

@XmlSeeAlso({Dimension.Standard.class,Dimension.Time.class,Dimension.Measure.class}) @NoArgsConstructor 

@Data 
@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
public abstract class Dimension extends Entity {

	public Dimension(String id) {
		
		super(id);
		this.sdmxCode = id; //default
		this.table=dbfy(refdata_prefix,"dim_"+id());
		this.selectionTable=dbfy(opdata_prefix,"selection_dim_"+id());
		this.hierarchyTable=dbfy(refdata_prefix,"dim_"+id()+"_hiearchy");
	}
	
	public abstract DimensionRef ref();
	
	@XmlAttribute(name="tableName")
	@NotEmpty(message="{table.required}")
	private String table;
	
	@XmlAttribute(name="selectionTableName")
	@NotEmpty(message="{table.required}")
	private String selectionTable;
	
	@XmlAttribute(name="hierarchyTableName")
	@NotEmpty(message="{table.required}")
	private String hierarchyTable;
	
	@XmlAttribute
	@NotEmpty(message="{parent.required}")
	private String parent = "PARENT";
	
	@XmlAttribute
	@NotEmpty(message="{child.required}")
	private String child = "CHILD";
	
	@XmlAttribute
	private String sdmxCode;
	
	////// lgeacy requirement: name specialisations

	@XmlRootElement(name="dimension")
	@NoArgsConstructor @EqualsAndHashCode(callSuper=true) @ToString
	public static class Standard extends Dimension {

		
		public Standard(String id) {
			super(id);
		}
		
		@Override
		public DimensionRef ref() {
			return new StandardRef(this);
		}
		
		
	}
	
	@XmlRootElement(name="timeDimension") 
	@NoArgsConstructor @EqualsAndHashCode(callSuper=true) @ToString
	public static class Time extends Dimension {

		
		public Time(String id) {
			super(id);
		}
			
		@Override
		public DimensionRef ref() {
			return new TimeRef(this);
		}
	}
	
	@XmlRootElement(name="measurementUnitDimension") 
	@NoArgsConstructor @EqualsAndHashCode(callSuper=true) @ToString
	public static class Measure extends Dimension {

		public Measure(String id) {
			super(id);
		}
		
		@Override
		public DimensionRef ref() {
			return new DimensionRef.MeasureRef(this);
		}
			
	}
	

}

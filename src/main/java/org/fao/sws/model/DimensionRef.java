package org.fao.sws.model;

import static lombok.AccessLevel.*;
import static org.fao.sws.common.Utils.*;

import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import org.fao.sws.model.DimensionRef.MeasureRef;
import org.fao.sws.model.DimensionRef.StandardRef;
import org.fao.sws.model.DimensionRef.TimeRef;
import org.fao.sws.model.common.Identified;
import org.hibernate.validator.constraints.NotEmpty;

@XmlSeeAlso({StandardRef.class, TimeRef.class, MeasureRef.class}) @NoArgsConstructor 

@Data
@ToString(doNotUseGetters=true)
@EqualsAndHashCode(doNotUseGetters=true) 
public abstract class DimensionRef implements Identified {
		
	@XmlAttribute(name="refCode") @XmlIDREF 
	@NonNull @Setter(NONE) 
	private Dimension target;
	
	@XmlAttribute
	@NotEmpty
	private String sdmxCode;
	
	@NotEmpty
	private String joinColumn;
	
	private boolean ascending = true;
	
	
	protected DimensionRef(Dimension target) {
		this.target = target;
		this.sdmxCode = target.id();
		this.joinColumn= dbfy(target.id());
	}
	
	@Override
	public String id() {
		return target==null?null:target.id();
	}
	
	
	public DimensionRef descending() {
		return ascending(false);
	}
	
	///////////////////////////////////////////////////////////////// roots
	
	@XmlElementWrapper(name="roots") @XmlElement(name="root")
	@Valid
	@Setter(NONE) 
	private Set<Root> roots = new HashSet<>();
	
	
	@Data 
	@NoArgsConstructor 
	@RequiredArgsConstructor
	public static class Root {
		
		@XmlAttribute
		@NonNull
		private String code;
	}
	
	
	public DimensionRef roots(@NonNull Object ... codes) {
		
		for (Object code : codes)
			roots.add(new Root(code.toString()));
		
		return this;
	}
		
		
	//////////////////////////////////////////////////////////// specialisations

	@XmlRootElement(name="dimension")
	@NoArgsConstructor
	public static class StandardRef extends DimensionRef {
		
		public StandardRef(Dimension dim) {
			super(dim);
		}
	}
	
	
	@XmlRootElement(name="timeDimension")
	@NoArgsConstructor
	public static class TimeRef extends DimensionRef {
		
		public TimeRef(Dimension dim) {
			super(dim);
		}
	}
	
	@XmlRootElement(name="measurementUnitDimension") 
	@NoArgsConstructor
	public static class MeasureRef extends DimensionRef {
		
		public MeasureRef(Dimension dim) {
			super(dim);
		}
	}	
	
	
	
	

}

package org.fao.sws.model;

import static java.util.Arrays.*;
import static lombok.AccessLevel.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

import org.fao.sws.model.common.Entity;
import org.fao.sws.model.common.Group;
import org.fao.sws.model.configuration.Validators.NotEmptyGroup;
import org.hibernate.validator.constraints.NotEmpty;


@XmlRootElement(name="dataSet") @NoArgsConstructor 

@Data
@EqualsAndHashCode(callSuper=true, exclude="boundDims") 
@ToString(callSuper=true)
public class Dataset extends Entity {

	public Dataset(String id) {
		super(id);
		sdmxCode = id; //default
	}
	
	
	@XmlAttribute(name="defaultEmptyRowsVisible")
	private boolean emptyRowsVisibile = false;
	
	@XmlAttribute
	@NotEmpty
	private String sdmxCode;
	
	
	///////////////////////////////////////////////////////////////////   dimensions
	
	@Valid @NotEmptyGroup
	@Setter(NONE) 
	private Group<DimensionRef> dimensions = new Group<>();
	
	
	//pro-fluency delegates: keeps up the builder pattern for inlined constructions
	public Dataset with(@NonNull DimensionRef ... dims) {
		
		dimensions.with(dims);
		
		return this;
	}
	
	@AssertTrue(message="{required_dimension_refs}")
	boolean hasAllDimensionTypes() {
		
		return typesOf(dimensions).containsAll(asList(Dimension.Standard.class,Dimension.Time.class,Dimension.Measure.class));
	
	}
	
	
	// helper
	private Set<Class<?>> typesOf(Group<DimensionRef> group) {
		
		Set<Class<?>> types = new HashSet<>();
		
		for (DimensionRef ref : group)
			types.add(ref.target().getClass());
		
		return types;
		
	}
	
	
	///////////////////////////////////////////////////////////////////   observations
	
	@XmlElement(name="observation")
	@Valid 
	@Setter(NONE) 
	private Observations observations;
	
	public Dataset with(@NonNull Observations observations) {
	
		this.observations = observations;
		
		return this;
	}
	
	
	// legacy format support via JAXB callback.
	// copies on binding to match both legacy format _and_ grouping facilities
	// you'd remove it entirely when/if legacy could be phased out.
	
	@XmlElementRef
	private Collection<DimensionRef> boundDims;
	
	
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

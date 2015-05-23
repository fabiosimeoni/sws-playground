package org.fao.sws.model;

import static java.util.Arrays.*;
import static lombok.AccessLevel.*;
import static org.fao.sws.common.Constants.*;
import static org.fao.sws.common.Utils.*;

import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import org.fao.sws.model.common.Entity;
import org.fao.sws.model.common.Group;
import org.fao.sws.model.configuration.Adapters.DatasetAdapter;
import org.fao.sws.model.configuration.Validators.NotEmptyGroup;
import org.hibernate.validator.constraints.NotEmpty;

@XmlJavaTypeAdapter(DatasetAdapter.class)

@Data
@EqualsAndHashCode(callSuper=true) 
@FieldDefaults(level=PRIVATE)
@ToString(callSuper=true)
public class Dataset extends Entity<Dataset> {
	

	boolean emptyRowsVisibile = false;
	
	@NotEmpty
	String sdmxCode;
	
	@Setter(NONE)
	String schema;
	
	@NotEmpty(message="{table.required}") String table;
	@NotEmpty(message="{table.required}") String coordinatesTable;
	@NotEmpty(message="{table.required}") String sessionObservationTable;
	@NotEmpty(message="{table.required}") String metadataTable;
	
	@NotEmpty(message="{table.required}") String metadataElementTable;
	@NotEmpty(message="{table.required}") String sessionMetadataTable;
	@NotEmpty(message="{table.required}") String sessionMetadataElementTable;
	@NotEmpty(message="{table.required}") String validationTable;
	@NotEmpty(message="{table.required}") String sessionValidationTable;
	@NotEmpty(message="{table.required}") String tagObservationTable;

	
	public Dataset(String id) { //by default, schema is taken from id.
		this(id,id); 
	}
	

	public Dataset(String id, String schema) { 	//allows schema customisations
		
		super(id);
		
		this.schema=schema;
		
		//defaults
		
		sdmxCode = id; 
		
		//mapping defaults
		
		this.table = dbfy(id,default_observation_table);
		this.coordinatesTable=dbfy(schema,default_coordinate_table);
		this.sessionObservationTable=dbfy(schema,default_session_observation_table);
		this.metadataTable=dbfy(schema,default_metadata_table);
		this.metadataElementTable=dbfy(schema,default_metadata_element_table);
		this.sessionMetadataTable=dbfy(schema,default_session_metadata_table);
		this.sessionMetadataElementTable=dbfy(schema,default_session_metadata_element_table);
		this.validationTable=dbfy(schema,default_validation_table);
		this.sessionValidationTable=dbfy(schema,default_session_validation_table);
		this.tagObservationTable=dbfy(schema,default_tag_observation_table);
	}
	
	
	
	///////////////////////////////////////////////////////////////////   dimensions
	
	@Valid @NotEmptyGroup
	@Setter(NONE) 
	Group<DimensionRef> dimensions = new Group<>();
	
	@Valid 
	//@NotEmptyGroup
	@Setter(NONE) 
	Group<FlagRef> flags = new Group<>();
	
	/////////////////////////////////////////////////////////    pro-fluency delegates
	
	public Dataset with(@NonNull DimensionRef ... dims) {
		
		dimensions.with(dims);
		
		return this;
	}
	
	public Dataset with(@NonNull FlagRef ... refs) {
		
		flags.with(refs);
		
		return this;
	}
	
	
	public Dataset showEmptyRows() {
		return emptyRowsVisibile(true);
	}
	
	
	////////////////////////////////////////////////////////////////////////// validation
	
	@AssertTrue(message="{required_dimension_refs}")
	boolean hasAllDimensionTypes() {
		
		Set<Class<?>> types = new HashSet<>();

		for (DimensionRef ref : dimensions)
			types.add(ref.target().getClass());
		
		return types.containsAll(asList(Dimension.Standard.class,
				                        Dimension.Time.class,
				                        Dimension.Measure.class));
	
	}
}

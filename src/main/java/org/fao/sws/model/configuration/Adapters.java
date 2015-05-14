package org.fao.sws.model.configuration;

import static java.util.Arrays.*;
import static org.fao.sws.model.configuration.Dsl.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.UtilityClass;

import org.fao.sws.model.Dataset;
import org.fao.sws.model.DimensionRef;
import org.fao.sws.model.FlagRef;
import org.fao.sws.model.common.Entity;
import org.fao.sws.model.common.Group;
import org.fao.sws.model.configuration.Adapters.DatasetAdapter.Ds.DimRef;
import org.fao.sws.model.configuration.Adapters.DatasetAdapter.Ds.PivotCol;

@UtilityClass
public class Adapters {

	
	//largest adapter: entirely replaces jaxb-agnotic bean to match legacy format requirements.
	//legacy format call for nest and redundancies that we want to hide in the data model
	//a full conversion is required.
	public class DatasetAdapter extends XmlAdapter<DatasetAdapter.Ds,Dataset> {
		
		@XmlRootElement(name="dataSet") @NoArgsConstructor 
		static class Ds {
			
			Ds(Dataset ds) {
				
				code=ds.id();
				dimensions=ds.dimensions().all();
				defaultEmptyRowsVisible=ds.emptyRowsVisibile();
				sdmxCode = ds.sdmxCode();
				observation.valueTableName=ds.table();
				observation.coordinatesTableName=ds.coordinatesTable();
				observation.metadataTableName=ds.metadataTable();
				observation.metadataElementTableName=ds.metadataElementTable();
				observation.sessionObservationTableName=ds.sessionObservationTable();
				observation.sessionMetadataTableName=ds.sessionMetadataTable();
				observation.sessionMetadataElementTableName=ds.sessionMetadataElementTable();
				observation.validationTableName=ds.validationTable();
				observation.sessionValidationTableName=ds.sessionValidationTable();
				observation.tagObservationTableName=ds.tagObservationTable();
				observation.flag = ds.flags().all();
						
				for (DimensionRef ref : ds.dimensions()) {
					observation.dimension.add(new DimRef(ref.id(),ref.joinColumn()));
					pivoting.add(new PivotCol(ref.id(), ref.ascending()));
				}
			}
			
			@XmlAttribute
			String code;
			
			@XmlAttribute
			private boolean defaultEmptyRowsVisible;
			
			@XmlAttribute
			private String sdmxCode;
			
			@XmlElementRef
			Collection<DimensionRef> dimensions;
			
			@XmlElement
			private Obs observation = new Obs();
			
			static class Obs {
				
				@XmlAttribute String valueTableName;
				@XmlAttribute String coordinatesTableName;
				@XmlAttribute String sessionObservationTableName;
				@XmlAttribute String metadataTableName;
				@XmlAttribute String metadataElementTableName;
				@XmlAttribute String sessionMetadataTableName;
				@XmlAttribute String sessionMetadataElementTableName;
				@XmlAttribute String validationTableName;
				@XmlAttribute String sessionValidationTableName;
				@XmlAttribute String tagObservationTableName;
				

				@XmlElement
				Collection<DimRef> dimension = new ArrayList<>();
				
				@XmlElement
				Collection<FlagRef> flag = new ArrayList<>();
			}
			
			@XmlElementWrapper(name="defaultPivoting")
			@XmlElement(name="dimension")
			private List<PivotCol> pivoting = new ArrayList<>();
			
			@NoArgsConstructor @AllArgsConstructor
			static class PivotCol {
				@XmlAttribute String refCode;
				@XmlAttribute boolean ascending;
			}
			
			@NoArgsConstructor @AllArgsConstructor
			static class DimRef {
				
				@XmlAttribute String refCode;
				@XmlAttribute String joinColumn;
			}
		
		}
		
		@Override
		public Ds marshal(Dataset v) throws Exception {
			return new Ds(v);
		}
		
		@Override
		public Dataset unmarshal(Ds ds) throws Exception {
			
			Dataset dataset = dataset(ds.code)
					  .with(ds.observation.flag.toArray(new FlagRef[0]))
					  .with(ds.dimensions.toArray(new DimensionRef[0]))
					  .table(ds.observation.valueTableName)
					  .coordinatesTable(ds.observation.coordinatesTableName)
					  .sessionObservationTable(ds.observation.sessionObservationTableName)
					  .metadataTable(ds.observation.metadataTableName)
					  .metadataElementTable(ds.observation.metadataElementTableName)
					  .sessionMetadataTable(ds.observation.sessionMetadataTableName)
					  .sessionMetadataElementTable(ds.observation.sessionMetadataElementTableName)
					  .validationTable(ds.observation.validationTableName)
					  .sessionValidationTable(ds.observation.sessionValidationTableName)
					  .tagObservationTable(ds.observation.tagObservationTableName);
			
			for (DimRef dim : ds.observation.dimension)
				dataset.dimensions().get(dim.refCode).joinColumn(dim.joinColumn);
			
			for (PivotCol dim : ds.pivoting)
				dataset.dimensions().get(dim.refCode).ascending(dim.ascending);
			
			
			return dataset;
		}
		
	}
	
	//legacy requirement
	
	public class ContactsAdapter extends XmlAdapter<String,Set<String>> {

		@Override
		public Set<String> unmarshal(String values) throws Exception {
			return new HashSet<>(asList(values.split(";")));
		}

		@Override
		public String marshal(Set<String> values) throws Exception {
			
			StringBuilder builder = new StringBuilder();
			
			for (String value : values)
				builder.append(value).append(";");
			
			builder.deleteCharAt(builder.lastIndexOf(";"));
			
			return builder.toString();
		}
	
		
	}
	
	
	
	 public class GroupAdapter extends XmlAdapter<GroupAdapter.Entities,Group<Entity>> {

		 @AllArgsConstructor @NoArgsConstructor
		 public static class Entities {
			 
			 @XmlElementRef
			 Collection<Entity> entities;
			 
		 }
		 
		@Override
		public Entities marshal(Group<Entity> group) throws Exception {
			return new Entities(group.all());
		}

		@Override
		public Group<Entity> unmarshal(Entities list) throws Exception {
			return new Group<>(list.entities);
		}
		
	}
}

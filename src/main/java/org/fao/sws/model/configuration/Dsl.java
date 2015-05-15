package org.fao.sws.model.configuration;

import static java.util.UUID.*;
import lombok.experimental.UtilityClass;

import org.fao.sws.model.Dataset;
import org.fao.sws.model.Dimension;
import org.fao.sws.model.Domain;
import org.fao.sws.model.Flag;

@UtilityClass
public class Dsl {

	public Configuration sws() {
		return new Configuration.Default();
	}
	
	public Domain domain() {
		return domain(randomUUID().toString());
	}
	
	public Domain domain(String id) {
		return new Domain(id);
	}
	
	public Dimension.Standard dimension() {
		return dimension(randomUUID().toString());
	}
	
	public Dimension.Standard dimension(String id) {
		return new Dimension.Standard(id);
	}
	
	public Dimension.Time timeDimension(String id) {
		return new Dimension.Time(id);
	}
	
	public Dimension.Time timeDimension() {
		return timeDimension(randomUUID().toString());
	}
	
	public Dimension.Measure measureDimension(String id) {
		return new Dimension.Measure(id);
	}
	
	public Dimension.Measure measureDimension() {
		return measureDimension(randomUUID().toString());
	}
	
	public Flag flag(String id) {
		return new Flag(id);
	}
	
	public Dataset dataset() {
		return dataset(randomUUID().toString());
	}
	
	public Dataset dataset(String id) {
		return new Dataset(id);
	}
	
	public Dataset dataset(String id,String schema) {
		return new Dataset(id,schema);
	}
}

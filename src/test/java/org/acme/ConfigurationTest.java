package org.acme;


import static org.fao.sws.model.config.DatabaseConfiguration.*;
import static org.fao.sws.model.configuration.Dsl.*;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import lombok.Cleanup;
import lombok.SneakyThrows;

import org.fao.sws.model.Dataset;
import org.fao.sws.model.Dimension;
import org.fao.sws.model.Domain;
import org.fao.sws.model.Flag;
import org.fao.sws.model.config.DataSetConfiguration;
import org.fao.sws.model.config.DatabaseConfiguration;
import org.fao.sws.model.config.DimensionConfiguration;
import org.fao.sws.model.config.DomainConfiguration;
import org.fao.sws.model.configuration.Binder;
import org.fao.sws.model.configuration.Configuration;
import org.fao.sws.model.configuration.Validator;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class ConfigurationTest {

	Binder binder = new Binder();
	
	Validator validator = new Validator();
	
	@Rule
	public TemporaryFolder folder = new TemporaryFolder();
	
	File location;
	
	@Before @SneakyThrows
	public void setup() {
		
		System.setProperty(CONFIG_ROOT_PROPERTY,folder.getRoot().getAbsolutePath());
		
		location = new File(folder.getRoot(),"config/application/dataset");
		
		location.mkdirs();
				
	}
	
	@Test
	public void roundtrips_and_is_valid() {
		
		Dimension dim = dimension("a");
		Dimension time = timeDimension("b");
		Dimension measure = measureDimension("c");
		Flag flag = flag("f");
		
		dim.labelKey("key"); //defaulting to id will do 99/100 times. just to test here.
				
		Dataset ds1 = dataset("ds1").with(
				
										dim.ref().roots(110,120,130),
										time.ref().sdmxCode("somecode").descending(),
										measure.ref()
										
									)
									.with(
										flag.ref()
									);
		
		
		Configuration sws = sws()
								.contact("john.doe@acme.org")
								.contact("joe.plumber@acme.org")
								.with(dim,time,measure)
								.with(flag)
								.with(domain("domain").with(ds1));
		
		
		Configuration parsed = roundtrip(sws);

		//decomment to debug problems
		//System.out.println(sws);
		//System.out.println(parsed);
		

		assertEquals(sws, parsed);
		
		validator.valid(sws);

		persist(sws);
		
		
		DatabaseConfiguration config = new DatabaseConfiguration();
		
		assertThatCanBeParsed(config);
		
	}
	
	
	@Test
	public void can_be_parsed_in_production() {
		
		Dimension dim = dimension("a");
		Dimension time = timeDimension("b");
		Dimension measure = measureDimension("c");
		Flag flag = flag("f");
		
		Dataset dataset = dataset("ds1").with(
				
				dim.ref().roots(110,120,130),
				time.ref().sdmxCode("somecode").descending(),
				measure.ref()
				
			)
			.with(
				flag.ref()
			);
		
		

		Domain domain = domain("d").with(dataset);
		
		Configuration sws = sws()
				.contact("john.doe@acme.org")
				.contact("joe.plumber@acme.org")
				.with(dim,time,measure)
				.with(flag)
				.with(domain);
			
		
		persist(sws);
		
		DatabaseConfiguration config = new DatabaseConfiguration();
		
		assertThatCanBeParsed(config);
		
		DomainConfiguration d = config.domain(domain.id());
		DataSetConfiguration ds = d.dataSet(dataset.id());
		DimensionConfiguration dm = ds.dimension(dim.id());
		
		assertEquals(dim.table(), dm.getTableName());
		assertEquals(dim.selectionTable(), dm.getSelectionTableName());
		assertTrue(dm.isHierarchical());
		assertEquals(dim.sdmxCode(), dm.getSdmxCode());
		
	}
	
	@Test
	public void fragment_is_valid_but_cannot_rountrip() {
		
		Configuration sws = sws()
								.contact("john.doe@acme.org")
								.contact("joe.plumber@acme.org")
								.with(aDomain());
		
		//references cannot be resolved
		assertNotEquals(sws, roundtrip(sws));
		
		validator.validFragment(sws);
	}
	
	@Test
	public void fragment_can_be_parsed_in_production() {
		
		Dimension dim = dimension("a");
		Dimension time = timeDimension("b");
		Dimension measure = measureDimension("c");
		Flag flag = flag("f");
		
		
		Configuration base = sws()
								.contact("john.doe@acme.org")
								.contact("joe.plumber@acme.org")
								.with(dim,time,measure)
								.with(flag);
		
		persist(base,"base");
		
		Domain d = aDomain();
		
		Configuration sws = sws().contact("john.doe@acme.org").with(d);
		
		persist(sws,"fragment");
		
		DatabaseConfiguration config = new DatabaseConfiguration();
		
		assertThatCanBeParsed(config);
		
		assertNotNull(config.domain(d.id()));
		
		
	}
	
	
	@Test
	public void datasets_are_lifted() {
		
		Dataset ds = dataset();
		
		Configuration sws = sws().with(domain().with(ds));
		
		assertTrue(sws.datasets().all().contains(ds));
		
	}
	
	
	@Test
	public void invalid_domains_are_discarded() {
		
		String blank = "";
		
		Domain bad = domain().with(dataset(blank));
		Domain good = aDomain();
		Configuration sws = sws().with(bad,aDomain());
		
		validator.validFragment(sws);
		
		assertFalse(sws.domains().has(bad));
		assertTrue(sws.domains().has(good));
		
	}
		
	
	
	////////////////////////////////////////////////////////////////////////////////
	
	@SneakyThrows
	Configuration roundtrip(Configuration config) {
		
		@Cleanup ByteArrayOutputStream stream = new ByteArrayOutputStream(); 
		
		binder.bind(config,stream);
		
		String xml = new String(stream.toByteArray());
		
		System.out.println(xml);
		
		@Cleanup ByteArrayInputStream in = new ByteArrayInputStream(xml.getBytes()); 
		
		return binder.bind(in);
	}

	void persist(Configuration config) {
		persist(config,"test");
		
	}
	
	@SneakyThrows
	void persist(Configuration config,String name) {
		
		@Cleanup ByteArrayOutputStream stream = new ByteArrayOutputStream();
		
		binder.bind(config,stream);
		
		String xml = new String(stream.toByteArray());
		
		System.out.println(xml);
		
		@Cleanup FileOutputStream fs = new FileOutputStream(new File(location,name+".xml")); 
		
		binder.bind(config,fs);
		
	}
	
	
	////////////////////////////////////////////////////////////////////////////////
	
	private void assertThatCanBeParsed(DatabaseConfiguration config) {
		
		assertNull(config.consumeConfigErrors());
		assertNull(config.consumeConfigAlerts());
	}
	

	Domain aDomain() {
		
		Dimension dim = dimension("a");
		Dimension time = timeDimension("b");
		Dimension measure = measureDimension("c");
		Flag flag = flag("f");
		
		dim.labelKey("key"); //defaulting to id will do 99/100 times. just to test here.
				
		Dataset ds1 = dataset("ds1").with(
				
				dim.ref().roots(110,120,130),
				time.ref().sdmxCode("somecode").descending(),
				measure.ref()
				
			)
			.with(
				flag.ref()
			);
		
		
		return domain("d").with(ds1);
		
		
	}
	
	
}

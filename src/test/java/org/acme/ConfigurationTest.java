package org.acme;


import static org.fao.sws.model.configuration.Dsl.*;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.inject.Inject;

import lombok.Cleanup;
import lombok.SneakyThrows;

import org.fao.sws.model.Dataset;
import org.fao.sws.model.Dimension;
import org.fao.sws.model.Domain;
import org.fao.sws.model.Flag;
import org.fao.sws.model.configuration.Binder;
import org.fao.sws.model.configuration.Configuration;
import org.fao.sws.model.configuration.Validator;
import org.junit.Test;

public class ConfigurationTest extends ApplicationTest {

	@Inject
	Binder binder;
	
	@Inject
	Validator validator;
	
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
	
	

	Domain aDomain() {
		
		Dimension dim = dimension("a");
		Dimension time = timeDimension("b");
		Dimension measure = measureDimension("c");
		Flag flag = flag("f");
		
		dim.labelKey("key"); //defaulting to id will do 99/100 times. just to test here.
				
		Dataset ds1 = dataset("ds1").with(
				
										dim.ref().roots(110,120,130),
										time.ref().sdmxCode("somecode"),
										measure.ref()
										
									)
									.with(flag.ref());
		
		
		return domain("d").with(ds1);
		
		
	}
}

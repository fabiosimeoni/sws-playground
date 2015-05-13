package org.acme;

import static org.junit.Assert.*;

import javax.inject.Inject;

import org.fao.sws.model.configuration.Configuration;
import org.junit.Test;

public class SmokeTest extends ApplicationTest {

	@Inject
	Configuration config; 
	
	@Inject
	TestConfiguration tconfig; 
	
	@Test
	public void injection_works_under_testing() {
		
		assertNotNull(config);
		
	}
	
	@Test
	public void test_config_has_priority_over_default_config() {
		
		assertSame(tconfig,config);
		
		//sample configuration
		tconfig.inner(new Configuration.Default());
		
	}
}

package org.fao.sws.model.configuration;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Produces;

@ApplicationScoped
public class Producers {

	@Produces @Alternative
	Configuration configuration(Locator locator, Binder binder, Validator validator){
		
		Configuration configuration = binder.bind(locator.locate());
		
		validator.valid(configuration);
	
		return configuration;
	}
	
}

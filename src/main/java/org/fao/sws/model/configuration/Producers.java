package org.fao.sws.model.configuration;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Produces;

@ApplicationScoped
public class Producers {

	@Produces @Alternative
	Configuration configuration(Locator locator, Binder binder, Validator validator){
		
		return validator.valid(binder.bind(locator.locate()));
	
	}
	
}

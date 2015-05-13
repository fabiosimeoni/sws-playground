package org.fao.sws.model.configuration;

import static java.lang.String.*;
import static javax.validation.Validation.*;

import java.util.Iterator;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.validation.ConstraintValidator;

import lombok.extern.slf4j.Slf4j;

import org.fao.sws.model.Domain;
import org.fao.sws.model.configuration.Validators.Global;
import org.jboss.weld.exceptions.IllegalStateException;

@ApplicationScoped @Slf4j
public class Validator {


	@Inject
	Instance<ConstraintValidator<?,?>> validators;
	
	private javax.validation.Validator inner = buildDefaultValidatorFactory().getValidator();
    
	
	/**
	 * Validates without global constraints.
	 */
	public Configuration validFragment(Configuration configuration) throws IllegalStateException {
		
		//disables all 
		
		return $valid(configuration);
	}
	
	
	/**
	 * Validates with global constraints.
	 */
	public Configuration valid(Configuration configuration) throws IllegalStateException {
		
		$valid(configuration,Global.class);
		return $valid(configuration);
	}
	
	
	///////////////////////////////////////////////////////////////////////////////////
	
	 
	
	
	//two-phase validation logic. it's two-phase: top-level (fatal) and per-domain (fatal) 
	//in both-phases, constraint groups
	private Configuration $valid(Configuration configuration, Class<?> ... groups) {
		
		
		///////////////////////////////////////////      top-level validation: outside domain, fatal.
		
		Set<?> toplevel = inner.validate(configuration,groups);
		
		//this is shallow: does not enter inside domains
		if (!toplevel.isEmpty())
			throw new IllegalStateException(format("fatal configuration errors:\n%s",toplevel));

			
		//////////////////////////////////////////////////////////////// domain validation, non-fatal.
			
		Iterator<Domain> domains = configuration.domains().iterator();
		
		while (domains.hasNext()) {
			
			Domain domain = domains.next();
			
			Set<?> errors = inner.validate(domain,groups);
			
			if (!errors.isEmpty()) {

				log.error("bad configuration for {}, discarding it\n{}",domain.id(),errors);
				
				domains.remove();
				
			}
		}
		
		/////////
		
		//pro-fluency convenience
		return configuration;
	}
}

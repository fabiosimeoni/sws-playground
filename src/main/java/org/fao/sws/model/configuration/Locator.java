package org.fao.sws.model.configuration;

import static java.lang.String.*;
import static java.lang.System.*;
import static java.nio.file.Files.*;
import static org.fao.sws.model.config.DatabaseConfiguration.*;

import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.enterprise.context.ApplicationScoped;

import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j @ApplicationScoped
public class Locator {
	
	@Getter
	private final Path location;
	
	public Locator() {
		this($defaultlocation());
		
	}

	public Locator(@NonNull String uri) {
		this(Paths.get(uri));
	}
	
	public Locator(@NonNull Path location) {
		
		this.location = location;
		
		if (!isDirectory(location))
			throw new IllegalArgumentException(format("invalid config location: %s doesn't exist or is not a directory",location));

		log.info("using configuration @ {}",location);
	}
	
	public InputStream locate() {
		//@TODO
		return null;
	}
	
	
	////////////////////////////////////////////////////////////////////////////
	
	private static Path $defaultlocation() {
	
		String uri = getProperty(CONFIG_ROOT_PROPERTY);
		
		if (uri==null)
			throw new IllegalStateException("invalid config location: CONFIG_ROOT_PROPERTY is not set");
		
		//expose to current convention in production. better update here than in all clients
		
		return Paths.get(getProperty(CONFIG_ROOT_PROPERTY))
								.resolve("config/application/dataset"); 
		
	}
}

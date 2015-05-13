package org.acme;

import static org.fao.sws.common.Constants.*;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;

import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Delegate;

import org.fao.sws.model.configuration.Configuration;



@ApplicationScoped @Alternative @Priority(high)
public class TestConfiguration implements Configuration {

	@NonNull @Setter @Delegate
	private Configuration inner;
}

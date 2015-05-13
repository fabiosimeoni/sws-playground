package org.fao.sws.model.configuration;

import static javax.xml.bind.Marshaller.*;

import java.io.InputStream;
import java.io.OutputStream;

import javax.enterprise.context.ApplicationScoped;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import lombok.SneakyThrows;

import org.fao.sws.model.common.Entity;
import org.fao.sws.model.common.Group;

@ApplicationScoped
public class Binder {

	private final JAXBContext ctx;
	
	@SneakyThrows
	public Binder() {
		ctx = JAXBContext.newInstance(Configuration.Default.class, Group.class, Entity.class);
	}
	
	@SneakyThrows
	public Configuration bind(InputStream stream) {
		
		return (Configuration) ctx.createUnmarshaller().unmarshal(stream);
	}
	
	@SneakyThrows
	public OutputStream bind(Configuration config, OutputStream stream) {
		
		Marshaller m = ctx.createMarshaller();
		
		m.setProperty(JAXB_FORMATTED_OUTPUT,true);
		
		m.marshal(config,stream);
		
		return stream;
	}
	
}

package org.fao.sws.model.common;

import static java.lang.String.*;
import static java.util.Arrays.*;
import static java.util.Spliterators.*;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Spliterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javax.validation.Valid;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import org.fao.sws.model.configuration.Adapters.GroupAdapter;

@XmlJavaTypeAdapter(GroupAdapter.class) @NoArgsConstructor
@Data 
public class Group<T extends Identified> implements Iterable<T> {

	@Valid
	private final Map<String,T> entities = new LinkedHashMap<>();
	
	public Group(Collection<T> entities) throws IllegalArgumentException {
		with(entities);
	}
	
	@Override
	public Iterator<T> iterator() {
		return entities.values().iterator();
	}
	
	public Stream<T> stream() {
		return StreamSupport.stream(spliteratorUnknownSize(iterator(), Spliterator.ORDERED),false);

	}
	
	@SuppressWarnings("unchecked")
	public Group<T> with(@NonNull T ...entities) {
		
		return with(asList(entities));
	}
	
	private Group<T> with(@NonNull Collection<T> entities) {
		
		for (T e : entities) {
			
			//can happen when deserialising fragment whose IDREFs cannot be resolved
			if (e.id()==null)
				continue;
			
			if (this.entities.put(e.id(),e)!=null)
				throw new IllegalArgumentException(format("duplicate entity %s",e.id()));
		
		};
		
		return this;
	}
	
	public Collection<T> all() {
		return entities.values();
	}
	
	public void remove(String id) throws IllegalStateException {
	
		entities.get(id);
		
		entities.remove(id);
	}
	
	public T get(String id) throws IllegalStateException {
		
		if (has(id))
			return entities.get(id);
		
		throw new IllegalStateException(format("no such entity %s",id));
		
	}
	
	public boolean has(@NonNull String id) {
		
		return entities.containsKey(id);
	}
	
	public boolean has(@NonNull T entity) {
		
		return entities.containsKey(entity.id());
	}
	
}

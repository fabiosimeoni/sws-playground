package org.fao.sws.model.common;

import static java.lang.String.*;
import static java.util.Arrays.*;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.validation.Valid;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

import org.fao.sws.model.configuration.Adapters.GroupAdapter;

@XmlJavaTypeAdapter(GroupAdapter.class)
@NoArgsConstructor @EqualsAndHashCode @ToString
public class Group<T extends Entity> implements Iterable<T> {

	@Valid
	private final Map<String,T> entities = new LinkedHashMap<>();
	
	public Group(Collection<T> entities) throws IllegalArgumentException {
		
		with(entities);
	}
	
	@Override
	public Iterator<T> iterator() {
		return entities.values().iterator();
	}
	
	@SuppressWarnings("unchecked")
	public Group<T> with(@NonNull T ...entities) {
		
		return with(asList(entities));
	}
	
	private Group<T> with(@NonNull Collection<T> entities) {
		
		for (T e : entities) {
			
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

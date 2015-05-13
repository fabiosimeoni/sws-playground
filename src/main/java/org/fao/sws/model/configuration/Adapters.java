package org.fao.sws.model.configuration;

import static java.util.Arrays.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import org.fao.sws.model.common.Entity;
import org.fao.sws.model.common.Group;

public class Adapters {

	//adapts entities to (only backwards) entity references
	public static class RefAdapter<T extends Entity> extends XmlAdapter<RefAdapter.RefEntity<T>,T> {

		@AllArgsConstructor @NoArgsConstructor
		public static class RefEntity<T extends Entity> {
			
			@XmlAttribute(name="refCode") 
			@XmlIDREF 
			T ref;
			
		}
		
		@Override
		public T unmarshal(RefEntity<T> v) throws Exception {
			return v.ref;
		}

		@Override
		public RefEntity<T> marshal(T v) throws Exception {
			return new RefEntity<T>(v);
		}
		
	}
	
	public static class ContactsAdapter extends XmlAdapter<String,Set<String>> {

		@Override
		public Set<String> unmarshal(String values) throws Exception {
			return new HashSet<>(asList(values.split(";")));
		}

		@Override
		public String marshal(Set<String> values) throws Exception {
			
			StringBuilder builder = new StringBuilder();
			
			for (String value : values)
				builder.append(value).append(";");
			
			builder.deleteCharAt(builder.lastIndexOf(";"));
			
			return builder.toString();
		}
	
		
	}
	
	
	
	 public static class GroupAdapter extends XmlAdapter<GroupAdapter.Entities,Group<Entity>> {

		 @AllArgsConstructor @NoArgsConstructor
		 public static class Entities {
			 
			 @XmlElementRef
			 Collection<Entity> entities;
			 
		 }
		 
		@Override
		public Entities marshal(Group<Entity> group) throws Exception {
			return new Entities(group.all());
		}

		@Override
		public Group<Entity> unmarshal(Entities list) throws Exception {
			return new Group<>(list.entities);
		}
		
	}
}

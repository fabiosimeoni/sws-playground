package org.fao.sws.model;

import static java.lang.String.*;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

import org.fao.sws.model.configuration.Adapters.RefAdapter;


@XmlRootElement @NoArgsConstructor @EqualsAndHashCode @ToString
public class Observations {

	@XmlElement(name="flag") @XmlJavaTypeAdapter(RefAdapter.class)
	@Getter 
	private List<Flag> flags = new ArrayList<>();
	
	public Observations with(@NonNull Flag ... flags) {
		
		for (Flag flag : flags ) {
			
			if (this.flags.contains(flag.id()))
				throw new IllegalArgumentException(format("duplicate entity %s",flag.id()));
		
			this.flags.add(flag);
		};
		
		return this;
	}
	
}

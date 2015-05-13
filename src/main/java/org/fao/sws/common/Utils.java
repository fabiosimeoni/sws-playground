package org.fao.sws.common;

import static java.lang.String.*;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Utils {

	 final String regex = "([a-z])([A-Z])";
     final String replacement = "$1_$2";
     private final String dbqn = "%s.%s";
     
     //from camel case to captial underscore
     public String dbfy(@NonNull String schema, @NonNull String name) {
    	 
    	 return dbfy(format(dbqn,schema,name));
     }
     
     public String dbfy(@NonNull String name) {
    	 
    	 return name.replaceAll(regex, replacement).toUpperCase();
     }

}

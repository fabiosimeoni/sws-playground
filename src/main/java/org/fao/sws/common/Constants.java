package org.fao.sws.common;

import static lombok.AccessLevel.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.UtilityClass;

@UtilityClass 
@FieldDefaults(makeFinal=true,level=PUBLIC)
public class Constants {

	int low = 0;
	int normal = 10;
	int high = Integer.MAX_VALUE;
	
	String refdata_prefix = "reference_data";
	String opdata_prefix = "operational_data";
	
	String default_observation_table = "observation";
	String default_coordinate_table = "observation_coordinate";
	String default_session_observation_table = "session_observation";
	String default_metadata_table = "metadata";
	String default_metadata_element_table = "metadata_element";
	String default_session_metadata_table = "session_metadata";
	String default_session_metadata_element_table = "session_metadata_element";
	String default_validation_table = "validation";
	String default_session_validation_table = "session_validation";
	String default_tag_observation_table = "tag_observation";
		
}



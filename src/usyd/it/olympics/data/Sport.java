package usyd.it.olympics.data;

import java.util.HashMap;

public class Sport {
	public static Integer getSportId(HashMap<String, Object> tuple) {
		Object value =  tuple.get("sport_id");
		return  ((value!=null && value instanceof Integer) ? (Integer) value : null);
	}

	public static String getDiscipline(HashMap<String, Object> tuple) {
		Object value =  tuple.get("discipline");
		return  ((value!=null && value instanceof String) ? (String) value : null);
	}

	public static String getSportName(HashMap<String, Object> tuple) {
		Object value =  tuple.get("sport_name");
		return  ((value!=null && value instanceof String) ? (String) value : null);
	}
	
}

package usyd.it.olympics.data;

import java.util.Date;
import java.util.HashMap;

import usyd.it.olympics.OlympicsDBException;

/**
 *
 * @author Bryn Jeffries
 */
public class Booking implements TupleConverter {
	// For tuple information
	public static final String[] attributeNames =     { "journey_id", "vehicle_code", 	"origin", 		"dest", 	"departs", 	"arrives", 	"booked_by"};
	public static final Class<?>[] attributeClasses = { Integer.class, String.class, 	String.class, 	String.class, 	Date.class, Date.class, String.class};

	@Override
	public String[] getAttributeNames() {
		return attributeNames;
	}

	@Override
	public Class<?>[] getColumnClasses() {
		return attributeClasses;
	}

	public static int getJourneyId(HashMap<String, Object> tuple) throws OlympicsDBException {
		Integer bookingId = (Integer) tuple.get("journey_id");
		if (bookingId==null) throw new OlympicsDBException("No journey ID available");
		return bookingId.intValue();
	}

}

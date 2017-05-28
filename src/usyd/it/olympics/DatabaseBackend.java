package usyd.it.olympics;


/**
 * Database back-end class for simple gui.
 * 
 * The DatabaseBackend class defined in this file holds all the methods to 
 * communicate with the database and pass the results back to the GUI.
 *
 *
 * Make sure you update the dbname variable to your own database name. You
 * can run this class on its own for testing without requiring the GUI.
 */
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;

/**
 * Database interfacing backend for client. This class uses JDBC to connect to
 * the database, and provides methods to obtain query data.
 * 
 * Most methods return database information in the form of HashMaps (sets of 
 * key-value pairs), or ArrayLists of HashMaps for multiple results.
 *
 * @author Bryn Jeffries {@literal <bryn.jeffries@sydney.edu.au>}
 */
public class DatabaseBackend {

    ///////////////////////////////
    /// DB Connection details
	/// These are set in the constructor so you should never need to read or 
	/// write to them yourself 
    ///////////////////////////////
    private final String dbUser;
    private final String dbPass;
	private final String connstring;


    ///////////////////////////////
    /// Student Defined Functions
    ///////////////////////////////

    /////  Login and Member  //////

    /**
     * Validate memberID details
     * 
     * Implements Core Functionality (a)
     *
     * @return basic details of user if username is for a valid memberID and password is correct
     * @throws OlympicsDBException 
     * @throws SQLException
     */
    public HashMap<String,Object> checkLogin(String member, char[] password) throws OlympicsDBException  {
    	// Note that password is a char array for security reasons.
    	// Don't worry about this: just turn it into a string to use in this function
    	// with "new String(password)"
    	
    	HashMap<String,Object> details = null;
        try {
            Connection conn = getConnection();
        	
	        // FIXME: REPLACE FOLLOWING LINES WITH REAL OPERATION
	        // Don't forget you have memberID variables memberUser available to
	        // use in a query.
	        // Query whether login (memberID, password) is correct...
            boolean valid = (member.equals("testuser") && new String(password).equals("testpass"));
            if (valid) {
            	details = new HashMap<String,Object>();

    	        // Populate with record data
            	details.put("member_type", "athlete");
            }
        } catch (Exception e) {
            throw new OlympicsDBException("Error checking login details", e);
        }
        return details;
    }

    /**
     * Obtain details for the current memberID
     * @param memberID 
     *
     * @return Details of member
     * @throws OlympicsDBException
     */
    public HashMap<String, Object> getMemberDetails(String memberID) throws OlympicsDBException {
        // FIXME: REPLACE FOLLOWING LINES WITH REAL OPERATION
    	HashMap<String, Object> details = new HashMap<String, Object>();
    	
    	details.put("member_id", memberID);
    	details.put("member_type", "athlete");
    	details.put("title", "Mr");
    	details.put("first_name", "Potato");
    	details.put("family_name", "Head");
    	details.put("country_name", "Australia");
    	details.put("residence", "SIT");
    	details.put("member_type", "athlete");
    	details.put("num_bookings", Integer.valueOf(20));
    	
    	// Some attributes fetched may depend upon member_type
    	// This is for an athlete
    	details.put("num_gold", Integer.valueOf(5));
    	details.put("num_silver", Integer.valueOf(4));
    	details.put("num_bronze", Integer.valueOf(1));
        
        return details;
    }


    //////////  Events  //////////

    /**
     * Get all of the events listed in the olympics for a given sport
     *
     * @param sportId the ID of the sport we are filtering by
     * @return List of the events for that sport
     * @throws OlympicsDBException
     */
    ArrayList<HashMap<String, Object>> getEventsOfSport(Integer sportId) throws OlympicsDBException {
        // FIXME: Replace the following with REAL OPERATIONS!

        ArrayList<HashMap<String, Object>> events = new ArrayList<>();
        
        HashMap<String,Object> event1 = new HashMap<String,Object>();
        event1.put("event_id", Integer.valueOf(123));
        event1.put("sport_id", Integer.valueOf(3));
        event1.put("event_name", "Women's 5km Egg & Spoon");
        event1.put("event_gender", "W");
        event1.put("sport_venue", "ANZ Stadium");
        event1.put("event_start", new Date());
        events.add(event1);
        
        HashMap<String,Object> event2 = new HashMap<String,Object>();
        event2.put("event_id", Integer.valueOf(123));
        event2.put("sport_id", Integer.valueOf(3));
        event2.put("event_name", "Men's 40km Cross-country Hopping");
        event2.put("event_gender", "M");
        event2.put("sport_venue", "Bennelong Point");
        event2.put("event_start", new Date());
        events.add(event2);

        return events;
    }

    /**
     * Retrieve the results for a single event
     * @param eventId the key of the event
     * @return a hashmap for each result in the event.
     * @throws OlympicsDBException
     */
    ArrayList<HashMap<String, Object>> getResultsOfEvent(Integer eventId) throws OlympicsDBException {
        // FIXME: Replace the following with REAL OPERATIONS!

    	ArrayList<HashMap<String, Object>> results = new ArrayList<>();

    	
        HashMap<String,Object> result1 = new HashMap<String,Object>();
        result1.put("participant", "The Frog, Kermit");
        result1.put("country_name", "Fraggle Rock");
        result1.put("medal", "Gold");
        results.add(result1);
        
        HashMap<String,Object> result2 = new HashMap<String,Object>();
        result2.put("participant", "Cirus, Miley");
        result2.put("country_name", "United States");
        result2.put("medal", "Silver");
        results.add(result2);
        
        HashMap<String,Object> result3 = new HashMap<String,Object>();
        result3.put("participant", "Bond, James");
        result3.put("country_name", "Great Britain");
        result3.put("medal", "Bronze");
        results.add(result3);

        HashMap<String,Object> result4 = new HashMap<String,Object>();
        result4.put("participant", "McKenzie, Namor");
        result4.put("country_name", "Atlantis");
        result4.put("medal", null);
        results.add(result4);

        return results;
    }


    ///////   Journeys    ////////

    /**
     * Array list of journeys from one place to another on a given date
     * @param journeyDate the date of the journey
     * @param fromPlace the origin, starting place.
     * @param toPlace the destination, place to go to.
     * @return a list of all journeys from the origin to destination
     */
    ArrayList<HashMap<String, Object>> findJourneys(String fromPlace, String toPlace, Date journeyDate) throws OlympicsDBException {
        // FIXME: Replace the following with REAL OPERATIONS!
        ArrayList<HashMap<String, Object>> journeys = new ArrayList<>();

        HashMap<String,Object> journey1 = new HashMap<String,Object>();
        journey1.put("journey_id", Integer.valueOf(17));
        journey1.put("vehicle_code", "XYZ124");
        journey1.put("origin_name", "SIT");
        journey1.put("dest_name", "Olympic Park");
        journey1.put("when_departs", new Date());
        journey1.put("when_arrives", new Date());
        journey1.put("available_seats", Integer.valueOf(3));
        journeys.add(journey1);
        
        return journeys;
    }
    
    ArrayList<HashMap<String,Object>> getMemberBookings(String memberID) throws OlympicsDBException {
        ArrayList<HashMap<String,Object>> bookings = new ArrayList<HashMap<String,Object>>();
        
        // FIXME: DUMMY FUNCTION NEEDS TO BE PROPERLY IMPLEMENTED
        HashMap<String,Object> bookingex1 = new HashMap<String,Object>();
        bookingex1.put("journey_id", Integer.valueOf(17));
        bookingex1.put("vehicle_code", "XYZ124");
        bookingex1.put("origin_name", "SIT");
        bookingex1.put("dest_name", "Olympic Park");
        bookingex1.put("when_departs", new Date());
        bookingex1.put("when_arrives", new Date());
        bookings.add(bookingex1);

        HashMap<String,Object> bookingex2 = new HashMap<String,Object>();
        bookingex2.put("journey_id", Integer.valueOf(25));
        bookingex2.put("vehicle_code", "ABC789");
        bookingex2.put("origin_name", "Olympic Park");
        bookingex2.put("dest_name", "Sydney Airport");
        bookingex2.put("when_departs", new Date());
        bookingex2.put("when_arrives", new Date());
        bookings.add(bookingex2);
        
        return bookings;
    }
                
    /**
     * Get details for a specific journey
     * 
     * @return Various details of journey - see JourneyDetails.java
     * @throws OlympicsDBException
     */
    public HashMap<String,Object> getJourneyDetails(Integer jouneyID) throws OlympicsDBException {
        // FIXME: REPLACE FOLLOWING LINES WITH REAL OPERATION
    	HashMap<String,Object> details = new HashMap<String,Object>();

    	details.put("journey_id", Integer.valueOf(17));
    	details.put("vehicle_code", "XYZ124");
        details.put("origin_name", "SIT");
        details.put("dest_name", "Olympic Park");
        details.put("when_departs", new Date());
        details.put("when_arrives", new Date());
        details.put("capacity", Integer.valueOf(6));
        details.put("nbooked", Integer.valueOf(3));
    	
        return details;
    }
    
    public HashMap<String,Object> makeBooking(String byStaff, String forMember, Date departs) throws OlympicsDBException {
    	HashMap<String,Object> booking = null;
    	
        // FIXME: DUMMY FUNCTION NEEDS TO BE PROPERLY IMPLEMENTED
    	booking = new HashMap<String,Object>();
        booking.put("vehicle", "TR870R");
    	booking.put("start_day", "21/12/2020");
    	booking.put("start_time", new Date());
    	booking.put("to", "SIT");
    	booking.put("from", "Wentworth");
    	booking.put("booked_by", "Mike");
    	booking.put("whenbooked", new Date());
    	return booking;
    }
    
    public HashMap<String,Object> getBookingDetails(String memberID, Integer journeyId) throws OlympicsDBException {
    	HashMap<String,Object> booking = null;

        // FIXME: DUMMY FUNCTION NEEDS TO BE PROPERLY IMPLEMENTED
    	booking = new HashMap<String,Object>();

    	booking.put("journey_id", journeyId);
        booking.put("vehicle_code", "TR870R");
    	booking.put("when_departs", new Date());
    	booking.put("dest_name", "SIT");
    	booking.put("origin_name", "Wentworth");
    	booking.put("bookedby_name", "Mrs Piggy");
    	booking.put("bookedfor_name", "Mike");
    	booking.put("when_booked", new Date());
    	booking.put("when_arrives", new Date());
    	

        return booking;
    }
    
	public ArrayList<HashMap<String, Object>> getSports() throws OlympicsDBException {
		ArrayList<HashMap<String,Object>> sports = new ArrayList<HashMap<String,Object>>();
		
		// FIXME: DUMMY FUNCTION NEEDS TO BE PROPERLY IMPLEMENTED
		HashMap<String,Object> sport1 = new HashMap<String,Object>();
		sport1.put("sport_id", Integer.valueOf(1));
		sport1.put("sport_name", "Chillaxing");
		sport1.put("discipline", "Couch Potatoing");
		sports.add(sport1);
		
		HashMap<String,Object> sport2 = new HashMap<String,Object>();
		sport2.put("sport_id", Integer.valueOf(2));
		sport2.put("sport_name", "Frobnicating");
		sport2.put("discipline", "Tweaking");
		sports.add(sport2);
		
		HashMap<String,Object> sport3 = new HashMap<String,Object>();
		sport3.put("sport_id", Integer.valueOf(3));
		sport3.put("sport_name", "Frobnicating");
		sport3.put("discipline", "Fiddling");
		sports.add(sport3);
		
		return sports;
	}


    /////////////////////////////////////////
    /// Functions below don't need
    /// to be touched.
    ///
    /// They are for connecting and handling errors!!
    /////////////////////////////////////////

    /**
     * Default constructor that simply loads the JDBC driver and sets to the
     * connection details.
     *
     * @throws ClassNotFoundException if the specified JDBC driver can't be
     * found.
     * @throws OlympicsDBException anything else
     */
    DatabaseBackend(InputStream config) throws ClassNotFoundException, OlympicsDBException {
    	Properties props = new Properties();
    	try {
			props.load(config);
		} catch (IOException e) {
			throw new OlympicsDBException("Couldn't read config data",e);
		}

    	dbUser = props.getProperty("username");
    	dbPass = props.getProperty("userpass");
    	String port = props.getProperty("port");
    	String dbname = props.getProperty("dbname");
    	String server = props.getProperty("address");;
    	
        // Load JDBC driver and setup connection details
    	String vendor = props.getProperty("dbvendor");
		if(vendor==null) {
    		throw new OlympicsDBException("No vendor config data");
    	} else if ("postgresql".equals(vendor)) { 
    		Class.forName("org.postgresql.Driver");
    		connstring = "jdbc:postgresql://" + server + ":" + port + "/" + dbname;
    	} else if ("oracle".equals(vendor)) {
    		Class.forName("oracle.jdbc.driver.OracleDriver");
    		connstring = "jdbc:oracle:thin:@" + server + ":" + port + ":" + dbname;
    	} else throw new OlympicsDBException("Unknown database vendor: " + vendor);
		
		// test the connection
		Connection conn = null;
		try {
			conn = getConnection();
		} catch (SQLException e) {
			throw new OlympicsDBException("Couldn't open connection", e);
		} finally {
			reallyClose(conn);
		}
    }

	/**
	 * Utility method to ensure a connection is closed without 
	 * generating any exceptions
	 * @param conn Database connection
	 */
	private void reallyClose(Connection conn) {
		if(conn!=null)
			try {
				conn.close();
			} catch (SQLException ignored) {}
	}

    /**
     * Construct object with open connection using configured login details
     * @return database connection
     * @throws SQLException if a DB connection cannot be established
     */
    private Connection getConnection() throws SQLException {
        Connection conn;
        conn = DriverManager.getConnection(connstring, dbUser, dbPass);
        return conn;
    }


    
}

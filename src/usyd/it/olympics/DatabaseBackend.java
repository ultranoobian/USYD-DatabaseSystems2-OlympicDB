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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.io.InputStream;
import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
			String sql = "SELECT m.member_id"
			+ " FROM Member m WHERE (m.member_id = ? AND m.pass_word = ?);";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, member);
			ps.setString(2, (new String(password)));
			ResultSet s = ps.executeQuery();
			details = new HashMap<String, Object>();
			if(s.next()){
				details.put("member_id", s.getString(1));
			} else {
				return null;
			}
			
			ps.close();
			reallyClose(conn);

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
		HashMap<String, Object> details = new HashMap<String, Object>();

		try {
			Connection conn = getConnection();

			String sql;
			PreparedStatement ps;
			ResultSet s;

			sql = "SELECT * FROM member_details(?)";
			ps = conn.prepareStatement(sql);
			ps.setString(1, memberID);

			s = ps.executeQuery();

			s.next();

			details.put("member_id", s.getString("member_id"));
			details.put("member_type", s.getString("member_type"));
			details.put("title", s.getString("title"));
			details.put("first_name", s.getString("given_names"));
			details.put("family_name", s.getString("family_name"));
			details.put("country_name", s.getString("country_name"));
			details.put("residence", s.getString("place_name"));
			details.put("num_bookings", s.getInt("bookings_count"));

			switch ((String) details.get("member_type")) {
			case "athlete":
				sql = "SELECT * FROM athlete_details(?)";
				ps = conn.prepareStatement(sql);
				ps.setString(1, memberID);
				s = ps.executeQuery();

				s.next();
				details.put("num_gold", s.getInt("g"));
				details.put("num_silver", s.getInt("s"));
				details.put("num_bronze", s.getInt("b"));

				Array teams = s.getArray("teams");
				if (teams != null) {
					String[] team_names = (String[]) teams.getArray();
					String teamlist = "";
					for (int i = 0; i < team_names.length; i++) {
						teamlist += team_names[i];
						if (i < team_names.length - 1) {
							teamlist += ",";
						}
					}
					details.put("teamlist", teamlist);
				} else {
					details.put("teamlist", null);
				}

				break;
			case "staff":
				sql = "SELECT * FROM staff_details(?)";
				ps = conn.prepareStatement(sql);
				ps.setString(1, memberID);
				s = ps.executeQuery();

				s.next();
				details.put("bookings_created", s.getInt("bookings_made_count"));

				break;
			case "official":
				sql = "SELECT * FROM official_details(?)";
				ps = conn.prepareStatement(sql);
				ps.setString(1, memberID);
				s = ps.executeQuery();

				s.next();
				details.put("running_events_count", s.getInt("running_events_count"));
				// details.put("running_events", s.getS("runing_events")); Array
				// get

				Array events = s.getArray("running_events");
				if (events != null) {
					String[] event_names = (String[]) events.getArray();
					String eventlist = "";
					for (int i = 0; i < event_names.length; i++) {
						eventlist += event_names[i];
						if (i < event_names.length - 1) {
							eventlist += ",";
						}
					}
					details.put("eventlist", eventlist);
				} else {
					details.put("eventlist", null);
				}
				break;

			default:
				break;
			}

			ps.close();
			s.close();
			reallyClose(conn);
		} catch (Exception e) {
			throw new OlympicsDBException("Error retrieving member details", e);
		}
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
        try{
        Connection conn = getConnection();
        	Statement stmt=null;
        	
        	String query="SELECT event_id,sport_id,event_name,event_gender,event_start,place_name "
        			+ "FROM Event JOIN Place On(Place_id=sport_venue) "
        			+ "WHERE sport_id = " 
        			+sportId;
        		stmt = conn.createStatement();
        		ResultSet rset = stmt.executeQuery(query);
        	while(rset.next()){	
        		HashMap<String,Object> eventNo = new HashMap<String,Object>();
        		eventNo.put("event_id", Integer.valueOf(rset.getInt("event_id")));
        		eventNo.put("sport_id", Integer.valueOf(rset.getInt("sport_id")));
        		 eventNo.put("event_name", rset.getString("event_name"));
        	     eventNo.put("event_gender",rset.getString("event_gender"));
        	     eventNo.put("sport_venue",rset.getString("place_name"));
        	     eventNo.put("event_start", rset.getTimestamp("event_start"));
        		events.add(eventNo);
        	}
        	reallyClose(conn);
        }catch(Exception e){
        	e.printStackTrace();
        	
        }

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
    	 try{
    	        Connection conn = getConnection();
    	        	Statement stmt=null;
    	        	boolean isIndividual =false;
    	        	String query="SELECT * FROM Participates WHERE event_id="+eventId;
    	        	stmt = conn.createStatement();
	        		ResultSet rset = stmt.executeQuery(query);
	        		if(rset.next()){
	        			
		        		if(rset.next()){
		        			isIndividual=true;
		        		}
		        		query="SELECT athlete_id ,country_name,medal,family_name,given_names "
	    	        			+ "FROM Participates join Member on(member_id=athlete_id) join Country using(country_code) "
	    	        			+" WHERE event_id="+eventId
	    	        			+" Order by family_name";
		        		rset = stmt.executeQuery(query);
		        		
		        		while(rset.next()){	
	    	        		HashMap<String,Object> resultNo = new HashMap<String,Object>();
	    	        		resultNo.put("participant", rset.getString("family_name")+", "+rset.getString("given_names"));
	    	        		resultNo.put("country_name",rset.getString("country_name"));
	    	        		String medals=rset.getString("medal");
	    	        		if(("G").equals(medals)){
	    	        			medals="Gold";
	    	        		}else if(("S").equals(medals)){
	    	        			medals="Silver";
	    	        		}else if(("B").equals(medals)){
	    	        			medals="Bronze";
	    	        		}
	    	        		resultNo.put("medal",medals);
	    	        		results.add(resultNo);
	    	        	}
	        		}
	        		
    	        	//team
    	        	if(isIndividual==false){
    	        		query="SELECT tm.athlete_id ,country_name,medal,family_name,given_names "
	    	        			+ " FROM	(Team  t join Teammember tm on t.event_id=tm.event_id and t.team_name=tm.team_name )join Member  on(athlete_id = member_id) join Country   on (Member.country_code=Country.country_code) "
	    	        			+" WHERE t.event_id= "+eventId
	    	        			+" Order by family_name ";
		        		rset = stmt.executeQuery(query);
		        		
		        		while(rset.next()){	
	    	        		HashMap<String,Object> resultNo = new HashMap<String,Object>();
	    	        		resultNo.put("participant", rset.getString("family_name")+", "+rset.getString("given_names"));
	    	        		resultNo.put("country_name",rset.getString("country_name"));
	    	        		String medals=rset.getString("medal");
	    	        		if(("G").equals(medals)){
	    	        			medals="Gold";
	    	        		}else if(("S").equals(medals)){
	    	        			medals="Silver";
	    	        		}else if(("B").equals(medals)){
	    	        			medals="Bronze";
	    	        		}
	    	        		resultNo.put("medal",medals);
	    	        		results.add(resultNo);
	    	        	}
    	        		
    	        	}
	        		
    	        	reallyClose(conn);
    	        }catch(Exception e){
    	        	e.printStackTrace();
    	        	
    	        }
    	
    	

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
     * @param journey_id
     */
    public HashMap<String,Object> getJourneyDetails(Integer jouneyId) throws OlympicsDBException {
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

    public HashMap<String,Object> makeBooking(String byStaff, String forMember, String vehicle, Date departs) throws OlympicsDBException {
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
		try{
		Connection conn= getConnection();
		Statement stmt=null;
    	
    	String query="SELECT sport_id,sport_name,discipline "
    			+ "FROM Sport ";
    		stmt = conn.createStatement();
    		ResultSet rset = stmt.executeQuery(query);
    	while(rset.next()){	
    		HashMap<String,Object> sportNo = new HashMap<String,Object>();
    		sportNo.put("sport_id", Integer.valueOf(rset.getInt("sport_id")));
    		 sportNo.put("sport_name", rset.getString("sport_name"));
    	     sportNo.put("discipline",rset.getString("discipline"));
    		sports.add(sportNo);
    	}
    	if (conn != null)
    	{
    		conn.close();
    	}
		}catch(Exception e){
        	e.printStackTrace();
        	
        }
		
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

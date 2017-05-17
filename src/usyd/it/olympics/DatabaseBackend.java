package usyd.it.olympics;


/**
 * Database back-end class for simple gui.
 * 
 * The DatabaseBackend class defined in this file holds all the methods to 
 * communicate with the database and pass the results back to the GUI.
 *
 *
 * Make sure you update the dbname variable to your own database name. If you
 * want to run this class on its own you also need to put your login details
 * (userid and password) into main() at the bottom of the file.
 */

import usyd.it.olympics.data.BayBookingListLine;
import usyd.it.olympics.data.BayListLineDetails;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;

import usyd.it.olympics.data.BayDetails;
import usyd.it.olympics.data.OlympicEvent;

/**
 * Database interfacing backend for client. This class uses JDBC to connect to
 * the database, and provides methods to obtain query data.
 *
 * @author Bryn Jeffries {@literal <bryn.jeffries@sydney.edu.au>}
 */
public class DatabaseBackend {

    ///////////////////////////////
    /// DB Connection details
    ///////////////////////////////
    private final String dbUser;
    private final String dbPass;
	private final String connstring;


    ///////////////////////////////
    /// Student Defined Functions
    ///////////////////////////////

    /**
     * Validate memberID details
     * 
     * Implements Core Functionality (a)
     *
     * @return true if username is for a valid memberID and password is correct
     * @throws OlympicsDBException 
     * @throws SQLException
     */
    public boolean checkLogin(String member, char[] password) throws OlympicsDBException  {
        boolean valid = false; // Assume the worst.
        try {
            Connection conn = getConnection();
        	
	        // FIXME: REPLACE FOLLOWING LINES WITH REAL OPERATION
	        // Don't forget you have memberID variables memberUser available to
	        // use in a query.
	        // Query whether login (memberID, password) is correct...
	        valid = (member.equals("testuser") && new String(password).equals("testpass"));

        } catch (Exception e) {
            complain("Error checking login details");
            throw new OlympicsDBException("Error checking login details", e);
        }
        return valid;
    }

    /**
     * Obtain details for the current memberID
     * @param memberID 
     *
     *
     * @return text to be displayed in the home screen
     * @throws OlympicsDBException
     */
    public String memberDetails(String memberID) throws OlympicsDBException {
        // FIXME: REPLACE FOLLOWING LINES WITH REAL OPERATION
        String details = "Hello Mr Joe Bloggs";
        details = details.concat("\nYou are an: " + "Athlete");
        details = details.concat("\nYou are from: " + "Australia");
        details = details.concat("\nYour live at: " + "SIT");
        details = details.concat("\nYour medal tally is:");
        details = details.concat("\n\tGold: 5");
        details = details.concat("\n\tSilver: 4");
        details = details.concat("\n\tBronze: 1");
        details = details.concat("\nYou have made 20 bookings");
        return details;
    }


    /**
     * Get the events listed in the olympics
     * @return List of events
     * @throws OlympicsDBException
     */
    ArrayList<OlympicEvent> allEvents() throws OlympicsDBException {
        // FIXME: Replace The following with REAL OPERATIONS!

        ArrayList<OlympicEvent> events = new ArrayList<>();
        events.add(new OlympicEvent("200M Freestyle", new Date(),"Points", "Swimming", "SIT"));
        return events;
    }


    /**
     * Get status of user in their current hunt
     * @return Details of hunt
     * @throws OlympicsDBException
     */
    BayDetails getFavouriteBay() throws OlympicsDBException {
        
        // FIXME: REPLACE FOLLOWING LINES WITH REAL OPERATION
        // See the constructor in BayStatus.java
        BayDetails details =  new BayDetails(92, "Left of driveway", 
                "62" , "Bourne Drive, Ludlum", "Scarlatti",
                12543.233, 12787.34665, 
                3, 4, 6,
                9, 5, 10, 4, 
                new int[]{10, 11, 2, 3});
        return details;
    }
    
    ArrayList<BayBookingListLine> allBookings() throws OlympicsDBException {
        ArrayList<BayBookingListLine> bookings = new ArrayList<BayBookingListLine>();

        // FIXME: DUMMY FUNCTION NEEDS TO BE PROPERLY IMPLEMENTED
        bookings.add(new BayBookingListLine(73, "Mike the Speedy Potato", "Bay 5, 7A Ocean View, ", new Timestamp(111,03,11,13,00, 0, 0), new Timestamp(111,03,11,23,99, 0, 0)));
        return bookings;
    }
                
    /**
     * Obtain a list of all available bays
     *
     * Implements Core Functionality (e) 
     * 
     * @return list of bays
     * @throws OlympicsDBException
     */
    public ArrayList<BayListLineDetails> getMatchingBays(String address) throws OlympicsDBException {
        ArrayList<BayListLineDetails> bays = new ArrayList<BayListLineDetails>();

        // FIXME: REPLACE FOLLOWING LINES WITH REAL OPERATION
        // BayListLineDetails is a simple container class to hold the required attributes of an individual hunt
        // create one as "new BayListLineDetails(name, start, capacity, available)"
        bays.add(new BayListLineDetails(42, "Lot 3", "7", "Acacia Crescent, Foggy Bottom", "Newcastle"));
        bays.add(new BayListLineDetails(12, "Space 43", "14B", "Threepwood Avenue, Guybrushbury", "Monkey Island"));
        bays.add(new BayListLineDetails(98, "Only bay", "11/12", "LeChuck Heights, Melee Heights", "Monkey Island"));

        return bays;
    }

    /**
     * Get details for a specific bay
     * 
     * @return Various details of hunt - see BayDetails.java
     * @throws OlympicsDBException
     */
    public BayDetails getBayDetails(int bay) throws OlympicsDBException {
        // FIXME: REPLACE FOLLOWING LINES WITH REAL OPERATION
        // See the constructor in BayDetails.java
        BayDetails details = new BayDetails(42, 
                "Lot 5", 
                "7", "Acacia Avenue, Foggy Bottom", "Newcastle",
                123.456, 874.3455,
                3, 2.5, 5,
                10, 4, 0, 0,
                new int[]{});
        return details;
    }
    
    HashMap<Integer, String> getMemberCars() throws OlympicsDBException {
        HashMap<Integer, String> cars = new HashMap<Integer, String>();
        
        // FIXME: REPLACE FOLLOWING LINES WITH REAL OPERATION
        cars.put(7863,"Growler");
        cars.put(8973,"Rumbler");
        cars.put(9331,"Lemon");        
        
        return cars;
    }

    public String makeBooking(int bay, int car, Date bookingStart, Date bookingEnd) throws OlympicsDBException {
        String newBooking;

        // FIXME: DUMMY FUNCTION NEEDS TO BE PROPERLY IMPLEMENTED
        newBooking = "SUCCESS!\nBooking made for bay at"
                + "\nLot 3, 4B Yoda Lane,"
                + "\nDantooine"
                + "\n\nFor your car Annie (TRS874)"
                + "\nOn Tuesday, May 18 2015"
                + "\nFrom 8am to 10am"
                + "\nPrice $10.05";

        return newBooking;
    }
    
    public String getBookingDetails(int bookingId) throws OlympicsDBException {
        String booking;

        // FIXME: DUMMY FUNCTION NEEDS TO BE PROPERLY IMPLEMENTED
        booking = "Booking made for bay at"
                + "\nSpace 4, 84 Water Lane,"
                + "\nGordon Hill"
                + "\n\nFor your car Yasmine (VFG434)"
                + "\nOn Monday,April 21 2014"
                + "\nFrom 9am to 2pm"
                + "\nPrice $21.12";

        return booking;
    }


    /////////////////////////////////////////
    /// Functions below don't need
    /// to be touched.
    ///
    /// They are for connecting and handling errors!!
    /////////////////////////////////////////


    /**
     * Print any log/error message to console Use this for debugging, not for
 messages to the user - throw a OlympicsDBException instead.
     *
     * @param message
     */
    private void complain(String message) {
        System.err.println("DatabaseBackend: " + message);
    }

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

package usyd.it.olympics;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.swing.SwingUtilities;

import usyd.it.olympics.data.BookingDetails;
import usyd.it.olympics.gui.GuiFrontEnd;

public class OlympicsDBClient {
    // All GUI stuff is performed by this object

    private final GuiFrontEnd gui;
    // All database operations (logging in, running queries) are performed by this object
    private DatabaseBackend db;
	private String memberId; // Member ID
	private String memberType;

    OlympicsDBClient(String config) {
        // Make sure the DB backend works
        try {
            db = new DatabaseBackend(new FileInputStream(config)); // Note, doesn't connect to DB
        } catch (Exception e) {
            // Can't do much so die noisily.
            e.printStackTrace();
            System.exit(1);
        }
        gui = new GuiFrontEnd(this);
        setMessage("Welcome to Olympics DB Client.");
    }

    private void setMessage(String msg) {
        gui.setStatus(msg);
    }

    //
    // Client user login/logout
    //
    public void login(String memUser, char [] memPass) {
        setMessage("Connecting to DB.");
        try {
        	memberId = null;
        	HashMap<String, Object> basicDetails = db.checkLogin(memUser, memPass);
            if(basicDetails!=null) {
            	memberId = memUser;
            	memberType = (String) basicDetails.get("member_type");
            	setMessage("Verified login, Fetching member details");
            	HashMap<String, Object> fullDetails = db.memberDetails(memberId, memberType);
            	gui.getMainMenuScreen().showMemberDetails(fullDetails);
            	gui.showMainMenuScreen();
            	setMessage("Login successful.");
            } else {
            	setMessage("Login details incorrect.");
            }
        } catch (OlympicsDBException e) {
            setMessage(e.getMessage());
        }
    }

    public void logout() {
        setMessage("Logging out");
        memberId = null;
        gui.showLoginScreen();
        setMessage("Logged out");
    }

    //
    // Member info 
    //
    public void showMemberDetails() {
        setMessage("Fetching member details.");
        try {
            HashMap<String, Object> member = db.memberDetails(memberId, memberType);
            gui.getMainMenuScreen().showMemberDetails(member);
            gui.showMainMenuScreen();
            setMessage("Details fetched.");
        } catch (OlympicsDBException e) {
            setMessage(e.getMessage());
        }
    }


    
    /**
     * Create a new client GUI, running on its own event thread
     *
     * @param args Ignored
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                new OlympicsDBClient("olympicsdb.properties");
            }
        });
    }

    //
    // Journey Finder
    //
    public void showMatchingJourneys(String address) {
        setMessage("Fetching journey availabilities.");
        try {
            ArrayList<HashMap<String, Object>> journeys = db.getMatchingJourneys(address);
            gui.getJourneyFinderScreen().showTuples(journeys);
            setMessage("All journeys fetched.");
        } catch (OlympicsDBException e) {
            setMessage(e.getMessage());
            gui.getJourneyFinderScreen().showTuples(new ArrayList<HashMap<String, Object>>());
        }
        gui.showJourneyFinderScreen();
    }
    
    public void showJourneyAvailability() {
        setMessage("Fetching journey availabilities.");
        gui.showJourneyFinderScreen();
    }
    
    public void getJourneyDetails(HashMap<String, Object> hashMap) {
        setMessage("Retrieving details");
        try {
        	HashMap<String, Object> details = db.getJourneyDetails(0);//hashMap);
            gui.getJourneyDetailsScreen().showJourneyDetails(details);
            gui.showJourneyDetailsScreen();
            setMessage("Details retrieved");
        } catch (OlympicsDBException e) {
            setMessage(e.getMessage());
        }
    }

    public void makeBooking(String forMember, String vehicle, Date departs) {
        setMessage("Submitting booking");
        try {
        	HashMap<String,Object> bookingDetails = db.makeBooking(memberId, 
        			forMember, departs);
        	if(bookingDetails==null) {
                setMessage("Could not make booking");
        	} else {
                gui.getReportScreen().show(BookingDetails.getSummary(bookingDetails));
                gui.showReportScreen();
                setMessage("Submission complete");
        	}
        } catch (OlympicsDBException e) {
            setMessage(e.getMessage());
        }
    }
    
    public void showHistory() {
        setMessage("Fetching booking history.");
        try {
            ArrayList<HashMap<String,Object>> bookings = db.allBookings(memberId);
            gui.getHistoryScreen().showBookings(bookings);
            gui.showHistoryScreen();
            setMessage("All bookings fetched.");
        } catch (OlympicsDBException e) {
            setMessage(e.getMessage());
        }    
    }

    public void startBooking(int journeyId) {
        setMessage("Fetching details to start booking.");
        //HashMap<String, Object> place = db.getMemberHome(memberID);
		//Integer placeid = Place.getPlaceId(place);
		gui.getBookingsCreationScreen().startBooking(journeyId, memberId);
		gui.showBookingsCreationScreen();
		setMessage("Details fetched."); 
    }

    public void showBookingDetails(int journeyid) {
       setMessage("Getting booking details");
        try {
        	HashMap<String,Object> bookingDetails = db.getBookingDetails(memberId, journeyid);
            gui.getReportScreen().show(BookingDetails.getSummary(bookingDetails));
            gui.showReportScreen();
            setMessage("Details fetched.");
        } catch (OlympicsDBException e) {
            setMessage(e.getMessage());
        }
    }

	public void getEventResults(HashMap<String, Object> tuple) {
		// TODO Auto-generated method stub
		
	}

	public void showEventBrowser() {
       setMessage("Getting sports");
        try {
        	ArrayList<HashMap<String, Object>> sports = db.getSports();
            gui.getEventBrowserScreen().setSports(sports);
            gui.showEventBrowserScreen();
            setMessage("Sports fetched.");
        } catch (OlympicsDBException e) {
            setMessage(e.getMessage());
        }
	}


}

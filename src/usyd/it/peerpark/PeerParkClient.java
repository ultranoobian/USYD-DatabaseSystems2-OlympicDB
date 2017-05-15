package usyd.it.peerpark;

import usyd.it.peerpark.data.BayBookingListLine;
import usyd.it.peerpark.data.BayListLineDetails;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import javax.swing.SwingUtilities;
import usyd.it.peerpark.data.BayDetails;
import usyd.it.peerpark.gui.GuiFrontEnd;

public class PeerParkClient {
    // All GUI stuff is performed by this object

    private final GuiFrontEnd gui;
    // All database operations (logging in, running queries) are performed by this object
    private DatabaseBackend db;

    PeerParkClient() {
        // Make sure the DB backend works
        try {
            db = new DatabaseBackend(); // Note, doesn't connect to DB
        } catch (ClassNotFoundException e) {
            // Can't do much so die noisily.
            e.printStackTrace();
            System.exit(1);
        }
        gui = new GuiFrontEnd(this);
        setMessage("Welcome to PeerPark Client.");
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
            db.openConnection(memUser, memPass);
            setMessage("Verified login, Fetching member details");
            String member = db.getMemberDetails();
            gui.getMainMenuScreen().showMemberDetails(member);
            gui.showMainMenuScreen();
            setMessage("Login successful.");
        } catch (PeerParkException e) {
            setMessage(e.getMessage());
            db.closeConnection();
        }
    }

    public void logout() {
        setMessage("Logging out");
        db.closeConnection();
        gui.showLoginScreen();
        setMessage("Logged out");
    }

    //
    // Member info 
    //
    public void showMemberDetails() {
        setMessage("Fetching member details.");
        try {
            String member = db.getMemberDetails();
            gui.getMainMenuScreen().showMemberDetails(member);
            gui.showMainMenuScreen();
            setMessage("Details fetched.");
        } catch (PeerParkException e) {
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
                new PeerParkClient();
            }
        });
    }

    //
    // Bay Finder
    //
    public void showMatchingBays(String address) {
        setMessage("Fetching bay availabilities.");
        try {
            ArrayList<BayListLineDetails> bays = db.getMatchingBays(address);
            gui.getBayFinderScreen().showBays(bays);
            setMessage("All bays fetched.");
        } catch (PeerParkException e) {
            setMessage(e.getMessage());
            gui.getBayFinderScreen().showBays(new ArrayList<BayListLineDetails>());
        }
        gui.showBayFinderScreen();
    }
    
    public void showBayAvailability() {
//        setMessage("Fetching bay availabilities.");
        gui.showBayFinderScreen();
    }
    
    public void getBayDetails(int bay) {
        setMessage("Retrieving details");
        try {
            BayDetails details = db.getBayDetails(bay);
            gui.getBayDetailsScreen().showBayDetails(details);
            gui.showBayDetailsScreen();
            setMessage("Details retrieved");
        } catch (PeerParkException e) {
            setMessage(e.getMessage());
        }
    }

    public void makeBooking(int bay, int car, Date bookingStart, Date bookingEnd) {
        setMessage("Submitting booking");
        try {
            String bookingDetails = db.makeBooking(bay, car,
                            bookingStart, bookingEnd);
                gui.getReportScreen().show(bookingDetails);
                gui.showReportScreen();
                setMessage("Submission complete");
        } catch (PeerParkException e) {
            setMessage(e.getMessage());
        }
    }
    
    public void showFavouriteBay() {
        setMessage("Retrieving bay details");
        try {
            BayDetails bay = db.getFavouriteBay();
            if (bay == null)
                setMessage("No bay details found");
            else {    
                gui.getBayDetailsScreen().showBayDetails(bay);
                gui.showBayDetailsScreen();
                setMessage("Bay details retrieved");
            }
        } catch (PeerParkException e) {
            setMessage(e.getMessage());
        }    
    }

    public void showHistory() {
        setMessage("Fetching booking history.");
        try {
            ArrayList<BayBookingListLine> bookings = db.getBookings();
            gui.getHistoryScreen().showBookings(bookings);
            gui.showHistoryScreen();
            setMessage("All bookings fetched.");
        } catch (PeerParkException e) {
            setMessage(e.getMessage());
        }    
    }

    public void startBooking(int bayId) {
        setMessage("Fetching details to start booking.");
        try {
            HashMap<Integer, String> cars = db.getMemberCars();
            gui.getBookingsCreationScreen().startBooking(bayId, cars);
            gui.showBookingsCreationScreen();
            setMessage("Details fetched.");
        } catch (PeerParkException e) {
            setMessage(e.getMessage());
        } 
    }

    public void showBookingDetails(int bookingId) {
       setMessage("Getting booking details");
        try {
            String bookingDetails = db.getBookingDetails(bookingId);
            gui.getReportScreen().show(bookingDetails);
            gui.showReportScreen();
            setMessage("Details fetched.");
        } catch (PeerParkException e) {
            setMessage(e.getMessage());
        }
    }


}
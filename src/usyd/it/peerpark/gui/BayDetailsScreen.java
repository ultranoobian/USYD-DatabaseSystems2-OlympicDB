package usyd.it.peerpark.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JTextArea;
import usyd.it.peerpark.PeerParkClient;
import usyd.it.peerpark.data.BayDetails;

/**
 * Very simple bay details screen, cloned from HomeScreen
 * @author Bryn
 */
public class BayDetailsScreen extends GuiScreen {

    private final JTextArea description;
    private final JButton btnMakeBooking;
    private Integer bayId;
    public BayDetailsScreen(PeerParkClient r) {
        super(r);
        panel_.setLayout(new BoxLayout(panel_, BoxLayout.Y_AXIS));

        description = new JTextArea();
        description.setEditable(false);
        panel_.add(description);
        
        // Extra panel to include buttons
        // Include a button to allow a booking to be made for the current bay
        btnMakeBooking = new JButton("Make Booking");
        panel_.add(btnMakeBooking);
        btnMakeBooking.setEnabled(false);
        btnMakeBooking.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                client_.startBooking(bayId);
            }
        });   
    }

    public void showBayDetails(BayDetails m) {
        bayId = m.getBayId();
        btnMakeBooking.setEnabled(true);
        String s = "Name: " + m.getSite()
                + "\nAddress: " + m.getHouseNum() + " " + m.getStreet()
                + ", " + m.getCity()
                + "\nGPS co-ords: (" + m.getGpsLong() + "," + m.getGpsLat() + ")"
                + "\nWeekday availability: " + m.getWkAvailStart() + ":00 to " + m.getWkAvailEnd() + ":00"
                + "\nWeekend availability: " + m.getWeAvailStart() + ":00 to " + m.getWeAvailEnd() + ":00";

        int[] hours = m.getTodayHours();
        if (hours.length<1) {
            s = s.concat("\nThere is no availability today");
        } else {
            String hrsText = "\nBay is available at the following hours";
            for(int hour : hours) {
                hrsText = hrsText.concat("\n" + hour + ":00");
            }
            s= s.concat(hrsText);
        }
        description.setText(s);
    }
}

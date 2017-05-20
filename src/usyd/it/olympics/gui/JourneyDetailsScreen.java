package usyd.it.olympics.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JTextArea;

import usyd.it.olympics.OlympicsDBClient;

/**
 * Very simple details screen, cloned from HomeScreen
 * @author Bryn
 */
public class JourneyDetailsScreen extends GuiScreen {

    private final JTextArea description;
    private final JButton btnMakeBooking;
    private Integer journey_id;
    public JourneyDetailsScreen(OlympicsDBClient r) {
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
                client_.startBooking(journey_id);
            }
        });   
    }

    public void showJourneyDetails(HashMap<String, Object> journey) {
        journey_id = (Integer) journey.get("journey_id");
        btnMakeBooking.setEnabled(true);
        
        String s = "Vehicle: " + journey.get("vehicle_code")
                + "\nLeaves " + journey.get("from_name") + " at " + (Date) journey.get("depart_time")
                + "\nand travels to " + journey.get("to_name")
                + "\nCurrently there are  " + (Integer) journey.get("nbooked") + " bookings for this journey";
        description.setText(s);
    }
}

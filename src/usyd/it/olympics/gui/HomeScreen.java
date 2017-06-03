package usyd.it.olympics.gui;

import java.awt.BorderLayout;
import java.util.HashMap;

import javax.swing.JTextArea;
import usyd.it.olympics.OlympicsDBClient;

/**
 * Very simple home screen: Nothing fancy, just prints player details in a text area.
 * @author Bryn
 */
public class HomeScreen extends GuiScreen {

    private final JTextArea description;

    public HomeScreen(OlympicsDBClient r) {
        super(r);
        panel_.setLayout(new BorderLayout(0, 0));

        description = new JTextArea();
        description.setEditable(false);
        panel_.add(description);
    }


	public void showMemberDetails(HashMap<String, Object> details) {
		String message = "Hello " + details.get("title") + " " + details.get("first_name") + " "
				+ details.get("family_name");
		message = message.concat("\nYou are an: " + details.get("member_type"));
		message = message.concat("\nYou are from: " + details.get("country_name"));
		message = message.concat("\nYour live at: " + details.get("residence"));
		message = message.concat("\nYou have " + details.get("num_bookings") + " bookings");

		// This should be type-specific
		message = message.concat("\n\nAs a " + (String) details.get("member_type") + ":");
		switch ((String) details.get("member_type")) {
		case "staff":
			if (details.get("bookings_created") != null) {
				message = message.concat(
						"\nYou have made " + details.get("bookings_created") + " Booking(s) for other people so far");
			} else {
				message = message.concat("\nYou have not made any bookings for other people so far");
			}
			break;
		case "official":
			if (details.get("eventlist") != null) {
				message = message.concat("\nYou are running these events: " + details.get("eventlist"));
			} else {
				message = message.concat("\nYou not running an events");
			}
			break;
		case "athlete":
			if (details.get("teamlist") != null) {
				message = message.concat("\nYou are part of Team(s): " + details.get("teamlist"));
			} else {
				message = message.concat("\nYou are not part of a Olympic Team");
			}
			message = message.concat("\nYour medal tally is:");
			message = message.concat("\n\tGold: " + details.get("num_gold"));
			message = message.concat("\n\tSilver: " + details.get("num_silver"));
			message = message.concat("\n\tBronze: " + details.get("num_bronze"));
			break;
		}


		description.setText(message);
	}
}

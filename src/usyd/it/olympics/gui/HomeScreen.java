package usyd.it.olympics.gui;

import java.awt.BorderLayout;
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

    public void showMemberDetails(String message) {
        description.setText(message);
    }
}

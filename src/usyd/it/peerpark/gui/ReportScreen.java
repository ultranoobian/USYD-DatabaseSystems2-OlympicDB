package usyd.it.peerpark.gui;

import java.awt.BorderLayout;
import javax.swing.JTextPane;
import usyd.it.peerpark.PeerParkClient;

public class ReportScreen extends GuiScreen {

    private JTextPane reportPane;

    public ReportScreen(PeerParkClient r) {
        super(r);
        panel_.setLayout(new BorderLayout(0, 0));
        reportPane = new JTextPane();
        panel_.add(reportPane);

    }

    public void show(String text) {
        reportPane.setText(text);
    }
}

package usyd.it.olympics.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import usyd.it.olympics.OlympicsDBClient;

/**
 * Main class for GUI, containing all screens and providing methods
 * to navigate between them.
 * @author Bryn
 */
public final class GuiFrontEnd {
    /// Parent reference for button ActionListeners
    private OlympicsDBClient client;
    
    //
    // Top-level GUI objects
    //
    private final JFrame window;
    private final JPanel mainPanel;
    private final CardLayout screenSelect;
    private final JLabel statusMsg;
    
    //
    // References to individual mainPanel screens
    //
    private final JourneyFinderScreen journeyFinderScreen;
    private final HomeScreen mainMenuScreen;
    private final JourneyDetailsScreen journeyDetailsScreen;
    private final ReportScreen generalReportScreen;
    private final BookingsCreationScreen bookingsCreationScreen;
    private final BookingHistoryScreen historyScreen;
    
    // Navigation buttons
    private final JPanel navBar;
    private final HashMap<String, JButton> buttons = new HashMap<String, JButton>();
    private final String optHome;
//    private final String optFavourite;
    private final String optBrowseJourneys = "Browse Journeys";
    private final String optHistory = "Booking History";
    private final String optLogOut = "Log Out";
    

    public GuiFrontEnd(OlympicsDBClient root) {
        client = root;
        window = new JFrame("Client GUI");
        window.setPreferredSize(new Dimension(800, 400));
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // This will be the content pane for the window
        JPanel content = new JPanel();
        content.setLayout(new BorderLayout());


        // Navigation buttons at top
        navBar = new JPanel();
        navBar.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
        
        // Put navigation buttons in bar
        optHome = "Home";
        addMenuOption(optHome, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                client.showMemberDetails();
            }
        });
        
        addMenuOption(optBrowseJourneys, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                client.showJourneyAvailability();
            }
        });

        addMenuOption(optHistory,new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                client.showHistory();
            }
        });

        addMenuOption(optLogOut, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                client.logout();
            }
        });

        setNavButtons(false);
        content.add(navBar, BorderLayout.NORTH);

        // Status messages at bottom
        JPanel statusPanel = new JPanel();
        statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
        statusMsg = new JLabel("Status");
        statusPanel.setMinimumSize(new Dimension(100, 40));
        statusPanel.setPreferredSize(new Dimension(100, 40));
        statusPanel.setLayout(new GridLayout(1, 1, 0, 0));
        statusPanel.add(statusMsg);
        content.add(statusPanel, BorderLayout.SOUTH);

        // All main interface stuff goes into a central panel
        mainPanel = new JPanel();
        screenSelect = new CardLayout();
        mainPanel.setLayout(screenSelect);

        registerScreen(new LoginScreen(root), "loginScreen");
        registerScreen(mainMenuScreen = new HomeScreen(root), "mainMenuScreen");
        registerScreen(journeyFinderScreen = new JourneyFinderScreen(root), "journeyFinderScreen");
        registerScreen(journeyDetailsScreen = new JourneyDetailsScreen(root), "journeyDetailsScreen");
        registerScreen(generalReportScreen = new ReportScreen(root), "generalReportScreen");
        registerScreen(historyScreen = new BookingHistoryScreen(root), "historyScreen");
        registerScreen(bookingsCreationScreen = new BookingsCreationScreen(root), "bookingsCreationScreen");

        content.add(mainPanel, BorderLayout.CENTER);
        window.setContentPane(content);
        window.pack();
        window.setVisible(true);
    }

    // Control all navbar buttons together
    public void setNavButtons(boolean status) {
        for (JButton button : buttons.values()) {
            button.setEnabled(status);
        }
    }
    
    public void setNavButton(String name, boolean status) {
        buttons.get(name).setEnabled(status);
    }
    
    public void showLoginScreen() {
        screenSelect.show(mainPanel, "loginScreen");
        setNavButtons(false);
    }

    public void showMainMenuScreen() {
        screenSelect.show(mainPanel, "mainMenuScreen");
        setNavButtons(true);
        setNavButton(optHome, false);
    }
        
    public void showJourneyFinderScreen() {
        screenSelect.show(mainPanel, "journeyFinderScreen");
        setNavButtons(true);
        setNavButton(optBrowseJourneys, false);
    }
    
    public void showJourneyDetailsScreen() {
        screenSelect.show(mainPanel, "journeyDetailsScreen");
        setNavButtons(true);
    }

    public void showReportScreen() {
        screenSelect.show(mainPanel, "generalReportScreen");
        setNavButtons(true);
    }
    
    public void showHistoryScreen() {
        screenSelect.show(mainPanel, "historyScreen");
        setNavButtons(true);
    }
    
    public void showBookingsCreationScreen() {
        screenSelect.show(mainPanel, "bookingsCreationScreen");
        setNavButtons(true);
    }

    public void setStatus(String msg) {
        statusMsg.setText(msg);
    }

    public HomeScreen getMainMenuScreen() {
        return mainMenuScreen;
    }
       
    public JourneyFinderScreen getJourneyFinderScreen() {
        return journeyFinderScreen;
    }    
    
    public ReportScreen getReportScreen() {
        return generalReportScreen;
    }
    
    public JourneyDetailsScreen getJourneyDetailsScreen() {
        return journeyDetailsScreen;
    }
    
    public BookingHistoryScreen getHistoryScreen() {
        return historyScreen;
    }
    
    public BookingsCreationScreen getBookingsCreationScreen() {
        return bookingsCreationScreen;
    }
        
    private void registerScreen(GuiScreen screen, String label) {
        mainPanel.add(screen.getPanel(), label);
    }

    private JButton addMenuOption(String label, ActionListener actionListener) {
        JButton button = new JButton(label);
        button.addActionListener(actionListener);
        navBar.add(button);
        buttons.put(label, button);
        return button;
    }

}

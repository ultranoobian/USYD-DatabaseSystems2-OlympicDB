package usyd.it.peerpark.gui;

import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import java.util.Date;
import java.util.Calendar;
import javax.swing.JButton;
import java.awt.GridLayout;
import java.awt.Component;
import javax.swing.Box;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.SpinnerListModel;
import usyd.it.peerpark.PeerParkClient;

public class BookingsCreationScreen extends GuiScreen {
    private Integer bayId;
    private SpinnerDateModel startDates = new SpinnerDateModel(new Date(1305554400000L), null, null, Calendar.HOUR_OF_DAY);
    private SpinnerDateModel endDates = new SpinnerDateModel(new Date(1305554400000L), null, null, Calendar.HOUR_OF_DAY);
    private final SpinnerListModel carNameModel;
    private HashMap<Integer, String> carList;

    public BookingsCreationScreen(PeerParkClient r) {
        super(r);
        panel_.setLayout(new GridLayout(0, 2, 0, 0));

        JLabel lblCarName = new JLabel("Car Name:");
        panel_.add(lblCarName);

        JSpinner carSpinner = new JSpinner();
        String[] tempNames = {"(Empty)"};
        carNameModel = new SpinnerListModel(tempNames);
        carSpinner.setModel(carNameModel);
        panel_.add(carSpinner);

        JLabel lblStartDate = new JLabel("Start");
        panel_.add(lblStartDate);

        JSpinner startDateSelect = new JSpinner();
        startDateSelect.setModel(startDates);
        panel_.add(startDateSelect);

        JLabel lblFinish = new JLabel("Finish");
        panel_.add(lblFinish);

        JSpinner finishDateSelect = new JSpinner();
        finishDateSelect.setModel(endDates);
        panel_.add(finishDateSelect);

        Component horizontalGlue = Box.createHorizontalGlue();
        panel_.add(horizontalGlue);

        JButton btnSubmitBooking = new JButton("Submit Booking");
        btnSubmitBooking.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                client_.makeBooking(bayId, getCarId(), (Date)startDates.getValue(), (Date)endDates.getValue());
            }
        });
        panel_.add(btnSubmitBooking);
    }

    private Integer getCarId() {
        String carName = (String)carNameModel.getValue();
        for (Map.Entry<Integer, String> entry : carList.entrySet()) {
            if(carName.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }
    
    public void startBooking(int bay, HashMap<Integer, String> cars) {
        bayId = bay;
        carList = cars;
        carNameModel.setList(new ArrayList<String>(carList.values()));
    }

}

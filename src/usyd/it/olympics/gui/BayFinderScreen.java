package usyd.it.olympics.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import usyd.it.olympics.OlympicsDBClient;
import usyd.it.olympics.data.BayListLineDetails;

public class BayFinderScreen extends GuiScreen {
    private final HashMapTupleTabelModel bayList = new HashMapTupleTabelModel(BayListLineDetails.idName, BayListLineDetails.columnNames, BayListLineDetails.columnClasses);
    private final ListSelectionModel baySelection;
    private final JTextField txtAddress;

    public BayFinderScreen(OlympicsDBClient r) {
        super(r);
        panel_.setLayout(new BoxLayout(panel_, BoxLayout.Y_AXIS));

        JPanel bayChoicePanel = new JPanel();
        bayChoicePanel.setMaximumSize(new Dimension(32767, 23));
        panel_.add(bayChoicePanel);
        bayChoicePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        JLabel lblAddress = new JLabel("Address");
        bayChoicePanel.add(lblAddress);
        txtAddress = new JTextField();
        txtAddress.setText("address");
        bayChoicePanel.add(txtAddress);
        txtAddress.setColumns(20);
        
        JButton btnUpdate = new JButton("Search");
        btnUpdate.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                        client_.showMatchingBays(txtAddress.getText());
                }
        });
        bayChoicePanel.add(btnUpdate);
        
        // Listing results
        JScrollPane bayListScrollPane = new JScrollPane();
        panel_.add(bayListScrollPane);

        JTable bayListTable = new JTable();
        bayListTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bayListTable.setModel(bayList);
        baySelection = bayListTable.getSelectionModel();
        bayListScrollPane.setViewportView(bayListTable);


        JPanel selectionOptionsPanel = new JPanel();
        panel_.add(selectionOptionsPanel);
        // Include a button to allow a booking to be made for the currently selected row
        final JButton btnGetDetails = new JButton("Get Details");
        selectionOptionsPanel.add(btnGetDetails);

        btnGetDetails.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                int selectionIndex = baySelection.getMinSelectionIndex();
                if (selectionIndex >= 0) {
                    client_.getBayDetails(bayList.getTupleId(selectionIndex));
                }
            }
        });
        
        // Only allow row-specific buttons to work if a row has been selected
        btnGetDetails.setEnabled(false);

        baySelection.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                boolean isValid = !lsm.isSelectionEmpty();
                btnGetDetails.setEnabled(isValid);
            }
        });
    }

    public void showBays(ArrayList<HashMap<String, Object>> bays) {
        //bayList.update(bays);
    }

}

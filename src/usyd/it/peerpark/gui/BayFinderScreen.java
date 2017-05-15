package usyd.it.peerpark.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import usyd.it.peerpark.PeerParkClient;
import usyd.it.peerpark.data.BayListLineDetails;

public class BayFinderScreen extends GuiScreen {
    private final BayListTabelModel bayList = new BayListTabelModel();
    private final ListSelectionModel baySelection;
    private final JTextField txtAddress;

    public BayFinderScreen(PeerParkClient r) {
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
                    client_.getBayDetails(bayList.getBayId(selectionIndex));
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

    public void showBays(ArrayList<BayListLineDetails> bays) {
        bayList.update(bays);
    }

    @SuppressWarnings("serial")
    class BayListTabelModel extends AbstractTableModel {

        private ArrayList<BayListLineDetails> bays;
        private final String[] columnNames = {"Site", "Number", "Address", "City"};
        private final Class<?>[] columnClasses = {String.class, String.class, String.class, String.class};

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return columnClasses[columnIndex];
        }

        public BayListTabelModel() {
            bays = new ArrayList<BayListLineDetails>();
        }

        /**
         * Update the table with newly supplied data
         *
         * @param newBayDetails New list of bays
         */
        public void update(ArrayList<BayListLineDetails> newBayDetails) {
            this.bays = newBayDetails;
            super.fireTableDataChanged();
        }

        public int getBayId(int row) {
            return bays.get(row).getBayId();
        }
        /*
         * AbstractTableModel methods
         */
        @Override
        public String getColumnName(int col) {
            return columnNames[col];
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public int getRowCount() {
            return bays.size();
        }

        @Override
        public Object getValueAt(int row, int col) {
            Object o = null;
            BayListLineDetails bay = bays.get(row);
            switch (col) {
                case 0:
                    o = bay.getSite();
                    break;
                case 1:
                    o = bay.getHouseNum();
                    break;
                case 2:
                    o = bay.getStreet();
                    break;
                case 3:
                    o = bay.getCity();
                    break;
            }
            return o;
        }
    }
}

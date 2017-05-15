package usyd.it.peerpark.gui;
/**
 * Bay Bookings Screen: Display Bay Booking information
 * Author Bryn Jeffries
 */
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import usyd.it.peerpark.PeerParkClient;
import usyd.it.peerpark.data.BayBookingListLine;

public class BookingHistoryScreen extends GuiScreen {
    private final BookingsTabelModel bookingList = new BookingsTabelModel();
    private final JButton btnGetDetails;
    private final ListSelectionModel baySelection;

    public BookingHistoryScreen(PeerParkClient r) {
        super(r);
        panel_.setLayout(new BoxLayout(panel_, BoxLayout.Y_AXIS));
        JTable bookingsTable = new JTable(bookingList);
        bookingsTable.setDefaultRenderer(Date.class, new DateTimeRenderer());
        bookingsTable.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(bookingsTable);
        panel_.add(scrollPane);
        
        bookingsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        baySelection = bookingsTable.getSelectionModel();
        
        // Include a button to allow a booking to be made for the current bay
        btnGetDetails = new JButton("Get Details");
        panel_.add(btnGetDetails);
        btnGetDetails.setEnabled(false);
        btnGetDetails.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                int index = baySelection.getMinSelectionIndex();
                if (index>=0)
                    client_.showBookingDetails(bookingList.getBookingId(index));
                }
        }); 
        
        baySelection.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) { 
                ListSelectionModel lsm = (ListSelectionModel)e.getSource();
                btnGetDetails.setEnabled(!lsm.isSelectionEmpty());
        }});

    }

    public void showBookings(ArrayList<BayBookingListLine> newBookings) {
            if(newBookings == null) { // Prevent nullpointer exceptions
                    bookingList.update(new ArrayList<BayBookingListLine>());
                    btnGetDetails.setEnabled(false);
            } else {
                    bookingList.update(newBookings);
            }
    }

    /// Display date and time in JTable
    static class DateTimeRenderer extends DefaultTableCellRenderer {
        DateFormat formatter;
        public DateTimeRenderer() { super(); }

        @Override
        public void setValue(Object value) {
            if (formatter==null) {
                formatter = new SimpleDateFormat("dd-MMM-yyyy h:mm a");
            }
            setText((value == null) ? "" : formatter.format(value));
        }
    }
    
    @SuppressWarnings("serial")
    class BookingsTabelModel extends AbstractTableModel {
            private ArrayList<BayBookingListLine> bookings;

            private final String [] columnNames = { "Car", "Parking Bay", "From", "Till"};
            private final Class<?>[] columnClasses = { String.class, String.class, Date.class, Date.class};

        public Class<?> getColumnClass(int columnIndex) {
                    return columnClasses[columnIndex];
            }

        public BookingsTabelModel() {
            bookings = new ArrayList<BayBookingListLine>();
            }

            /**
                * Update the table with newly supplied data
                * @param newCars New list of cars
                */
            public void update(ArrayList<BayBookingListLine> newBookings) {
                    bookings = newBookings;
                    super.fireTableDataChanged();
            }
            
            public int getBookingId(int row) {
                return bookings.get(row).getBookingId();
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
                    return bookings.size();
            }

            @Override
            public Object getValueAt(int row, int col) {
                Object o=null;
                BayBookingListLine booking = bookings.get(row);
                switch(col) {
                    case 0:
                        o = booking.getCarName();
                        break;
                    case 1:
                        o = booking.getBayLocation();
                        break;
                    case 2:
                        o = booking.getStartTime();
                        break;
                    case 3:
                        o = booking.getEndTime();
                        break;
                }
                return o;
            }

    }
	
}

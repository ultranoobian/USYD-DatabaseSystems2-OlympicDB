package usyd.it.olympics.gui;
/**
 * Bookings Screen: Display Booking information
 * Author Bryn Jeffries
 */
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
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
import usyd.it.olympics.OlympicsDBClient;
import usyd.it.olympics.data.BookingTuple;

public class BookingHistoryScreen extends GuiScreen {
    private final BookingsTabelModel bookingList = new BookingsTabelModel();
    private final JButton btnGetDetails;
    private final ListSelectionModel baySelection;

    public BookingHistoryScreen(OlympicsDBClient r) {
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

    public void showBookings(ArrayList<HashMap<String, Object>> bookings) {
            if(bookings == null) { // Prevent nullpointer exceptions
                    bookingList.update(new ArrayList<HashMap<String, Object>>());
                    btnGetDetails.setEnabled(false);
            } else {
                    bookingList.update(bookings);
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
            private ArrayList<HashMap<String, Object>> bookings;

            private final String idName = BookingTuple.idName;
            private final String [] columnNames = BookingTuple.columnNames;
            private final Class<?>[] columnClasses = BookingTuple.columnClasses;

        public Class<?> getColumnClass(int columnIndex) {
                    return columnClasses[columnIndex];
            }

        public BookingsTabelModel() {
            bookings = new ArrayList<HashMap<String, Object>>();
            }

            /**
                * Update the table with newly supplied data
                * @param newCars New list of cars
                */
            public void update(ArrayList<HashMap<String, Object>> newBookings) {
                    bookings = newBookings;
                    super.fireTableDataChanged();
            }
            
            public Integer getBookingId(int row) {
                HashMap<String, Object> booking = bookings.get(row);
				return booking==null ? null : (Integer) booking.get(idName);
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
                HashMap<String, Object> booking = bookings.get(row);
                o = booking!=null && col>=0 && col<columnNames.length ? booking.get(columnNames[col]) : null;
                return o;
            }

    }
	
}

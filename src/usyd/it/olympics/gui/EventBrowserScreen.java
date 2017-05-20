package usyd.it.olympics.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import usyd.it.olympics.OlympicsDBClient;
import usyd.it.olympics.data.Place;
import usyd.it.olympics.data.Sport;

/*
 * Derived from JourneyFinderScreen
 */
public class EventBrowserScreen extends GuiScreen {
	private final HashMapTupleTabelModel list = new HashMapTupleTabelModel(new Place());
    private final ListSelectionModel listSelection;
    private final JComboBox<HashMap<String, Object>> sportChooser;

    public EventBrowserScreen(OlympicsDBClient r) {
        super(r);
        panel_.setLayout(new BoxLayout(panel_, BoxLayout.Y_AXIS));

        JPanel choicePanel = new JPanel();
        choicePanel.setMaximumSize(new Dimension(32767, 23));
        panel_.add(choicePanel);
        choicePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        JLabel lblSport = new JLabel("Sport");
        choicePanel.add(lblSport);
        sportChooser = new JComboBox<HashMap<String, Object>>();
        sportChooser.setRenderer(new EventTupleRenderer());
        choicePanel.add(sportChooser);
        
        JButton btnUpdate = new JButton("Search");
        btnUpdate.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                        client_.getEvents(Sport.getSportId((HashMap<String, Object>) sportChooser.getSelectedItem()));
                }
        });
        choicePanel.add(btnUpdate);
        
        // Listing results
        JScrollPane listScrollPane = new JScrollPane();
        panel_.add(listScrollPane);

        JTable listTable = new JTable();
        listTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listTable.setModel(list);
        listSelection = listTable.getSelectionModel();
        listScrollPane.setViewportView(listTable);


        JPanel selectionOptionsPanel = new JPanel();
        panel_.add(selectionOptionsPanel);
        // Include a button to allow details to be retrieved for the currently selected event
        final JButton btnGetDetails = new JButton("Get Event Details");
        selectionOptionsPanel.add(btnGetDetails);

        btnGetDetails.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                int selectionIndex = listSelection.getMinSelectionIndex();
                if (selectionIndex >= 0) {
                    client_.getEventResults(list.getTuple(selectionIndex));
                }
            }
        });
        
        // Only allow row-specific buttons to work if a row has been selected
        btnGetDetails.setEnabled(false);

        listSelection.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                boolean isValid = !lsm.isSelectionEmpty();
                btnGetDetails.setEnabled(isValid);
            }
        });
    }

    public void showTuples(ArrayList<HashMap<String, Object>> newTuples) {
        list.update(newTuples);
    }

	@SuppressWarnings("unchecked")
	public void setSports(ArrayList<HashMap<String, Object>> sports) {
		sportChooser.setModel(new DefaultComboBoxModel<HashMap<String, Object>>(sports.toArray(new HashMap[sports.size()])));
	}
	
    private class EventTupleRenderer extends JLabel implements ListCellRenderer<HashMap<String, Object>> {

    	// Borrowed from http://grepcode.com/file/repository.grepcode.com/java/root/jdk/openjdk/6-b14/javax/swing/DefaultListCellRenderer.java
    	private Border getNoFocusBorder() {
    		if (System.getSecurityManager() != null) {
    			return new EmptyBorder(1, 1, 1, 1);
    		} else {
    			return UIManager.getBorder("List.noFocusBorder");
    		}
    	}

		@Override
		public Component getListCellRendererComponent(JList<? extends HashMap<String, Object>> list,
				HashMap<String, Object> value, int index, boolean isSelected, boolean cellHasFocus) {
			Color bg = null;
			Color fg = null;
			if (isSelected) {
			    setBackground(bg == null ? list.getSelectionBackground() : bg);
			    setForeground(fg == null ? list.getSelectionForeground() : fg);
			} else {
			    setBackground(list.getBackground());
			    setForeground(list.getForeground());
			}
			
			if (value!=null) {
				this.setText(Sport.getSportName(value) + " - " + Sport.getDiscipline(value));
			}
			
			setEnabled(list.isEnabled());
			setFont(list.getFont());
			
			Border border = null;
			if (cellHasFocus) {
			    if (isSelected) {
			        border = UIManager.getBorder("List.focusSelectedCellHighlightBorder");
			    }

			    if (border == null) {
			        border = UIManager.getBorder("List.focusCellHighlightBorder");
			    }
			} else {
			    border = getNoFocusBorder();
			}
			setBorder(border);
			
			return this;
		}

	}
	
}
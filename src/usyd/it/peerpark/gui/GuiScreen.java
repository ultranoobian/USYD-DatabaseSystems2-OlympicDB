package usyd.it.peerpark.gui;

import javax.swing.JPanel;
import usyd.it.peerpark.PeerParkClient;

/**
 *
 * @author Bryn
 */
class GuiScreen {

    protected final JPanel panel_;
    protected final PeerParkClient client_;

    /**
     * @return the panel_
     */
    public JPanel getPanel() {
        return panel_;
    }
    
    protected GuiScreen(PeerParkClient r) {
        panel_ = new JPanel();
        client_ = r;
    }
}

package net.bounceme.dur.nntp.swing;

import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import net.bounceme.dur.nntp.gnu.Page;
import net.bounceme.dur.nntp.gnu.Usenet;

public class MyPanel extends JScrollPane {

    private static final Logger LOG = Logger.getLogger(MyPanel.class.getName());
    private JList jList;
    private DefaultListModel defaultListModel;
    private Usenet u = Usenet.INSTANCE;
    private Page page;

    public MyPanel() {
        page = u.getPage(new Page().getPmd()); //uncaught exception
        jList = new JList(defaultListModel); // null, need to initialize
        initComponents();
    }

    private void initComponents() {
    }
}

package net.bounceme.dur.nntp.swing;

import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import net.bounceme.dur.nntp.gnu.Page;
import net.bounceme.dur.nntp.gnu.Usenet;

public class MyPanel extends JScrollPane {

    private static final Logger LOG = Logger.getLogger(MyPanel.class.getName());
    private static final long serialVersionUID = 1L;
    private JList<String> jList = new JList<>();
    private DefaultListModel<String> dlm = new DefaultListModel<>();
    private Usenet u = Usenet.INSTANCE;
    private Page page=new Page();

    @SuppressWarnings("unchecked")
    public MyPanel() throws Exception {
        page = u.getPage(page.getPmd()); //uncaught exception
        jList = new JList<>(dlm); // null, need to initialize
        initComponents();
    }

    private void initComponents() {
    }
}

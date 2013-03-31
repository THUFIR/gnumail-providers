package net.bounceme.dur.nntp.swing;

import java.util.List;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import net.bounceme.dur.nntp.gnu.Page;
import net.bounceme.dur.nntp.gnu.Usenet;

public class MyPanel extends JScrollPane {

    private static final Logger LOG = Logger.getLogger(MyPanel.class.getName());
    private static final long serialVersionUID = 1L;
    private JList<Message> jList;
    private DefaultListModel<Message> dlm;
    private Usenet u = Usenet.INSTANCE;
    private Page page = new Page();

    @SuppressWarnings({"unchecked"})
    public MyPanel() throws Exception {
        page = u.getPage(page.getPmd());
        List<Message> messages = page.getMessages();
        for (Message m : messages) {
            dlm.addElement(m);
        }
        jList = new JList<>(dlm);
        this.add(jList);
        initComponents();
    }

    private void initComponents() {
        this.setSize(300, 100);
        this.setVisible(true);
    }
}

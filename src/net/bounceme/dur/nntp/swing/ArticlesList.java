package net.bounceme.dur.nntp.swing;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import net.bounceme.dur.nntp.gnu.PageMetaData;
import net.bounceme.dur.nntp.gnu.Page;
import net.bounceme.dur.nntp.gnu.Usenet;

public class ArticlesList extends JScrollPane {

    private static final Logger LOG = Logger.getLogger(ArticlesList.class.getName());
    private static final long serialVersionUID = 1L;
    public static final String INDEX = "index";
    private JList<String> jList = new JList<>();
    private DefaultListModel<String> dlm = new DefaultListModel<>();
    private Page page;
    private Usenet usenetConnection = Usenet.INSTANCE;
    private PageMetaData pageMetaData = new PageMetaData();

    public ArticlesList() {
        try {
            nextPage();
        } catch (Exception ex) {
            LOG.warning(ex.toString());
        }
        initComponents();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        jList.setModel(dlm);
        jList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jList.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseReleased(java.awt.event.MouseEvent evt) {
                itemSelected();
            }
        });
        jList.addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyReleased(java.awt.event.KeyEvent evt) {
                itemSelected();
            }
        });

        setSize(2000, 2000);
        setViewportView(jList);
        jList.setVisible(true);
        setVisible(true);
    }

    private void itemSelected() {
        int index = jList.getSelectedIndex();
        LOG.fine("selected\t\t" + index);
        this.firePropertyChange("index", null, index);
    }

    public final void nextPage() throws Exception {
        page = new Page(pageMetaData);
        pageMetaData = new PageMetaData(page.getPageMetaData(), true);
        page = usenetConnection.getPage(pageMetaData);
        List<Message> messages = page.getMessages();
        loadDLM(messages);
        LOG.fine(page.toString());
    }

    private void loadDLM(List<Message> messages) {
        dlm = new DefaultListModel<>();
        for (Message m : messages) {
            try {
                dlm.addElement(m.getSubject());
            } catch (MessagingException ex) {
                LOG.warning("bad message\n" + m.toString() + "\n" + ex);
            }
        }
        jList.setModel(dlm);
    }

    public Message getMessage(int i) {
        List<Message> messages = page.getMessages();
        Message m = messages.get(i);
        return m;
    }
}

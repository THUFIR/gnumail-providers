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
    public static final String PHYSICIST = "physicist";
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
                mouseReleases(evt);
            }
        });
        jList.addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyReleased(java.awt.event.KeyEvent evt) {
                keyReleases(evt);
            }
        });

        setSize(2000, 2000);
        setViewportView(jList);
        jList.setVisible(true);
        setVisible(true);
    }

    private void keyReleases(KeyEvent evt) {
        itemSelected();
    }

    private void mouseReleases(MouseEvent evt) {
        itemSelected();
    }

    private void itemSelected() {
        LOG.info("selected\t\t" + jList.getSelectedValue());
    }

    public final void nextPage() throws Exception {
        page = new Page(pageMetaData);
        pageMetaData = new PageMetaData(page.getPageMetaData(), true);
        page = usenetConnection.getPage(pageMetaData);
        List<Message> messages = page.getMessages();
        loadDLM(messages);
        LOG.info(page.toString());
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
}

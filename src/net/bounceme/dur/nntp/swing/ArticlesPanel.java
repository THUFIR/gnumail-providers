package net.bounceme.dur.nntp.swing;

import gnu.mail.providers.nntp.GroupMetaData;
import java.util.List;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import net.bounceme.dur.nntp.gnu.PMD;
import net.bounceme.dur.nntp.gnu.Page;
import net.bounceme.dur.nntp.gnu.Usenet;

public class ArticlesPanel extends JPanel {

    private static final Logger LOG = Logger.getLogger(ArticlesPanel.class.getName());
    private static final long serialVersionUID = 1L;
    private JList<String> jList = new JList<>();
    private JScrollPane scrollPane = new JScrollPane();
    private DefaultListModel<String> dlm;

    public ArticlesPanel() throws MessagingException {


        dlm = new DefaultListModel<>();
        for (int i = 1; i < 9; i++) {
            dlm.addElement("item\t\t" + i);
        }
        initComponents();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        setLayout(new java.awt.BorderLayout());


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

        scrollPane.setViewportView(jList);
        add(scrollPane, java.awt.BorderLayout.CENTER);

        this.setSize(300, 100);
        this.setVisible(true);
    }

    private void keyReleases(java.awt.event.KeyEvent evt) {
        itemSelected();
    }

    private void mouseReleases(java.awt.event.MouseEvent evt) {
        itemSelected();
    }

    private void itemSelected() {
        LOG.info("selected\t\t" + jList.getSelectedValue());
    }
}

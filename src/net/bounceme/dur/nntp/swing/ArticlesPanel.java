package net.bounceme.dur.nntp.swing;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
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
    private JButton next = new JButton("next");
    private Page page;
    private Usenet u = Usenet.INSTANCE;
    private PMD pmd = new PMD();

    public ArticlesPanel() {
        dlm = new DefaultListModel<>();
        for (int i = 1; i < 9; i++) {
            dlm.addElement("item\t\t" + i);
        }
        try {
            page = new Page();
            pmd = page.getPmd();
        } catch (Exception ex) {
            LOG.warning("no page\n" + ex);
        }

        for (int i = 0; i < 5; i++) {
            LOG.fine("in loop");
            page = u.getPage(pmd);
            pmd = new PMD(page.getPmd(), true);
            LOG.info(page.toString());
        }
        initComponents();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        setLayout(new java.awt.BorderLayout());


        next.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                nextPage(e);
            }
        });



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
        add(scrollPane, BorderLayout.CENTER);
        add(next, BorderLayout.SOUTH);

        this.setSize(300, 100);
        scrollPane.setVisible(true);
        this.setVisible(true);
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

    private void nextPage(ActionEvent e) {
        page = u.getPage(pmd);
        pmd = new PMD(page.getPmd(), true);
        List<Message> messages = page.getMessages();
        dlm = new DefaultListModel<>();
        for (Message m : messages) {
            try {
                dlm.addElement(m.getSubject());
            } catch (MessagingException ex) {
                LOG.warning("no message\n" + ex);
            }
        }
        jList.setModel(dlm);
        LOG.info(page.toString());
    }
}

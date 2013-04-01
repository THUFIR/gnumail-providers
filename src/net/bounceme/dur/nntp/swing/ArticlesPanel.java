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
import net.bounceme.dur.nntp.gnu.PageMetaData;
import net.bounceme.dur.nntp.gnu.Page;
import net.bounceme.dur.nntp.gnu.Usenet;

public class ArticlesPanel extends JPanel {

    private static final Logger LOG = Logger.getLogger(ArticlesPanel.class.getName());
    private static final long serialVersionUID = 1L;
    private JList<String> jList = new JList<>();
    private JScrollPane scrollPane = new JScrollPane();
    private DefaultListModel<String> defaultListModel;
    private JButton next = new JButton("next");
    private Page page;
    private Usenet usenetConnection = Usenet.INSTANCE;  //ensures correct connection
    private PageMetaData pageMetaData = new PageMetaData();

    public ArticlesPanel() {
        nextPage(null);
        nextPage(null);  //only because default page starts at zero
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

        jList.setModel(defaultListModel);
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
        add(scrollPane, BorderLayout.WEST);
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
        page = usenetConnection.getPage(pageMetaData);  //first time, default
        pageMetaData = new PageMetaData(page.getPageMetaData(), true); //get next is true
        List<Message> messages = page.getMessages(); //breaks MVC?
        defaultListModel = new DefaultListModel<>();  //clear or new?
        for (Message m : messages) {
            try {
                defaultListModel.addElement(m.getSubject());
            } catch (MessagingException ex) {
                LOG.warning("bad message\n" + m.toString() + "\n" + ex);
            }
        }
        jList.setModel(defaultListModel);
        LOG.fine(page.toString());
    }
}

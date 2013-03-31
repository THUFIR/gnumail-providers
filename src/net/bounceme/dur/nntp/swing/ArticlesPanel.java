package net.bounceme.dur.nntp.swing;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class ArticlesPanel extends JPanel {

    private static final Logger LOG = Logger.getLogger(ArticlesPanel.class.getName());
    private static final long serialVersionUID = 1L;
    private JList<String> jList = new JList<>();
    private JScrollPane scrollPane = new JScrollPane();
    private DefaultListModel<String> dlm;
    private JButton next = new JButton("next");

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


        next.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                click(e);
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

    private void click(ActionEvent e) {
        LOG.info("You clicked the button");
    }
}

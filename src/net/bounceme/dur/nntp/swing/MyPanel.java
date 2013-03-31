package net.bounceme.dur.nntp.swing;

import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class MyPanel extends JPanel {

    private static final Logger LOG = Logger.getLogger(MyPanel.class.getName());
    private static final long serialVersionUID = 1L;
    private JList<String> jList = new JList<>();
    private JScrollPane scrollPane = new JScrollPane();

    public MyPanel() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        setLayout(new java.awt.BorderLayout());

        DefaultListModel<String> dlm = new DefaultListModel<>();
        for (int i = 1; i < 9; i++) {
            dlm.addElement("item\t\t" + i);
        }

        jList.setModel(dlm);
        jList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jList.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseReleased(java.awt.event.MouseEvent evt) {
                mouseReleases(evt);
            }
        });
        jList.addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyReleased(java.awt.event.KeyEvent evt) {
                keyReleased(evt);
            }
        });

        scrollPane.setViewportView(jList);
        add(scrollPane, java.awt.BorderLayout.CENTER);

        this.setSize(300, 100);
        this.setVisible(true);
    }

    private void keyReleased(java.awt.event.KeyEvent evt) {
        itemSelected();
    }

    private void mouseReleases(java.awt.event.MouseEvent evt) {
        itemSelected();
    }

    private void itemSelected() {
        LOG.info("selected\t\t" + jList.getSelectedValue());
    }
}

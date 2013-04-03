package net.bounceme.dur.nntp.swing;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class ArticlesPanel extends JPanel {

    private static final Logger LOG = Logger.getLogger(ArticlesPanel.class.getName());
    private static final long serialVersionUID = 1L;

    private ArticlesList observed = new ArticlesList();
    private JButton next = new JButton("next");

    public ArticlesPanel() {
        LOG.info("creating...");
        initComponents();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        setLayout(new java.awt.BorderLayout());
        next.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    nextPage(e);
                } catch (Exception ex) {
                    LOG.warning(ex.toString());
                }
            }

        });

      observed.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent e) {
                if (e.getPropertyName().equals(ArticlesList.PHYSICIST)) {
                    String value = e.getNewValue().toString();
                    LOG.info(value);
                }
            }
        });

        add(observed);
        add(observed, BorderLayout.WEST);
        add(next, BorderLayout.SOUTH);
        setSize(300, 100);
        observed.setVisible(true);
        setVisible(true);
    }
            private void nextPage(ActionEvent e) throws Exception {
                observed.nextPage();
            }
}

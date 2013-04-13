package net.bounceme.dur.nntp.swing;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.swing.JButton;
import javax.swing.JPanel;

public class ArticlesPanel extends JPanel {

    private static final Logger LOG = Logger.getLogger(ArticlesPanel.class.getName());
    private static final long serialVersionUID = 1L;
    private ArticlesList articlesList = new ArticlesList();
    private JButton next = new JButton("next");
    private ArticleContent articleContent = new ArticleContent();
    private ArticleContent east = new ArticleContent();

    public ArticlesPanel() {
        LOG.fine("creating...");
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

        articlesList.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent e) {
                LOG.info("trying..." + e.getPropertyName());
                if ("message".equals(e.getPropertyName())) {
                    try {
                        Message m = (Message) e.getNewValue();
                        LOG.info("good message?\t" + m.getSubject());
                        articleContent.setText(m);
                    } catch (IOException | MessagingException ex) {
                        LOG.info("bad message?\t" + ex.toString());
                    }
                }
            }
        });

        add(articlesList, BorderLayout.WEST);
        add(articleContent, BorderLayout.CENTER);
        add(east, BorderLayout.EAST);
        add(next, BorderLayout.SOUTH);
        setSize(300, 100);
        articlesList.setVisible(true);
        articleContent.setVisible(true);
        east.setVisible(true);
        setVisible(true);
    }

    private void nextPage(ActionEvent e) throws Exception {
        articlesList.nextPage();
    }
}

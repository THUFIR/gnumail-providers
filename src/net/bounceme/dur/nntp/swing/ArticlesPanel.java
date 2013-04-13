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
    private ArticleContent ac = new ArticleContent();

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
                try {
                    String articleIndex = e.getNewValue().toString();
                    LOG.fine(articleIndex);
                    ac.setText(articleIndex);
                    //somehow,get article content from the map in MessageFolder
                    //or something
                    Message m = articlesList.getArticle(0);
                    ac.setText(m.getContent().toString());
                } catch (IOException | MessagingException ex) {
                    LOG.fine("bad message?\n" + ex);
                }
            }
        });

        add(articlesList, BorderLayout.WEST);
        add(ac, BorderLayout.CENTER);
        add(next, BorderLayout.SOUTH);
        setSize(300, 100);
        articlesList.setVisible(true);
        ac.setVisible(true);
        setVisible(true);
    }

    private void nextPage(ActionEvent e) throws Exception {
        articlesList.nextPage();
    }
}

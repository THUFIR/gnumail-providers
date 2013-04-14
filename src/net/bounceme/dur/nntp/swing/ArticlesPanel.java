package net.bounceme.dur.nntp.swing;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.swing.JButton;
import javax.swing.JPanel;

public class ArticlesPanel extends JPanel {

    private static final Logger LOG = Logger.getLogger(ArticlesPanel.class.getName());
    private static final long serialVersionUID = 1L;
    private ArticlesList articlesList = new ArticlesList();
    private JButton next = new JButton("next");
    private ArticleContent articleContent = new ArticleContent();
    private ArticleContent headers = new ArticleContent();

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
                LOG.fine("trying..." + e.getPropertyName());
                try {
                    Message m = (Message) e.getNewValue();
                    headers.setMessage(m);
                    LOG.fine("good message?\t" + m.getSubject());
                    articleContent.setMessage(m);
                    headers.setMessageXref(m);
                } catch (Exception ex) {
                    LOG.fine("bad message?\t" + ex.toString());
                }

            }
        });

        add(articlesList, BorderLayout.WEST);
        add(articleContent, BorderLayout.CENTER);
        add(headers, BorderLayout.EAST);
        add(next, BorderLayout.SOUTH);
        setSize(300, 100);
        articlesList.setVisible(true);
        articleContent.setVisible(true);
        headers.setVisible(true);
        setVisible(true);
    }

    private void nextPage(ActionEvent e) throws Exception {
        articlesList.nextPage();
    }
}

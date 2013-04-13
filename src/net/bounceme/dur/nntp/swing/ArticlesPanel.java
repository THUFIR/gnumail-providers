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
                String messageIndexString = e.getNewValue().toString();
                articleContent.setText(messageIndexString);
                int index = 0;
                try {
                    index = Integer.getInteger(messageIndexString);
                    Message message = articlesList.getMessage(index);
                    articleContent.setText(message.getContent().toString());
                } catch (Exception ex) {
                    LOG.warning("bad message?\t" + index + "\t" + ex);
                    //articleContent.setText("bad message?\t" + index + "\t" + ex);
                }
            }
        });

        add(articlesList, BorderLayout.WEST);
        add(articleContent, BorderLayout.CENTER);
        add(next, BorderLayout.SOUTH);
        setSize(300, 100);
        articlesList.setVisible(true);
        articleContent.setVisible(true);
        setVisible(true);
    }

    private void nextPage(ActionEvent e) throws Exception {
        articlesList.nextPage();
    }
}

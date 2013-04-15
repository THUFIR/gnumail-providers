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
import net.bounceme.dur.nntp.gnu.PageMetaData;

public class ArticlesJPanelContainer extends JPanel {

    private static final Logger LOG = Logger.getLogger(ArticlesJPanelContainer.class.getName());
    private static final long serialVersionUID = 1L;
    private ArticlesTable articlesTable = null;
    private JButton next = new JButton("next");
    private ArticleContent articleContent = new ArticleContent();
    private ArticleContent headers = new ArticleContent();

    public ArticlesJPanelContainer() throws Exception {
        LOG.fine("creating...");
        articlesTable = new ArticlesTable();
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
                    LOG.fine("initComponents.addActionListener");
                    LOG.fine(ex.toString());
                }
            }
        });

        articlesTable.addPropertyChangeListener(new PropertyChangeListener() {

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

        add(articlesTable, BorderLayout.WEST);
        add(articleContent, BorderLayout.CENTER);
        add(headers, BorderLayout.EAST);
        add(next, BorderLayout.SOUTH);
        setSize(300, 100);
        articlesTable.setVisible(true);
        articleContent.setVisible(true);
        headers.setVisible(true);
        setVisible(true);
    }

    private void nextPage(ActionEvent e) throws Exception {
        articlesTable.nextPage(new PageMetaData());
    }
}

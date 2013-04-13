package net.bounceme.dur.nntp.swing;

import java.awt.BorderLayout;
import java.io.IOException;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ArticleContent extends JPanel {

    private static final Logger LOG = Logger.getLogger(ArticleContent.class.getName());
    private static final long serialVersionUID = 1L;
    private JTextArea textArea;
    private JScrollPane scrollPane;

    public ArticleContent() {
        LOG.fine("creating...");
        initComponents();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        setLayout(new java.awt.BorderLayout());

        textArea = new JTextArea(5, 20);
        scrollPane = new JScrollPane(textArea);
        textArea.setEditable(false);

        add(scrollPane, BorderLayout.CENTER);
        setSize(300, 100);
        scrollPane.setVisible(true);
        setVisible(true);
    }

    public void setText(Message message) throws  IOException, MessagingException {
        textArea.setText(message.getContent().toString());
    }
}

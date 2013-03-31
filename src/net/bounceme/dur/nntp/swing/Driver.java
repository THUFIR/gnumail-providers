package net.bounceme.dur.nntp.swing;

import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.swing.*;
import java.awt.*;

public class Driver {

    private static final Logger LOG = Logger.getLogger(Driver.class.getName());

    private static void createAndShowGUI() throws MessagingException  {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(true);
        GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
        frame.setMaximizedBounds(e.getMaximumWindowBounds());
        frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        frame.setLayout(new BorderLayout());

        JTabbedPane tabs = new JTabbedPane();

        ArticlesPanel myPanel = new ArticlesPanel();
        tabs.add(myPanel);
        frame.add(tabs);

        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String... args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                try {
                    createAndShowGUI();
                } catch (MessagingException ex) {
                    LOG.warning(ex.getLocalizedMessage());
                }
            }
        });
    }
}
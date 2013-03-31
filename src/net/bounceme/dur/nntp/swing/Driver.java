package net.bounceme.dur.nntp.swing;

import java.util.logging.Logger;
import javax.swing.*;
import java.awt.*;

public class Driver {

    private static final Logger LOG = Logger.getLogger(Driver.class.getName());

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("HelloWorldSwing");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension d = new Dimension(400, 40);
        JLabel label = new JLabel("Hello World");
        frame.getContentPane().add(label);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int height = screenSize.height;
        int width = screenSize.width;
        frame.setSize(height, width + 50);
        LOG.fine(height + "\t\t\t" + width);
        frame.setVisible(true);
    }

    public static void main(String... args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                createAndShowGUI();
            }
        });
    }
}
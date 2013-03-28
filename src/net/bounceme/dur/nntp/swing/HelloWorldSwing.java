package net.bounceme.dur.nntp.swing;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.logging.Logger;
import javax.swing.*;
import java.awt.*;

public class HelloWorldSwing {

    private static final Logger LOG = Logger.getLogger(HelloWorldSwing.class.getName());

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("HelloWorldSwing");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension d = new Dimension(400, 40);
        JLabel label = new JLabel("Hello World");
        frame.getContentPane().add(label);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int height = screenSize.height;
        int width = screenSize.width;
        frame.setSize(height, width);
        LOG.info(height + "\t\t\t" + width);
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
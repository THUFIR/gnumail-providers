package net.bounceme.dur.nntp.gnu;



import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PropertiesReader {

    private static final Logger LOG = Logger.getLogger(PropertiesReader.class.getName());
    private static Properties props = new Properties();

    public static Properties getProps() {
        try {
            props.load(PropertiesReader.class.getResourceAsStream("/usenet.properties"));
        } catch (IOException ex) {
            Logger.getLogger(PropertiesReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        LOG.fine(props.toString());
        return props;
    }
}
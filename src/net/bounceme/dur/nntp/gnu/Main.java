package net.bounceme.dur.nntp.gnu;

import java.util.List;
import java.util.logging.Logger;
import javax.mail.Message;


public class Main {

    private final static Logger LOG = Logger.getLogger(Main.class.getName());
    Usenet u = Usenet.INSTANCE;

    public Main() throws Exception {
        LOG.info("in constructor");
        u.connect();
        Page page = new Page();
        List<Message> messages = u.getMessages(page);
        for (Message m : messages) {
            LOG.info(m.getSubject());
        }
    }

    public static void main(String... args) throws Exception {
        LOG.info("well here we are");
        new Main();
    }
}

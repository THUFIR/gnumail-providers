package net.bounceme.dur.nntp.gnu;

import gnu.mail.providers.nntp.GMD;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;

public class Page {

    private final static Logger LOG = Logger.getLogger(Page.class.getName());
    private List<Message> m = new ArrayList<>();
    private PMD pmd = new PMD();
    Usenet u = Usenet.INSTANCE;

    public Page() {
        GMD gmd = new GMD(getNewsgroup());
        pmd = new PMD(gmd);
    }

    Page(GMD gmd) {
        pmd = new PMD(gmd);
    }

    private void init() throws Exception {
        m = u.getMessages(this);
    }

    public List<Message> getMessages() throws MessagingException {
        for (Message message : m) {
            LOG.info(message.getSubject());
        }
        return m;
    }

    public String getNewsgroup() {
        return "comp.lang.java.help";
    }

    public String toString() {
        return "fuck";
    }
}

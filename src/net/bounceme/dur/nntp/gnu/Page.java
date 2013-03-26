package net.bounceme.dur.nntp.gnu;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Logger;
import javax.mail.Header;
import javax.mail.Message;
import javax.mail.MessagingException;

public class Page {

    private final static Logger LOG = Logger.getLogger(Page.class.getName());
    private List<Message> messages = new ArrayList<>();
    private PMD pmd = new PMD();

    public Page() throws Exception {
        GMD gmd = new GMD(getNewsgroup());
        pmd = new PMD(gmd);
    }

    public Page(GMD gmd) throws Exception {
        pmd = new PMD(gmd);
    }

    public Page(PMD pmd) throws Exception {
        this.pmd = pmd;
    }

    public Page(PMD pmd, List<Message> messages) throws MessagingException {
        this.pmd = pmd;
        this.messages = messages;
    }

    public String getNewsgroup() {
        return "comp.lang.java.help";
    }

    public PMD getPmd() {
        return pmd;
    }

    public void setPmd(PMD pmd) {
        this.pmd = pmd;
    }

    private String printXref() throws MessagingException {
        LOG.fine("starting xref printing...\t\t" + messages.size());
        StringBuilder sb = new StringBuilder();
        String s = null;
        String headerString = null;
        String subString = null;
        for (Message message : messages) {
            LOG.fine(message.getSubject());
            int i = messages.indexOf(message);
            Enumeration headers = message.getAllHeaders();
            while (headers.hasMoreElements()) {
                Object o = headers.nextElement();
                Header header = (Header) o;
                if ("Xref".equals(header.getName())) {
                    headerString = header.getValue();
                    int index = headerString.indexOf(":");
                    subString = headerString.substring(index + 1);
                    int xref = Integer.parseInt(subString);
                    s = "\n" + i + "\t\t" + xref;
                    sb.append(s);
                }
            }
            
            LOG.fine("\n\n\n**********************\n\n\n");
        }
        LOG.fine(sb.toString());
        return sb.toString();
    }

    public String toString() {
        String s = null;
        try {
            s = printXref();
        } catch (MessagingException ex) {
            LOG.severe(ex.getMessage());
        }
        return s;
    }
}

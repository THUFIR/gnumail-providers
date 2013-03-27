package net.bounceme.dur.nntp.gnu;

import gnu.mail.providers.nntp.GroupMetaData;
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
    private PageMetaData pmd = new PageMetaData();

    public Page() throws Exception {
        GroupMetaData gmd = new GroupMetaData();
        pmd = new PageMetaData(gmd);
    }

    public Page(GroupMetaData gmd) throws Exception {
        pmd = new PageMetaData(gmd);
    }

    public Page(PageMetaData pmd) throws Exception {
        this.pmd = pmd;
    }

    public Page(PageMetaData pmd, List<Message> messages) throws MessagingException {
        this.pmd = pmd;
        this.messages = messages;
    }

    public PageMetaData getPmd() {
        return pmd;
    }

    public void setPmd(PageMetaData pmd) {
        this.pmd = pmd;
    }

    private String printXref() {
        LOG.fine("starting xref printing...\t" + messages.size());
        StringBuilder sb = new StringBuilder();
        String s = null;
        String headerString = null;
        String subString = null;
        sb.append("messages to follow\n");
        for (Message message : messages) {
            try {
                LOG.fine(message.getSubject());
            } catch (MessagingException ex) {
            }
            int i = messages.indexOf(message);
            Enumeration headers = null;
            try {
                headers = message.getAllHeaders();
            } catch (MessagingException ex) {
            }
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
        String s = "\n---new page---\n" + pmd.toString() + "\n";
        s = s + printXref();
        return s;
    }
}

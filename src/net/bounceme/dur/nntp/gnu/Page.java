package net.bounceme.dur.nntp.gnu;

import gnu.mail.providers.nntp.GroupMetaData;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Logger;
import javax.mail.Header;
import javax.mail.Message;
import javax.mail.MessagingException;

public class Page {

    private final static Logger LOG = Logger.getLogger(Page.class.getName());
    private List<Message> messages = new ArrayList<>();
    private PageMetaData pageMetaData = new PageMetaData();

    public Page() throws Exception {
        GroupMetaData gmd = new GroupMetaData();
        pageMetaData = new PageMetaData(gmd);
    }

    public Page(GroupMetaData gmd) throws Exception {
        pageMetaData = new PageMetaData(gmd);
    }

    public Page(PageMetaData pmd) throws Exception {
        this.pageMetaData = pmd;
    }

    public Page(PageMetaData pmd, List<Message> messages) throws MessagingException {
        this.pageMetaData = pmd;
        this.messages = messages;
    }

    public List<Message> getMessages() {
        return Collections.unmodifiableList(messages);
    }

    private String printXref() throws MessagingException {
        LOG.fine("starting xref printing...\t" + messages.size());
        StringBuilder sb = new StringBuilder();
        String s = null;
        String headerString = null;
        String subString = null;
        sb.append("messages to follow\n");
        for (Message message : messages) {

            LOG.fine(message.getSubject());

            int i = messages.indexOf(message);
            Enumeration headers = null;

            headers = message.getAllHeaders();

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
                    s = message.getSubject();
                    sb.append("\t").append(s);
                }
            }
            LOG.fine("\n\n\n**********************\n\n\n");
        }
        LOG.fine(sb.toString());
        return sb.toString();
    }

    public String toString() {
        String s = "\n---new page---\n" + getPageMetaData().toString() + "\n";
        try {
            s = s + printXref();
        } catch (MessagingException ex) {
            LOG.warning(ex.toString());
        }
        return s;
    }

    public PageMetaData getPageMetaData() {
        return pageMetaData;
    }

    public void setPageMetaData(PageMetaData pageMetaData) {
        this.pageMetaData = pageMetaData;
    }
}

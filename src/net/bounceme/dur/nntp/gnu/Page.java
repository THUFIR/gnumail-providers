package net.bounceme.dur.nntp.gnu;

import gnu.mail.providers.nntp.GroupMetaData;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;

public class Page {

    private final static Logger LOG = Logger.getLogger(Page.class.getName());
    private Map<Integer, Message> messages = new HashMap<>();
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

    public Page(PageMetaData pmd, Map<Integer, Message> messages) throws MessagingException {
        this.pageMetaData = pmd;
        this.messages = messages;
    }

    public Map<Integer, Message> getMessages() {

        return Collections.unmodifiableMap(messages);
    }

    public String toString() {
        int key = 0;
        Message v = null;
        StringBuilder s = new StringBuilder();
        String t = null;
        s.append("\n---page start---\n");
        s.append(pageMetaData.toString());
        for (Entry<Integer, Message> entry : messages.entrySet()) {
            key = entry.getKey();
            v = messages.get(key);
            try {
                t = key + "\t\t" + v.getSubject() + "\n";
            } catch (MessagingException ex) {
                LOG.warning("bad message\n" + ex);
            }
            s.append(t);
        }
        s.append("---page end---");
        return s.toString();
    }

    public PageMetaData getPageMetaData() {
        return pageMetaData;
    }

    public void setPageMetaData(PageMetaData pageMetaData) {
        this.pageMetaData = pageMetaData;
    }
}

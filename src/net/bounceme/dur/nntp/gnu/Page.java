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

    public String toString() {
        String s = "\n---new page---\n" + getPageMetaData().toString() + "\n";
        return s;
    }

    public PageMetaData getPageMetaData() {
        return pageMetaData;
    }

    public void setPageMetaData(PageMetaData pageMetaData) {
        this.pageMetaData = pageMetaData;
    }
}

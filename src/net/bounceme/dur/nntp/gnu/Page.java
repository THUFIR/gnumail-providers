package net.bounceme.dur.nntp.gnu;

import gnu.mail.providers.nntp.GMD;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Logger;
import javax.mail.Header;
import javax.mail.Message;

public class Page {

    private final static Logger LOG = Logger.getLogger(Page.class.getName());
    private List<Message> m = new ArrayList<>();
    private PMD pmd = new PMD();

    public Page() throws Exception {
        GMD gmd = new GMD(getNewsgroup());
        pmd = new PMD(gmd);
    }

    Page(GMD gmd) throws Exception {
        pmd = new PMD(gmd);
    }

    Page(PMD pmd) throws Exception {
        this.pmd = pmd;
    }

    Page(PMD pmd, List<Message> m) throws Exception {
        this.pmd = pmd;
        this.m = m;
        print();
    }

    public String getNewsgroup() {
        return "comp.lang.java.help";
    }

    public String toString() {
        return "fuck";
    }

    public PMD getPmd() {
        return pmd;
    }

    public void setPmd(PMD pmd) {
        this.pmd = pmd;
    }

    private void print() throws Exception {
        Object o;
        for (int i = 1; i < 5; i++) {
            Message msg = m.get(i);
            Enumeration headers = msg.getAllHeaders();
            while (headers.hasMoreElements()) {
                o = headers.nextElement();
                Header header = (Header) o;
                if ("Xref".equals(header.getName())) {
                    LOG.info("\n" + header.getName() + "\n" + header.getValue());
                }
            }
            LOG.info(msg.getSubject());
            LOG.warning("\n\n\n**********************\n\n\n");
        }
    }
}

package net.bounceme.dur.nntp.gnu;

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

    public Page(GMD gmd) throws Exception {
        pmd = new PMD(gmd);
    }

    public Page(PMD pmd) throws Exception {
        this.pmd = pmd;
    }

    public Page(PMD pmd, List<Message> m)  {
        this.pmd = pmd;
        this.m = m;
        try {
            printXref();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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

    private void printXref() throws Exception {
        for (Message msg : m) {
            int i = m.indexOf(msg);
            Enumeration headers = msg.getAllHeaders();
            while (headers.hasMoreElements()) {
                Object o = headers.nextElement();
                Header header = (Header) o;
                if ("Xref".equals(header.getName())) {
                    String headerString = header.getValue();
                    int index = headerString.indexOf(":");
                    String subString = headerString.substring(index + 1);
                    int xref = Integer.parseInt(subString);
                    LOG.info(i + "\t\t" + xref);
                }
            }
            LOG.fine(msg.getSubject());
            LOG.fine("\n\n\n**********************\n\n\n");
        }
    }
}

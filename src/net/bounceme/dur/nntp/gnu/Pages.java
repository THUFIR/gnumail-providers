package net.bounceme.dur.nntp.gnu;

import gnu.mail.providers.nntp.GroupMetaData;
import java.util.logging.Logger;

public class Pages {

    private final static Logger LOG = Logger.getLogger(Pages.class.getName());
    private Usenet u = Usenet.INSTANCE;

    public Pages() throws Exception {
        LOG.fine("in constructor");
        PageMetaData pmd = new PageMetaData(new GroupMetaData("comp.lang.java.help")); //dummy
        Page page = null;
        for (int i = 0; i < 5; i++) {
            LOG.fine("in loop");
            page = u.getPage(pmd);
            pmd = new PageMetaData(page.getPageMetaData(), true);
            LOG.info(page.toString());
        }
    }

    //public static void main(String... args) throws Exception {
    //    new Pages();
    //}
}

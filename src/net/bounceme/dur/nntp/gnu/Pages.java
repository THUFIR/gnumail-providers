package net.bounceme.dur.nntp.gnu;

import gnu.mail.providers.nntp.GMD;
import java.util.logging.Logger;

public class Pages {

    private final static Logger LOG = Logger.getLogger(Pages.class.getName());
    Usenet u = Usenet.INSTANCE;

    public Pages() throws Exception {
        LOG.info("in constructor");
        PMD pmd = new PMD(new GMD("comp.lang.java.help")); //dummy
        Page page = u.getPage(pmd);
        LOG.info(page.toString());
    }

    public static void main(String... args) throws Exception {
        new Pages();
    }
}

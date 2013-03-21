package net.bounceme.dur.nntp.gnu;

import java.util.logging.Logger;

public class Main {

    private final static Logger LOG = Logger.getLogger(Main.class.getName());

    Usenet u = Usenet.INSTANCE;

    public Main() throws Exception {
                LOG.info("in constructor");
                u.connect();
    }

    public static void main(String... args) throws Exception {
        LOG.info("well here we are");
        new Main();
    }
}

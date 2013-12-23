package net.bounceme.dur.nntp.gnu;

import java.util.List;
import java.util.logging.Logger;
import javax.mail.Folder;
import javax.mail.NoSuchProviderException;

public class Main {

    private static final Logger log = Logger.getLogger(Main.class.getName());
    NNTP nntp = NNTP.INSTANCE;

    Main() throws NoSuchProviderException  {
        foo();
    }

    public static void main(String[] args) throws NoSuchProviderException {
        new Main();
    }

    private void foo() throws NoSuchProviderException   {
        nntp.connect();
        List<Folder> folders = nntp.getFolders();
        for (Folder f : folders) {
            log.info(f.getFullName());
        }
    }
}

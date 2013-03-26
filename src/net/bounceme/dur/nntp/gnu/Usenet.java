package net.bounceme.dur.nntp.gnu;

import gnu.mail.providers.nntp.GMD;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.*;

//trim, trim, trim
public enum Usenet {

    INSTANCE;
    private final Logger LOG = Logger.getLogger(Usenet.class.getName());
    private Properties props = new Properties();
    private Folder root = null;
    private Store store = null;
    private List<Folder> folders = new ArrayList<>();
    private Folder folder = null;

    Usenet() {
        LOG.fine("controller..");
        props = PropertiesReader.getProps();
        try {
            connect();
        } catch (Exception ex) {
            Logger.getLogger(Usenet.class.getName()).log(Level.SEVERE, "FAILED TO LOAD MESSAGES", ex);
        }
    }

    //hmm, is the property nntp.host correct?
    public void connect() throws Exception {
        LOG.fine("Usenet.connect..");
        Session session = Session.getDefaultInstance(props);
        session.setDebug(false);
        store = session.getStore(new URLName(props.getProperty("nntp.host")));
        store.connect();
        root = store.getDefaultFolder();
        LOG.fine("store is...\t" + store.toString());
        LOG.fine("root is..\t" + root.toString());
        LOG.fine("root size is..\t" + root.getFullName());
        LOG.fine("root.listSubscribed are..\t" + root.listSubscribed().toString());
        setFolders(Arrays.asList(root.listSubscribed()));
    }

    public Page getPage(PMD pmd) throws Exception {
        LOG.fine("fetching.." + pmd.getGmd().getGroup());
        folder = root.getFolder(pmd.getGmd().getGroup());
        folder.open(Folder.READ_ONLY);
        LOG.fine("..fetched " + folder);
        pmd.init();
        LOG.fine(pmd.toString());
        List<Message> messages = Arrays.asList(folder.getMessages());
        messages = Collections.unmodifiableList(messages);
        Page p = new Page(pmd, messages);

        return p;
    }

    public List<Folder> getFolders() {
        LOG.fine("folders " + folders);
        return Collections.unmodifiableList(folders);
    }

    private void setFolders(List<Folder> folders) {
        this.folders = folders;
    }
}

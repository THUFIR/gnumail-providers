package net.bounceme.dur.nntp.gnu;

import gnu.mail.providers.nntp.NNTPFolder;
import gnu.mail.providers.nntp.NNTPRootFolder;
import java.util.*;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.*;

public enum NNTP {

    INSTANCE;
    private final Logger LOG = Logger.getLogger(NNTP.class.getName());
    private Properties props = new Properties();
    private NNTPRootFolder root = null;
    private Store store = null;
    private List<Folder> folders = new ArrayList<>();
    private NNTPFolder folder = null;

    NNTP() {
        LOG.fine("controller..");
        props = PropertiesReader.getProps();
     
            connect();
      
     
    }

    public void connect() throws NoSuchProviderException   {
        LOG.fine("Usenet.connect..");
        Session session = Session.getDefaultInstance(props);
        session.setDebug(false);
        store = session.getStore(new URLName(props.getProperty("nntp.host")));
        store.connect();
        root = (NNTPRootFolder) store.getDefaultFolder();
        LOG.log(Level.FINE, "store is...\t{0}", store.toString());
        LOG.log(Level.FINE, "root is..\t{0}", root.toString());
        LOG.log(Level.FINE, "root size is..\t{0}", root.getFullName());
        LOG.fine("root.listSubscribed are..\t" + root.listSubscribed().toString());
        setFolders(Arrays.asList(root.listSubscribed()));
    }

    public Page getPage(PageMetaData pmd)  {
        String group = pmd.getGmd().getGroup();
        LOG.fine("fetching.." + group);
        folder = (NNTPFolder) root.getFolder(group);
        folder.open(Folder.READ_ONLY);
        LOG.fine("..fetched " + folder);
        LOG.fine(pmd.toString());
        Map<Integer, Message> messages = folder.getCache(pmd);
        int key;
        Message m;
        /*    for (Entry<Integer, Message> entry : messages.entrySet()) {
        key = entry.getKey();
        m = entry.getValue();
        LOG.fine(key + m.getSubject());
        }*/
        Page page = new Page(pmd, messages);
        LOG.fine(page.toString());
        return page;
    }

    public List<Folder> getFolders() {
        LOG.fine("folders " + folders);
        return Collections.unmodifiableList(folders);
    }

    private void setFolders(List<Folder> folders) {
        this.folders = folders;
    }
}

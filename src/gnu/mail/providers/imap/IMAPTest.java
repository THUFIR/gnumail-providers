package gnu.mail.providers.imap;

import java.io.IOException;
import java.util.Properties;
import javax.mail.FetchProfile;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;
import javax.mail.event.ConnectionEvent;
import javax.mail.event.ConnectionListener;
import javax.mail.event.FolderEvent;
import javax.mail.event.FolderListener;
import javax.mail.event.StoreEvent;
import javax.mail.event.StoreListener;

/**
 * Simple test for IMAP.
 */
public class IMAPTest
  implements ConnectionListener, StoreListener, FolderListener
{

  public void opened(ConnectionEvent e)
  {
    System.out.println("IMAPTest.opened: "+e.getSource());
  }

  public void closed(ConnectionEvent e)
  {
    System.out.println("IMAPTest.closed: "+e.getSource());
  }

  public void disconnected(ConnectionEvent e)
  {
    System.out.println("IMAPTest.disconnected: "+e.getSource());
  }

  public void notification(StoreEvent e)
  {
    System.out.println("IMAPTest.notification: "+e.getSource());
  }

  public void folderCreated(FolderEvent e)
  {
    System.out.println("IMAPTest.folderCreated: "+e.getSource());
  }

  public void folderDeleted(FolderEvent e)
  {
    System.out.println("IMAPTest.folderDeleted: "+e.getSource());
  }

  public void folderRenamed(FolderEvent e)
  {
    System.out.println("IMAPTest.folderRenamed: "+e.getSource());
  }

  public static void main(String[] args)
  {
    if (args.length<1)
    {
      System.out.println("Syntax: IMAPTest <url>");
      System.exit(1);
    }
    try
    {
      IMAPTest test = new IMAPTest();
      Properties properties = System.getProperties();
      Session session = Session.getDefaultInstance(properties);
      URLName url = new URLName(args[0]);
      System.out.println("Test:getStore");
      Store store = session.getStore(url);
      store.addConnectionListener(test);
      store.addStoreListener(test);
      store.addFolderListener(test);
      System.out.println("Test:connect");
      store.connect();
      System.out.println("Test:getDefaultFolder");
      Folder root = store.getDefaultFolder();
      System.out.println("Test:getFolder");
      Folder inbox = root.getFolder("INBOX");
      System.out.println("Test:open");
      inbox.open(Folder.READ_ONLY);
      System.out.println("Test:getMessages");
      Message[] messages = inbox.getMessages();
      FetchProfile fp = new FetchProfile();
      fp.add(FetchProfile.Item.ENVELOPE);
      System.out.println("Test:fetch");
      inbox.fetch(messages, fp);
      for (int i=0; i<messages.length; i++)
      {
        System.out.println("Test:getMessageNumber");
        int msgnum = messages[i].getMessageNumber();
        System.out.println("Test:getSubject");
        String subject = messages[i].getSubject();
        System.out.println("Message "+msgnum+": Subject: "+subject);
        if (messages[i] instanceof Part)
          printPart((Part)messages[i], 0);
      }
      inbox.close(false);
      store.close();
    }
    catch (MessagingException e)
    {
      e.printStackTrace(System.err);
    }
    catch (IOException e)
    {
      e.printStackTrace(System.err);
    }
  }

  static void printPart(Part part, int depth)
    throws MessagingException, IOException
  {
    System.out.println("* content-type="+part.getContentType());
    Object content = part.getContent();
    if (content instanceof Multipart)
    {
      Multipart multipart = (Multipart)content;
      int count = multipart.getCount();
      for (int j=0; j<count; j++)
      {
        System.out.println("-- Part "+(j+1)+" --");
        printPart(multipart.getBodyPart(j), depth+1);
        System.out.println("----");
      }
    }
    else if (content instanceof String)
    {
      System.out.println("--");
      System.out.println((String)content);
    }
    else
    {
      System.out.println("--");
      System.out.println("* content="+content);
    }
    System.out.println("------");
  }

}

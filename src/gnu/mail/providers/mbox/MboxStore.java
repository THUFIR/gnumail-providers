/*
 * MboxStore.java
 * Copyright (C) 1999 Chris Burdess <dog@gnu.org>
 *
 * This file is part of GNU JavaMail, a library.
 *
 * GNU JavaMail is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * GNU JavaMail is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * As a special exception, if you link this library with other files to
 * produce an executable, this library does not by itself cause the
 * resulting executable to be covered by the GNU General Public License.
 * This exception does not however invalidate any other reasons why the
 * executable file might be covered by the GNU General Public License.
 */

package gnu.mail.providers.mbox;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;

import gnu.mail.treeutil.StatusEvent;
import gnu.mail.treeutil.StatusListener;
import gnu.mail.treeutil.StatusSource;

//import gnu.inet.util.Logger;

/**
 * The storage class implementing the Mbox mailbox file format.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
public final class MboxStore
  extends Store
  implements StatusSource
{

  private static final char separatorChar = '/';
  private static final String INBOX = "inbox";

  static boolean attemptFallback = true;

  private List statusListeners = new ArrayList();

  /**
   * Constructor.
   */
  public MboxStore(Session session, URLName urlname)
  {
    super(session, urlname);
    String af = session.getProperty("mail.mbox.attemptfallback");
    if (af!=null)
      attemptFallback = Boolean.valueOf(af).booleanValue();
  }

  /**
   * There isn't a protocol to implement, so this method just returns.
   */
  protected boolean protocolConnect(
      String host,
      int port,
      String username,
      String password)
    throws MessagingException
  {
    return true;
  }

  /**
   * Returns the default folder.
   */
  public Folder getDefaultFolder()
    throws MessagingException
  {
    // If the url used to contruct the store references a file directly,
    // return this file.
    if (url!=null)
    {
      String file = url.getFile();
      if (file!=null && file.length()>0)
        return getFolder(file);
    }
    // Otherwise attempt to return a sensible root folder.
    String mailhome = session.getProperty("mail.mbox.mailhome");
    if (mailhome == null)
    {
      try
      {
        String userhome = System.getProperty("user.home");
        mailhome = userhome+"/Mail"; // elm
        if (!exists(mailhome))
          mailhome = userhome+"/mail";
        if (!exists(mailhome))
          mailhome = null;
      }
      catch (SecurityException e)
      {
        log("access denied reading system properties");
        mailhome = "/";
      }
    }
    return getFolder(mailhome);
  }

  /**
   * Returns the folder with the specified filename.
   */
  public Folder getFolder(String filename)
    throws MessagingException
  {
    if (filename==null)
      filename = "";
    boolean inbox = false;
    if (INBOX.equalsIgnoreCase(filename))
    {
      // First try the session property mail.mbox.inbox.
      String inboxname = session.getProperty("mail.mbox.inbox");
      if (!exists(inboxname))
        inboxname = null;
      if (inboxname==null && attemptFallback)
      {
        // Try some common (UNIX) locations.
        try
        {
          String username = System.getProperty("user.name");
          inboxname = "/var/mail/"+username; // GNU
          if (!exists(inboxname))
            inboxname = "/var/spool/mail/"+username; // common alternative
          if (!exists(inboxname))
          {
            inboxname = null;
            String userhome = System.getProperty("user.home");
            inboxname = userhome+"/Mailbox"; // qmail etc
          }
          if (!exists(inboxname))
            inboxname = null;
        }
        catch (SecurityException e)
        {
          // not allowed to read system properties
          log("unable to access system properties");
        }
      }
      if (inboxname!=null)
      {
        filename = inboxname;
        inbox = true;
      }
      // otherwise we assume it is actually a file called "inbox"
    }

    // convert into a valid filename for this platform
    StringBuffer buf = new StringBuffer();
    if (filename.length()<1 || filename.charAt(0)!=separatorChar)
      buf.append(File.separator);
    if (separatorChar!=File.separatorChar)
      buf.append(filename.replace(separatorChar, File.separatorChar));
    else
      buf.append(filename);
    filename = buf.toString();

    return new MboxFolder(this, filename, inbox);
  }

  /*
   * Indicates whether the file referred to by the specified filename exists.
   */
  private boolean exists(String filename)
  {
    if (filename!=null)
    {
      File file = new File(filename);
      if (separatorChar!=File.separatorChar)
        file = new File(filename.replace(separatorChar, File.separatorChar));
      return file.exists();
    }
    return false;
  }

  /**
   * Returns the folder specified by the filename of the URLName.
   */
  public Folder getFolder(URLName urlname)
    throws MessagingException
  {
    return getFolder(urlname.getFile());
  }

  Session getSession()
  {
    return session;
  }

  /**
   * Print a log message.
   */
  void log(String message)
  {
    if (session.getDebug())
		{
			Logger logger = Logger.getInstance();
      logger.log("mbox", message);
		}
  }

  // -- StatusSource --

  /**
   * Adds a status listener to this store.
   * The listener will be informed of state changes during potentially
   * lengthy procedures (opening and closing mboxes).
   * @param l the status listener
   * @see #removeStatusListener
   */
  public void addStatusListener(StatusListener l)
  {
    synchronized (statusListeners)
    {
      statusListeners.add(l);
    }
  }

  /**
   * Removes a status listener from this store.
   * @param l the status listener
   * @see #addStatusListener
   */
  public void removeStatusListener(StatusListener l)
  {
    synchronized (statusListeners)
    {
      statusListeners.remove(l);
    }
  }

  /**
   * Processes a status event.
   * This dispatches the event to all the registered listeners.
   * @param event the status event
   */
  protected void processStatusEvent(StatusEvent event)
  {
    StatusListener[] listeners;
    synchronized (statusListeners)
    {
      listeners = new StatusListener[statusListeners.size()];
      statusListeners.toArray(listeners);
    }
    switch (event.getType())
    {
      case StatusEvent.OPERATION_START:
        for (int i=0; i<listeners.length; i++)
          listeners[i].statusOperationStarted(event);
        break;
      case StatusEvent.OPERATION_UPDATE:
        for (int i=0; i<listeners.length; i++)
          listeners[i].statusProgressUpdate(event);
        break;
      case StatusEvent.OPERATION_END:
        for (int i=0; i<listeners.length; i++)
          listeners[i].statusOperationEnded(event);
        break;
    }
  }

}

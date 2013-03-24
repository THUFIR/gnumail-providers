/*
 * IMAPStore.java
 * Copyright (C) 2003 Chris Burdess <dog@gnu.org>
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

package gnu.mail.providers.imap;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;
import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.StoreClosedException;
import javax.mail.URLName;
import javax.mail.event.StoreEvent;

import gnu.inet.imap.IMAPConnection;
import gnu.inet.imap.IMAPConstants;

/**
 * The storage class implementing the IMAP4rev1 mail protocol.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 * @version 0.1
 */
public class IMAPStore
  extends Store
{

  /**
   * The connection to the IMAP server.
   */
  protected IMAPConnection connection = null;

  /**
   * Folder representing the root namespace of the IMAP connection.
   */
  protected IMAPFolder root = null;

  /**
   * The currently selected folder.
   */
  protected IMAPFolder selected = null;

  /**
   * Constructor.
   */
  public IMAPStore(Session session, URLName url)
  {
    super(session, url);
  }

  /**
   * Connects to the IMAP server and authenticates with the specified
   * parameters.
   */
  protected boolean protocolConnect(String host, int port, String username,
      String password)
    throws MessagingException
  {
    if (connection != null)
      return true;
    if (host == null)
      host = getProperty("host");
    if (username == null)
      username = getProperty("user");
    if (port < 0)
      port = getIntProperty("port");
    if (host == null || username == null || password == null)
      return false;
    synchronized (this)
    {
      try
      {
        int connectionTimeout = getIntProperty("connectiontimeout");
        int timeout = getIntProperty("timeout");
        connection = new IMAPConnection(host, port,
            connectionTimeout, timeout, session.getDebug());
        
        if (propertyIsTrue("debug.ansi"))
          connection.setAnsiDebug(true);
        
        List capabilities = connection.capability();
        if (!propertyIsFalse("tls"))
        {
          if (capabilities.contains(IMAPConstants.STARTTLS))
            connection.starttls();
        }
        // Try SASL authentication
        // TODO user ordering of mechanisms
        for (Iterator i = capabilities.iterator(); i.hasNext(); )
        {
          String cap = (String)i.next();
          if (cap.startsWith("AUTH="))
          {
            cap = cap.substring(5);
            if (connection.authenticate(cap, username, password))
              return true;
          }
        }
        if (!capabilities.contains(IMAPConstants.LOGINDISABLED))
          return connection.login(username, password);
        else
          return false; // sorry
      }
      catch (UnknownHostException e)
      {
        throw new MessagingException(e.getMessage(), e);
      }
      catch (IOException e)
      {
        throw new MessagingException(e.getMessage(), e);
      }
      finally
      {
        if (connection!=null && connection.alertsPending())
          processAlerts();
      }
    }
  }

  /**
   * Closes the connection.
   */
  public synchronized void close()
    throws MessagingException
  {
    if (connection!=null)
    {
      synchronized (this)
      {
        try
        {
          connection.logout();
        }
        catch (IOException e)
        {
        }
        connection = null;
      }
    }
    super.close();
  }

  /**
   * Returns the root folder.
   */
  public Folder getDefaultFolder()
    throws MessagingException
  {
    if (root==null)
      root = new IMAPFolder(this, "");
    return root;
  }

  /**
   * Returns the folder with the specified name.
   */
  public Folder getFolder(String name)
    throws MessagingException
  {
    return new IMAPFolder(this, name);
  }

  /**
   * Returns the folder whose name is the file part of the specified URLName.
   */
  public Folder getFolder(URLName urlname)
    throws MessagingException
  {
    return getFolder(urlname.getFile());
  }

  /**
   * Returns the IMAP connection used by this store.
   * @exception StoreClosedException if the store is not currently connected
   */
  protected IMAPConnection getConnection()
    throws StoreClosedException
  {
    if (!isConnected())
      throw new StoreClosedException(this);
    return connection;
  }

  /**
   * Indicates whether the specified folder is selected.
   */
  protected boolean isSelected(IMAPFolder folder)
  {
    return folder.equals(selected);
  }

  /**
   * Sets the selected folder.
   */
  protected void setSelected(IMAPFolder folder)
  {
    selected = folder;
  }

  /**
   * Process any alerts supplied by the server.
   */
  protected void processAlerts()
  {
    String[] alerts = connection.getAlerts();
    for (int i=0; i<alerts.length; i++)
      notifyStoreListeners(StoreEvent.ALERT, alerts[i]);
  }

  // -- Utility methods --
  
  private int getIntProperty(String key)
  {
    String value = getProperty(key);
    if (value!=null)
    {
      try
      {
        return Integer.parseInt(value);
      }
      catch (Exception e)
      {
      }
    }
    return -1;
  }

  private boolean propertyIsFalse(String key)
  {
    return "false".equals(getProperty(key));
  }

  private boolean propertyIsTrue(String key)
  {
    return "true".equals(getProperty(key));
  }

  /*
   * Returns the provider-specific or general mail property corresponding to
   * the specified key.
   */
  private String getProperty(String key)
  {
    String value = session.getProperty("mail.imap."+key);
    if (value==null)
      value = session.getProperty("mail."+key);
    return value;
  }

}

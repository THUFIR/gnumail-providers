/*
 * POP3Store.java
 * Copyright (C) 1999, 2003 Chris Burdess <dog@gnu.org>
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

package gnu.mail.providers.pop3;

import java.io.InputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;

import gnu.inet.pop3.POP3Connection;

/**
 * The storage class implementing the POP3 mail protocol.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 * @author <a href='mailto:nferrier@tapsellferrier.co.uk'>Nic Ferrier</a>
 * @version 1.3
 */
public final class POP3Store
  extends Store
{

	/*
	 * The connection.
	 */
	POP3Connection connection;

	/*
	 * The root folder.
	 */
  POP3Folder root;

  /**
   * If rue, disable use of APOP.
   */
  boolean disableApop;

  /**
   * Constructor.
   */
  public POP3Store(Session session, URLName urlname)
  {
    super(session, urlname);
  }

  /**
   * Connects to the POP3 server and authenticates with the specified
   * parameters.
   */
  protected boolean protocolConnect(String host, int port, String username,
		 	String password)
    throws MessagingException
  {
    if (connection!=null)
      return true;
    if (host==null)
            host = getProperty("host");
    if (username==null)
      username = getProperty("user");
    if (port<0)
      port = getIntProperty("port");
    if (host==null || username==null || password==null)
      return false;
    disableApop = false;
    synchronized (this)
    {
      try
      {
				int connectionTimeout = getIntProperty("connectiontimeout");
				int timeout = getIntProperty("timeout");
				connection = new POP3Connection(host, port,
						connectionTimeout, timeout, session.getDebug());
        // Disable APOP if necessary
        if (propertyIsFalse("apop"))
          disableApop = true;
        // Get capabilities
        List capa = connection.capa();
        if (capa!=null)
        {
          if (!propertyIsFalse("tls") && capa.contains("STLS"))
              connection.stls();
          // Try SASL authentication
          // TODO user ordering of mechanisms
          for (Iterator i = capa.iterator(); i.hasNext(); )
          {
            String cap = (String)i.next();
            if (cap.startsWith("SASL "))
            {
              cap = cap.substring(5);
              if (connection.auth(cap, username, password))
                return true;
            }
          }
        }
        // Fall back to APOP or login
        if (!disableApop)
          return connection.apop(username, password);
        else
          return connection.login(username, password);
      }
      catch (UnknownHostException e)
      {
        throw new MessagingException("Connect failed", e);
      }
      catch (IOException e)
      {
        throw new MessagingException("Connect failed", e);
      }
    }
  }

  /**
   * Closes the connection.
   */
  public void close()
    throws MessagingException
  {
    if (connection!=null)
    {
      synchronized (connection)
      {
        try
        {
					if (propertyIsTrue("rsetbeforequit"))
						connection.rset();
					connection.quit();
        }
        catch (IOException e)
        {
          throw new MessagingException("Close failed", e);
        }
      }
			connection = null;
    }
    super.close();
  }

  /**
   * Returns the root folder.
   */
  public Folder getDefaultFolder()
    throws MessagingException
  {
    synchronized (this)
    {
      if (root==null)
        root = new POP3Folder(this, Folder.HOLDS_FOLDERS);
    }
    return root;
  }

  /**
   * Returns the folder with the specified name.
   */
  public Folder getFolder(String s)
    throws MessagingException
  {
    return getDefaultFolder().getFolder(s);
  }

  /**
   * Returns the folder whose name is the file part of the specified URLName.
   */
  public Folder getFolder(URLName urlname)
    throws MessagingException
  {
    return getDefaultFolder().getFolder(urlname.getFile());
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
    String value = session.getProperty("mail.pop3."+key);
    if (value==null)
      value = session.getProperty("mail."+key);
    return value;
  }

}

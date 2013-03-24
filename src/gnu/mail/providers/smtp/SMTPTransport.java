/*
  GNU-Classpath Extensions: GNU Javamail - SMTP Service Provider
  Copyright (C) 2001 Benjamin A. Speakmon

  For more information on the classpathx please mail: classpathx-discuss@gnu.org

  This program is free software; you can redistribute it and/or
  modify it under the terms of the GNU Lesser General Public License
  as published by the Free Software Foundation; either version 2
  of the License, or (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU Lesser General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
*/

package gnu.mail.providers.smtp;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.URLName;
import javax.mail.event.TransportEvent;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import gnu.inet.smtp.Parameter;
import gnu.inet.smtp.ParameterList;
import gnu.inet.smtp.SMTPConnection;
import gnu.inet.util.Logger;

/** 
 * This transport handles communications with an SMTP server.
 *
 * @author Andrew Selkirk
 * @author Ben Speakmon
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 * @version 2.0
 */
public class SMTPTransport
  extends Transport
{

  /**
   * The connection used to communicate with the server.
   */
  protected SMTPConnection connection;
  
  protected String localHostName;
  
  private List extensions = null;
  private List authenticationMechanisms = null;

  /**
   * Creates a new <code>SMTPTransport</code> instance.
   *
   * @param session a <code>Session</code> value
   * @param urlName an <code>URLName</code> value
   */
  public SMTPTransport(Session session, URLName urlName) 
  {
    super(session, urlName);
   
    // Check for mail.smtp.localhost property
    localHostName = getProperty("localhost");
    if (localHostName==null)
    {
      try
      {
        localHostName = InetAddress.getLocalHost().getHostName();
      }
      catch (UnknownHostException e)
      {
        localHostName = "localhost";
      }
    }
  }

  /**
   * Connects to the SMTP server.
   */
  protected boolean protocolConnect(String host, int port, String username,
      String password)
    throws MessagingException 
  {
    if (connection!=null)
      return true;
    if (host==null)
      host = getProperty("host");
    if (port<0)
      port = getIntProperty("port");
    if (username==null)
      username = getProperty("user");
    
    // Check host
    if (host==null) 
      throw new MessagingException("No SMTP host set (mail.smtp.host)");
    
    try
    {	
      int connectionTimeout = getIntProperty("connectiontimeout");
      int timeout = getIntProperty("timeout");
      connection = new SMTPConnection(host, port,
          connectionTimeout, timeout, session.getDebug());
    
      // EHLO/HELO
      if (propertyIsFalse("ehlo"))
      {
        if (!connection.helo(localHostName))
          throw new MessagingException("HELO failed: "+
              connection.getLastResponse());
      }
      else
      {
        extensions = connection.ehlo(localHostName);
        if (extensions==null)
        {
          if (!connection.helo(localHostName))
            throw new MessagingException("HELO failed: "+
                connection.getLastResponse());
        }
        else
        {
          for (Iterator i=extensions.iterator(); i.hasNext(); )
          {
            String extension = (String)i.next();
            if (extension.startsWith("AUTH "))
            {
              // Populate authenticationMechanisms
              authenticationMechanisms = new ArrayList();
              StringTokenizer st = new StringTokenizer(extension.substring(5));
              while (st.hasMoreTokens())
                authenticationMechanisms.add(st.nextToken());
            }
            else if ("STARTTLS".equals(extension))
            {
              if (!propertyIsFalse("tls"))
              {
                connection.starttls();
              }
            }
          }
        }
      }
      
      // User authentication
      if (authenticationMechanisms!=null &&
          !authenticationMechanisms.isEmpty() &&
          propertyIsTrue("auth"))
      {
        if (username!=null && password!=null)
        {
          // TODO user ordering preferences for auth mechanisms
          for (Iterator i = authenticationMechanisms.iterator(); i.hasNext(); )
          {
            String mechanism = (String)i.next();
            if (connection.authenticate(mechanism, username, password))
              return true;
          }
        }
        return false;
      }
      return true;
    } 
    catch (IOException e)
    {
      throw new MessagingException(e.getMessage(), e);
    }
  }
  
  /**
   * Send the specified message to the server.
   */
  public void sendMessage(Message message, Address[] addresses)
    throws MessagingException, SendFailedException 
  {
    if (!(message instanceof MimeMessage))
      throw new SendFailedException("only MimeMessages are supported");
    // Cast message
    MimeMessage mimeMessage = (MimeMessage)message;
      
    int len = addresses.length;
    List sent = new ArrayList(len);
    List unsent = new ArrayList(len);
    List invalid = new ArrayList(len);
    int deliveryStatus = 0;
    ParameterList params = null; // ESMTP parameters
    
    synchronized (connection)
    {
      if (!isConnected())
        throw new MessagingException("not connected");
      
      try
      {
        // reverse-path
        String from0 = getProperty("from");
        InternetAddress from = null;
        if (from0!=null)
        {
          InternetAddress[] from1 = InternetAddress.parse(from0);
          if (from1!=null && from1.length>0)
            from = from1[0];
        }
        if (from==null)
        {
          Address[] from2 = mimeMessage.getFrom();
          if (from2!=null && from2.length>0 &&
              from2[0] instanceof InternetAddress)
            from = (InternetAddress)from2[0];
        }
        if (from==null)
          from = InternetAddress.getLocalAddress(session);
        String reversePath = from.getAddress();
        // DSN RET
        String dsnRet = getProperty("dsn.ret");
        if (dsnRet!=null)
        {
          String FULL = "FULL", HDRS = "HDRS";
          String value = null;
          if (FULL.equalsIgnoreCase(dsnRet))
            value = FULL;
          else if (HDRS.equalsIgnoreCase(dsnRet))
            value = HDRS;
          if (value!=null)
          {
            params = new ParameterList();
            params.add(new Parameter("RET", value));
          }
        }
        // MAIL FROM
        if (!connection.mailFrom(reversePath, params))
          throw new SendFailedException(connection.getLastResponse());
        params = null;
       
        // DSN NOTIFY
        String dsnNotify = getProperty("dsn.notify");
        if (dsnNotify!=null)
        {
          String NEVER = "NEVER", SUCCESS = "SUCCESS";
          String FAILURE = "FAILURE", DELAY = "DELAY";
          String value = null;
          if (NEVER.equalsIgnoreCase(dsnNotify))
            value = NEVER;
          else
          {
            StringBuffer buf = new StringBuffer();
            StringTokenizer st = new StringTokenizer(dsnNotify, " ,");
            while (st.hasMoreTokens())
            {
              String token = st.nextToken();
              if (SUCCESS.equalsIgnoreCase(token))
              {
                if (buf.length()>0)
                  buf.append(',');
                buf.append(SUCCESS);
              }
              else if (FAILURE.equalsIgnoreCase(token))
              {
                if (buf.length()>0)
                  buf.append(',');
                buf.append(FAILURE);
              }
              else if (DELAY.equalsIgnoreCase(token))
              {
                if (buf.length()>0)
                  buf.append(',');
                buf.append(DELAY);
              }
            }
            if (buf.length()>0)
              value = buf.toString();
          }
          if (value!=null)
          {
            params = new ParameterList();
            params.add(new Parameter("NOTIFY", value));
          }
        }
        // RCPT TO
        for (int i=0; i<addresses.length; i++)
        {
          Address address = addresses[i];
          if (address instanceof InternetAddress)
          {
            String forwardPath = ((InternetAddress)address).getAddress();
            if (connection.rcptTo(forwardPath, params))
              sent.add(address);
            else
              invalid.add(address);
          }
          else
            invalid.add(address);
        }
      }
      catch (IOException e)
      {
        try
        {
          // Reset connection
          connection.rset();
        }
        catch (IOException e2)
        {
          // Possible transport-level problem
        }
        throw new SendFailedException(e.getMessage());
      }
      
      try
      {  
        // DATA
        OutputStream dataStream = connection.data();
        if (dataStream==null)
          throw new MessagingException(connection.getLastResponse());
        mimeMessage.writeTo(dataStream);
        dataStream.flush();
        if (connection.finishData())
        {
          unsent.addAll(sent);
          sent.clear();
          deliveryStatus = TransportEvent.MESSAGE_NOT_DELIVERED;
        }
        else 
        {
          deliveryStatus = invalid.isEmpty() ?
            TransportEvent.MESSAGE_DELIVERED :
            TransportEvent.MESSAGE_PARTIALLY_DELIVERED;
        }
      }
      catch (IOException e)
      {
        try
        {
          // Attempt to ensure that connection is in control mode
          if (connection.finishData())
            connection.rset();
        }
        catch (IOException e2)
        {
          // Possible transport-level problem
        }
        throw new SendFailedException(e.getMessage());
      }
    }
  
    // Notify transport listeners
    Address[] a_sent = new Address[sent.size()];
    sent.toArray(a_sent);
    Address[] a_unsent = new Address[unsent.size()];
    unsent.toArray(a_unsent);
    Address[] a_invalid = new Address[invalid.size()];
    invalid.toArray(a_invalid);

    notifyTransportListeners(deliveryStatus, a_sent, a_unsent, a_invalid,
        mimeMessage);
  }

  /**
   * Close this transport.
   */
  public void close()
    throws MessagingException 
  {
    if (isConnected()) 
    {
      synchronized (connection)
      {
        try 
        {
          connection.quit();
        }
        catch (IOException e) 
        {
          throw new MessagingException(e.getMessage(), e);
        }
        finally
        {
          connection = null;
        }
      }
    }
    super.close();
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
    String value = session.getProperty("mail.smtp."+key);
    if (value==null)
      value = session.getProperty("mail."+key);
    return value;
  }
  
}

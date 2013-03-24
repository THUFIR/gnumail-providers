/*
 * POP3Message.java
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
import java.io.OutputStream;
import java.util.Enumeration;
import javax.activation.DataHandler;
import javax.mail.MessagingException;
import javax.mail.internet.InternetHeaders;

import gnu.inet.pop3.POP3Connection;
import gnu.mail.providers.ReadOnlyMessage;

/**
 * The message class implementing the POP3 mail protocol.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 * @author <a href='mailto:nferrier@tapsellferrier.co.uk'>Nic Ferrier</a>
 * @version 1.2
 */
public final class POP3Message 
  extends ReadOnlyMessage 
{

  /*
   * The size of this message.
   */
  int size;

  /**
   * Create a POP3Message.
	 * @param folder the parent folder
	 * @param msgnum the message number
	 * @param size the size of the entire message
   */
  POP3Message(POP3Folder folder, int msgnum, int size) 
    throws MessagingException 
  {
    super(folder, msgnum);
		this.size = size;
  }

  // -- Content --

  /** 
   * Retrieves the content of the message.
   * This uses the POP3Store to do the retrieval.
   */
  void fetchContent() 
    throws MessagingException 
  {
    POP3Connection connection = ((POP3Store)folder.getStore()).connection;
		synchronized (connection)
		{
			if (content!=null)
				return;
			try
			{
				InputStream retr = connection.retr(msgnum);
				parse(retr);
				// Read to end of stream
				int c = retr.read();
				while (c!=-1)
					c = retr.read();
			}
			catch (IOException e)
			{
				throw new MessagingException(e.getMessage(), e);
			}
		}
  }

  /** 
   * Causes the content to be read in.
   */
  public DataHandler getDataHandler() 
    throws MessagingException
  {
    if (content==null)
      fetchContent();
    return super.getDataHandler();
  }

  /** 
   * Causes the content to be read in.
   */
  protected InputStream getContentStream() 
    throws MessagingException 
  {
    if (content==null)
      fetchContent();
    return super.getContentStream();
  }

  /** 
   * Gets the size of the message.
   * Uses the cached size if it's available to us.
   */
  public int getSize() 
    throws MessagingException 
  {
    if (size>-1)
      return size;
    if (content==null)
      fetchContent();
    return super.getSize();    
  }

  // -- Headers --

  /** 
   * Causes the headers to be read.
   */
  void fetchHeaders() 
    throws MessagingException 
  {
    POP3Connection connection = ((POP3Store)folder.getStore()).connection;
		synchronized (connection)
		{
			if (headers!=null)
				return;
			try
			{
				InputStream top = connection.top(msgnum);
				headers = createInternetHeaders(top);
				// Read to end of stream
				int c = top.read();
				while (c!=-1)
					c = top.read();
			}
			catch (IOException e)
			{
				throw new MessagingException(e.getMessage(), e);
			}
		}
  }

  /**
   * Causes the headers to be read.
   */
  public String[] getHeader(String name) 
    throws MessagingException 
  {
    if (headers==null)
      fetchHeaders();
    return super.getHeader(name);
  }
  
  /**
   * Causes the headers to be read.
   */
  public String getHeader(String name, String delimiter) 
    throws MessagingException 
  {
    if (headers==null)
      fetchHeaders();
    return super.getHeader(name, delimiter);
  }

  /** 
   * Causes the headers to be read.
   */
  public Enumeration getAllHeaders() 
    throws MessagingException 
  {
    if (headers==null)
      fetchHeaders();
    return super.getAllHeaders();
  }

  /** 
   * Causes the headers to be read.
   */
  public Enumeration getAllHeaderLines() 
    throws MessagingException 
  {
    if (headers==null)
      fetchHeaders();
    return super.getAllHeaderLines();
  }

  /** 
   * Causes the headers to be read.
   */
  public Enumeration getMatchingHeaders(String[] names) 
    throws MessagingException 
  {
    if (headers==null)
      fetchHeaders();
    return super.getMatchingHeaders(names);
  }

  /** 
   * Causes the headers to be read.
   */
  public Enumeration getMatchingHeaderLines(String[] names) 
    throws MessagingException 
  {
    if (headers==null)
      fetchHeaders();
    return super.getMatchingHeaderLines(names);
  }

  /** 
   * Causes the headers to be read.
   */
  public Enumeration getNonMatchingHeaders(String[] names) 
    throws MessagingException 
  {
    if (headers==null)
      fetchHeaders();
    return super.getNonMatchingHeaders(names);
  }

  /** 
   * Causes the headers to be read.
   */
  public Enumeration getNonMatchingHeaderLines(String[] names) 
    throws MessagingException 
  {
    if (headers==null)
      fetchHeaders();
    return super.getNonMatchingHeaderLines(names);
  }

  // -- Utility --

  public void writeTo(OutputStream msgStream) 
    throws IOException, MessagingException 
  {
    if (content==null)
      fetchContent();
    super.writeTo(msgStream);
  }

  public void writeTo(OutputStream msgStream, String[] ignoreList) 
    throws IOException, MessagingException 
  {
    if (content==null)
      fetchContent();
    super.writeTo(msgStream, ignoreList);
  }

}

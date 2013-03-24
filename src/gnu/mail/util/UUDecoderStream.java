/*
 * UUDecoderStream.java
 * Copyright (C) 2000 2001  Andrew Selkirk, Nic Ferrier
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

package gnu.mail.util;

import java.io.InputStream;
import java.io.FilterInputStream;
import java.io.IOException;

import gnu.inet.util.LineInputStream;

/** 
 * A stream that does UU Decoding.
 * UU-Decoding is defined in the MIME rfcs.
 *
 * @author Andrew Selkirk: aselkirk@mailandnews.com
 * @author Nic Ferrier: nferrier@tapsellferrier.co.uk
 * @version 1.0
 */
public class UUDecoderStream
  extends FilterInputStream
{

  /** buffer of bytes to be decoded.
   */
  private byte[] decode_buffer;

  /** current number of bytes in buffer to be decoded.
   */
  private int index;

  /** decoded buffer.
   */
  private byte[] buffer;

  /** current number of bytes in buffer.
   */
  private int bufsize;

  /** flag to indicate if prefix read.
   */
  private boolean hasPrefix;

  /** name of file to be uudecoded.
   */
  protected String name;

  /** permission mode of file.
   */
  protected int mode;

  /** create a new UU-Decoding stream.
   *
   * @param stream Input stream
   */
  public UUDecoderStream(InputStream in) 
  {
    super(in);
    if (!(in instanceof LineInputStream))
      this.in = new LineInputStream(in);
    this.name = null;
    this.mode = -1;
    hasPrefix = false;
    buffer = new byte[45];
    bufsize = 0;
    decode_buffer = new byte[60];
    index = 0;
  }

  /** @return Name of file
   * @throws IOException IO Exception occurred
   */
  public String getName()
    throws IOException 
  {
    if (!hasPrefix) 
      readPrefix();
    return name;
  }

  /**
   * Decode bytes in decode_buffer to buffer for reading.
   * @throws IOException IO Exception occurred
   */
  private void decode()
    throws IOException 
  {
    int decode_index;
    int c1;
    int c2;
    int c3;
    int c4;
    int a;
    int b;
    int c;
    // Process bytes in groups of four
    decode_index = 0;
    index = 0;
    bufsize = 0;
    while (decode_index < decode_buffer.length) 
    {
      // Read in 4 bytes
      c1 = (decode_buffer[decode_index] - ' ') & 0x3f;
      c2 = (decode_buffer[decode_index+1] - ' ') & 0x3f;
      c3 = (decode_buffer[decode_index+2] - ' ') & 0x3f;
      c4 = (decode_buffer[decode_index+3] - ' ') & 0x3f;
      // Decode Bytes
      a = ((c1 << 2) & 0xfc) | ((c2 >>> 4) & 3);
      b = ((c2 << 4) & 0xf0) | ((c3 >>> 2) & 0xf);
      c = ((c3 << 6) & 0xc0) | (c4 & 0x3f);
      // Store Decoded Bytes in read buffer
      buffer[bufsize]   = (byte) (a & 0xff);
      buffer[bufsize+1] = (byte) (b & 0xff);
      buffer[bufsize+2] = (byte) (c & 0xff);
      // Increment Index
      decode_index += 4;
      bufsize += 3;
    }
  }

  /** 
   * Read byte from decoded buffer.
   * If buffer empty, the next line of encoded bytes
   * is read and decoded.
   *
   * @return next byte
   * @throws IOException IO Exception occurred
   */
  public int read()
    throws IOException 
  {
    int next;
    int lengthByte;
    int length;
    int decodeLength;
    int result;
    String line;
    // Check for Read Prefix
    if (hasPrefix == false) 
      readPrefix();
    // Check for Reading in Decoding buffer
    LineInputStream lin = (LineInputStream)in;
    if (index == bufsize) 
    {
      // Read Line
      line = lin.readLine();
      // Get Length
      lengthByte = line.getBytes()[0];
      length = (lengthByte - ' ') & 0x3f;
      // Check for Decoded length
      if (length > 45) 
        throw new IOException("UUDecode error: line length to large ("
            + length + ")");
      // Check for End
      if (length == 0) 
      {
        readSuffix();
        return -1;
      }
      // Get Encoded Line Size
      decodeLength = line.length() - 1;
      // Check for encoded line length multiple of 4
      if ((decodeLength % 4) != 0) 
        throw new IOException("UUDecode error: line length not multiple of 4 ("
            + decodeLength + ")");
      // Get Encoded Line
      decode_buffer = line.substring(1).getBytes();
      // Decode Buffer
      decode();
    }
    // Get Next Byte
    next = buffer[index];
    // Increment Index
    index += 1;
    return next;
  }

  /**
   * Read byte from decoded buffer.
   * If buffer empty, the next line of encoded bytes is read and decoded.
   *
   * @param bytes Byte array to write bytes into
   * @param offset Offset of array to write
   * @param length Number of bytes to write
   * @return Number of bytes read
   * @throws IOException IO Exception occurred
   */
  public int read(byte[] bytes, int offset, int length)
    throws IOException 
  {
    // FIXME: This implementation is not as efficient as
    // it could be.  Instead of delegating the work to
    // read(), the bytes should be read in bulk
    // to the buffer and initiating decoding if necessary.
    // This idea does introduce some duplication of code.
    int index;
    // Write Bytes
    for (index = offset; index < length; index++) 
      bytes[index] = (byte) read();
    // Return Number of bytes
    return length - offset;
  }

  /** @return the number of bytes that are available that will not block.
   * @throws IOException IO Exception occurred
   */
  public int available()
    throws IOException 
  {
    if (hasPrefix == false) 
    {
      readPrefix();
      read();
      index = 0;
    }
    // Calculate # bytes left in current buffer
    return bufsize - index;
  }

  /**  @return false because mark is not supported in UU Decoding.
   */
  public boolean markSupported() 
  {
    return false;
  }

  /** get mode from UU-Decoding.
   *
   * @return File permission mode
   * @throws IOException IO Exception occurred
   */
  public int getMode()
    throws IOException 
  {
    if (!hasPrefix) 
      readPrefix();
    return mode;
  }

  /** 
   * Read prefix.
   *
   * @throws IOException IO Exception occurred
   */
  private void readPrefix()
    throws IOException 
  {
    // Note: One enhancement for this is to scan lines
    // until we find a 'begin'.  This way, it is fine
    // if there are a number of newlines before the
    // stream begins.
    // Get First line
    String line = ((LineInputStream)in).readLine();
    // Check for 'begin'
    if (!line.startsWith("begin") == false) 
      throw new IOException("UUDecoder error: No Begin");
    // Check for Mode
    try 
    {
      mode = Integer.parseInt(line.substring(6, 9));
    }
    catch (Exception e) 
    {
      throw new IOException("UUDecoder error: Unable to determine mode");
    }
    // Check for name
    try 
    {
      name = line.substring(10);
    }
    catch (Exception e) 
    {
      throw new IOException("UUDecoder error: Unable to determine name");
    }
    // Mark Prefix Read
    hasPrefix = true;
  }

  /** 
   * Read suffix.
   *
   * @throws IOException IO Exception occurred
   */
  private void readSuffix()
    throws IOException 
  {
    // Get First line
    String line = ((LineInputStream)in).readLine();
    // Check for 'end'
    if (!line.startsWith("end")) 
      throw new IOException("UUDecoder error: No End: " + line);
  }

}

/*
 * PrintStreamLogger.java
 * Copyright (C) 2004 The Free Software Foundation
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

import java.io.PrintStream;
import gnu.inet.util.Logger;

/** 
 * A logger that writes to the specified print stream.
 *
 * @author <a href="mailto:dog@gnu.org">Chris Burdess</a>
 */
public class PrintStreamLogger
  extends Logger
{

  PrintStream out;
  
  /**
   * Constructor.
   * @param out the underlying print stream to write to
   */
  public PrintStreamLogger(PrintStream out)
  {
    this.out = out;
  }

  /**
   * Returns the underlying print stream used by this logger.
   */
  public PrintStream getPrintStream()
  {
    return out;
  }

  public void config(String message)
  {
    out.print("DEBUG: ");
    out.println(message);
  }

  public void log(String protocol, String message)
  {
    System.err.print(protocol);
    System.err.print(": ");
    System.err.println(message);
  }
  
}

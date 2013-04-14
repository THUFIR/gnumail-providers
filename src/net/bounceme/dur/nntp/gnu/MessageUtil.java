package net.bounceme.dur.nntp.gnu;

import java.util.Enumeration;
import java.util.logging.Logger;
import javax.mail.Header;
import javax.mail.Message;
import javax.mail.MessagingException;

public class MessageUtil {

    private final static Logger LOG = Logger.getLogger(MessageUtil.class.getName());

    public String getXref(Message message) throws MessagingException {
        LOG.fine("starting xref printing...\t" + message.getSubject());
        StringBuilder sb = new StringBuilder();
        String s = null;
        String headerString = null;
        String subString = null;
        sb.append("messages to follow\n");
        //for (Message message : messages) {

        LOG.fine(message.getSubject());

        @SuppressWarnings("unchecked")
        Enumeration<Header> headers = message.getAllHeaders();

        while (headers.hasMoreElements()) {
            Header header = headers.nextElement();
            if ("Xref".equals(header.getName())) {
                headerString = header.getValue();
                int index = headerString.indexOf(":");
                subString = headerString.substring(index + 1);
                int xref = Integer.parseInt(subString);
                s = "\n" + "\t\t" + xref;
                sb.append(s);
                s = message.getSubject();
                sb.append("\t").append(s);
            }
        }
        LOG.fine("\n\n\n**********************\n\n\n");
        //}
        LOG.fine(sb.toString());
        return sb.toString();
    }

    public int getXrefObj(Message message) throws MessagingException {
        LOG.fine("starting xref printing...\t" + message.getSubject());
        int xref = 0;
        StringBuilder sb = new StringBuilder();
        String s = null;
        String headerString = null;
        String subString = null;
        sb.append("messages to follow\n");
        //for (Message message : messages) {

        LOG.fine(message.getSubject());

        @SuppressWarnings("unchecked")
        Enumeration<Header> headers = message.getAllHeaders();

        while (headers.hasMoreElements()) {
            Header header = headers.nextElement();
            if ("Xref".equals(header.getName())) {
                headerString = header.getValue();
                int index = headerString.indexOf(":");
                subString = headerString.substring(index + 1);
                xref = Integer.parseInt(subString);
                s = "\n" + "\t\t" + xref;
                sb.append(s);
                s = message.getSubject();
                sb.append("\t").append(s);
            }
        }
        LOG.fine("\n\n\n**********************\n\n\n");
        //}
        LOG.fine(sb.toString());
        return xref;
    }
}

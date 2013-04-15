package net.bounceme.dur.nntp.swing;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import net.bounceme.dur.nntp.gnu.PageMetaData;
import net.bounceme.dur.nntp.gnu.Page;
import net.bounceme.dur.nntp.gnu.Usenet;

public class ArticlesTable extends JScrollPane {

    private static final Logger LOG = Logger.getLogger(ArticlesTable.class.getName());
    private static final long serialVersionUID = 1L;
    public static final String INDEX = "index";
    private JTable jTable = new JTable();
    private DefaultTableModel defaultTableModel = new DefaultTableModel();
    private Page page;
    private Usenet usenetConnection = Usenet.INSTANCE;

    public ArticlesTable() throws Exception {
        page = new Page();
        LOG.fine(page.toString());
        initComponents();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        defaultTableModel = new DefaultTableModel(new Object[][]{
                    {"some", "text"}, {"any", "text"}, {"even", "more"},
                    {"text", "strings"}, {"and", "other"}, {"text", "values"}},
                new Object[]{"Column 1", "Column 2"});
        jTable.setModel(defaultTableModel);
        jTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jTable.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseReleased(java.awt.event.MouseEvent evt) {
                itemSelected();
            }
        });
        jTable.addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyReleased(java.awt.event.KeyEvent evt) {
                itemSelected();
            }
        });

        setSize(2000, 2000);
        setViewportView(jTable);
        jTable.setVisible(true);
        setVisible(true);
    }

    private void itemSelected() {
        int row = jTable.getSelectedRow();
        int column = 0;
        Object selectedObject = jTable.getValueAt(row, column);
        int i = (int) selectedObject;
        Map<Integer, Message> messages = page.getMessages();
        Message message = messages.get(i);
        firePropertyChange("message", null, message);
    }

    public final void nextPage() throws Exception {
        LOG.fine("trying to get next page..." + page);
        PageMetaData pageMetaData = page.getPageMetaData();
        pageMetaData.next();
        page = usenetConnection.getPage(pageMetaData);
        LOG.fine("was the page advanced?" + page);
        loadDLM();
        LOG.info(page.toString());
    }

    @SuppressWarnings("unchecked")
    private void loadDLM() throws MessagingException {
        LOG.fine("trying to load...");
        defaultTableModel = new DefaultTableModel();
        defaultTableModel.addColumn("key");
        defaultTableModel.addColumn("message subject");
        Vector rowData = new Vector();
        Message message = null;
        Map<Integer, Message> messages = page.getMessages();
        int key = 0;
        LOG.fine("trying to traverse..." + messages.size());
        for (Entry<Integer, Message> entry : messages.entrySet()) {
            rowData.clear();
            key = entry.getKey();
            message = messages.get(key);
            rowData.add(key);
            rowData.add(message.getSubject());
            LOG.fine("vector\t" + key + "\t" + message.getSubject());
            defaultTableModel.addRow(rowData);
        }
    }

    private Message getMessage(int i) {
        LOG.fine("trying to get\t\t" + i);
        Map<Integer, Message> messages = page.getMessages();
        Message m = messages.get(i);
        return m;
    }
}

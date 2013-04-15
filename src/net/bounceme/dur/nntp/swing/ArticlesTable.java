package net.bounceme.dur.nntp.swing;

import java.util.HashMap;
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
    private PageMetaData pageMetaData = new PageMetaData();
    //private Map<Integer, Message> messages = new HashMap<>();

    public ArticlesTable() {
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
        int column = 1;
        Object selectedObject = jTable.getValueAt(row, column);
        @SuppressWarnings("unchecked")
        Entry<Integer, Message> entry = (Entry<Integer, Message>) selectedObject;
        LOG.fine("selected\t\t" + row);
        row = (row < 0) ? 0 : row;
        firePropertyChange("message", null, entry);
    }

    public final void nextPage() throws Exception {
        page = new Page(pageMetaData);
        pageMetaData = new PageMetaData(page.getPageMetaData(), true);
        page = usenetConnection.getPage(pageMetaData);
        Map<Integer, Message> messages = page.getMessages();
        loadDLM();
        LOG.fine(page.toString());
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
            rowData.add(message);
            LOG.fine("vector\t" + key + "\t" + message.getSubject());
            defaultTableModel.addRow(rowData);
        }
        jTable.setModel(defaultTableModel);
        jTable.repaint();
        jTable.revalidate();
        repaint();
        revalidate();
    }

    private Message getMessage(int i) {
        LOG.fine("trying to get\t\t" + i);
        Map<Integer, Message> messages = page.getMessages();
        Message m = messages.get(i);
        return m;
    }
}

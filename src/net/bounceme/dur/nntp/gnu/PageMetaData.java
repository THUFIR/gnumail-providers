package net.bounceme.dur.nntp.gnu;

import gnu.mail.providers.nntp.GroupMetaData;
import java.util.logging.Logger;

public class PageMetaData {

    private static final Logger LOG = Logger.getLogger(PageMetaData.class.getName());
    private GroupMetaData gmd = new GroupMetaData();
    private int pageStart = 0;
    private int pageEnd = 0;
    private int rowsPerPage = 5;
    private boolean lastPage = false;

    public PageMetaData() {
        //init();
    }

    PageMetaData(GroupMetaData gmd) {
        this.gmd = gmd;
        //init();
    }

    /*public final void init() {
    pageEnd = Math.abs(pageEnd);
    pageStart = Math.abs(pageStart);
    rowsPerPage = Math.abs(rowsPerPage);

    pageStart = pageStart > gmd.getFirst() ? gmd.getFirst() : pageStart;
    pageEnd = pageEnd + rowsPerPage > gmd.getLast() ? gmd.getLast() : pageEnd;
    if (pageEnd == gmd.getLast()) {
    setLastPage(true);
    }

    if (lastPage) {
    pageEnd = gmd.getLast();
    pageStart = pageEnd - rowsPerPage;
    }

    pageEnd = Math.abs(pageEnd);
    pageStart = Math.abs(pageStart);
    rowsPerPage = Math.abs(rowsPerPage);

    if (pageStart > pageEnd) {
    pageStart = pageEnd;
    }

    //here starts gibberish
    if (pageStart < 2) {
    pageStart = 5;
    pageEnd = pageStart + rowsPerPage;
    }

    }*/
    public GroupMetaData getGmd() {
        return gmd;
    }

    public void setGmd(GroupMetaData gmd) {
        this.gmd = gmd;
    }

    public int getPageStart() {
        LOG.fine("pageStart\t\t" + pageStart);
        return pageStart;
    }

    public void setPageStart(int pageStart) {
        this.pageStart = pageStart;
    }

    public int getPageEnd() {

        LOG.fine("pageEnd\t\t" + pageEnd);
        return pageEnd;
    }

    public void setPageEnd(int pageEnd) {
        this.pageEnd = pageEnd;
    }

    public int getRowsPerPage() {
        return rowsPerPage;
    }

    public void setRowsPerPage(int rowsPerPage) {
        this.rowsPerPage = rowsPerPage;
    }

    public boolean isLastPage() {
        return lastPage;
    }

    public void setLastPage(boolean lastPage) {
        this.lastPage = lastPage;
    }

    public PageMetaData getNext() {
        LOG.info("\ngetting next..\n" + this.toString() + "\n\n\n");
        PageMetaData pmd = new PageMetaData();
        pmd.setGmd(gmd);
        pmd.setPageEnd(pageEnd + rowsPerPage);
        pmd.setPageStart(pageEnd + 1);
        pmd.setRowsPerPage(rowsPerPage);
        LOG.info("\nreturning next..\n" + this.toString() + "\n\n\n");
        return pmd;
    }

    public String toString() {
        return "---pmd---\n" + pageStart + "\t\t" + pageEnd;
    }
}

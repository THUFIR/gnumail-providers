package net.bounceme.dur.nntp.gnu;

import java.util.logging.Logger;

public class PMD {

    private static final Logger LOG = Logger.getLogger(PMD.class.getName());
    private GMD gmd = new GMD();
    private int pageStart = 0;
    private int pageEnd = 0;
    private int rowsPerPage = 5;
    private boolean lastPage = false;

    public PMD() {
        init();
    }

    PMD(GMD gmd) {
        this.gmd = gmd;
        init();
    }

    public final void init() {
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

    }

    public GMD getGmd() {
        return gmd;
    }

    public void setGmd(GMD gmd) {
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

    public PMD getNext() {
        PMD pmd = new PMD();
        pmd.setGmd(gmd);
        pmd.setPageEnd(pageEnd + rowsPerPage);
        pmd.setPageStart(pageEnd + 1);
        pmd.setRowsPerPage(rowsPerPage);
        pmd.init();
        return pmd;
    }

    public String toString() {
        return pageStart + "\t\t" + pageEnd;
    }
}

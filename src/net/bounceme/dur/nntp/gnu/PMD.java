package net.bounceme.dur.nntp.gnu;

import gnu.mail.providers.nntp.GroupMetaData;
import java.util.logging.Logger;

public class PMD {

    private static final Logger LOG = Logger.getLogger(PMD.class.getName());
    private GroupMetaData gmd = new GroupMetaData();
    private int pageStart = 0;
    private int pageEnd = 0;
    private int rowsPerPage = 5;
    private boolean lastPage = false;

    public PMD() {
    }

    PMD(GroupMetaData gmd) {
        this.gmd = gmd;
    }

    public PMD(PMD previousPageMetaData, boolean next) {
        gmd = previousPageMetaData.getGmd();
        setRowsPerPage(previousPageMetaData.getRowsPerPage());
        setPageStart(previousPageMetaData.getPageEnd() + 1);
        setPageEnd(previousPageMetaData.getPageEnd() + rowsPerPage);
        LOG.fine("\nreturning next..\n" + this.toString() + "\n\n\n");
    }

    public GroupMetaData getGmd() {
        return gmd;
    }

    private void setGmd(GroupMetaData gmd) {
        this.gmd = gmd;
    }

    public int getPageStart() {
        LOG.fine("pageStart\t\t" + pageStart);
        return pageStart;
    }

    private void setPageStart(int pageStart) {
        this.pageStart = pageStart;
    }

    public int getPageEnd() {

        LOG.fine("pageEnd\t\t" + pageEnd);
        return pageEnd;
    }

    private void setPageEnd(int pageEnd) {
        this.pageEnd = pageEnd;
    }

    public int getRowsPerPage() {
        return rowsPerPage;
    }

    private void setRowsPerPage(int rowsPerPage) {
        this.rowsPerPage = rowsPerPage;
    }

    public boolean isLastPage() {
        return lastPage;
    }

    private void setLastPage(boolean lastPage) {
        this.lastPage = lastPage;
    }

    public String toString() {
        return "---pmd---\n" + gmd + pageStart + "\t\t" + pageEnd;
    }
}

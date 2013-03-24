/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.bounceme.dur.nntp.gnu;

/**
 *
 * @author thufir
 */
class Newsgroup {

    private String newsgroup;

    Newsgroup(Page page) {
        newsgroup = page.getNewsgroup();
    }

    String getNewsgroup() {
        return newsgroup;
    }

    public void setNewsgroup(String newsgroup) {
        this.newsgroup = newsgroup;
    }
}

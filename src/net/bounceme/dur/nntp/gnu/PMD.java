package net.bounceme.dur.nntp.gnu;

import gnu.mail.providers.nntp.GMD;

public class PMD {

    private GMD gmd = new GMD();
    private int cursor = 0;
    private int min = 0;
    private int max = 0;
    private int size = 5;

    public PMD() {
    }

    PMD(GMD gmd) {
        this.gmd = gmd;
    }

    public GMD getGmd() {
        return gmd;
    }

    public void setGmd(GMD gmd) {
        this.gmd = gmd;
    }

    public int getCursor() {
        return cursor;
    }

    public void setCursor(int cursor) {
        this.cursor = cursor;
    }

    public int getMin() {
        return Math.abs(min);
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return Math.abs(max);
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getSize() {
        return Math.abs(size);
    }

    public void setSize(int size) {
        this.size = size;
    }
}

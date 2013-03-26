package net.bounceme.dur.nntp.gnu;

import gnu.inet.nntp.GroupResponse;

public class GroupMetaData {

    private String group = "no.group.here.null.null.null";
    private int count = 0;
    private int first = 0;
    private int last = 0;
    private boolean open = false;

    public GroupMetaData() {
    }

    public GroupMetaData(String group) {
        this.group = group;
    }

    public GroupMetaData(GroupResponse gr) {
        group = gr.group;
    }

    public GroupMetaData(GroupResponse gr, boolean open) {
        group = gr.group;
        this.open = open;
    }

    public String getGroup() {
        return group;
    }

    public int getCount() {
        return count;
    }

    public int getFirst() {
        return first;
    }

    public int getLast() {
        return last;
    }

    public boolean isOpen() {
        return open;
    }

    void setOpen(boolean open) {
        this.open = open;
    }

    public String toString() {
        return group + "\t\tisOpen\t\t" + open;
    }
}

package gnu.mail.providers.nntp;

import gnu.inet.nntp.GroupResponse;

public class GMD {

    private String group = "no.group.here.null.null.null";
    private int count = 0;
    private int first = 0;
    private int last = 0;
    private boolean isOpen = false;

    public GMD() {
    }

    public GMD(String group) {
        this.group = group;
    }

    public GMD(GroupResponse gr) {
        group = gr.group;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getFirst() {
        return first;
    }

    public void setFirst(int first) {
        this.first = first;
    }

    public int getLast() {
        return last;
    }

    public void setLast(int last) {
        this.last = last;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }
}

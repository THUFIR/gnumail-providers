package gnu.mail.providers.nntp;

import gnu.inet.nntp.GroupResponse;
import java.util.logging.Logger;

public class GroupMetaData {

    private static final Logger LOG = Logger.getLogger(GroupMetaData.class.getName());
    private String group = "comp.lang.java.help"; //default
    private int count = 0;
    private int first = 0;
    private int last = 0;
    private boolean open = false;

    public GroupMetaData() {
    }

    public GroupMetaData(String group) {
        this.group = group;
    }

    GroupMetaData(GroupResponse groupResponse) {
        group = groupResponse.group;
        count = groupResponse.count;
        first = groupResponse.first;
        last = groupResponse.last;
    }

    GroupMetaData(GroupResponse groupResponse, boolean open) {
        group = groupResponse.group;
        count = groupResponse.count;
        first = groupResponse.first;
        last = groupResponse.last;
        this.open = open;
    }

    GroupMetaData(GroupMetaData groupMetaData, boolean open) {
        group = groupMetaData.getGroup();
        count = groupMetaData.getCount();
        first = groupMetaData.getFirst();
        last = groupMetaData.getLast();
        this.open = open; //open or close the group
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

    public String toString() {
        return "\n---gmd---\n" + group + "\t" + open + "\n";
    }
}

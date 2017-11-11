package com.personal.sajidkhan.garmentmanager;

/**
 * Created by sajid khan on 07-09-2016.
 */
public class GroupItem {

    private String itemId = "";
    private int itemCount = 0;
    private String itemOverview = "";
    private String itemDate = "";
    private int doneStatus = 0;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public String getItemDate() {
        return itemDate;
    }

    public void setItemDate(String itemDate) {
        this.itemDate = itemDate;
    }

    public String getItemOverview() {
        return itemOverview;
    }

    public void setItemOverview(String itemOverview) {
        this.itemOverview = itemOverview;
    }

    public int getDoneStatus() {
        return doneStatus;
    }

    public void setDoneStatus(int doneStatus) {
        this.doneStatus = doneStatus;
    }
}

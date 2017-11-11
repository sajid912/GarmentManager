package com.personal.sajidkhan.garmentmanager;

/**
 * Created by sajidkhan on 07/09/16.
 */
public class GarmentItem {

    private String groupId = "";
    private String garmentName = "";
    private int garmentCount = 0;
    private String date = "";

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getGarmentName() {
        return garmentName;
    }

    public void setGarmentName(String garmentName) {
        this.garmentName = garmentName;
    }

    public int getGarmentCount() {
        return garmentCount;
    }

    public void setGarmentCount(int garmentCount) {
        this.garmentCount = garmentCount;
    }
}

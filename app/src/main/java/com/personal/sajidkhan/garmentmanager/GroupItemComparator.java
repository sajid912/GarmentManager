package com.personal.sajidkhan.garmentmanager;

import java.util.Comparator;
import java.util.Date;

/**
 * Created by sajid khan on 13-09-2016.
 */
public class GroupItemComparator implements Comparator<GroupItem> {
    @Override
    public int compare(GroupItem lhs, GroupItem rhs) {

        Date lhsDate = Util.getDateFromDateStr(lhs.getItemDate());
        Date rhsDate = Util.getDateFromDateStr(rhs.getItemDate());
        return rhsDate.compareTo(lhsDate);
    }
}

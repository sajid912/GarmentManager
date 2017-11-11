package com.personal.sajidkhan.garmentmanager;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by sajid khan on 07-09-2016.
 */
public class GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.ViewHolderStub> {

    private Context mContext;
    private ArrayList<GroupItem> groupList;

    public GroupListAdapter(Context context) {
        mContext = context;
        groupList = new ArrayList<>(getGroupList());
    }

    @Override
    public ViewHolderStub onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.group_item_template, parent, false);
        ViewHolderStub viewHolderStub = new ViewHolderStub(v);

        return viewHolderStub;
    }

    @Override
    public void onBindViewHolder(ViewHolderStub holder, int position) {

        int count = groupList.get(position).getItemCount();
        StringBuffer sb = new StringBuffer();

        if (count == 1) sb.append(count).append(mContext.getString(R.string.garment));
        else sb.append(count).append(mContext.getString(R.string.garments));

        holder.groupTitle.setText(sb.toString());
        holder.groupOverview.setText(groupList.get(position).getItemOverview());
        String date = groupList.get(position).getItemDate();
        String formattedDate = Util.getDisplayDate(date);
        String formattedTime = Util.getDisplayTime(date);
        holder.groupDate.setText(formattedDate);
        holder.groupTime.setText(formattedTime);

        int doneStatus = groupList.get(position).getDoneStatus();

        if (doneStatus == 0) {
            holder.groupTitle.setTextColor(mContext.getResources().getColor(R.color.colorBlack));
            holder.groupDate.setTextColor(mContext.getResources().getColor(R.color.colorBlue));
            holder.groupTime.setTextColor(mContext.getResources().getColor(R.color.colorBlue));

            holder.groupTitle.setTypeface(null, Typeface.BOLD);
            holder.groupDate.setTypeface(null, Typeface.BOLD);
            holder.groupTime.setTypeface(null, Typeface.BOLD);
        } else if (doneStatus == 1) {
            holder.groupTitle.setTextColor(holder.grayColor);
            holder.groupDate.setTextColor(holder.grayColor);
            holder.groupTime.setTextColor(holder.grayColor);

            holder.groupTitle.setTypeface(null, Typeface.NORMAL);
            holder.groupDate.setTypeface(null, Typeface.NORMAL);
            holder.groupTime.setTypeface(null, Typeface.NORMAL);
        }
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

//    private ArrayList<GroupItem> prepareGroupList() {
//        DatabaseHelper databaseHelper = new DatabaseHelper(mContext);
//        databaseHelper.openDB();
//        ArrayList<GarmentItem> garmentList = new ArrayList<>(databaseHelper.getAllGarments());
//        databaseHelper.closeDB();
//
//        ArrayList<String> groupIdList = new ArrayList<>();
//
//        for (GarmentItem item : garmentList) {
//            if (!isGroupIdPresent(groupIdList, item.getGroupId()))
//                groupIdList.add(item.getGroupId());
//        }
//
//        ArrayList<GroupItem> groupItems = new ArrayList<>();
//
//        for (String s : groupIdList) {
//            //For each group id, get the garments and prepare Group list
//            databaseHelper.openDB();
//            ArrayList<GarmentItem> list = new ArrayList<>(databaseHelper.getGarmentsForGroupId(s));
//            databaseHelper.closeDB();
//
//            GroupItem groupItem = new GroupItem();
//            StringBuilder sb = new StringBuilder();
//
//            for (GarmentItem item : list) {
//                sb.append(item.getGarmentCount());
//
//                switch (item.getGarmentName()) {
//                    case "Shirts":
//                        sb.append("S ");
//                        break;
//                    case "T-shirts":
//                        sb.append("T ");
//                        break;
//                    case "Jeans":
//                        sb.append("J ");
//                        break;
//                    case "Tracks":
//                        sb.append("Tr ");
//                        break;
//                    case "Shorts":
//                        sb.append("Sh ");
//                        break;
//                }
//            }
//
//            groupItem.setItemOverview(sb.toString());
//            groupItem.setItemDate(list.get(0).getDate());
//            groupItems.add(groupItem);
//
//        }
//        return groupItems;
//    }
//
//    private boolean isGroupIdPresent(ArrayList<String> list, String groupId) {
//        boolean isPresent = false;
//
//        for (String s : list) {
//            if (s.equals(groupId)) {
//                isPresent = true;
//                break;
//            }
//        }
//
//        return isPresent;
//    }

    public ArrayList<GroupItem> getGroupList() {
        ArrayList<GroupItem> list;

        DatabaseHelper databaseHelper = new DatabaseHelper(mContext);
        databaseHelper.openDB();
        list = new ArrayList<>(databaseHelper.getAllGroups());
        databaseHelper.closeDB();

        GroupItemComparator groupItemComparator = new GroupItemComparator();
        Collections.sort(list, groupItemComparator);

        return list;
    }

    protected void refreshAdapter() {
        groupList.clear();
        groupList.addAll(getGroupList());
        notifyDataSetChanged();
    }

    class ViewHolderStub extends RecyclerView.ViewHolder {

        private TextView groupTitle;
        private TextView groupOverview;
        private TextView groupDate;
        private TextView groupTime;
        private ColorStateList grayColor;

        public ViewHolderStub(View itemView) {
            super(itemView);

            groupTitle = (TextView) itemView.findViewById(R.id.groupTitle);
            groupOverview = (TextView) itemView.findViewById(R.id.groupOverViewText);
            groupDate = (TextView) itemView.findViewById(R.id.groupDate);
            groupTime = (TextView) itemView.findViewById(R.id.groupTime);
            grayColor = groupTitle.getTextColors();
        }
    }
}

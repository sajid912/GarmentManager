package com.personal.sajidkhan.garmentmanager;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by sajidkhan on 07/09/16.
 */
public class GarmentListAdapter extends RecyclerView.Adapter<GarmentListAdapter.ViewHolderStub> {

    private Context mContext;
    private ArrayList<String> garmentTypeList;
    private GarmentCountCallback garmentCountCallback;
    private boolean isEdit;
    private String groupId;
    private boolean showCountingButtons = false;


    public GarmentListAdapter(Context context, boolean isEdit, String groupId, GarmentCountCallback callback) {
        mContext = context;
        garmentTypeList = new ArrayList<>(getGarmentTypesList());
        garmentCountCallback = callback;
        this.isEdit = isEdit;
        this.groupId = groupId;
    }

    @Override
    public ViewHolderStub onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.garment_item_template, parent, false);
        ViewHolderStub viewHolderStub = new ViewHolderStub(v);

        return viewHolderStub;
    }

    @Override
    public void onBindViewHolder(final ViewHolderStub holder, int position) {

        holder.garmentName.setText(garmentTypeList.get(position));

        if (isEdit) setCount(holder, showCountingButtons);

        holder.minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateCount(holder, false);
                garmentCountCallback.countChanged();
            }
        });

        holder.plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateCount(holder, true);
                garmentCountCallback.countChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return garmentTypeList.size();
    }


    private void updateCount(ViewHolderStub holder, boolean isAddition) {
        int count = Integer.parseInt(holder.garmentCount.getText().toString());

        if (isAddition) {
            if (count >= 0)
                holder.garmentCount.setText((++count) + "");
        } else {
            if (count > 0)
                holder.garmentCount.setText((--count) + "");
        }

    }

    private void setCount(ViewHolderStub holder, boolean showCountingButtons) {
        DatabaseHelper databaseHelper = new DatabaseHelper(mContext);

        String garmentName = holder.garmentName.getText().toString();
        databaseHelper.openDB();
        int count = databaseHelper.getCountForGarmentType(garmentName, groupId);
        databaseHelper.closeDB();

        holder.garmentCount.setText(count + "");

        if (showCountingButtons) {
            holder.minusButton.setVisibility(View.VISIBLE);
            holder.plusButton.setVisibility(View.VISIBLE);
        } else {
            holder.minusButton.setVisibility(View.INVISIBLE);
            holder.plusButton.setVisibility(View.INVISIBLE);
        }
    }

    private ArrayList<String> getGarmentTypesList() {
        DatabaseHelper databaseHelper = new DatabaseHelper(mContext);
        databaseHelper.openDB();
        ArrayList<String> list = new ArrayList<>(databaseHelper.getAllGarmentTypes());
        databaseHelper.closeDB();

        return list;

    }

    public void refreshAdapter() {
        showCountingButtons = true;
        garmentTypeList.clear();
        garmentTypeList.addAll(getGarmentTypesList());
        notifyDataSetChanged();
    }

    interface GarmentCountCallback {
        void countChanged();
    }

    class ViewHolderStub extends RecyclerView.ViewHolder {
        private TextView garmentName;
        private ImageView minusButton;
        private TextView garmentCount;
        private ImageView plusButton;


        public ViewHolderStub(View itemView) {
            super(itemView);

            garmentName = (TextView) itemView.findViewById(R.id.garmentName);
            minusButton = (ImageView) itemView.findViewById(R.id.minusButton);
            garmentCount = (TextView) itemView.findViewById(R.id.garmentCount);
            plusButton = (ImageView) itemView.findViewById(R.id.plusButton);
        }
    }
}

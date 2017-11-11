package com.personal.sajidkhan.garmentmanager;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private GroupListAdapter groupListAdapter;
    private Callbacks callbacks;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initializeControls(view);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof Callbacks)
            callbacks = (Callbacks) activity;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case Constants.ADD_GARMENT_REQ_CODE:

                if (resultCode == Constants.ADD_GARMENT_RES_CODE) {
                    if (groupListAdapter != null) groupListAdapter.refreshAdapter();
                }
                break;
        }
    }

    private void initializeControls(View view) {
        RecyclerView groupList = (RecyclerView) view.findViewById(R.id.collectionList);
        ItemClickSupport.addTo(groupList).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                String groupId = groupListAdapter.getGroupList().get(position).getItemId();
                callbacks.openCollectionWithGroupId(groupId);
            }
        });
        groupListAdapter = new GroupListAdapter(getActivity());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false);
        groupList.setHasFixedSize(true);
        groupList.setLayoutManager(gridLayoutManager);
        groupList.setAdapter(groupListAdapter);
    }

    protected void refreshAdapter() {
        if (groupListAdapter != null) groupListAdapter.refreshAdapter();
    }


    public interface Callbacks {
        void openCollectionWithGroupId(String s);
    }

}

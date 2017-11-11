package com.personal.sajidkhan.garmentmanager;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

public class AddGarmetActivity extends AppCompatActivity implements GarmentListAdapter.GarmentCountCallback {

    private RecyclerView garmentTypesList;
    private FloatingActionButton fab;
    private GarmentListAdapter garmentListAdapter;
    private boolean isEdit;
    private String groupId;
    private int collectionType;
    private LinearLayout addGarmentTypeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_garmet);

        Bundle bundle = getIntent().getExtras();
        isEdit = bundle.getBoolean(Constants.EDIT_GARMENT);
        groupId = bundle.getString(Constants.EDIT_GARMENT_GROUP_ID);
        if (isEdit) collectionType = Constants.VIEW_COLLECTION;
        else collectionType = Constants.ADD_COLLECTION;
        initializeControls();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_garment, menu);
        MenuItem editIcon = menu.findItem(R.id.editIcon);
        MenuItem deleteIcon = menu.findItem(R.id.deleteIcon);
        switch (collectionType) {
            case Constants.ADD_COLLECTION:
                editIcon.setVisible(false);
                deleteIcon.setVisible(false);
                fab.setImageDrawable(getDrawable(R.drawable.ic_save));
                break;
            case Constants.VIEW_COLLECTION:
                editIcon.setVisible(true);
                deleteIcon.setVisible(true);
                fab.setImageDrawable(getDrawable(R.drawable.ic_done));
                if (getDoneStatusForGroupId()) fab.hide();
                else fab.show();
                break;
            case Constants.EDIT_COLLECTION:
                editIcon.setVisible(false);
                deleteIcon.setVisible(true);
                fab.setImageDrawable(getDrawable(R.drawable.ic_save));
                fab.hide();
                break;
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.editIcon:
                garmentListAdapter.refreshAdapter();
                getSupportActionBar().setTitle(R.string.editthiscollection);
                collectionType = Constants.EDIT_COLLECTION;
                this.invalidateOptionsMenu();
                updateAddGarmentTypeVisibility(true);
                break;
            case R.id.deleteIcon:
                openDeleteDialog();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    private void initializeControls() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (isEdit)
            getSupportActionBar().setTitle(R.string.collection);
        else
            getSupportActionBar().setTitle(R.string.addGarmentActivityTitle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (collectionType == Constants.VIEW_COLLECTION) {
                    openDoneDialog();
                } else {
                    if (checkIfCollectionIsNotEmpty(garmentTypesList))
                        dummy(AddGarmetActivity.this);
                    saveGroupToDB(garmentTypesList);
                }

            }
        });

        fab.hide();

        final ScrollView addGarmentContent = (ScrollView) findViewById(R.id.addGarmentContent);
        garmentTypesList = (RecyclerView) findViewById(R.id.garmentTypesList);
        garmentListAdapter = new GarmentListAdapter(AddGarmetActivity.this, isEdit, groupId, AddGarmetActivity.this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(AddGarmetActivity.this, 1, GridLayoutManager.VERTICAL, false);
        garmentTypesList.setHasFixedSize(true);
        garmentTypesList.setLayoutManager(gridLayoutManager);
        garmentTypesList.setAdapter(garmentListAdapter);

        LinearLayout collectionDetails = (LinearLayout) findViewById(R.id.collectionDetails);
        TextView collectionTitle = (TextView) findViewById(R.id.collectionTitle);
        TextView createdDate = (TextView) findViewById(R.id.createdDate);
        TextView modifiedDate = (TextView) findViewById(R.id.modifiedDate);

//        Snackbar snackbar = Snackbar.make(addGarmentContent, "", Snackbar.LENGTH_INDEFINITE);
//        Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbar.getView();
////        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
////        inflater.inflate(R.layout.custom_snackbar, snackbarLayout);
//        snackbarLayout.setBackgroundColor(getResources().getColor(R.color.colorGreen));
//        TextView textView = (TextView) snackbarLayout.findViewById(android.support.design.R.id.snackbar_text);
//        textView.setText("Hi there");
//        snackbar.show();

        addGarmentTypeLayout = (LinearLayout) findViewById(R.id.addGarmentTypeLayout);
        final TextView addGarmentTextView = (TextView) findViewById(R.id.addGarmentTextView);
        final EditText addGarmentTypeField = (EditText) findViewById(R.id.addGarmentTypeField);
        final ImageView crossInAboveLayout = (ImageView) findViewById(R.id.crossInAddGarmentType);

        addGarmentTypeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addGarmentTypeField.setVisibility(View.VISIBLE);
                addGarmentTextView.setVisibility(View.GONE);
                crossInAboveLayout.setVisibility(View.VISIBLE);
            }
        });

        crossInAboveLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (addGarmentTypeField.getText().toString().equals("")) {

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(addGarmentTypeField.getWindowToken(), 0);

                    addGarmentTypeField.setVisibility(View.GONE);
                    addGarmentTextView.setVisibility(View.VISIBLE);
                    crossInAboveLayout.setVisibility(View.GONE);

                } else
                    addGarmentTypeField.setText("");
            }
        });

        addGarmentTypeField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(addGarmentTypeField.getWindowToken(), 0);

                    String s = addGarmentTypeField.getText().toString().trim();

                    if (s.equals(""))
                        useThisSnackBar(addGarmentContent, getString(R.string.invalidgarmentname));
                    else if (isGarmentTypePresent(s))
                        useThisSnackBar(addGarmentContent, getString(R.string.garmentnamealreadypresent));
                    else
                        addGarmentTypeToDB(s);

                    addGarmentTypeField.setText("");
                    addGarmentTypeField.setVisibility(View.GONE);
                    addGarmentTextView.setVisibility(View.VISIBLE);
                    crossInAboveLayout.setVisibility(View.GONE);

                    return true;
                }

                return false;
            }
        });

        updateAddScreenLayoutsVisibility(collectionDetails, collectionTitle, createdDate, modifiedDate);
        //dummy(AddGarmetActivity.this);
    }

    private boolean checkIfCollectionIsNotEmpty(RecyclerView list) {
        boolean notEmpty = false;
        int size = list.getAdapter().getItemCount();

        for (int i = 0; i < size; i++) {
            View holder = list.getChildAt(i);
            TextView garmentCountView = (TextView) holder.findViewById(R.id.garmentCount);

            int count = Integer.parseInt(garmentCountView.getText().toString());

            if (count != 0) {
                notEmpty = true;
                break;
            }
        }

        return notEmpty;
    }

    private void saveGroupToDB(RecyclerView list) {
        int groupCount = 0;
        int size = list.getAdapter().getItemCount();
        String localGroupId;
        if (isEdit) localGroupId = groupId;
        else localGroupId = Util.getGroupId();
        String date = Util.getCurrentDate();
        //Toast.makeText(AddGarmetActivity.this, "Time "+time, Toast.LENGTH_SHORT).show();
        StringBuffer sb = new StringBuffer();
        DatabaseHelper databaseHelper = new DatabaseHelper(AddGarmetActivity.this);

        for (int i = 0; i < size; i++) {
            View holder = list.getChildAt(i);
            TextView garmentNameView = (TextView) holder.findViewById(R.id.garmentName);
            TextView garmentCountView = (TextView) holder.findViewById(R.id.garmentCount);

            String garmentName = garmentNameView.getText().toString();
            int count = Integer.parseInt(garmentCountView.getText().toString());
            groupCount = groupCount + count;
            if (i == (size - 1)) sb.append(count + " " + garmentName);
            else sb.append(count + " " + garmentName + ", ");

            databaseHelper.openDB();
            if (isEdit)
                databaseHelper.updateOneGarment(localGroupId, garmentName, count);
            else
                databaseHelper.addOneGarment(localGroupId, garmentName, count);
            databaseHelper.closeDB();

        }

        databaseHelper.openDB();
        if (isEdit)
            databaseHelper.updateOneGroup(localGroupId, groupCount, sb.toString(), date, 0);
        else
            databaseHelper.addOneGroup(localGroupId, groupCount, sb.toString(), date);
        databaseHelper.closeDB();

        finishWithIntent();
    }

    private void openDeleteDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AddGarmetActivity.this);
        alertDialogBuilder.setTitle(getString(R.string.deleteDialogTitle));
        alertDialogBuilder.setMessage(getString(R.string.deleteDialogMsg));
        alertDialogBuilder.setPositiveButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        alertDialogBuilder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                deleteCollection();
            }
        });
        alertDialogBuilder.create().show();
    }

    private void openDoneDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AddGarmetActivity.this);
        alertDialogBuilder.setTitle(getString(R.string.donedialogTitle));
        alertDialogBuilder.setMessage(getString(R.string.doneDialogMsg));
        alertDialogBuilder.setPositiveButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        alertDialogBuilder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                markCollectionDone();
            }
        });
        alertDialogBuilder.create().show();
    }

    private void deleteCollection() {
        DatabaseHelper databaseHelper = new DatabaseHelper(AddGarmetActivity.this);

        databaseHelper.openDB();
        databaseHelper.deleteGarmentsForGroupId(groupId);
        databaseHelper.deleteGroup(groupId);
        databaseHelper.closeDB();
        finishWithIntent();
    }

    private void markCollectionDone() {
        DatabaseHelper databaseHelper = new DatabaseHelper(AddGarmetActivity.this);

        databaseHelper.openDB();
        databaseHelper.updateDoneStatusForGroupId(groupId, 1);
        databaseHelper.closeDB();
        finishWithIntent();
    }

    private boolean getDoneStatusForGroupId() {
        boolean done;
        DatabaseHelper databaseHelper = new DatabaseHelper(AddGarmetActivity.this);

        databaseHelper.openDB();
        done = databaseHelper.getDoneStatusForGroupId(groupId);
        databaseHelper.closeDB();

        return done;
    }

    private void updateAddScreenLayoutsVisibility(LinearLayout collectionDetailsLayout, TextView collectionTitle, TextView createdDate, TextView modifiedDate) {
        if (collectionType == Constants.VIEW_COLLECTION) {
            collectionDetailsLayout.setVisibility(View.VISIBLE);
            createdDate.setText(" " + Util.getDisplayDateTime(groupId));

            DatabaseHelper databaseHelper = new DatabaseHelper(AddGarmetActivity.this);
            databaseHelper.openDB();
            String dateStr = databaseHelper.getModifiedDateGroupId(groupId);
            int count = databaseHelper.getCountForGroupId(groupId);
            databaseHelper.closeDB();

            if (count == 1) collectionTitle.setText(count + " garment");
            else if (count > 1) collectionTitle.setText(count + " garments");
            modifiedDate.setText(" " + Util.getDisplayDateTime(dateStr));

            updateAddGarmentTypeVisibility(false);

        } else if (collectionType == Constants.ADD_COLLECTION)
            collectionDetailsLayout.setVisibility(View.GONE);
    }

    private void updateAddGarmentTypeVisibility(boolean show) {
        if (addGarmentTypeLayout != null && show)
            addGarmentTypeLayout.setVisibility(View.VISIBLE);
        else if (addGarmentTypeLayout != null)
            addGarmentTypeLayout.setVisibility(View.GONE);

    }


    private boolean isGarmentTypePresent(String newType) {
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        databaseHelper.openDB();
        ArrayList<String> list = new ArrayList(databaseHelper.getAllGarmentTypes());
        databaseHelper.closeDB();

        boolean isPresent = false;

        for (String s : list) {
            if (s.toLowerCase().equals(newType.toLowerCase())) isPresent = true;
        }
        return isPresent;
    }

    private void addGarmentTypeToDB(String newType) {

        String s = newType.substring(0, 1).toUpperCase() + newType.substring(1);
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        databaseHelper.openDB();
        databaseHelper.addOneGarmentType(s);
        databaseHelper.closeDB();

        garmentListAdapter.refreshAdapter();
    }

    private void useThisSnackBar(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    private void finishWithIntent() {
        Intent intent = new Intent();
        setResult(Constants.ADD_GARMENT_RES_CODE, intent);
        finish();
    }

    @Override
    public void countChanged() {

        if (checkIfCollectionIsNotEmpty(garmentTypesList)) fab.show();
        else fab.hide();
    }


    private void dummy(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 38);
        calendar.set(Calendar.SECOND, 0);

        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }


}

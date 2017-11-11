package com.personal.sajidkhan.garmentmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by sajidkhan on 07/09/16.
 */
public class DatabaseHelper {

    public static final int VER = 1;
    private static final String GARMENT_DB = "garmentDb.db"; // DB name

    private static final String GARMENT_MASTER_TABLE = "garmentMasterTable"; // Table name
    private static final String GARMENT_GROUPS_TABLE = "garmentGroupsTable";
    private static final String GARMENT_TYPES_TABLE = "garmentTypesTable";

    private static final String GARMENT_GROUP_ID = "garmentGroupId";
    private static final String GARMENT_NAME = "garmentName"; // Table keys
    private static final String GARMENT_COUNT = "garmentCount";

    private static final String GARMENTS_GROUP_COUNT = "garmentsGroupCount";
    private static final String GARMENTS_OVERVIEW = "garmentsOverview";
    private static final String GARMENT_GROUP_DATE = "garmentGroupDate";
    private static final String GARMENT_GROUP_DONE = "garmentGroupDone";

    private static final String GARMENT_TYPE_NAME = "garmentTypeName";

    private static final String CREATE_GARMENT_TABLE = "CREATE TABLE " + GARMENT_MASTER_TABLE + " (" + GARMENT_GROUP_ID + " TEXT , " + GARMENT_NAME + " TEXT , " + GARMENT_COUNT + " INTEGER)";
    private static final String DROP_GARMENT_TABLE = "DROP TABLE IF EXISTS " + GARMENT_MASTER_TABLE;
    private static final String[] GARMENT_ALL_FIELDS = new String[]{GARMENT_GROUP_ID, GARMENT_NAME, GARMENT_COUNT};

    private static final String CREATE_GARMENT_GROUPS_TABLE = "CREATE TABLE " + GARMENT_GROUPS_TABLE + " (" + GARMENT_GROUP_ID + " TEXT , "
            + GARMENTS_GROUP_COUNT + " INTEGER , " + GARMENTS_OVERVIEW + " TEXT , " + GARMENT_GROUP_DATE + " TEXT , " + GARMENT_GROUP_DONE + " INTEGER)";
    private static final String DROP_GARMENT_GROUPS_TABLE = "DROP TABLE IF EXISTS " + GARMENT_GROUPS_TABLE;
    private static final String[] GARMENT_GROUPS_ALL_FIELDS = new String[]{GARMENT_GROUP_ID, GARMENTS_GROUP_COUNT, GARMENTS_OVERVIEW, GARMENT_GROUP_DATE, GARMENT_GROUP_DONE};

    private static final String CREATE_GARMENT_TYPES_TABLE = "CREATE TABLE " + GARMENT_TYPES_TABLE + " (" + GARMENT_TYPE_NAME + " TEXT)";
    private static final String DROP_GARMENT_TYPES_TABLE = "DROP TABLE IF EXISTS " + GARMENT_TYPES_TABLE;
    private static final String[] GARMENT_TYPES_ALL_FIELDS = new String[]{GARMENT_TYPE_NAME};

    private SQLiteDatabase db;
    private MySQLiteOpenHelper mySQLiteOpenHelper;

    public DatabaseHelper(Context context) {
        mySQLiteOpenHelper = new MySQLiteOpenHelper(context);
    }

    protected void openDB() throws SQLiteException {
        db = mySQLiteOpenHelper.getWritableDatabase();
    }

    protected void closeDB() {
        db.close();
    }

    protected void clearGarmentDB() {
        db.execSQL("delete from " + GARMENT_MASTER_TABLE);
    }

    protected void clearGarmentTypesDB() {
        db.execSQL("delete from " + GARMENT_TYPES_TABLE);
    }


    protected long addOneGarmentType(String s) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(GARMENT_TYPE_NAME, s);
        return db.insert(GARMENT_TYPES_TABLE, null, contentValues);
    }

    protected ArrayList<String> getAllGarmentTypes() {
        ArrayList<String> list = new ArrayList<>();
        Cursor cursor = db.query(GARMENT_TYPES_TABLE, GARMENT_TYPES_ALL_FIELDS, null, null, null, null, null);
        cursor.moveToFirst();

        for (int i = 0; i < cursor.getCount(); i++) {
            list.add(cursor.getString(cursor.getColumnIndex(GARMENT_TYPE_NAME)));
            cursor.moveToNext();
        }
        cursor.close();

        return list;
    }

    protected boolean areGarmentTypesPresent() {
        Cursor cursor = db.query(GARMENT_TYPES_TABLE, GARMENT_TYPES_ALL_FIELDS, null, null, null, null, null);
        cursor.moveToFirst();
        boolean present = (cursor.getCount() > 0);
        cursor.close();

        return present;
    }

    protected long addOneGarment(String groupId, String garmentName, int count) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(GARMENT_GROUP_ID, groupId);
        contentValues.put(GARMENT_NAME, garmentName);
        contentValues.put(GARMENT_COUNT, count);

        return db.insert(GARMENT_MASTER_TABLE, null, contentValues);
    }

    protected long updateOneGarment(String groupId, String garmentName, int count) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(GARMENT_COUNT, count);

        return db.update(GARMENT_MASTER_TABLE, contentValues, GARMENT_GROUP_ID + " = ? AND " + GARMENT_NAME + " = ? ", new String[]{groupId, garmentName});
    }

    protected long deleteGarmentsForGroupId(String groupId) {
        return db.delete(GARMENT_MASTER_TABLE, GARMENT_GROUP_ID + " = ? ", new String[]{groupId});
    }


    protected ArrayList<GarmentItem> getAllGarments() {
        ArrayList<GarmentItem> list = new ArrayList<>();
        Cursor cursor = db.query(GARMENT_MASTER_TABLE, GARMENT_ALL_FIELDS, null, null, null, null, null);
        cursor.moveToFirst();

        for (int i = 0; i < cursor.getCount(); i++) {
            GarmentItem item = new GarmentItem();
            item.setGroupId(cursor.getString(cursor.getColumnIndex(GARMENT_GROUP_ID)));
            item.setGarmentName(cursor.getString(cursor.getColumnIndex(GARMENT_NAME)));
            item.setGarmentCount(cursor.getInt(cursor.getColumnIndex(GARMENT_COUNT)));
            list.add(item);
            cursor.moveToNext();
        }
        cursor.close();

        return list;
    }

    protected ArrayList<GarmentItem> getGarmentsForGroupId(String groupId) {
        ArrayList<GarmentItem> list = new ArrayList<>();
        Cursor cursor = db.query(GARMENT_MASTER_TABLE, GARMENT_ALL_FIELDS, GARMENT_GROUP_ID + " = ? ", new String[]{groupId}, null, null, null);
        cursor.moveToFirst();

        for (int i = 0; i < cursor.getCount(); i++) {
            GarmentItem item = new GarmentItem();
            item.setGroupId(cursor.getString(cursor.getColumnIndex(GARMENT_GROUP_ID)));
            item.setGarmentName(cursor.getString(cursor.getColumnIndex(GARMENT_NAME)));
            item.setGarmentCount(cursor.getInt(cursor.getColumnIndex(GARMENT_COUNT)));
            list.add(item);
            cursor.moveToNext();
        }
        cursor.close();

        return list;
    }

    protected int getCountForGarmentType(String garmentName, String groupId) {
        Cursor cursor = db.query(GARMENT_MASTER_TABLE, GARMENT_ALL_FIELDS, GARMENT_GROUP_ID + " = ? AND " + GARMENT_NAME + " = ? ", new String[]{groupId, garmentName}, null, null, null);
        cursor.moveToFirst();

        int count = 0;
        if (cursor.getCount() == 1)
            count = cursor.getInt(cursor.getColumnIndex(GARMENT_COUNT));

        cursor.close();
        return count;

    }

    protected long addOneGroup(String groupId, int groupCount, String overViewText, String date) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(GARMENT_GROUP_ID, groupId);
        contentValues.put(GARMENTS_GROUP_COUNT, groupCount);
        contentValues.put(GARMENTS_OVERVIEW, overViewText);
        contentValues.put(GARMENT_GROUP_DATE, date);
        contentValues.put(GARMENT_GROUP_DONE, 0);

        return db.insert(GARMENT_GROUPS_TABLE, null, contentValues);
    }

    protected long updateOneGroup(String groupId, int groupCount, String overViewText, String date, int done) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(GARMENTS_GROUP_COUNT, groupCount);
        contentValues.put(GARMENTS_OVERVIEW, overViewText);
        contentValues.put(GARMENT_GROUP_DATE, date);
        contentValues.put(GARMENT_GROUP_DONE, done);

        return db.update(GARMENT_GROUPS_TABLE, contentValues, GARMENT_GROUP_ID + " = ? ", new String[]{groupId});
    }

    protected long updateDoneStatusForGroupId(String groupId, int done) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(GARMENT_GROUP_DONE, done);

        return db.update(GARMENT_GROUPS_TABLE, contentValues, GARMENT_GROUP_ID + " = ? ", new String[]{groupId});
    }

    protected boolean getDoneStatusForGroupId(String groupId) {
        Cursor cursor = db.query(GARMENT_GROUPS_TABLE, GARMENT_GROUPS_ALL_FIELDS, GARMENT_GROUP_ID + " = ? ", new String[]{groupId}, null, null, null);
        cursor.moveToFirst();

        int status = 0;
        if (cursor.getCount() == 1)
            status = cursor.getInt(cursor.getColumnIndex(GARMENT_GROUP_DONE));

        cursor.close();
        boolean doneStatus = false;
        if (status == 1) doneStatus = true;

        return doneStatus;
    }

    protected long deleteGroup(String groupId) {
        return db.delete(GARMENT_GROUPS_TABLE, GARMENT_GROUP_ID + " = ? ", new String[]{groupId});
    }

    protected String getModifiedDateGroupId(String groupId) {
        Cursor cursor = db.query(GARMENT_GROUPS_TABLE, GARMENT_GROUPS_ALL_FIELDS, GARMENT_GROUP_ID + " = ? ", new String[]{groupId}, null, null, null);
        cursor.moveToFirst();

        String dateStr = "";
        if (cursor.getCount() == 1)
            dateStr = cursor.getString(cursor.getColumnIndex(GARMENT_GROUP_DATE));

        cursor.close();

        return dateStr;
    }

    protected int getCountForGroupId(String groupId) {
        Cursor cursor = db.query(GARMENT_GROUPS_TABLE, GARMENT_GROUPS_ALL_FIELDS, GARMENT_GROUP_ID + " = ? ", new String[]{groupId}, null, null, null);
        cursor.moveToFirst();

        int count = 0;
        if (cursor.getCount() == 1)
            count = cursor.getInt(cursor.getColumnIndex(GARMENTS_GROUP_COUNT));

        cursor.close();

        return count;
    }


    protected ArrayList<GroupItem> getAllGroups() {
        ArrayList<GroupItem> list = new ArrayList<>();
        Cursor cursor = db.query(GARMENT_GROUPS_TABLE, GARMENT_GROUPS_ALL_FIELDS, null, null, null, null, null);
        cursor.moveToFirst();

        for (int i = 0; i < cursor.getCount(); i++) {
            GroupItem item = new GroupItem();
            item.setItemId(cursor.getString(cursor.getColumnIndex(GARMENT_GROUP_ID)));
            item.setItemCount(cursor.getInt(cursor.getColumnIndex(GARMENTS_GROUP_COUNT)));
            item.setItemOverview(cursor.getString(cursor.getColumnIndex(GARMENTS_OVERVIEW)));
            item.setItemDate(cursor.getString(cursor.getColumnIndex(GARMENT_GROUP_DATE)));
            item.setDoneStatus(cursor.getInt(cursor.getColumnIndex(GARMENT_GROUP_DONE)));
            list.add(item);
            cursor.moveToNext();
        }
        cursor.close();

        return list;

    }

    class MySQLiteOpenHelper extends SQLiteOpenHelper {

        public MySQLiteOpenHelper(Context context) {
            super(context, GARMENT_DB, null, VER);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(CREATE_GARMENT_TABLE);
            db.execSQL(CREATE_GARMENT_GROUPS_TABLE);
            db.execSQL(CREATE_GARMENT_TYPES_TABLE);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            if (newVersion > oldVersion) {
                db.execSQL(DROP_GARMENT_TABLE);
                db.execSQL(DROP_GARMENT_GROUPS_TABLE);
                db.execSQL(DROP_GARMENT_TYPES_TABLE);
                onCreate(db);
            }
        }
    }
}

package com.personal.sajidkhan.garmentmanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by sajid khan on 11-03-2017.
 */
public class AppMemory {

    public static final String DAY = "foodType";
    Context mContext;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;

    public AppMemory(Context context) {
        mContext = context;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        editor = sharedPreferences.edit();
    }


}

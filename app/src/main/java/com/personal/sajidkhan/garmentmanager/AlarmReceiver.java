package com.personal.sajidkhan.garmentmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by sajidkhan on 22/09/16.
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, "Hi there !!", Toast.LENGTH_SHORT).show();
        Util.showNotificationInPanel("Hi there!!", context);
    }
}

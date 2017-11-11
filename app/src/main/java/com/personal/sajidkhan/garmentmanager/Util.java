package com.personal.sajidkhan.garmentmanager;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by sajidkhan on 07/09/16.
 */
public class Util {

    protected static String getCurrentDate() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);

    }

    protected static String getDisplayDate(String dateStr) {

        Date date = getDateFromDateStr(dateStr);

        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd");
        return sdf.format(date);

    }

    protected static String getGroupId() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    protected static String getDisplayTime(String dateStr) {

        Date date = getDateFromDateStr(dateStr);
        SimpleDateFormat sdf = new SimpleDateFormat("K:mm a", Locale.ENGLISH);
        return sdf.format(date);
    }

    protected static String getDisplayDateTime(String dateStr) {
        Date date = getDateFromDateStr(dateStr);

        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd K:mm a");
        return sdf.format(date);
    }

    protected static Date getDateFromDateStr(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            Log.v(Constants.LOG_TAG, "Parse Exception occurred");
        }
        return date;
    }

    protected static void showNotificationInPanel(String message, Context context) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentTitle(context.getResources().getString(R.string.app_name))
                .setContentText(message)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_icon));

        Intent intent = new Intent(context, HomeActivity.class);
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
        taskStackBuilder.addParentStack(HomeActivity.class);
        taskStackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(Constants.NOTIFICATION_ID, builder.build());

    }

}

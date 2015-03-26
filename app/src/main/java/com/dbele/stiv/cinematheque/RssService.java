package com.dbele.stiv.cinematheque;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.dbele.stiv.rss.RssParser;


public class RssService extends IntentService {

    private static final int SERVICE_INTERVAL = 1000 * 60 * 60 * 12;

    public RssService() {
        super("RssService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.v("Service", "parsing rss");
        RssParser.upadateDatabaseFromRssFeed(this);

    }



    public static void setRepeatingService(Context context) {
        Intent intent = new Intent(context, RssService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis(), SERVICE_INTERVAL, pendingIntent);
    }


}

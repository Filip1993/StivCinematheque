package com.dbele.stiv.cinematheque;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import com.dbele.stiv.rss.RssParser;
import com.dbele.stiv.utitlities.ActivityHandler;
import com.dbele.stiv.utitlities.ConnectivityHandler;

import java.util.Calendar;


public class RssService extends IntentService {

    private static final int SERVICE_INTERVAL = 1000 * 60 * 60 * 12;

    public RssService() {
        super("RssService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.v("Service", "parsing rss");

        boolean isInForeGround = ActivityHandler.applicationIsInForeground(getApplicationContext());

        if (ConnectivityHandler.deviceIsConnected(getApplicationContext()) && !isInForeGround){
            RssParser.upadateDatabaseFromRssFeed(this);
        }
    }

    public static void setRepeatingService(Context context) {
        Intent intent = new Intent(context, RssService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH)+1);
        calendar.set(Calendar.HOUR_OF_DAY, 2);
        calendar.set(Calendar.MINUTE, 30);
        alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), SERVICE_INTERVAL, pendingIntent);
        //alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis(), 1000*60, pendingIntent);
    }


}

package com.dbele.stiv.cinematheque;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import com.dbele.stiv.rss.RssParser;
import com.dbele.stiv.utitlities.ActivityHandler;
import com.dbele.stiv.utitlities.AlarmHandler;
import com.dbele.stiv.utitlities.ConnectivityHandler;
import com.dbele.stiv.utitlities.PreferencesHandler;
import java.util.Calendar;


public class RssService extends IntentService {

    private static final int SERVICE_INTERVAL = 1000 * 60 * 60 * 12;

    public RssService() {
        super("RssService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (ActivityHandler.applicationIsInForeground(getApplicationContext())) {
            return;
        }
        if (PreferencesHandler.shouldPullUpdatesOnlyOnWIFI(getApplicationContext())
                && !ConnectivityHandler.isWifiConnection(getApplicationContext())) {
            return;
        }
        if (ConnectivityHandler.deviceIsConnected(getApplicationContext())){
            RssParser.upadateDatabaseFromRssFeed(this);
        }
    }

    public static void setRepeatingService(Context context) {
        Intent intent = new Intent(context, RssService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH)+1);
        calendar.set(Calendar.HOUR_OF_DAY, 2);
        calendar.set(Calendar.MINUTE, 30);
        AlarmHandler.setRepeatingService(context, calendar, SERVICE_INTERVAL, pendingIntent);
    }
}

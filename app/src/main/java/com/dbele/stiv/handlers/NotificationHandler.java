package com.dbele.stiv.handlers;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import com.dbele.stiv.cinematheque.HostActivity;
import com.dbele.stiv.cinematheque.R;

public class NotificationHandler {

    public static void sendMoviesInsertedNotification(Context context) {
        Resources r = context.getResources();
        PendingIntent pi = PendingIntent.getActivity(context, 0, new Intent(context, HostActivity.class), 0);
        Notification notification = new NotificationCompat.Builder(context)
                .setTicker(r.getString(R.string.rss_parsed_ticker))
                .setSmallIcon(R.drawable.notification)
                .setContentTitle(r.getString(R.string.rss_parsed_content_title))
                .setContentText(r.getString(R.string.rss_parsed_content_text))
                .setContentIntent(pi)
                .setAutoCancel(true)
                .setSound(Uri.parse("android.resource://" + context.getPackageName() + "/" +  R.raw.notification))
                .build();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Activity.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }
}

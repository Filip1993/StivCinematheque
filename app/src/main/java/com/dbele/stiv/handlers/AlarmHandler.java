package com.dbele.stiv.handlers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import java.util.Calendar;

public class AlarmHandler {

    public static void setRepeatingService(Context context, Calendar calendar, int serviceInterval, PendingIntent pendingIntent) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), serviceInterval, pendingIntent);
    }
}

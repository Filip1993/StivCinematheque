package com.dbele.stiv.cinematheque;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {
    public BootReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        RssService.setRepeatingService(context);
        Log.v("BroadcastReceiver", "boot completed");
    }
}

package com.dbele.stiv.utitlities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectivityHandler {

    public static boolean deviceIsConnected(Context context) {
        NetworkInfo activeNetwork = getConnectivityManager(context).getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public static boolean isWifiConnection(Context context) {
        NetworkInfo networkInfo = getConnectivityManager(context).getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return networkInfo.isConnected();
    }

    private static ConnectivityManager getConnectivityManager(Context context) {
        return (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }
}

package com.dbele.stiv.handlers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.dbele.stiv.cinematheque.R;


public class PreferencesHandler {

    private static final String SYSTEM_PREFERENCES = "SYSTEM_PREFERENCES";
    private static final String DB_LOADED = "DB_LOADED";
    private static final String CONTINUE_PLAYING = "CONTINUE_PLAYING";

    public static boolean checkIfDbLoaded(Context context) {
        SharedPreferences sharedPreferences =  context.getSharedPreferences(SYSTEM_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(DB_LOADED, false);
    }

    public static void setDbLoaded(Context context) {
        SharedPreferences sharedPreferences =  context.getSharedPreferences(SYSTEM_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(DB_LOADED, true);
        editor.apply();
    }

    public static boolean shouldPlayBackgroundMusic(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(context.getResources().getString(R.string.background_music_key), false);
    }

    public static boolean shouldPullUpdatesOnlyOnWIFI(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(context.getResources().getString(R.string.only_on_wifi_key), false);
    }

    public static boolean checkIfToContinuePlaying(Context context) {
        return getPrivateSharedPreferences(context).getBoolean(CONTINUE_PLAYING, false);
    }

    public static void setContinuePlaying(Context context, boolean continuePlaying) {
        SharedPreferences.Editor editor = getPrivateSharedPreferences(context).edit();
        editor.putBoolean(CONTINUE_PLAYING, continuePlaying);
        editor.apply();
    }

    private static SharedPreferences getPrivateSharedPreferences(Context context) {
        return context.getSharedPreferences(SYSTEM_PREFERENCES, Context.MODE_PRIVATE);
    }
}

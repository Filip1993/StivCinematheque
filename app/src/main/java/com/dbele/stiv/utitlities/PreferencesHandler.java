package com.dbele.stiv.utitlities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.dbele.stiv.cinematheque.R;


/**
 * Created by dbele on 3/28/2015.
 */
public class PreferencesHandler {

    private static final String SYSTEM_PREFERENCES = "SYSTEM_PREFERENCES";
    private static final String DB_LOADED = "DB_LOADED";

    public static boolean checkIfDbLoaded(Context context) {
        SharedPreferences sharedPreferences =  context.getSharedPreferences(SYSTEM_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(DB_LOADED, false);
    }

    public static void setDbLoaded(Context context) {
        SharedPreferences sharedPreferences =  context.getSharedPreferences(SYSTEM_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(DB_LOADED, true);
        editor.commit();
    }

    public static boolean shouldPlayBackgroundMusic(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(context.getResources().getString(R.string.background_music_key), false);
    }

    public static boolean shouldPullUpdatesOnlyOnWIFI(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(context.getResources().getString(R.string.only_on_wifi_key), false);
    }
}

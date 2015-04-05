package com.dbele.stiv.utitlities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.dbele.stiv.cinematheque.BackgroundMusicService;
import com.dbele.stiv.cinematheque.R;

/**
 * Created by dbele on 4/5/2015.
 */
public class BackgroundMusicHandler {

    private static boolean shouldPlay = false;

    public static void setShouldPlay(boolean shouldPlay) {
        BackgroundMusicHandler.shouldPlay = shouldPlay;
    }

    public static boolean getShouldPlay() {
        return shouldPlay;
    }

    public static void handleMusicPlay(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean playMusic = preferences.getBoolean(context.getResources().getString(R.string.background_music_key), false);

        if (playMusic) {
            startBackgroundMusicService(context);
        } else {
            stopBackgroundService();
        }

    }

    private static void stopBackgroundService() {
        shouldPlay = false;
    }

    private static void startBackgroundMusicService(Context context) {
        if (!shouldPlay) {
            Intent intent = new Intent(context, BackgroundMusicService.class);
            context.startService(intent);
        }
    }
}
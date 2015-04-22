package com.dbele.stiv.utitlities;

import android.content.Context;
import android.content.Intent;
import com.dbele.stiv.cinematheque.BackgroundMusicService;

public class BackgroundMusicHandler {

    private static boolean shouldPlay = false;

    public static void handleMusicPlay(Context context) {
        if (PreferencesHandler.shouldPlayBackgroundMusic(context)) {
            startBackgroundMusicService(context);
        } else {
            stopBackgroundService();
        }
    }

    public static void setShouldPlay(boolean shouldPlay) {
        BackgroundMusicHandler.shouldPlay = shouldPlay;
    }

    public static boolean getShouldPlay() {
        return shouldPlay;
    }

    private static void startBackgroundMusicService(Context context) {
        if (!shouldPlay) {
            Intent intent = new Intent(context, BackgroundMusicService.class);
            context.startService(intent);
        }
    }

    private static void stopBackgroundService() {
        shouldPlay = false;
    }
}
package com.dbele.stiv.cinematheque;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import com.dbele.stiv.utitlities.ActivityHandler;
import com.dbele.stiv.utitlities.BackgroundMusicHandler;
import com.dbele.stiv.utitlities.PreferencesHandler;

public class BackgroundMusicService extends Service {

    private MediaPlayer player;

    public IBinder onBind(Intent arg0) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        createPlayer();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        if (player!=null && !player.isPlaying()) {
            player.start();
            BackgroundMusicHandler.setShouldPlay(true);
            startListeningThread();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        stopPlayer();
    }

    private void createPlayer() {
        player = MediaPlayer.create(this, R.raw.mex);
        player.setLooping(true); // Set looping
        player.setVolume(100, 100);
    }

    private void stopPlayer() {
        if (player!=null && player.isPlaying()) {
            player.stop();
            player.release();
        }
        BackgroundMusicHandler.setShouldPlay(false);
    }

    private void startListeningThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean isInForeground = ActivityHandler.applicationIsInForeground(getApplicationContext());
                boolean shouldPlay = BackgroundMusicHandler.getShouldPlay();
                boolean continuePlaying = PreferencesHandler.checkIfToContinuePlaying(getApplicationContext());
                while((isInForeground && shouldPlay) || continuePlaying) {
                    try {
                        Thread.sleep(210);
                    } catch (InterruptedException e) {
                        Log.e(getClass().getName(), "Exception", e);
                    }
                    isInForeground = ActivityHandler.applicationIsInForeground(getApplicationContext());
                    if (!isInForeground) {
                        try {
                            Thread.sleep(60);
                        } catch (InterruptedException e) {
                            Log.e(getClass().getName(), "Exception", e);
                        }
                        isInForeground = ActivityHandler.applicationIsInForeground(getApplicationContext());
                        if (!isInForeground) {
                            try {
                                Thread.sleep(30);
                            } catch (InterruptedException e) {
                                Log.e(getClass().getName(), "Exception", e);

                            }
                            isInForeground = ActivityHandler.applicationIsInForeground(getApplicationContext());
                        }
                    }
                    shouldPlay = BackgroundMusicHandler.getShouldPlay();
                    continuePlaying = PreferencesHandler.checkIfToContinuePlaying(getApplicationContext());
                }
                stopSelf();
            }
        }).start();
    }
}

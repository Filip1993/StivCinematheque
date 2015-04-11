package com.dbele.stiv.cinematheque;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import com.dbele.stiv.utitlities.ActivityHandler;
import com.dbele.stiv.utitlities.BackgroundMusicHandler;

public class BackgroundMusicService extends Service {

    MediaPlayer player;
    public IBinder onBind(Intent arg0) {

        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        startPlayer();

    }

    private void startPlayer() {
        player = MediaPlayer.create(this, R.raw.mex);
        player.setLooping(true); // Set looping
        player.setVolume(100, 100);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        if (player!=null && !player.isPlaying()) {
            player.start();
            BackgroundMusicHandler.setShouldPlay(true);
            startListeningThread();
        }
        return 1;
    }

    private void startListeningThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //Log.e("Bacground service", "running");
                boolean isInForeground = ActivityHandler.applicationIsInForeground(getApplicationContext());
                boolean shouldPlay = BackgroundMusicHandler.getShouldPlay();
                while( isInForeground && shouldPlay) {
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        Log.e(getClass().getName(), "Exception", e);

                    }
                    isInForeground = ActivityHandler.applicationIsInForeground(getApplicationContext());
                    shouldPlay = BackgroundMusicHandler.getShouldPlay();
                    //Log.e("isInForeground", isInForeground+"");
                    //Log.e("isPlaying", shouldPlay+"");
                }
                //Log.e("Bacground service ", "stopping");
                stopSelf();
            }
        }).start();
    }


    @Override
    public void onDestroy() {
        stopPlayer();
    }

    private void stopPlayer() {
        if (player!=null && player.isPlaying())
        player.stop();
        player.release();
        BackgroundMusicHandler.setShouldPlay(false);
    }

}

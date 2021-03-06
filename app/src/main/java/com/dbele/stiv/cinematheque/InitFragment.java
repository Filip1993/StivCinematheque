package com.dbele.stiv.cinematheque;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.dbele.stiv.utilities.RSSParser;
import com.dbele.stiv.handlers.AnimationHandler;
import com.dbele.stiv.handlers.ConnectivityHandler;
import com.dbele.stiv.handlers.PreferencesHandler;


public class InitFragment extends Fragment {

    private View view;
    private MediaPlayer mediaPlayer;
    private boolean interrupted = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_init, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!PreferencesHandler.checkIfDbLoaded(getActivity())) {
            if (!ConnectivityHandler.deviceIsConnected(getActivity())){
                Toast.makeText(getActivity(), R.string.unable_to_load_data_on_init, Toast.LENGTH_LONG).show();
                getActivity().finish();
                return;
            }
            startAnimation();
            playIntroMusic();
            new RssParserAsynchTask().execute();
        } else {
            startHostActivity();
        }
        RssService.setRepeatingService(getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mediaPlayer!=null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        interrupted = true;
        getActivity().finish();
    }

    private void playIntroMusic() {
        mediaPlayer = MediaPlayer.create(getActivity(), R.raw.looper);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    private void startAnimation() {
        final ImageView myImage = (ImageView) view.findViewById(R.id.animatedImage);
        AnimationHandler.startRotatingAnimation(getActivity(), myImage);
    }

    private void startHostActivity() {
        Intent intent = new Intent(getActivity(), HostActivity.class);
        startActivity(intent);
    }

    private class RssParserAsynchTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            RSSParser.upadateDatabaseFromRssFeed(getActivity());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (!interrupted) {
                startHostActivity();
            }
        }
    }
}

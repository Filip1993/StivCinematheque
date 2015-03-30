package com.dbele.stiv.cinematheque;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.dbele.stiv.rss.RssParser;
import com.dbele.stiv.utitlities.ConnectivityHandler;
import com.dbele.stiv.utitlities.PreferencesHandler;


public class InitFragment extends Fragment {

    private View view;
    private MediaPlayer mediaPlayer;


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
            RssService.setRepeatingService(getActivity());
        } else {
            startMovieListActivity();
        }
    }

    private void playIntroMusic() {
        mediaPlayer = MediaPlayer.create(getActivity(), R.raw.intro_looper);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    private void startAnimation() {
        final ImageView myImage = (ImageView) view.findViewById(R.id.animatedImage);
        final Animation myRotation = AnimationUtils.loadAnimation(getActivity(), R.anim.image_rotation);
        myImage.startAnimation(myRotation);
    }

    private void startMovieListActivity() {
        Intent intent = new Intent(getActivity(), MovieListActivity.class);
        startActivity(intent);
        cleanAndFinish();
    }

    private void cleanAndFinish() {
        if (mediaPlayer!=null) {
            mediaPlayer.stop();
        }
        getActivity().finish();
    }

    private class RssParserAsynchTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            RssParser.upadateDatabaseFromRssFeed(getActivity());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            startMovieListActivity();
        }
    }


}

package com.dbele.stiv.cinematheque;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dbele.stiv.rss.RssParser;
import com.dbele.stiv.utitlities.PreferencesHandler;


public class InitFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_init, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!PreferencesHandler.checkIfDbLoaded(getActivity())) {
            new RssParserAsynchTask().execute();
            RssService.setRepeatingService(getActivity());
        } else {
            startMovieListActivity();
        }
    }

    private void startMovieListActivity() {
        Intent intent = new Intent(getActivity(), MovieListActivity.class);
        startActivity(intent);
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

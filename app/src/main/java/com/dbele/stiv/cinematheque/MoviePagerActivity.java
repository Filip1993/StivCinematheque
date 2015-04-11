package com.dbele.stiv.cinematheque;

import android.app.ActionBar;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;


import com.dbele.stiv.model.Movie;
import com.dbele.stiv.persistence.MovieDatabaseHelper;
import com.dbele.stiv.persistence.MoviesContentProvider;
import com.dbele.stiv.utitlities.BackgroundMusicHandler;

import java.util.Date;

public class MoviePagerActivity extends FragmentActivity {

    private ViewPager movieViewPager;
    private Cursor cursor;

    private int moviePosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(Boolean.TRUE);
        }

        movieViewPager = new ViewPager(this);
        movieViewPager.setId(R.id.movieViewPager);
        setContentView(movieViewPager);

        if (getIntent().getIntExtra(MovieFragment.EXTRA_MOVIE_POSITION, -1) != -1) {
            moviePosition = getIntent().getIntExtra(MovieFragment.EXTRA_MOVIE_POSITION, -1);
        }

        cursor = this.getContentResolver().
                query(MoviesContentProvider.CONTENT_URI, MoviesContentProvider.MOVIE_PROJECTION,
                        null, null, MovieDatabaseHelper.COLUMN_NAME);

        handleViewPagerAdapter();
        setCurrentItem();
    }

    private void handleViewPagerAdapter() {
        FragmentManager fm = getSupportFragmentManager();
        movieViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public int getCount() {
                return cursor.getCount();
            }

            @Override
            public Fragment getItem(int pos) {
                Movie movie = createMovieFromCursor(pos);
                return MovieFragment.createMovieFragment(movie);
            }
        });

        movieViewPager.setOnPageChangeListener(onPageChangeListener);
        onPageChangeListener.onPageSelected(0);
    }

    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        public void onPageScrollStateChanged(int state) {
        }
        public void onPageScrolled(int pos, float posOffset, int posOffsetPixels) {
        }
        public void onPageSelected(int pos) {
            Movie movie = createMovieFromCursor(pos);
            if (movie.getName() != null) {
                setTitle(movie.getName());
            }
        }
    };

    private void setCurrentItem() {
        movieViewPager.setCurrentItem(moviePosition);
    }

    private Movie createMovieFromCursor(int pos) {
        cursor.moveToPosition(pos);
        Movie movie = new Movie();
        movie.setIdMovie(cursor.getLong(cursor.getColumnIndexOrThrow(MovieDatabaseHelper.COLUMN_ID)));
        movie.setName(cursor.getString(cursor.getColumnIndexOrThrow(MovieDatabaseHelper.COLUMN_NAME)));
        movie.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(MovieDatabaseHelper.COLUMN_DESCRIPTION)));
        movie.setDirector(cursor.getString(cursor.getColumnIndexOrThrow(MovieDatabaseHelper.COLUMN_DIRECTOR)));
        movie.setActors(cursor.getString(cursor.getColumnIndexOrThrow(MovieDatabaseHelper.COLUMN_ACTORS)));
        movie.setLength(cursor.getInt(cursor.getColumnIndexOrThrow(MovieDatabaseHelper.COLUMN_LENGTH)));
        movie.setGenre(cursor.getString(cursor.getColumnIndexOrThrow(MovieDatabaseHelper.COLUMN_GENRE)));
        movie.setPicturePath(cursor.getString(cursor.getColumnIndexOrThrow(MovieDatabaseHelper.COLUMN_PICTURE_PATH)));
        movie.setTicketPath(cursor.getString(cursor.getColumnIndexOrThrow(MovieDatabaseHelper.COLUMN_TICKET_PATH)));
        long watchedDate = cursor.getLong(cursor.getColumnIndexOrThrow(MovieDatabaseHelper.COLUMN_WATCHED_DATE));
        if (watchedDate > 0) {
            movie.setWatchedDate(new Date(watchedDate));
        }
        return movie;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        BackgroundMusicHandler.handleMusicPlay(this);
    }
}

package com.dbele.stiv.cinematheque;

import android.app.ActionBar;
import android.content.res.Resources;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.widget.ImageView;

import com.dbele.stiv.model.Movie;
import com.dbele.stiv.persistence.MovieDatabaseHelper;
import com.dbele.stiv.persistence.MoviesContentProvider;
import com.dbele.stiv.handlers.BackgroundMusicHandler;

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
            int upId = Resources.getSystem().getIdentifier("up", "id", "android");
            if (upId > 0) {
                ImageView up = (ImageView) findViewById(upId);
                up.setImageResource(R.drawable.back);
            }
        }
        movieViewPager = new ViewPager(this);
        movieViewPager.setId(R.id.movieViewPager);
        setContentView(movieViewPager);
        long movieId = -1;
        if (getIntent().getLongExtra(MovieFragment.EXTRA_MOVIE_ID, -1) != -1) {
            movieId = getIntent().getLongExtra(MovieFragment.EXTRA_MOVIE_ID, -1);
        }
        cursor = this.getContentResolver().query(
                MoviesContentProvider.CONTENT_URI,
                MoviesContentProvider.MOVIE_PROJECTION,
                MovieDatabaseHelper.SELECTION_ALL_BUT_ARCHIVED,
                null,
                MovieDatabaseHelper.COLUMN_NAME);
        moviePosition = calculatePostionById(movieId);
        handleViewPagerAdapter();
        setCurrentItem();
    }

    @Override
    protected void onResume() {
        super.onResume();
        BackgroundMusicHandler.handleMusicPlay(this);
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

    private void handleViewPagerAdapter() {
        FragmentManager fm = getSupportFragmentManager();
        movieViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public int getCount() {
                return cursor.getCount();
            }

            @Override
            public Fragment getItem(int pos) {
                Movie movie = Movie.createMovieFromCursor(cursor, pos);
                return MovieFragment.createMovieFragment(movie);
            }
        });
        movieViewPager.setOnPageChangeListener(onPageChangeListener);
        onPageChangeListener.onPageSelected(0);
    }

    private void setCurrentItem() {
        movieViewPager.setCurrentItem(moviePosition);
    }

    private int calculatePostionById(long movieId) {
        int position = 0;
        while (cursor.moveToNext()) {
            if (cursor.getLong(cursor.getColumnIndexOrThrow(MovieDatabaseHelper.COLUMN_ID)) == movieId) {
                return position;
            }
            position++;
        }
        return 0;
    }

    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        public void onPageScrollStateChanged(int state) {
        }

        public void onPageScrolled(int pos, float posOffset, int posOffsetPixels) {
        }

        public void onPageSelected(int pos) {
            Movie movie = Movie.createMovieFromCursor(cursor, pos);
            if (movie.getName() != null) {
                setTitle(movie.getName());
            }
        }
    };

}

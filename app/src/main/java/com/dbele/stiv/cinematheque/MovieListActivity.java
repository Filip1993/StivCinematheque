package com.dbele.stiv.cinematheque;

import android.support.v4.app.Fragment;

/**
 * Created by dbele on 3/24/2015.
 */
public class MovieListActivity extends AFragmentActivity {

    @Override
    public Fragment createFragment() {
        return new MoviesListFragment();
    }
}

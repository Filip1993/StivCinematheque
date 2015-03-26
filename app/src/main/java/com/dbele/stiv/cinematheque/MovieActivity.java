package com.dbele.stiv.cinematheque;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;


public class MovieActivity extends AFragmentActivity {

    @Override
    public Fragment createFragment() {
        return new MovieFragment();
    }

}

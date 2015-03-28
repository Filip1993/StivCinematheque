package com.dbele.stiv.cinematheque;

import android.support.v4.app.Fragment;


public class InitActivity extends AFragmentActivity {
    @Override
    public Fragment createFragment() {
        return new InitFragment();
    }
}

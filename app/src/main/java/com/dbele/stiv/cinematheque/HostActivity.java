package com.dbele.stiv.cinematheque;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.dbele.stiv.model.NavigationItem;
import com.dbele.stiv.utitlities.BackgroundMusicHandler;

import java.util.ArrayList;


public class HostActivity extends Activity {

    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private ActionBarDrawerToggle drawerToggle;

    private String[] navMenuTitles;
    private String[] navMenuFragments;
    private TypedArray navMenuIcons;

    private ArrayList<NavigationItem> navigationItems;
    private NavDrawerListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);

        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);

        navMenuFragments = getResources().getStringArray(R.array.nav_fragments);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.left_drawer);

        navigationItems = new ArrayList<NavigationItem>();

        for (int i=0; i<navMenuFragments.length; i++) {
            navigationItems.add(new NavigationItem(navMenuTitles[i], navMenuIcons.getResourceId(i, -1), navMenuFragments[i]));

        }

        navMenuIcons.recycle();

        drawerList.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                        drawerLayout.closeDrawer(drawerList);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                displayView(position);
                            }
                        }, 300);

                    }
                }

        );

        adapter = new NavDrawerListAdapter(getApplicationContext(),
                navigationItems);
        drawerList.setAdapter(adapter);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        //getActionBar().setHomeButtonEnabled(true);


        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.string.drawer_open, // nav drawer open - description for accessibility
                R.string.drawer_close // nav drawer close - description for accessibility
        ) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            public void onDrawerOpened(View view) {
                super.onDrawerOpened(view);
            }
        };

        drawerToggle.setDrawerIndicatorEnabled(true);

        drawerLayout.setDrawerListener(drawerToggle);

        displayView(0);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    private void displayView(int position) {

        drawerList.setItemChecked(position, true);
        drawerList.setSelection(position);
        setTitle(navMenuTitles[position]);
        drawerLayout.closeDrawer(drawerList);

        NavigationItem navigationItem = navigationItems.get(position);

        handleFragment(navigationItem.getFragmentName());

    }

    private void handleFragment(String className) {
        FragmentManager fm = getFragmentManager();
        Fragment fragment = null;
        try{
            fragment = (Fragment)Class.forName(className).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            Log.e(getClass().getName(), e.getMessage());
        }
        if (fragment != null) {
                fm.beginTransaction()
                        .replace(R.id.content_frame, fragment)
                        .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);


    }

    @Override
    protected void onResume() {
        super.onResume();
        BackgroundMusicHandler.handleMusicPlay(this);
    }

}

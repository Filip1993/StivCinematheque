package com.dbele.stiv.cinematheque;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
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
import com.dbele.stiv.handlers.BackgroundMusicHandler;
import java.util.ArrayList;

public class HostActivity extends Activity {

    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private ActionBarDrawerToggle drawerToggle;
    private String[] navMenuTitles;
    private ArrayList<NavigationItem> navigationItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);
        ActionBar actionBar = getActionBar();
        if (actionBar!=null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
        init();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        BackgroundMusicHandler.handleMusicPlay(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return drawerToggle.onOptionsItemSelected(item);
    }

    private void init() {
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        TypedArray navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);
        String[] navMenuFragments = getResources().getStringArray(R.array.nav_fragments);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.left_drawer);
        navigationItems = new ArrayList<>();
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
                                displayView(position-1);
                            }
                        }, 300);
                    }
                }
        );
        ImageView imageView =  new ImageView(this);
        imageView.setImageResource(R.drawable.navigation_drawer_header);
        drawerList.addHeaderView(imageView, null, false);
        NavDrawerListAdapter adapter = new NavDrawerListAdapter(getApplicationContext(),
                navigationItems);
        drawerList.setAdapter(adapter);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.string.drawer_open, R.string.drawer_close ) {
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

    private void displayView(int position) {
        drawerList.setItemChecked(position+1, true);
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
        } catch (InstantiationException e) {
            Log.e(getClass().getName(), e.getMessage());
        } catch (IllegalAccessException e) {
            Log.e(getClass().getName(), e.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e(getClass().getName(), e.getMessage());
        }
        if (fragment != null) {
            fm.beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .commit();
        }
    }
}

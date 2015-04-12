package com.dbele.stiv.cinematheque;

import android.app.ActionBar;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.dbele.stiv.persistence.MovieDatabaseHelper;
import com.dbele.stiv.persistence.MoviesContentProvider;

public class MoviesListFragment extends ListFragment {

    private Cursor cursor;
    private SearchView searchView;
    private MoviesCursorAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setTitle(getResources().getString(R.string.movie_list_fragment_title));

        ActionBar actionBar = getActivity().getActionBar();
        if(actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(Boolean.FALSE);
        }

        setHasOptionsMenu(Boolean.TRUE);

        setCursor(MovieDatabaseHelper.SELECTION_ALL, null);

        adapter = new MoviesCursorAdapter(getActivity(), cursor);
        setListAdapter(adapter);
    }

    private void setCursor(String selection, String[] selectionArgs) {
        cursor = getActivity().getContentResolver().
                query(MoviesContentProvider.CONTENT_URI, MoviesContentProvider.MOVIES_LIST_PROJECTION, selection, selectionArgs, MovieDatabaseHelper.COLUMN_NAME);
        if (adapter!=null) {
            adapter.swapCursor(cursor);
            //adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_list_movies, menu);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        setupSearchView();

    }

    private void setupSearchView() {
        searchView.setIconifiedByDefault(true);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                setCursor(MovieDatabaseHelper.SELECTION_NAME, new String[] {newText + "%"});
                return true;
            }
        });


    }

    @Override
    public void onDestroy() {
        cursor.close();
        super.onDestroy();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new Intent(getActivity(), MoviePagerActivity.class);
        intent.putExtra(MovieFragment.EXTRA_MOVIE_ID, id);
        startActivity(intent);
    }


    private static class MoviesCursorAdapter extends CursorAdapter {
        public MoviesCursorAdapter(Context context, Cursor cursor) {
            super(context, cursor, 0);
        }
        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            return inflater.inflate(R.layout.movies_list_item, parent, false);
        }
        @Override
        public void bindView(View view, Context context, Cursor cursor) {

            TextView tvMovieListName = (TextView) view.findViewById(R.id.tvMovieListName);
            TextView tvMovieListGenre = (TextView) view.findViewById(R.id.tvMovieListGenre);

            tvMovieListName.setText(cursor.getString(cursor.getColumnIndexOrThrow(MovieDatabaseHelper.COLUMN_NAME)));
            tvMovieListGenre.setText(cursor.getString(cursor.getColumnIndexOrThrow(MovieDatabaseHelper.COLUMN_GENRE)));

            ImageView ivMovieListPic = (ImageView) view.findViewById(R.id.ivMovieListPic);
            String picturePath = cursor.getString(cursor.getColumnIndexOrThrow(MovieDatabaseHelper.COLUMN_PICTURE_PATH));
            if (picturePath != null) {
                Uri pictureUri = Uri.parse(picturePath);
                ivMovieListPic.setImageURI(pictureUri);
            } else {
                ivMovieListPic.setImageResource(R.drawable.logo);
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_watched:
                setCursor(MovieDatabaseHelper.SELECTION_WATCHED, null);
                break;
            case R.id.action_notwatched:
                setCursor(MovieDatabaseHelper.SELECTION_NOT_WATCHED, null);
                break;
            case R.id.action_all:
                setCursor(MovieDatabaseHelper.SELECTION_ALL, null);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}

package com.dbele.stiv.cinematheque;

import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import com.dbele.stiv.persistence.MovieDatabaseHelper;
import com.dbele.stiv.persistence.MoviesContentProvider;

public class MoviesListFragment extends ListFragment {

    private static final int LOADER_ID = 1;
    private SearchView searchView;
    private MoviesCursorAdapter adapter;
    private LoaderManager loaderManager;
    private String selection = null;
    private String[] selectionArgs = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setTitle(getResources().getString(R.string.movie_list_fragment_title));
        setHasOptionsMenu(Boolean.TRUE);
        selection = MovieDatabaseHelper.SELECTION_ALL_BUT_ARCHIVED;
        adapter = new MoviesCursorAdapter(getActivity(), null);
        setListAdapter(adapter);
        loaderManager = getLoaderManager();
        loaderManager.initLoader(LOADER_ID, null, loaderCallbacks);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        registerForContextMenu(getListView());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_list_movies, menu);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        setupSearchView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_watched:
                selection = MovieDatabaseHelper.SELECTION_WATCHED;
                selectionArgs = null;
                loaderManager.restartLoader(1, null, loaderCallbacks);
                break;
            case R.id.action_notwatched:
                selection = MovieDatabaseHelper.SELECTION_NOT_WATCHED;
                selectionArgs = null;
                loaderManager.restartLoader(1, null, loaderCallbacks);
                break;
            case R.id.action_all:
                selection = MovieDatabaseHelper.SELECTION_ALL_BUT_ARCHIVED;
                selectionArgs = null;
                loaderManager.restartLoader(1, null, loaderCallbacks);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new Intent(getActivity(), MoviePagerActivity.class);
        intent.putExtra(MovieFragment.EXTRA_MOVIE_ID, id);
        startActivity(intent);
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
                selection = MovieDatabaseHelper.SELECTION_NAME;
                selectionArgs = new String[] {newText + "%"};
                loaderManager.restartLoader(1, null, loaderCallbacks);
                return true;
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle(getResources().getString(R.string.context_menu_delete));
        getActivity().getMenuInflater().inflate(R.menu.context_menu_movies_list, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.context_menu_delete:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                long idMovie = info.id;
                ContentValues contentValues = new ContentValues();
                contentValues.put(MovieDatabaseHelper.COLUMN_ARCHIVED, 1);
                getActivity().getContentResolver().
                        update(Uri.parse(MoviesContentProvider.CONTENT_URI + "/" + idMovie),
                                contentValues, null, null);
        }
        return true;
    }

    private LoaderManager.LoaderCallbacks<Cursor> loaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new CursorLoader(getActivity(), MoviesContentProvider.CONTENT_URI,
                    MoviesContentProvider.MOVIES_LIST_PROJECTION,
                    selection,
                    selectionArgs, MovieDatabaseHelper.COLUMN_NAME);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            data.setNotificationUri(getActivity().getContentResolver(), MoviesContentProvider.CONTENT_URI);
            if(adapter!=null) {
                adapter.swapCursor(data);
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            if(adapter!=null) {
                adapter.swapCursor(null);
            }
        }
    };

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
            tvMovieListName.setText(cursor.getString(cursor.getColumnIndexOrThrow(MovieDatabaseHelper.COLUMN_NAME)));
            boolean watched = cursor.getInt(cursor.getColumnIndexOrThrow(MovieDatabaseHelper.COLUMN_WATCHED))==1;
            ImageView ivWatched = (ImageView) view.findViewById(R.id.ivWatched);
            if(watched) {
                ivWatched.setImageResource(R.drawable.watched);
            } else {
                ivWatched.setImageResource(android.R.color.transparent);
            }
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

}

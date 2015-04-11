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
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dbele.stiv.persistence.MovieDatabaseHelper;
import com.dbele.stiv.persistence.MoviesContentProvider;


public class MoviesListFragment extends ListFragment {

    private Cursor cursor;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setTitle(getResources().getString(R.string.movie_list_fragment_title));

        ActionBar actionBar = getActivity().getActionBar();
        if(actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(Boolean.FALSE);
        }

        setHasOptionsMenu(Boolean.TRUE);

        cursor = getActivity().getContentResolver().
                query(MoviesContentProvider.CONTENT_URI, MoviesContentProvider.MOVIES_LIST_PROJECTION, null, null, MovieDatabaseHelper.COLUMN_NAME);

        MoviesCursorAdapter adapter = new MoviesCursorAdapter(getActivity(), cursor);
        setListAdapter(adapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_list_movies, menu);
    }

    @Override
    public void onDestroy() {
        cursor.close();
        super.onDestroy();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new Intent(getActivity(), MoviePagerActivity.class);
        //intent.putExtra(MovieFragment.EXTRA_MOVIE_ID, id);
        intent.putExtra(MovieFragment.EXTRA_MOVIE_POSITION, position);
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
                ivMovieListPic.setImageResource(R.drawable.launcher);
            }
        }

    }

}

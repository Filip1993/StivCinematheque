package com.dbele.stiv.cinematheque;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

        cursor = getActivity().getContentResolver().
                query(MoviesContentProvider.CONTENT_URI, MoviesContentProvider.MOVIES_LIST_PROJECTION, null, null, MovieDatabaseHelper.COLUMN_NAME);

        MoviesCursorAdapter adapter = new MoviesCursorAdapter(getActivity(), cursor);
        setListAdapter(adapter);
    }

    @Override
    public void onDestroy() {
        cursor.close();
        super.onDestroy();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new Intent(getActivity(), MovieActivity.class);
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
                ivMovieListPic.setImageResource(R.mipmap.ic_launcher);
            }
        }

    }

}

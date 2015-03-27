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

import com.dbele.stiv.db.MovieDatabaseHelper;
import com.dbele.stiv.model.Movie;
import com.dbele.stiv.persistence.MovieRepository;



public class MoviesListFragment extends ListFragment {

    private MovieDatabaseHelper.MovieCursor moviesCursor;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setTitle(getResources().getString(R.string.movie_list_fragment_title));

        moviesCursor = MovieRepository.getInstance(getActivity()).getMovies();
        MoviesCursorAdapter adapter = new MoviesCursorAdapter(getActivity(), moviesCursor);
        setListAdapter(adapter);
    }

    @Override
    public void onDestroy() {
        moviesCursor.close();
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new Intent(getActivity(), MovieActivity.class);
        intent.putExtra(MovieFragment.EXTRA_MOVIE_ID, id);
        startActivity(intent);
    }


    private static class MoviesCursorAdapter extends CursorAdapter {
        private MovieDatabaseHelper.MovieCursor movieCursor;
        public MoviesCursorAdapter(Context context, MovieDatabaseHelper.MovieCursor cursor) {
            super(context, cursor, 0);
            movieCursor = cursor;
        }
        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater)context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            return inflater
                    .inflate(R.layout.movies_list_item, parent, false);
        }
        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            Movie movie = movieCursor.getMovie();

            TextView tvMovieListName = (TextView) view.findViewById(R.id.tvMovieListName);
            TextView tvMovieListDirector = (TextView) view.findViewById(R.id.tvMovieListDirector);

            tvMovieListName.setText(movie.getName());
            tvMovieListDirector.setText(movie.getDirector());

            ImageView ivMovieListPic = (ImageView) view.findViewById(R.id.ivMovieListPic);
            if (movie.getPicturePath() != null) {
                Uri pictureUri = Uri.parse(movie.getPicturePath());
                ivMovieListPic.setImageURI(pictureUri);
            } else {
                ivMovieListPic.setImageResource(R.mipmap.ic_launcher);
            }
        }

    }


}

package com.dbele.stiv.cinematheque;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dbele.stiv.model.Movie;
import com.dbele.stiv.persistence.MovieRepository;
import com.dbele.stiv.rss.RssParser;
import com.dbele.stiv.utitlities.Utility;

import java.util.ArrayList;


public class MoviesListFragment extends ListFragment {

    private ImageView ivMovieListPic;
    private ArrayList<Movie> movies;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setTitle(getResources().getString(R.string.movie_list_fragment_title));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies_list, null);

        movies = MovieRepository.getInstance(getActivity()).getMovies();

        ArrayAdapter<Movie> adapter = new MoviesListAdapter(movies);
        setListAdapter(adapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MoviesListAdapter)getListAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Movie movie = (Movie)getListAdapter().getItem(position);

        Intent intent = new Intent(getActivity(), MovieActivity.class);
        intent.putExtra(MovieFragment.EXTRA_MOVIE_ID, movie.getIdMovie());

        startActivity(intent);
    }

    private class MoviesListAdapter extends ArrayAdapter<Movie> {
        public MoviesListAdapter(ArrayList<Movie> movies) {
            super(getActivity(), 0, movies);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.movies_list_item, parent, false);
            }

            TextView tvMovieListName = (TextView)convertView.findViewById(R.id.tvMovieListName);
            TextView tvMovieListDirector = (TextView)convertView.findViewById(R.id.tvMovieListDirector);

            Movie movie = getItem(position);

            tvMovieListName.setText(movie.getName());
            tvMovieListDirector.setText(movie.getDirector());
            ivMovieListPic = (ImageView)convertView.findViewById(R.id.ivMovieListPic);
            if (movie.getPicturePath()!=null) {
                Uri pictureUri = Uri.parse(movie.getPicturePath());
                ivMovieListPic.setImageURI(pictureUri);
            } else {
                ivMovieListPic.setImageResource(R.mipmap.ic_launcher);
            }

            return convertView;

        }
    }


}

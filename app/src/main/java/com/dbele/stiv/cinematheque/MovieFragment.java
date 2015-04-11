package com.dbele.stiv.cinematheque;

import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dbele.stiv.model.Movie;
import com.dbele.stiv.persistence.MovieDatabaseHelper;
import com.dbele.stiv.persistence.MovieRepository;
import com.dbele.stiv.persistence.MoviesContentProvider;
import com.dbele.stiv.utitlities.Utility;

import java.io.File;
import java.util.Date;


public class MovieFragment extends Fragment {

    public static final String EXTRA_MOVIE_POSITION = "com.dbele.stiv.cinematheque.extra.movie.position";
    public static final String EXTRA_MOVIE_ID = "com.dbele.stiv.cinematheque.extra.movie.id";

    private Movie movie;
    private TextView tvMovieName;
    private TextView tvMovieGenre;
    private TextView tvMovieDesc;
    private TextView tvMovieDirector;
    private TextView tvMovieActors;
    private ImageView ivMoviePic;

    public static MovieFragment createMovieFragment(Movie movie) {
        MovieFragment movieFragment = new MovieFragment();
        movieFragment.movie = movie;
        return movieFragment;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        ViewGroup viewGroup = (ViewGroup) getView();
        viewGroup.removeAllViewsInLayout();
        View view = onCreateView(inflater, viewGroup, null);
        viewGroup.addView(view);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie, container, false);
        fillData(view);
        return view;
    }

    private void fillData(View view) {
        tvMovieName = (TextView)view.findViewById(R.id.tvMovieName);
        tvMovieName.setText(movie.getName()!=null ? movie.getName() : "");

        tvMovieDesc = (TextView)view.findViewById(R.id.tvMovieDesc);
        tvMovieDesc.setText(movie.getDescription()!=null ? movie.getDescription() : "");

        tvMovieGenre = (TextView)view.findViewById(R.id.tvMovieGenre);
        tvMovieGenre.setText(movie.getGenre()!=null ? movie.getGenre() : "");

        tvMovieDirector = (TextView)view.findViewById(R.id.tvMovieDirector);
        tvMovieDirector.setText(movie.getDirector()!=null ? movie.getDirector() : "");

        tvMovieActors = (TextView)view.findViewById(R.id.tvMovieActors);
        tvMovieActors.setText(movie.getActors()!=null ? movie.getActors() : "");

        ivMoviePic = (ImageView)view.findViewById(R.id.ivMoviePic);
        if (movie.getPicturePath()!=null) {

            Uri pictureUri = Uri.parse(movie.getPicturePath());
            //Log.v("MF picture path", pictureUri.getPath());
            ivMoviePic.setImageURI(pictureUri);
        } else {
            ivMoviePic.setImageResource(R.drawable.launcher);
        }
    }

}

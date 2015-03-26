package com.dbele.stiv.cinematheque;

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

import com.dbele.stiv.model.Movie;
import com.dbele.stiv.persistence.MovieRepository;
import com.dbele.stiv.utitlities.Utility;

import java.io.File;


public class MovieFragment extends Fragment {

    public static final String EXTRA_MOVIE_ID = "com.dbele.stiv.cinematheque.extra.movie.id";

    private Movie movie;
    private EditText etMovieName;
    private Button btnChooseDate;
    private ImageView ivMoviePic;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivity().getWindow().setTitle(getResources().getString(R.string.movie_fragment_title));
        int idMovie = getActivity().getIntent().getIntExtra(EXTRA_MOVIE_ID, -1);
        if (idMovie!=-1){
            movie = MovieRepository.getInstance(getActivity()).getMovie(idMovie);
        } else {
            movie = new Movie();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie, container, false);

        etMovieName = (EditText)view.findViewById(R.id.etMovieName);
        etMovieName.setText(movie.getName()!=null ? movie.getName() : "");

        btnChooseDate = (Button)view.findViewById(R.id.btnChooseDate);
        btnChooseDate.setText(Utility.getFormattedDate("dd/MM/yyyy", movie.getPublishedDate()));
        btnChooseDate.setEnabled(false);

        ivMoviePic = (ImageView)view.findViewById(R.id.ivMoviePic);
        if (movie.getPicturePath()!=null) {

            Uri pictureUri = Uri.parse(movie.getPicturePath());
            //Log.v("MF picture path", pictureUri.getPath());
            ivMoviePic.setImageURI(pictureUri);
        } else {
            ivMoviePic.setImageResource(R.mipmap.ic_launcher);
        }


        return view;


    }

}

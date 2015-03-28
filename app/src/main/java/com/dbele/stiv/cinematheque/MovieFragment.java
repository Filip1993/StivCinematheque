package com.dbele.stiv.cinematheque;

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

import com.dbele.stiv.model.Movie;
import com.dbele.stiv.persistence.MovieDatabaseHelper;
import com.dbele.stiv.persistence.MovieRepository;
import com.dbele.stiv.persistence.MoviesContentProvider;
import com.dbele.stiv.utitlities.Utility;

import java.io.File;
import java.util.Date;


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

        long idMovie = getActivity().getIntent().getLongExtra(EXTRA_MOVIE_ID, -1);

        Cursor cursor =  getActivity().getContentResolver().query(Uri.parse(MoviesContentProvider.CONTENT_URI + "/" + idMovie),
                MoviesContentProvider.MOVIE_PROJECTION, null, null, null);

        if(cursor!=null) {
            cursor.moveToNext();
        }
        movie = new Movie();

        movie.setIdMovie(cursor.getLong(cursor.getColumnIndexOrThrow(MovieDatabaseHelper.COLUMN_ID)));
        movie.setName(cursor.getString(cursor.getColumnIndexOrThrow(MovieDatabaseHelper.COLUMN_NAME)));
        movie.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(MovieDatabaseHelper.COLUMN_DESCRIPTION)));

        movie.setDirector(cursor.getString(cursor.getColumnIndexOrThrow(MovieDatabaseHelper.COLUMN_DIRECTOR)));
        movie.setActors(cursor.getString(cursor.getColumnIndexOrThrow(MovieDatabaseHelper.COLUMN_ACTORS)));
        movie.setLength(cursor.getInt(cursor.getColumnIndexOrThrow(MovieDatabaseHelper.COLUMN_LENGTH)));
        movie.setGenre(cursor.getString(cursor.getColumnIndexOrThrow(MovieDatabaseHelper.COLUMN_GENRE)));
        movie.setPicturePath(cursor.getString(cursor.getColumnIndexOrThrow(MovieDatabaseHelper.COLUMN_PICTURE_PATH)));
        movie.setTicketPath(cursor.getString(cursor.getColumnIndexOrThrow(MovieDatabaseHelper.COLUMN_TICKET_PATH)));
        long watchedDate = cursor.getLong(cursor.getColumnIndexOrThrow(MovieDatabaseHelper.COLUMN_WATCHED_DATE));

        if (watchedDate>0) {
            movie.setWatchedDate(new Date(watchedDate));
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie, container, false);

        etMovieName = (EditText)view.findViewById(R.id.etMovieName);
        etMovieName.setText(movie.getName()!=null ? movie.getName() : "");

        btnChooseDate = (Button)view.findViewById(R.id.btnChooseDate);
        //btnChooseDate.setText(Utility.getFormattedDate("dd/MM/yyyy", movie.getPublishedDate()));
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

package com.dbele.stiv.persistence;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.dbele.stiv.cinematheque.MovieListActivity;
import com.dbele.stiv.cinematheque.R;
import com.dbele.stiv.model.Movie;
import com.dbele.stiv.utitlities.NotificationHandler;
import com.dbele.stiv.utitlities.PreferencesHandler;

import java.util.ArrayList;

/**
 * Created by dbele on 3/24/2015.
 */
public class MovieRepository {

    private Context context;
    private static MovieRepository movieRepositoryInstance;

    private MovieDatabaseHelper dbHelper;

    public MovieRepository(Context context) {
        this.context = context;
        dbHelper = new MovieDatabaseHelper(context);
    }


    public static MovieRepository getInstance(Context context) {
        if (movieRepositoryInstance == null) {
            movieRepositoryInstance = new MovieRepository(context.getApplicationContext());
        }
        return movieRepositoryInstance;
    }

    public MovieDatabaseHelper.MovieCursor getMovies(){
        return dbHelper.queryMovies();
    }

    public Movie getMovie(long id) {
        Movie movie = null;
        MovieDatabaseHelper.MovieCursor cursor = dbHelper.queryMovie(id);
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            movie = cursor.getMovie();
        }
        cursor.close();
        return movie;
    }

    public void insertMovie(Movie movie) {
        dbHelper.insertMovie(movie);
    }

    public void insertMovies(ArrayList<Movie> movies) {
        dbHelper.insertMovies(movies);
        Log.v("MovieRepository", "movies inserted");

        if (!PreferencesHandler.checkIfDbLoaded(context)) {
            PreferencesHandler.setDbLoaded(context);
        } else {
            NotificationHandler.sendMoviesInsertedNotification(context);
        }

    }







}

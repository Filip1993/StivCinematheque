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
import com.dbele.stiv.rss.RssParser;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by dbele on 3/24/2015.
 */
public class MovieRepository {

    private Context context;
    private static MovieRepository movieRepositoryInstance;

    private ArrayList<Movie> movies;

    public MovieRepository(Context context) {
        this.context = context;
        movies = new ArrayList<>();


    }

    public static MovieRepository getInstance(Context context) {
        if (movieRepositoryInstance == null) {
            movieRepositoryInstance = new MovieRepository(context.getApplicationContext());
            Log.v("MovieRepository", "created");

        }
        return movieRepositoryInstance;
    }

    //TODO - change to dynamic
    public Movie getMovie(int idMovie) {
        for (Movie m : movies) {
            if (m.getIdMovie() == idMovie) {
                return m;
            }
        }
        return null;
    }

    //TODO - change to dynamic
    public ArrayList<Movie> getMovies () {
        return movies;
    }

    public void insertMovie(Movie movie) {
        movies.add(movie);
    }

    public void insertMovies(ArrayList<Movie> movies) {
        this.movies = new ArrayList<>();
        this.movies.addAll(movies);
        Log.v("MovieRepository", "movies inserted");

        sendNotification();

    }

    private void sendNotification() {
        Resources r = context.getResources();
        PendingIntent pi = PendingIntent.getActivity(context, 0, new Intent(context, MovieListActivity.class), 0);
        Notification notification = new NotificationCompat.Builder(context)
                .setTicker(r.getString(R.string.rss_parsed_ticker))
                .setSmallIcon(android.R.drawable.ic_menu_report_image)
                .setContentTitle(r.getString(R.string.rss_parsed_content_title))
                .setContentText(r.getString(R.string.rss_parsed_content_text))
                .setContentIntent(pi)
                .setAutoCancel(true)
                .build();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }





}

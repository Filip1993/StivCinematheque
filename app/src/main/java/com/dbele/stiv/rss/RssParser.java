package com.dbele.stiv.rss;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.dbele.stiv.model.Movie;
import com.dbele.stiv.persistence.MovieDatabaseHelper;
import com.dbele.stiv.persistence.MovieRepository;
import com.dbele.stiv.persistence.MoviesContentProvider;
import com.dbele.stiv.utitlities.ImagesHandler;
import com.dbele.stiv.utitlities.Utility;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


/**
 * Created by dbele on 3/24/2015.
 */
public class RssParser {

    private Context context;
    private ArrayList<Movie> movies = new ArrayList<>();

    private Set<String> movieNames = new HashSet<>();

    private static final String RSS_URL = "http://www.blitz-cinestar.hr/rss.aspx";
    private static final String TAG = "RssParser";

    private static final String ITEM = "item";

    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final String REDATELJ = "redatelj";
    private static final String GLUMCI = "glumci";
    private static final String TRAJANJE = "trajanje";
    private static final String ZANR = "zanr";

    private XmlPullParserFactory xmlPullParserFactory;
    public volatile boolean done = false;

    private static ArrayList<String> watched_archived_movies;

    public RssParser(Context context) {
        this.context = context;
    }

    public static void upadateDatabaseFromRssFeed(Context context) {
        populateWatchedArchivedMovies(context);
        RssParser p =  new RssParser(context);
        p.run();
    }

    private static void populateWatchedArchivedMovies(Context context) {
        watched_archived_movies = new ArrayList<>();
        Cursor cursor = context.getContentResolver().
                query(MoviesContentProvider.CONTENT_URI, MoviesContentProvider.MOVIE_NAMES_PROJECTION,
                        MovieDatabaseHelper.SELECTION_ARCHIVED_WATCHED, null, MovieDatabaseHelper.COLUMN_NAME);
        while(cursor.moveToNext()) {
            watched_archived_movies.add(cursor.getString(cursor.getColumnIndexOrThrow(MovieDatabaseHelper.COLUMN_NAME)));
        }
    }

    public void run() {


        InputStream stream = null;
        try {
            URL url = new URL(RSS_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            conn.connect();
            stream = conn.getInputStream();
            xmlPullParserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = xmlPullParserFactory.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(stream, null);

            parse(parser);

            MovieRepository.getInstance(context).loadDatabaseAndNotifyIfNeeded(movies);

        } catch (IOException | XmlPullParserException e) {
            Log.d(TAG, "Parse not completed", e);
        } finally {
            if (stream!=null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void parse(XmlPullParser parser) {
        int event;
        String text=null;

        boolean itemsStarted = false;
        try {
            event = parser.getEventType();
            Movie movie = null;
            int idMovieName = 0;
            while (event != XmlPullParser.END_DOCUMENT) {
                String name=parser.getName();

                switch (event){
                    case XmlPullParser.START_TAG:
                        if(ITEM.equals(name)) {
                            movie = new Movie();
                            //avoiding duplicate names for movies
                            itemsStarted = true;
                            //movie.setWatched(idMovieName%2);
                        }
                        break;
                    case XmlPullParser.TEXT:
                        if (itemsStarted) {
                            text = parser.getText();
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if(itemsStarted) {
                            switch (name) {
                                case(TITLE):
                                    if(text.length() > 39) {
                                        text = text.substring(0, 36) + "...";
                                    }
                                    movie.setName(text);
                                    if (!watched_archived_movies.contains(movie.getName()) && movieNames.add(movie.getName())) {
                                        movies.add(movie);
                                    }
                                    break;
                                case(DESCRIPTION):
                                    movie.setDescription(Utility.extractDescription(text));
                                    if (movies.contains(movie)) {
                                        String fileUrl = Utility.extractImagePathFromDescription(text);
                                        //Log.v("RSSParser", fileUrl);
                                            String picturePath = ImagesHandler.downloadImageAndStore(context, fileUrl, Movie.MOVIE_JPG_PREFIX + (movie.getName().hashCode()));
                                        if (picturePath != null) {
                                            movie.setPicturePath(picturePath);
                                        }
                                    }
                                    break;
                                case(GLUMCI):
                                    movie.setActors(text);
                                    break;
                                case(REDATELJ):
                                    movie.setDirector(text);
                                    break;
                                case(ZANR):
                                    movie.setGenre(text);
                                    break;
                                case(TRAJANJE):
                                    try {
                                        movie.setLength(Integer.parseInt(text));
                                    } catch (NumberFormatException nfe) {
                                        movie.setLength(0);
                                    }
                                    break;
                            }

                        }
                }
                event = parser.next();
            }
            done = false;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            done = true;
        }
    }

}

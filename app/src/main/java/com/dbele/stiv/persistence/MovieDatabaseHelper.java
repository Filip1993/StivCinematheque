package com.dbele.stiv.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dbele.stiv.model.Movie;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by dbele on 3/27/2015.
 */
public class MovieDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "movies.db";
    private static final int VERSION = 1;

    private static final String TABLE_NAME = "movie";

    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_DIRECTOR= "director";
    private static final String COLUMN_ACTORS = "actors";
    private static final String COLUMN_LENGTH = "length";
    private static final String COLUMN_GENRE = "genre";
    private static final String COLUMN_WATCHED_DATE = "watched_date";
    private static final String COLUMN_PICTURE_PATH = "picture_path";
    private static final String COLUMN_TICKET_PATH = "ticket_path";
    private static final String COLUMN_IMPRESSIONS = "impressions";
    private static final String DROP_TABLE_STMT = "drop table " + TABLE_NAME;
    private static String CREATE_TABLE_STMT = null;
    private static final String DELETE_MOVIES_STMT = "delete from " + TABLE_NAME;

    static {
        CREATE_TABLE_STMT = prepareSqlInsertStatement();
    }

    private static String prepareSqlInsertStatement() {
        StringBuilder sb = new StringBuilder();
        sb.append("create table " + TABLE_NAME + " (");
        sb.append(COLUMN_ID + " integer primary key autoincrement, ");
        sb.append(COLUMN_NAME + " varchar(20), ");
        sb.append(COLUMN_DESCRIPTION + " varchar(200), ");
        sb.append(COLUMN_DIRECTOR + " varchar(50), ");
        sb.append(COLUMN_ACTORS + " varchar(50), ");
        sb.append(COLUMN_LENGTH + " integer, ");
        sb.append(COLUMN_GENRE + " varchar(50), ");
        sb.append(COLUMN_WATCHED_DATE + " integer, ");
        sb.append(COLUMN_PICTURE_PATH + " varchar(100), ");
        sb.append(COLUMN_TICKET_PATH + " varchar(100), ");
        sb.append(COLUMN_IMPRESSIONS + " varchar(200) )");
        return sb.toString();
    }

    public MovieDatabaseHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }
        @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_STMT);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE_STMT);
        onCreate(db);
    }

    public void insertMovies(ArrayList<Movie> movies) {
        deleteMovies();
        for(Movie m : movies) {
            insertMovie(m);
        }
    }

    public void insertMovie(Movie movie) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, movie.getName());
        contentValues.put(COLUMN_DESCRIPTION, movie.getDescription());
        contentValues.put(COLUMN_DIRECTOR, movie.getDirector());
        contentValues.put(COLUMN_ACTORS, movie.getActors());
        contentValues.put(COLUMN_LENGTH, movie.getLength());
        contentValues.put(COLUMN_GENRE, movie.getGenre());
        contentValues.put(COLUMN_WATCHED_DATE, movie.getWatchedDate() != null ? movie.getWatchedDate().getTime() : -1);
        contentValues.put(COLUMN_PICTURE_PATH, movie.getPicturePath());
        contentValues.put(COLUMN_TICKET_PATH, movie.getTicketPath());
        contentValues.put(COLUMN_IMPRESSIONS, movie.getImpressions());

        getWritableDatabase().insert(TABLE_NAME, null, contentValues);
    }

    public void deleteMovies() {
        getWritableDatabase().execSQL(DELETE_MOVIES_STMT);
    }

    public MovieCursor queryMovies() {
       Cursor wrapped = getReadableDatabase().query(TABLE_NAME,
                null, null, null, null, null, COLUMN_NAME + " asc");
        return new MovieCursor(wrapped);
    }

    public MovieCursor queryMovie(long id) {
        Cursor wrapped = getReadableDatabase().query(TABLE_NAME,
                null,
                COLUMN_ID + " = ?", // Look for a run ID
                new String[]{String.valueOf(id)}, // with this value
                null, // group by
                null, // order by
                null, // having
                "1"); // limit 1 row
        return new MovieCursor(wrapped);
    }

    public static class MovieCursor extends CursorWrapper {
        public MovieCursor(Cursor c) {
            super(c);
        }

        public Movie getMovie() {
            if (isBeforeFirst() || isAfterLast())
                return null;
            Movie movie = new Movie();
            movie.setIdMovie(getLong(getColumnIndex(COLUMN_ID)));
            movie.setName(getString(getColumnIndex(COLUMN_NAME)));
            movie.setDescription(getString(getColumnIndex(COLUMN_DESCRIPTION)));
            movie.setActors(getString(getColumnIndex(COLUMN_ACTORS)));
            movie.setDirector(getString(getColumnIndex(COLUMN_DIRECTOR)));
            movie.setLength(getInt(getColumnIndex(COLUMN_LENGTH)));
            movie.setGenre(getString(getColumnIndex(COLUMN_GENRE)));
            movie.setWatchedDate(new Date(getLong(getColumnIndex(COLUMN_WATCHED_DATE))));
            movie.setPicturePath(getString(getColumnIndex(COLUMN_PICTURE_PATH)));
            movie.setTicketPath(getString(getColumnIndex(COLUMN_PICTURE_PATH)));
            movie.setImpressions(getString(getColumnIndex(COLUMN_IMPRESSIONS)));
            return movie;
        }
    }




}

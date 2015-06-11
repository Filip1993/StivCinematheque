package com.dbele.stiv.model;

import android.database.Cursor;
import android.support.annotation.NonNull;

import com.dbele.stiv.persistence.MovieDatabaseHelper;

public class Movie implements Comparable<Movie> {

    public static final String MOVIE_JPG_PREFIX = "movie_";
    public static final String TICKET_JPG_PREFIX = "ticket_";

    private long idMovie;
    private String name;
    private String description;
    private String director;
    private String actors;
    private int length;
    private String genre;
    private long watchedDate;
    private String picturePath;
    private String ticketPath;
    private String impressions;
    private int watched;
    private int archived;
    private float degree;
    private String rank;

    public float getDegree() {
        return degree;
    }

    public void setDegree(float degree) {
        this.degree = degree;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getCinemaName() {
        return cinemaName;
    }

    public void setCinemaName(String cinemaName) {
        this.cinemaName = cinemaName;
    }

    private String cinemaName;

    public int getWatched() {
        return watched;
    }

    public void setWatched(int watched) {
        this.watched = watched;
    }

    public int getArchived() {
        return archived;
    }

    public void setArchived(int archived) {
        this.archived = archived;
    }

    public long getIdMovie() {
        return idMovie;
    }

    public void setIdMovie(long idMovie) {
        this.idMovie = idMovie;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public String getTicketPath() {
        return ticketPath;
    }

    public void setTicketPath(String ticketPath) {
        this.ticketPath = ticketPath;
    }

    public String getImpressions() {
        return impressions;
    }

    public void setImpressions(String impressions) {
        this.impressions = impressions;
    }

    public long getWatchedDate() {
        return watchedDate;
    }

    public void setWatchedDate(long watchedDate) {
        this.watchedDate = watchedDate;
    }

    @Override
    public int compareTo(@NonNull Movie movie) {
        return this.getName().compareTo(movie.getName());
    }

    public static Movie createMovieFromCursor(Cursor cursor, int pos) {
        cursor.moveToPosition(pos);
        Movie movie = new Movie();
        movie.setIdMovie(cursor.getLong(cursor.getColumnIndexOrThrow(MovieDatabaseHelper.COLUMN_ID)));
        movie.setName(cursor.getString(cursor.getColumnIndexOrThrow(MovieDatabaseHelper.COLUMN_NAME)));
        movie.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(MovieDatabaseHelper.COLUMN_DESCRIPTION)));
        movie.setDirector(cursor.getString(cursor.getColumnIndexOrThrow(MovieDatabaseHelper.COLUMN_DIRECTOR)));
        movie.setActors(cursor.getString(cursor.getColumnIndexOrThrow(MovieDatabaseHelper.COLUMN_ACTORS)));
        movie.setLength(cursor.getInt(cursor.getColumnIndexOrThrow(MovieDatabaseHelper.COLUMN_LENGTH)));
        movie.setGenre(cursor.getString(cursor.getColumnIndexOrThrow(MovieDatabaseHelper.COLUMN_GENRE)));
        movie.setPicturePath(cursor.getString(cursor.getColumnIndexOrThrow(MovieDatabaseHelper.COLUMN_PICTURE_PATH)));
        movie.setCinemaName(cursor.getString(cursor.getColumnIndexOrThrow(MovieDatabaseHelper.COLUMN_CINEMANAME)));
        movie.setTicketPath(cursor.getString(cursor.getColumnIndexOrThrow(MovieDatabaseHelper.COLUMN_TICKET_PATH)));
        movie.setWatchedDate(cursor.getLong(cursor.getColumnIndexOrThrow(MovieDatabaseHelper.COLUMN_WATCHED_DATE)));
        movie.setImpressions(cursor.getString(cursor.getColumnIndexOrThrow(MovieDatabaseHelper.COLUMN_IMPRESSIONS)));
        movie.setRank(cursor.getString(cursor.getColumnIndexOrThrow(MovieDatabaseHelper.COLUMN_RANK)));
        movie.setDegree(cursor.getFloat(cursor.getColumnIndexOrThrow(MovieDatabaseHelper.COLUMN_DEGREE)));
        movie.setWatched(cursor.getInt(cursor.getColumnIndexOrThrow(MovieDatabaseHelper.COLUMN_WATCHED)));
        movie.setArchived(cursor.getInt(cursor.getColumnIndexOrThrow(MovieDatabaseHelper.COLUMN_ARCHIVED)));
        return movie;
    }
}

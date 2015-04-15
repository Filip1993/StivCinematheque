package com.dbele.stiv.model;


import java.util.Date;

/**
 * Created by dbele on 3/23/2015.
 */
public class Movie implements Comparable<Movie> {

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
    public int compareTo(Movie movie) {
        return this.getName().compareTo(movie.getName());
    }
}

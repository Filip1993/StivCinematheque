package com.dbele.stiv.persistence;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class MoviesContentProvider extends ContentProvider {

    private MovieDatabaseHelper dbHelper;

    private static final int MOVIES = 10;
    private static final int MOVIE_ID = 20;

    private static final String AUTHORITY = "com.dbele.stiv.cinematheque.movies.provider";

    private static final String BASE_PATH = "movies";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/movies";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/movie";

    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    public static final String[] MOVIES_LIST_PROJECTION = {MovieDatabaseHelper.COLUMN_ID, MovieDatabaseHelper.COLUMN_NAME,
            MovieDatabaseHelper.COLUMN_WATCHED, MovieDatabaseHelper.COLUMN_PICTURE_PATH};

    public static final String[] MOVIE_PROJECTION = {MovieDatabaseHelper.COLUMN_ID, MovieDatabaseHelper.COLUMN_NAME,
            MovieDatabaseHelper.COLUMN_DESCRIPTION, MovieDatabaseHelper.COLUMN_DIRECTOR, MovieDatabaseHelper.COLUMN_ACTORS,
            MovieDatabaseHelper.COLUMN_LENGTH, MovieDatabaseHelper.COLUMN_GENRE, MovieDatabaseHelper.COLUMN_PICTURE_PATH,
            MovieDatabaseHelper.COLUMN_CINEMANAME, MovieDatabaseHelper.COLUMN_TICKET_PATH,
            MovieDatabaseHelper.COLUMN_IMPRESSIONS, MovieDatabaseHelper.COLUMN_WATCHED,
            MovieDatabaseHelper.COLUMN_ARCHIVED, MovieDatabaseHelper.COLUMN_WATCHED_DATE};

    public static final String[] MOVIE_NAMES_PROJECTION = {MovieDatabaseHelper.COLUMN_NAME};


    static {
        URI_MATCHER.addURI(AUTHORITY, BASE_PATH, MOVIES);
        URI_MATCHER.addURI(AUTHORITY, BASE_PATH + "/#", MOVIE_ID);
    }


    @Override
    public boolean onCreate() {
        dbHelper = new MovieDatabaseHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        queryBuilder.setTables(MovieDatabaseHelper.TABLE_NAME);
        int uriType = URI_MATCHER.match(uri);
        switch (uriType) {
            case MOVIES:
                break;
            case MOVIE_ID:
                // adding the ID to the original query
                queryBuilder.appendWhere(MovieDatabaseHelper.COLUMN_ID + "="
                        + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);

        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = URI_MATCHER.match(uri);
        SQLiteDatabase sqlDB = dbHelper.getWritableDatabase();
        long id = 0;
        switch (uriType) {
            case MOVIES:
                id = sqlDB.insert(MovieDatabaseHelper.TABLE_NAME, null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = URI_MATCHER.match(uri);
        SQLiteDatabase sqlDB = dbHelper.getWritableDatabase();
        int rowsDeleted = 0;
        switch (uriType) {
            case MOVIES:
                rowsDeleted = sqlDB.delete(MovieDatabaseHelper.TABLE_NAME, selection,
                        selectionArgs);
                break;
            case MOVIE_ID:
                String id = uri.getLastPathSegment();
                rowsDeleted = sqlDB.delete(MovieDatabaseHelper.TABLE_NAME,
                        MovieDatabaseHelper.COLUMN_ID + "=" + id, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }


    @Override
    public String getType(Uri uri) {
        switch (URI_MATCHER.match(uri)) {
            case MOVIES:
                return CONTENT_TYPE;
            case MOVIE_ID:
                return CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }


    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int uriType = URI_MATCHER.match(uri);
        SQLiteDatabase sqlDB = dbHelper.getWritableDatabase();
        int updateCount = 0;
        switch (uriType) {
            case MOVIES:
                break;
            case MOVIE_ID:
                String id = uri.getLastPathSegment();
                String where = MovieDatabaseHelper.COLUMN_ID + " = " + id;

                if (!TextUtils.isEmpty(selection)) {
                    where += " AND " + selection;
                }

                updateCount = sqlDB.update(MovieDatabaseHelper.TABLE_NAME, values, where, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return updateCount;
    }
}

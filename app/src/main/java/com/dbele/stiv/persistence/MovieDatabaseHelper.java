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

    public static final String TABLE_NAME = "movie";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_DIRECTOR= "director";
    public static final String COLUMN_ACTORS = "actors";
    public static final String COLUMN_LENGTH = "length";
    public static final String COLUMN_GENRE = "genre";
    public static final String COLUMN_WATCHED_DATE = "watched_date";
    public static final String COLUMN_PICTURE_PATH = "picture_path";
    public static final String COLUMN_TICKET_PATH = "ticket_path";
    public static final String COLUMN_IMPRESSIONS = "impressions";
    private static final String DROP_TABLE_STMT = "drop table " + TABLE_NAME;
    private static String CREATE_TABLE_STMT = null;

    public static final String SELECTION_NAME = COLUMN_NAME + " like ?";

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


}

package com.example.submission5.myDatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.submission5.myDatabase.DatabaseContract.MovieFavColumns.MOVIE_TABLE_NAME;
import static com.example.submission5.myDatabase.DatabaseContract.TVFavColumns.TV_TABLE_NAME;

/**
 * @author zulkarnaen
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    /*Create Database name and version*/
    private static final String DATABASE_NAME = "submission_5_zulka";
    private static final int DATABASE_VERSION = 1;
    /*if you change database you must change database version*/

    /*some table in movie favorite*/
    private static final String SQL_CREATE_TABLE_MOVIE_FAVORITE = String.format("CREATE TABLE %s"
                    + " (%s INTEGER PRIMARY KEY," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL)",
            MOVIE_TABLE_NAME,
            DatabaseContract.MovieFavColumns._ID,
            DatabaseContract.MovieFavColumns.MOVIE_TITLE,
            DatabaseContract.MovieFavColumns.MOVIE_VOTE_AVERAGE,
            DatabaseContract.MovieFavColumns.MOVIE_VOTE_COUNT,
            DatabaseContract.MovieFavColumns.MOVIE_FIRST_AIR_DATE,
            DatabaseContract.MovieFavColumns.MOVIE_OVERVIEW,
            DatabaseContract.MovieFavColumns.MOVIE_PHOTO
    );

    /*some table in tv show favorite*/
    private static final String SQL_CREATE_TABLE_TV_SHOW_FAVORITE = String.format(
            " CREATE TABLE %s" +
                    " (%s INTEGER PRIMARY KEY," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL)",
            TV_TABLE_NAME,
            DatabaseContract.TVFavColumns._ID,
            DatabaseContract.TVFavColumns.TV_TITLE,
            DatabaseContract.TVFavColumns.TV_VOTE_AVERAGE,
            DatabaseContract.TVFavColumns.TV_VOTE_COUNT,
            DatabaseContract.TVFavColumns.TV_FIRST_AIR_DATE,
            DatabaseContract.TVFavColumns.TV_OVERVIEW,
            DatabaseContract.TVFavColumns.TV_PHOTO
    );

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /*Create table*/
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_MOVIE_FAVORITE);
        db.execSQL(SQL_CREATE_TABLE_TV_SHOW_FAVORITE);

    }

    /*use this for drop table*/
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MOVIE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TV_TABLE_NAME);
        onCreate(db);
    }
}

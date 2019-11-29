package com.example.submission5.database;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * @author zulkarnaen
 */
public class DatabaseContract {
    /*Set my database for movies*/
    public static final class MovieFavColumns implements BaseColumns {
        public static final String MOVIE_TABLE_NAME = "movie_favorites_submission_5";
        public static final String MOVIE_TITLE = "movie_title";
        public static final String MOVIE_VOTE_AVARAGE = "movie_averages";
        public static final String MOVIE_VOTE_COUNT = "movie_counts";
        public static final String MOVIE_FIRST_AIR_DATE = "movie_first_air_dates";
        public static final String MOVIE_OVERVIEW = "movie_overview";
        public static final String MOVIE_PHOTO = "movie_photo";

    }
    /*Set my database for TV Show*/
    static final class TVFavColumns implements BaseColumns {
        static final String TV_TABLE_NAME = "tv_favorites_2";
        static final String TV_TITLE = "tv_title";
        static final String TV_VOTE_AVARAGE = "tv_averages";
        static final String TV_VOTE_COUNT = "tv_counts";
        static final String TV_FIRST_AIR_DATE = "tv_first_air_dates";
        static final String TV_OVERVIEW = "tv_overview";
        static final String TV_PHOTO = "tv_photo";


    }

    /*Create authority and content uri*/
    public static final String AUTHORITY = "com.example.submission5";
    public static final Uri CONTENT_URI = new Uri.Builder().scheme("content")
            .authority(AUTHORITY)
            .appendPath(MovieFavColumns.MOVIE_TABLE_NAME)
            .build();

    /*get all column string when data is save to database*/
    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

}

package com.example.myfavorit.database;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * @author zulkarnaen
 */
public class DatabaseContract {
    public static final class MovieFavColumns implements BaseColumns {
        static final String MOVIE_TABLE_NAME = "movie_favorites_submission_5";
        public static final String MOVIE_TITLE = "movie_title";
        public static final String MOVIE_OVERVIEW = "movie_overview";
        public static final String MOVIE_PHOTO = "movie_photo";

    }

    private static final String AUTHORITY = "com.example.submission5";
    public static final Uri CONTENT_URI = new Uri.Builder().scheme("content")
            .authority(AUTHORITY)
            .appendPath(MovieFavColumns.MOVIE_TABLE_NAME)
            .build();

    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

}

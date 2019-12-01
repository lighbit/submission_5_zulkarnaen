package com.example.submission5.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import com.example.submission5.model.Movies;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.example.submission5.database.DatabaseContract.CONTENT_URI;
import static com.example.submission5.database.DatabaseContract.MovieFavColumns.MOVIE_FIRST_AIR_DATE;
import static com.example.submission5.database.DatabaseContract.MovieFavColumns.MOVIE_OVERVIEW;
import static com.example.submission5.database.DatabaseContract.MovieFavColumns.MOVIE_PHOTO;
import static com.example.submission5.database.DatabaseContract.MovieFavColumns.MOVIE_TABLE_NAME;
import static com.example.submission5.database.DatabaseContract.MovieFavColumns.MOVIE_TITLE;
import static com.example.submission5.database.DatabaseContract.MovieFavColumns.MOVIE_VOTE_AVERAGE;
import static com.example.submission5.database.DatabaseContract.MovieFavColumns.MOVIE_VOTE_COUNT;

/**
 * @author zulkarnaen
 */
public class MovieFavoriteHelper {

    @SuppressLint("StaticFieldLeak")
    private static MovieFavoriteHelper INSTANCE;
    private static final String DATABASE_TABLE = MOVIE_TABLE_NAME;
    private static DatabaseHelper dataBaseHelper;
    private Context context;
    private static SQLiteDatabase database;

    /*create constructor*/
    public MovieFavoriteHelper(Context context) {
        this.context = context;
        dataBaseHelper = new DatabaseHelper(context);
    }

    /*getInstance*/
    public static MovieFavoriteHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new MovieFavoriteHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    /*open database for databaseHandler*/
    public void openDatabase() throws SQLException {
        database = dataBaseHelper.getWritableDatabase();
    }

    /*close database*/
    public void closeDatabase() {
        dataBaseHelper.close();

        if (database.isOpen())
            database.close();
    }

    /*get all movies favorite Data*/
    public ArrayList<Movies> getAllMoviesFavorite() {
        ArrayList<Movies> arrayList = new ArrayList<>();
        Cursor cursor = database.query(DATABASE_TABLE, null,
                null,
                null,
                null,
                null,
                _ID + " ASC",
                null);
        cursor.moveToFirst();
        Movies movieFavorite;
        if (cursor.getCount() > 0) {
            do {
                movieFavorite = new Movies();
                movieFavorite.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                movieFavorite.setOriginal_title(cursor.getString(cursor.getColumnIndexOrThrow(MOVIE_TITLE)));
                movieFavorite.setVote_average(cursor.getString(cursor.getColumnIndexOrThrow(MOVIE_VOTE_AVERAGE)));
                movieFavorite.setVote_count(cursor.getString(cursor.getColumnIndexOrThrow(MOVIE_VOTE_COUNT)));
                movieFavorite.setRelease_date(cursor.getString(cursor.getColumnIndexOrThrow(MOVIE_FIRST_AIR_DATE)));
                movieFavorite.setOverview(cursor.getString(cursor.getColumnIndexOrThrow(MOVIE_OVERVIEW)));
                movieFavorite.setPoster_path(cursor.getString(cursor.getColumnIndexOrThrow(MOVIE_PHOTO)));

                arrayList.add(movieFavorite);
                cursor.moveToNext();

            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    /*Insert Data to the database*/
    public long insertIntoMovie(Movies movieFavorite) {

        try {
            ContentValues args = new ContentValues();
            args.put(_ID, movieFavorite.getId());
            args.put(MOVIE_TITLE, movieFavorite.getOriginal_title());
            args.put(MOVIE_VOTE_AVERAGE, movieFavorite.getVote_average());
            args.put(MOVIE_VOTE_COUNT, movieFavorite.getVote_count());
            args.put(MOVIE_FIRST_AIR_DATE, movieFavorite.getRelease_date());
            args.put(MOVIE_OVERVIEW, movieFavorite.getOverview());
            args.put(MOVIE_PHOTO, movieFavorite.getPoster_path());

            return database.insert(DATABASE_TABLE, null, args);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    /*Delete movies when you don't want that movie again*/
    public long deleteIntoMovie(int id) {

        Uri uri = CONTENT_URI;
        uri = uri.buildUpon().appendPath(String.valueOf(id)).build();
        return context.getContentResolver().delete(uri, null, null);
    }

    /*query data if wan to DESCENDING*/
    public Cursor queryDescProvider() {
        return database.query(
                DATABASE_TABLE,
                null,
                null,
                null,
                null,
                null, _ID + " DESC"
        );
    }

    /*call by id*/
    public Cursor queryProviderByID(String id) {
        return database.query(DATABASE_TABLE, null
                , _ID + " = ?"
                , new String[]{id}
                , null
                , null
                , null
                , null);
    }

    /*Insert Provide*/
    public long insertProvider(ContentValues values) {
        return database.insert(DATABASE_TABLE, null, values);
    }
    /*Update Provide*/
    public int updateProvider(String id, ContentValues values) {
        return database.update(DATABASE_TABLE, values, _ID + " = ?", new String[]{id});
    }
    /*Delete Provide*/
    public int deleteProvider(String id) {
        return database.delete(DATABASE_TABLE, _ID + " = ?", new String[]{id});
    }

}

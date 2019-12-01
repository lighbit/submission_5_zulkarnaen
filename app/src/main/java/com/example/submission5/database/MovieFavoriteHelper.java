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
    private static DatabaseHelper myDatabaseHelper;
    private Context myContext;
    private static SQLiteDatabase myDatabase;

    /*create constructor*/
    public MovieFavoriteHelper(Context myContext) {
        this.myContext = myContext;
        myDatabaseHelper = new DatabaseHelper(myContext);
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

    /*openMyDatabase myDatabase for databaseHandler*/
    public void openDatabase() throws SQLException {
        myDatabase = myDatabaseHelper.getWritableDatabase();
    }

    /*get all movies favorite Data*/
    public ArrayList<Movies> getAllMoviesFavorite() {
        ArrayList<Movies> arrayList = new ArrayList<>();
        Cursor cursor = myDatabase.query(DATABASE_TABLE, null,
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

    /*Insert Data to the myDatabase*/
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

            return myDatabase.insert(DATABASE_TABLE, null, args);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    /*Delete movies when you don't want that movie again*/
    public long deleteIntoMovie(int id) {

        Uri uri = CONTENT_URI;
        uri = uri.buildUpon().appendPath(String.valueOf(id)).build();
        return myContext.getContentResolver().delete(uri, null, null);
    }

    /*query data if wan to DESCENDING*/
    public Cursor queryIntoDescProvider() {
        return myDatabase.query(
                DATABASE_TABLE,
                null,
                null,
                null,
                null,
                null, _ID + " DESC"
        );
    }

    /*call by id*/
    public Cursor queryIntoProviderByID(String id) {
        return myDatabase.query(DATABASE_TABLE, null
                , _ID + " = ?"
                , new String[]{id}
                , null
                , null
                , null
                , null);
    }

    /*Insert Provide*/
    public long insertIntoProvider(ContentValues values) {
        return myDatabase.insert(DATABASE_TABLE, null, values);
    }
    /*Update Provide*/
    public int updateIntoProvider(String id, ContentValues values) {
        return myDatabase.update(DATABASE_TABLE, values, _ID + " = ?", new String[]{id});
    }
    /*Delete Provide*/
    public int deleteIntoProvider(String id) {
        return myDatabase.delete(DATABASE_TABLE, _ID + " = ?", new String[]{id});
    }

}

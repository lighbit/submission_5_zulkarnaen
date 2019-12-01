package com.example.submission5.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.example.submission5.model.TvShow;

import java.util.ArrayList;

import static android.provider.UserDictionary.Words._ID;
import static com.example.submission5.database.DatabaseContract.TVFavColumns.TV_FIRST_AIR_DATE;
import static com.example.submission5.database.DatabaseContract.TVFavColumns.TV_OVERVIEW;
import static com.example.submission5.database.DatabaseContract.TVFavColumns.TV_PHOTO;
import static com.example.submission5.database.DatabaseContract.TVFavColumns.TV_TABLE_NAME;
import static com.example.submission5.database.DatabaseContract.TVFavColumns.TV_TITLE;
import static com.example.submission5.database.DatabaseContract.TVFavColumns.TV_VOTE_AVERAGE;
import static com.example.submission5.database.DatabaseContract.TVFavColumns.TV_VOTE_COUNT;


/**
 * @author zulkarnaen
 */
public class TVShowFavoriteHelper {
    private static final String DATABASE_TABLE = TV_TABLE_NAME;
    private static DatabaseHelper myDatabaseHelper;
    private static TVShowFavoriteHelper INSTANCE;

    private static SQLiteDatabase myDatabase;

    private TVShowFavoriteHelper(Context context) {
        myDatabaseHelper = new DatabaseHelper(context);
    }

    public static TVShowFavoriteHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new TVShowFavoriteHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    public void openMyDatabase() throws SQLException {
        myDatabase = myDatabaseHelper.getWritableDatabase();
    }

    public ArrayList<TvShow> getAllTvFavorite() {
        ArrayList<TvShow> arrayList = new ArrayList<>();
        Cursor cursor = myDatabase.query(DATABASE_TABLE, null,
                null,
                null,
                null,
                null,
                _ID + " ASC",
                null);
        cursor.moveToFirst();
        TvShow tvShowFavorite;
        if (cursor.getCount() > 0) {
            do {
                tvShowFavorite = new TvShow();
                tvShowFavorite.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                tvShowFavorite.setOriginal_name(cursor.getString(cursor.getColumnIndexOrThrow(TV_TITLE)));
                tvShowFavorite.setVote_average(cursor.getString(cursor.getColumnIndexOrThrow(TV_VOTE_AVERAGE)));
                tvShowFavorite.setVote_count(cursor.getString(cursor.getColumnIndexOrThrow(TV_VOTE_COUNT)));
                tvShowFavorite.setFirst_air_date(cursor.getString(cursor.getColumnIndexOrThrow(TV_FIRST_AIR_DATE)));
                tvShowFavorite.setPoster_path(cursor.getString(cursor.getColumnIndexOrThrow(TV_PHOTO)));
                tvShowFavorite.setOverview(cursor.getString(cursor.getColumnIndexOrThrow(TV_OVERVIEW)));

                arrayList.add(tvShowFavorite);
                cursor.moveToNext();

            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public long insertIntoTvShow(TvShow tvShowFavorite) {
        ContentValues args = new ContentValues();
        args.put(BaseColumns._ID, tvShowFavorite.getId());
        args.put(TV_TITLE, tvShowFavorite.getOriginal_name());
        args.put(TV_VOTE_AVERAGE, tvShowFavorite.getVote_average());
        args.put(TV_VOTE_COUNT, tvShowFavorite.getVote_count());
        args.put(TV_FIRST_AIR_DATE, tvShowFavorite.getFirst_air_date());
        args.put(TV_PHOTO, tvShowFavorite.getPoster_path());
        args.put(TV_OVERVIEW, tvShowFavorite.getOverview());
        return myDatabase.insert(DATABASE_TABLE, null, args);
    }

    public int deleteIntoTvShow(int id) {
        return myDatabase.delete(TV_TABLE_NAME, _ID + " = '" + id + "'", null);
    }


}

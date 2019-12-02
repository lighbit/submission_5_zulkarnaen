package com.example.submission5.myModel;

import android.database.Cursor;

import com.google.gson.annotations.SerializedName;

import static android.provider.BaseColumns._ID;
import static com.example.submission5.myDatabase.DatabaseContract.MovieFavColumns.MOVIE_PHOTO;
import static com.example.submission5.myDatabase.DatabaseContract.MovieFavColumns.MOVIE_TITLE;
import static com.example.submission5.myDatabase.DatabaseContract.getInt;
import static com.example.submission5.myDatabase.DatabaseContract.getString;

/**
 * @author zulkarnaen
 */
public class Notification {


    @SerializedName("original_title")
    private String original_title;
    @SerializedName("poster_path")
    private String poster_path;
    @SerializedName("id")
    private int id;

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }


    public Notification() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Notification(Cursor cursor) {
        this.id = getInt(cursor, _ID);
        this.original_title = getString(cursor, MOVIE_TITLE);
        this.poster_path = getString(cursor, MOVIE_PHOTO);
    }
}

package com.example.submission5.model;

import android.database.Cursor;

import com.google.gson.annotations.SerializedName;

import static android.provider.BaseColumns._ID;
import static com.example.submission5.database.DatabaseContract.MovieFavColumns.MOVIE_PHOTO;
import static com.example.submission5.database.DatabaseContract.MovieFavColumns.MOVIE_TITLE;
import static com.example.submission5.database.DatabaseContract.getInt;
import static com.example.submission5.database.DatabaseContract.getString;

/**
 * @author zulkarnaen
 */
public class ResultsItem {


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


    public ResultsItem() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ResultsItem(Cursor cursor) {
        this.id = getInt(cursor, _ID);
        this.original_title = getString(cursor, MOVIE_TITLE);
        this.poster_path = getString(cursor, MOVIE_PHOTO);
    }
}

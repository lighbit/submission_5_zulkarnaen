package com.example.submission5.model;

import android.database.Cursor;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

import static com.example.submission5.database.DatabaseContract.MovieFavColumns.MOVIE_FIRST_AIR_DATE;
import static com.example.submission5.database.DatabaseContract.MovieFavColumns.MOVIE_OVERVIEW;
import static com.example.submission5.database.DatabaseContract.MovieFavColumns.MOVIE_PHOTO;
import static com.example.submission5.database.DatabaseContract.MovieFavColumns.MOVIE_TITLE;
import static com.example.submission5.database.DatabaseContract.MovieFavColumns.MOVIE_VOTE_AVARAGE;
import static com.example.submission5.database.DatabaseContract.MovieFavColumns.MOVIE_VOTE_COUNT;
import static com.example.submission5.database.DatabaseContract.getColumnString;

/**
 * @author zulkarnaen
 */
public class ResultsItem {


    @SerializedName("original_title")
    private String original_title;

    @SerializedName("vote_average")
    private String vote_average;

    @SerializedName("release_date")
    private String release_date;

    @SerializedName("poster_path")
    private String poster_path;

    @SerializedName("vote_count")
    private String vote_count;

    @SerializedName("overview")
    private String overview;

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public void setVote_average(String vote_average) {
        this.vote_average = vote_average;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public void setVote_count(String vote_count) {
        this.vote_count = vote_count;
    }

    public ResultsItem() {
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public ResultsItem(Cursor cursor) {
        this.original_title = getColumnString(cursor, MOVIE_TITLE);
        this.vote_average = getColumnString(cursor, MOVIE_VOTE_AVARAGE);
        this.vote_count = getColumnString(cursor, MOVIE_VOTE_COUNT);
        this.overview = getColumnString(cursor, MOVIE_OVERVIEW);
        this.poster_path = getColumnString(cursor, MOVIE_PHOTO);
        this.release_date = getColumnString(cursor, MOVIE_FIRST_AIR_DATE);
    }

    @NotNull
    @Override
    public String toString() {
        return
                "ResultsItem{" +
                        "overview = '" + overview + '\'' +
                        ",original_title = '" + original_title + '\'' +
                        ",poster_path = '" + poster_path + '\'' +
                        ",vote_average = '" + vote_average + '\'' +
                        ",vote_count = '" + vote_count + '\'' +
                        ",release_date = '" + release_date + '\'' +
                        "}";
    }

}

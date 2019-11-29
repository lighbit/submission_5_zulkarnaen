package com.example.submission5.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author zulkarnaen
 */
public class Movies implements Parcelable {

    private int id;
    private boolean isOnfavorites;

    private String original_title;
    private String vote_average;
    private String overview;
    private String release_date;
    private String poster_path;
    private String vote_count;

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getVote_average() {
        return vote_average;
    }

    public void setVote_average(String vote_average) {
        this.vote_average = vote_average;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getVote_count() {
        return vote_count;
    }

    public void setVote_count(String vote_count) {
        this.vote_count = vote_count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public boolean isOnfavorites() {
        return isOnfavorites;
    }

    public void setOnfavorites(boolean onfavorites) {
        isOnfavorites = onfavorites;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.original_title);
        dest.writeString(this.vote_average);
        dest.writeString(this.overview);
        dest.writeString(this.release_date);
        dest.writeString(this.poster_path);
        dest.writeString(this.vote_count);
        dest.writeByte(this.isOnfavorites ? (byte) 1 : (byte) 0);
    }

    public Movies() {

    }

    public Movies(JSONObject object) {

        try {
            this.id = object.getInt("id");
            this.original_title = object.getString("original_title");
            this.vote_average = object.getString("vote_average");
            this.overview = object.getString("overview");
            this.release_date = object.getString("release_date");
            this.poster_path = object.getString("poster_path");
            this.vote_count = object.getString("vote_count");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected Movies(Parcel in) {
        this.id = in.readInt();
        this.original_title = in.readString();
        this.vote_average = in.readString();
        this.overview = in.readString();
        this.release_date = in.readString();
        this.poster_path = in.readString();
        this.vote_count = in.readString();
        this.isOnfavorites = in.readByte() != 0;
    }

    public static final Creator<Movies> CREATOR = new Creator<Movies>() {
        @Override
        public Movies createFromParcel(Parcel source) {
            return new Movies(source);
        }

        @Override
        public Movies[] newArray(int size) {
            return new Movies[size];
        }
    };
}

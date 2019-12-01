package com.example.submission5.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.submission5.R;
import com.example.submission5.database.MovieFavoriteHelper;

import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import static com.example.submission5.database.DatabaseContract.AUTHORITY;
import static com.example.submission5.database.DatabaseContract.CONTENT_URI;
import static com.example.submission5.database.DatabaseContract.MovieFavColumns.MOVIE_TABLE_NAME;

/**
 * @author zulkarnaen
 */
public class MoviesProvider extends ContentProvider {
    private static final int MY_MOVIE = 1;
    private static final int MY_MOVIE_ID = 2;
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private MovieFavoriteHelper myMoviesHelper;

    /* CREATE KECOCOKAN URI UNTUK AUTHORITY*/
    static {
        uriMatcher.addURI(AUTHORITY, MOVIE_TABLE_NAME, MY_MOVIE);
        uriMatcher.addURI(AUTHORITY, MOVIE_TABLE_NAME + "/#", MY_MOVIE_ID);
    }


    @Override
    public boolean onCreate() {
        new MovieFavoriteHelper(getContext());
        myMoviesHelper = new MovieFavoriteHelper(getContext());
        myMoviesHelper.openDatabase();
        return false;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }


    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;
        int matcherTest = uriMatcher.match(uri);
        switch (matcherTest) {
            case MY_MOVIE:
                cursor = myMoviesHelper.queryDescProvider();
                break;
            case MY_MOVIE_ID:
                cursor = myMoviesHelper.queryProviderByID(uri.getLastPathSegment());
                break;
            default:
                cursor = null;
                break;
        }

        if (cursor != null) {
            cursor.setNotificationUri(Objects.requireNonNull(getContext()).getContentResolver(), uri);
        }

        return cursor;
    }


    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        long addedInto;

        if (uriMatcher.match(uri) == MY_MOVIE) {
            addedInto = myMoviesHelper.insertProvider(values);
        } else {
            addedInto = 0;
        }
        if (addedInto > 0) {
            Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);
        }
        return Uri.parse(CONTENT_URI + "/" + addedInto);
    }


    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int movieUpdated;
        if (uriMatcher.match(uri) == MY_MOVIE_ID) {
            movieUpdated = myMoviesHelper.updateProvider(uri.getLastPathSegment(), values);
        } else {
            movieUpdated = 0;
        }

        if (movieUpdated > 0) {
            Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);
        }
        return movieUpdated;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int movieDeleted;

        int match = uriMatcher.match(uri);
        if (match == MY_MOVIE_ID) {
            movieDeleted = myMoviesHelper.deleteProvider(uri.getLastPathSegment());
            Log.v(Objects.requireNonNull(getContext()).getString(R.string.app_name_movie), "" + movieDeleted);
        } else {
            movieDeleted = 0;
        }

        if (movieDeleted > 0) {
            Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);
        }
        return movieDeleted;
    }
}

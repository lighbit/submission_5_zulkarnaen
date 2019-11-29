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
    private static final int MOVIE = 1;
    private static final int MOVIE_ID = 2;
    private static final UriMatcher matcherURI = new UriMatcher(UriMatcher.NO_MATCH);
    private MovieFavoriteHelper movieHelper;

    /* CREATE KECOCOKAN URI UNTUK AUTHORITY*/
    static {
        matcherURI.addURI(AUTHORITY, MOVIE_TABLE_NAME, MOVIE);
        matcherURI.addURI(AUTHORITY, MOVIE_TABLE_NAME + "/#", MOVIE_ID);
    }


    @Override
    public boolean onCreate() {
        new MovieFavoriteHelper(getContext());
        movieHelper = new MovieFavoriteHelper(getContext());
        movieHelper.openDatabase();
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
        int matcherTest = matcherURI.match(uri);
        switch (matcherTest) {
            case MOVIE:
                cursor = movieHelper.queryDescProvider();
                break;
            case MOVIE_ID:
                cursor = movieHelper.queryProviderByID(uri.getLastPathSegment());
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

        if (matcherURI.match(uri) == MOVIE) {
            addedInto = movieHelper.insertProvider(values);
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
        if (matcherURI.match(uri) == MOVIE_ID) {
            movieUpdated = movieHelper.updateProvider(uri.getLastPathSegment(), values);
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

        int match = matcherURI.match(uri);
        if (match == MOVIE_ID) {
            movieDeleted = movieHelper.deleteProvider(uri.getLastPathSegment());
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

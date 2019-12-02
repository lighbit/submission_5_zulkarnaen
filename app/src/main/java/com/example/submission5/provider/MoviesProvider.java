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
    private MovieFavoriteHelper myMoviesHelper;
    private static final UriMatcher myUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    /* CREATE KECOCOKAN URI UNTUK AUTHORITY*/
    static {
        myUriMatcher.addURI(AUTHORITY, MOVIE_TABLE_NAME, MY_MOVIE);
        myUriMatcher.addURI(AUTHORITY, MOVIE_TABLE_NAME + "/#", MY_MOVIE_ID);
    }

    @Override
    public boolean onCreate() {
        new MovieFavoriteHelper(getContext());
        myMoviesHelper = new MovieFavoriteHelper(getContext());
        myMoviesHelper.openMoviesDatabase();
        return false;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }


    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String mySelection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;
        int myMatcherTest = myUriMatcher.match(uri);
        switch (myMatcherTest) {
            case MY_MOVIE:
                cursor = myMoviesHelper.queryIntoDescProvider();
                break;
            case MY_MOVIE_ID:
                cursor = myMoviesHelper.queryIntoProviderByID(uri.getLastPathSegment());
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

        if (myUriMatcher.match(uri) == MY_MOVIE) {
            addedInto = myMoviesHelper.insertIntoProvider(values);
        } else {
            addedInto = 0;
        }
        if (addedInto > 0) {
            Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);
        }
        return Uri.parse(CONTENT_URI + "/" + addedInto);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String mySelection, @Nullable String[] selectionArgs) {
        int myMovieUpdates;
        if (myUriMatcher.match(uri) == MY_MOVIE_ID) {
            myMovieUpdates = myMoviesHelper.updateIntoProvider(uri.getLastPathSegment(), values);
        } else {
            myMovieUpdates = 0;
        }

        if (myMovieUpdates > 0) {
            Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);
        }
        return myMovieUpdates;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String mySelection, @Nullable String[] selectionArgs) {
        int myMovieDeletes;

        int match = myUriMatcher.match(uri);
        if (match == MY_MOVIE_ID) {
            myMovieDeletes = myMoviesHelper.deleteIntoProvider(uri.getLastPathSegment());
            Log.v(Objects.requireNonNull(getContext()).getString(R.string.app_name_movie), "" + myMovieDeletes);
        } else {
            myMovieDeletes = 0;
        }

        if (myMovieDeletes > 0) {
            Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);
        }
        return myMovieDeletes;
    }
}

package com.example.myfavorit.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myfavorit.R;
import com.squareup.picasso.Picasso;

import static com.example.myfavorit.database.DatabaseContract.MovieFavColumns.MOVIE_OVERVIEW;
import static com.example.myfavorit.database.DatabaseContract.MovieFavColumns.MOVIE_PHOTO;
import static com.example.myfavorit.database.DatabaseContract.MovieFavColumns.MOVIE_TITLE;
import static com.example.myfavorit.database.DatabaseContract.getColumnString;

/**
 * @author zulkarnaen
 */
public class MovieAdapter extends CursorAdapter {

    public MovieAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        return view;
    }

    @Override
    public Cursor getCursor() {
        return super.getCursor();
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        if (cursor != null) {
            TextView textViewTitle, txtvote_count, release_date;
            TextView textViewOverview;
            ImageView imgPoster;

            textViewTitle = view.findViewById(R.id.tv_title);
            imgPoster = view.findViewById(R.id.img_poster);
            textViewOverview = view.findViewById(R.id.tv_description);
            textViewTitle.setText(getColumnString(cursor, MOVIE_TITLE));
            textViewOverview.setText(getColumnString(cursor, MOVIE_OVERVIEW));
            Picasso.with(context).load("https://image.tmdb.org/t/p/original" + getColumnString(cursor, MOVIE_PHOTO)).placeholder(R.drawable.ic_launcher_foreground).error(R.drawable.ic_launcher_background).into(imgPoster);


        }

    }
}

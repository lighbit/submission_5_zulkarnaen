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

import static com.example.myfavorit.database.DatabaseContract.MovieFavColumns.MOVIE_FIRST_AIR_DATE;
import static com.example.myfavorit.database.DatabaseContract.MovieFavColumns.MOVIE_OVERVIEW;
import static com.example.myfavorit.database.DatabaseContract.MovieFavColumns.MOVIE_PHOTO;
import static com.example.myfavorit.database.DatabaseContract.MovieFavColumns.MOVIE_TITLE;
import static com.example.myfavorit.database.DatabaseContract.MovieFavColumns.MOVIE_VOTE_AVERAGE;
import static com.example.myfavorit.database.DatabaseContract.MovieFavColumns.MOVIE_VOTE_COUNT;
import static com.example.myfavorit.database.DatabaseContract.getString;

/**
 * @author zulkarnaen
 */
public class MoviesAdapter extends CursorAdapter {

    /*constructor*/
    public MoviesAdapter(Context myContext, Cursor myCursor, boolean myQuery) {
        super(myContext, myCursor, myQuery);
    }

    /*set layout*/
    @Override
    public View newView(Context myContext, Cursor myCursor, ViewGroup myViewGroup) {
        return LayoutInflater.from(myContext).inflate(R.layout.item_movies, myViewGroup, false);
    }

    /*layout will be set in here*/
    @Override
    public void bindView(View myView, Context myContext, Cursor myCursor) {

        if (myCursor != null) {
            TextView original_title, overview, release_date, vote_count, vote_average, suggest;
            ImageView poster_path;

            /*Choose Layer*/
            original_title = myView.findViewById(R.id.txt_name);
            vote_count = myView.findViewById(R.id.txt_genres);
            release_date = myView.findViewById(R.id.txt_tahun);
            poster_path = myView.findViewById(R.id.img_photo);
            overview = myView.findViewById(R.id.txt_description);
            vote_average = myView.findViewById(R.id.rate);
            suggest = myView.findViewById(R.id.suggest);

            String countSuggest = getString(myCursor, MOVIE_VOTE_AVERAGE);

            /*Set from database into view Text and image*/
            original_title.setText(getString(myCursor, MOVIE_TITLE));
            vote_count.setText(getString(myCursor, MOVIE_VOTE_COUNT));
            release_date.setText(getString(myCursor, MOVIE_FIRST_AIR_DATE));
            overview.setText(getString(myCursor, MOVIE_OVERVIEW));
            Picasso.with(myContext).load("https://image.tmdb.org/t/p/original" + getString(myCursor, MOVIE_PHOTO)).placeholder(R.drawable.ic_launcher_foreground).error(R.drawable.ic_launcher_background).into(poster_path);
            vote_average.setText(getString(myCursor, MOVIE_VOTE_AVERAGE));

            /*Create Some logic here*/
            if (countSuggest.startsWith("5") || countSuggest.startsWith("6")) {
                suggest.setText(myContext.getString(R.string.bad));
            } else if (countSuggest.startsWith("7") || countSuggest.startsWith("8")) {
                suggest.setText(myContext.getString(R.string.good));
            } else if (countSuggest.startsWith("9") || countSuggest.startsWith("10")) {
                suggest.setText(myContext.getString(R.string.awesome));
            } else {
                suggest.setText(myContext.getString(R.string.realy_bad));
            }
        }

    }
}

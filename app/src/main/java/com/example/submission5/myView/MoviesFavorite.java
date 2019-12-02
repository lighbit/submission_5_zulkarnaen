package com.example.submission5.myView;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.submission5.R;
import com.example.submission5.myDatabase.MovieFavoriteHelper;
import com.example.submission5.myModel.Movies;
import com.example.submission5.myPresenter.MoviesAdapter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * @author zulkarnaen
 */
public class MoviesFavorite extends AppCompatActivity {
    private ArrayList<Movies> listMovie = new ArrayList<>();


    /*Create on Create when Favorite call*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home);

        /*Open Database*/
        MovieFavoriteHelper myMovieFavoriteHelper = MovieFavoriteHelper.getInstance(getApplicationContext());
        myMovieFavoriteHelper.openMoviesDatabase();

        /*Create Array listMovie*/
        WeakReference<MovieFavoriteHelper> myWeakReference = new WeakReference<>(myMovieFavoriteHelper);
        ArrayList<Movies> myArrayList = myWeakReference.get().getAllMoviesFavorite();

        /*set array list to list*/
        listMovie.addAll(myArrayList);

        RecyclerView rvCategory = findViewById(R.id.rv_category);
        rvCategory.setHasFixedSize(true);

        /*Set new layout*/
        rvCategory.setLayoutManager(new LinearLayoutManager(this));
        MoviesAdapter movieAdapter = new MoviesAdapter(this);
        movieAdapter.setWordMovies(getResources().getString(R.string.choose));
        movieAdapter.setDataMovies(listMovie);
        movieAdapter.isOnFavoriteMovies(true);
        rvCategory.setAdapter(movieAdapter);

    }
}

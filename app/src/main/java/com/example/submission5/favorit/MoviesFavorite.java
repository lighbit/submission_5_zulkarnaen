package com.example.submission5.favorit;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.submission5.R;
import com.example.submission5.database.MovieFavoriteHelper;
import com.example.submission5.model.Movies;
import com.example.submission5.presenter.MoviesAdapter;

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
        MovieFavoriteHelper movieFavoriteHelper = MovieFavoriteHelper.getInstance(getApplicationContext());
        movieFavoriteHelper.openDatabase();

        /*Create Array listMovie*/
        WeakReference<MovieFavoriteHelper> weakReference = new WeakReference<>(movieFavoriteHelper);
        ArrayList<Movies> arrayList = weakReference.get().getAllMoviesFavorite();

        RecyclerView rvCategory = findViewById(R.id.rv_category);
        rvCategory.setHasFixedSize(true);


        listMovie.addAll(arrayList);

        /*Set new layout*/
        rvCategory.setLayoutManager(new LinearLayoutManager(this));
        MoviesAdapter movieAdapter = new MoviesAdapter(this);
        movieAdapter.setWordMovies(getResources().getString(R.string.choose));
        movieAdapter.setDataMovies(listMovie);
        movieAdapter.isOnFavoriteMovies(true);
        rvCategory.setAdapter(movieAdapter);

    }
}

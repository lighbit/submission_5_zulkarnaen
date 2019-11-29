package com.example.submission5.favorit;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.submission5.R;
import com.example.submission5.database.TVShowFavoriteHelper;
import com.example.submission5.model.TvShow;
import com.example.submission5.presenter.TvShowAdapter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * @author zulkarnaen
 */
public class TvShowFavorite extends AppCompatActivity {
    private ArrayList<TvShow> list = new ArrayList<>();

    /*Create on Create when Favorite call*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_movie_favorit);

        /*Open Database*/
        TVShowFavoriteHelper tvShowFavoriteHelper = TVShowFavoriteHelper.getInstance(getApplicationContext());
        tvShowFavoriteHelper.open();

        /*Create Array*/
        WeakReference<TVShowFavoriteHelper> weakReference = new WeakReference<>(tvShowFavoriteHelper);
        ArrayList<TvShow> arrayList = weakReference.get().getAllTvFavorite();

        RecyclerView rvCategory = findViewById(R.id.rv_category_favorite);
        rvCategory.setHasFixedSize(true);


        list.addAll(arrayList);

        /*Set new layout*/
        rvCategory.setLayoutManager(new LinearLayoutManager(this));
        TvShowAdapter tvAdapter = new TvShowAdapter(this);
        tvAdapter.setWord(getResources().getString(R.string.choose));
        tvAdapter.setDataTvshow(list);
        tvAdapter.isOnFavoriteTvShow(true);
        rvCategory.setAdapter(tvAdapter);

    }
}

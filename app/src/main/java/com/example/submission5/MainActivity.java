package com.example.submission5;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.submission5.favorit.MoviesFavorite;
import com.example.submission5.favorit.TvShowFavorite;
import com.example.submission5.notification.MenuSetting;
import com.example.submission5.view.MoviesFragment;
import com.example.submission5.view.SearchActivityMoviesFragment;
import com.example.submission5.view.SearchActivityTvShowFragment;
import com.example.submission5.view.TVShowFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

/**
 * @author zulkarnaen
 */
public class MainActivity extends AppCompatActivity {
    public static final String MY_INTENT_SEARCH = "intent_search";
    private int myFlagFavorite = 1;

    /*on Create with bottom navigation*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if (savedInstanceState == null) {
            navView.setSelectedItemId(R.id.navigation_movies);
        }
    }

    /*Set my navigation Item View*/
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_movies:
                    fragment = new MoviesFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.nav_host_fragment, fragment, fragment.getClass().getSimpleName())
                            .commit();
                    Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.app_name_movie);
                    myFlagFavorite = 1;
                    return true;
                case R.id.navigation_tv:
                    fragment = new TVShowFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.nav_host_fragment, fragment, fragment.getClass().getSimpleName())
                            .commit();
                    Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.app_name_tvshow);
                    myFlagFavorite = 2;
                    return true;
            }
            return false;
        }
    };

    /*when Create Menu*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                Fragment fragment;
                if (myFlagFavorite == 1) {
                    fragment = new SearchActivityMoviesFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString(MY_INTENT_SEARCH, query);

                    fragment.setArguments(bundle);

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.nav_host_fragment, fragment, fragment.getClass().getSimpleName())
                            .commit();

                } else if (myFlagFavorite == 2) {
                    fragment = new SearchActivityTvShowFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString(MY_INTENT_SEARCH, query);

                    fragment.setArguments(bundle);

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.nav_host_fragment, fragment, fragment.getClass().getSimpleName())
                            .commit();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    /*when Menu select*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_change_settings) {
            Intent mIntent = new Intent(this, MenuSetting.class);
            startActivity(mIntent);
        } else if (item.getItemId() == R.id.love) {
            if (myFlagFavorite == 1) {
                Intent intent = new Intent(this, MoviesFavorite.class);
                startActivity(intent);
                Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.app_name_movie);
                Toast.makeText(this, R.string.text_favorit_movie, Toast.LENGTH_SHORT).show();
            } else if (myFlagFavorite == 2) {
                Intent intent = new Intent(this, TvShowFavorite.class);
                startActivity(intent);
                Toast.makeText(this, R.string.text_favorit_tvShow, Toast.LENGTH_SHORT).show();
                Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.app_name_tvshow);
            }
        }
        return super.onOptionsItemSelected(item);
    }

}

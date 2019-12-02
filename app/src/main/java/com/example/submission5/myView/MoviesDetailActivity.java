package com.example.submission5.myView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.submission5.MainActivity;
import com.example.submission5.R;
import com.example.submission5.myDatabase.MovieFavoriteHelper;
import com.example.submission5.myModel.Movies;

/**
 * @author zulkarnaen
 */
public class MoviesDetailActivity extends AppCompatActivity implements View.OnClickListener {

    TextView original_title, vote_average, release_date, overview, vote_count;
    ImageView poster_path;
    String urlPhoto;
    Button btnSaveMovie, btnDeleteMovie;

    public static final String EXTRA_MOVIE = "extra_movie";
    public static final String EXTRA_MOVIE_FAVORITE = "extra_movie_favorite";
    public static final String EXTRA_POSITION_DEFAULT = "extra_position";

    private ProgressBar myProgressBar;
    private Movies myMovieFavorite;
    private MovieFavoriteHelper myMovieFavoriteHelper;
    private boolean isFavorite = false;
    public static final int ADD_RESULT = 101;
    private int flags, positionDefault;


    /*Create On Create*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_with_object);

        final Movies myMovies = getIntent().getParcelableExtra(EXTRA_MOVIE);
        positionDefault = getIntent().getIntExtra(EXTRA_POSITION_DEFAULT, 0);

        /*Open Database*/
        myMovieFavoriteHelper = MovieFavoriteHelper.getInstance(getApplicationContext());
        myMovieFavoriteHelper.openMoviesDatabase();

        myMovieFavorite = getIntent().getParcelableExtra(EXTRA_MOVIE_FAVORITE);

        /*if favorite is true*/
        assert myMovies != null;
        if (myMovies.isOnfavorites()) {

            /*Set to the layout*/
            myProgressBar = findViewById(R.id.progressDetailMovie);
            myProgressBar.setVisibility(View.INVISIBLE);

            original_title = findViewById(R.id.original_title);
            original_title.setText(myMovies.getOriginal_title());

            vote_average = findViewById(R.id.vote_average);
            vote_average.setText(myMovies.getVote_average());

            overview = findViewById(R.id.overview);
            overview.setText(myMovies.getOverview());

            release_date = findViewById(R.id.release_date);
            release_date.setText(myMovies.getRelease_date());

            vote_count = findViewById(R.id.vote_count);
            vote_count.setText(myMovies.getVote_count());

            String url = "https://image.tmdb.org/t/p/original" + myMovies.getPoster_path();
            poster_path = findViewById(R.id.poster_path);
            Glide.with(MoviesDetailActivity.this).load(url).into(poster_path);
            Glide.with(MoviesDetailActivity.this)
                    .load(url)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                        myProgressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(poster_path);

            btnSaveMovie = findViewById(R.id.btn_submits);
            btnSaveMovie.setOnClickListener(this);

            btnDeleteMovie = findViewById(R.id.btn_deletes);
            btnDeleteMovie.setOnClickListener(this);

            if (myMovies.isOnfavorites()) {
                flags = getIntent().getIntExtra(EXTRA_POSITION_DEFAULT, 0);
                isFavorite = true;
                btnSaveMovie.setVisibility(View.GONE);
            } else {
                myMovieFavorite = new Movies();
                btnDeleteMovie.setVisibility(View.GONE);
            }
        } else {

            final Handler handler = new Handler();
            new Thread(new Runnable() {
                public void run() {
                    try {
                        Thread.sleep(5000);
                    } catch (Exception ignored) {
                    }

                    handler.post(new Runnable() {
                        public void run() {

                            String url_image;
                            url_image = "https://image.tmdb.org/t/p/w185" + myMovies.getPoster_path();
                            urlPhoto = myMovies.getPoster_path();
                            original_title.setText(myMovies.getOriginal_title());
                            vote_average.setText(myMovies.getVote_average());
                            overview.setText(myMovies.getOverview());
                            release_date.setText(myMovies.getRelease_date());
                            vote_count.setText(myMovies.getVote_count());


                            Glide.with(MoviesDetailActivity.this)
                                    .load(url_image)
                                    .placeholder(R.color.colorAccent)
                                    .dontAnimate()
                                    .into(poster_path);
                            myProgressBar.setVisibility(View.INVISIBLE);

                        }
                    });
                }
            }).start();

            original_title = findViewById(R.id.original_title);
            vote_average = findViewById(R.id.vote_average);
            overview = findViewById(R.id.overview);
            release_date = findViewById(R.id.release_date);
            vote_count = findViewById(R.id.vote_count);
            poster_path = findViewById(R.id.poster_path);

            myProgressBar = findViewById(R.id.progressDetailMovie);
            myProgressBar.setVisibility(View.VISIBLE);

            btnSaveMovie = findViewById(R.id.btn_submits);
            btnSaveMovie.setOnClickListener(this);

            btnDeleteMovie = findViewById(R.id.btn_deletes);
            btnDeleteMovie.setOnClickListener(this);

            if (myMovies.isOnfavorites()) {
                flags = getIntent().getIntExtra(EXTRA_POSITION_DEFAULT, 0);
                isFavorite = true;
                btnSaveMovie.setVisibility(View.GONE);

            } else {
                myMovieFavorite = new Movies();
                btnDeleteMovie.setVisibility(View.GONE);
            }

        }

    }

    /*When Layout in click*/
    @Override
    public void onClick(View view) {

        /*When button is save*/
        if (view.getId() == R.id.btn_submits) {

            String title = original_title.getText().toString().trim();
            String vote = vote_average.getText().toString().trim();
            String release_dates = release_date.getText().toString().trim();
            String vote_counts = vote_count.getText().toString().trim();
            String overviews = overview.getText().toString().trim();
            String url_images = urlPhoto.trim();


            myMovieFavorite.setId(positionDefault);
            myMovieFavorite.setOriginal_title(title);
            myMovieFavorite.setVote_average(vote);
            myMovieFavorite.setVote_count(vote_counts);
            myMovieFavorite.setRelease_date(release_dates);
            myMovieFavorite.setOverview(overviews);
            myMovieFavorite.setPoster_path(url_images);

            Intent intent = new Intent();
            intent.putExtra(EXTRA_MOVIE_FAVORITE, myMovieFavorite);
            intent.putExtra(EXTRA_POSITION_DEFAULT, flags);

            /*if data not save earlier*/
            if (!isFavorite) {
                long result = myMovieFavoriteHelper.insertIntoMovie(myMovieFavorite);

                if (result > 0) {
                    myMovieFavorite.setId((int) result);
                    setResult(ADD_RESULT, intent);
                    Toast.makeText(MoviesDetailActivity.this, getString(R.string.succes_add_data), Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(MoviesDetailActivity.this, getString(R.string.failed_add_data), Toast.LENGTH_SHORT).show();
                }
            }

            /*When delete data*/
        } else if (view.getId() == R.id.btn_deletes) {
            myMovieFavoriteHelper = MovieFavoriteHelper.getInstance(getApplicationContext());
            long result = myMovieFavoriteHelper.deleteIntoMovie(positionDefault);
            if (result > 0) {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(MoviesDetailActivity.this, getString(R.string.notif_failed_delete), Toast.LENGTH_SHORT).show();
            }

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
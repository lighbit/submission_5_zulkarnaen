package com.example.submission5.view;

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
import com.example.submission5.database.MovieFavoriteHelper;
import com.example.submission5.model.Movies;

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
    public static final String EXTRA_POSITION = "extra_position";

    private ProgressBar progressBar;
    private Movies movieFavorite;
    private MovieFavoriteHelper movieFavoriteHelper;
    private boolean isFav = false;
    public static final int RESULT_ADD = 101;
    private int flag, position;


    /*Create On Create*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_with_object);

        final Movies myMovies = getIntent().getParcelableExtra(EXTRA_MOVIE);
        position = getIntent().getIntExtra(EXTRA_POSITION, 0);

        /*Open Database*/
        movieFavoriteHelper = MovieFavoriteHelper.getInstance(getApplicationContext());
        movieFavoriteHelper.openDatabase();

        movieFavorite = getIntent().getParcelableExtra(EXTRA_MOVIE_FAVORITE);

        /*if favorite is true*/
        assert myMovies != null;
        if (myMovies.isOnfavorites()) {

            /*Set to the layout*/
            progressBar = findViewById(R.id.progressDetailMovie);
            progressBar.setVisibility(View.INVISIBLE);

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
//                        progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(poster_path);

            btnSaveMovie = findViewById(R.id.btn_submits);
            btnSaveMovie.setOnClickListener(this);

            btnDeleteMovie = findViewById(R.id.btn_deletes);
            btnDeleteMovie.setOnClickListener(this);

            if (myMovies.isOnfavorites()) {
                flag = getIntent().getIntExtra(EXTRA_POSITION, 0);
                isFav = true;
                btnSaveMovie.setVisibility(View.GONE);
            } else {
                movieFavorite = new Movies();
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
                            progressBar.setVisibility(View.INVISIBLE);

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

            progressBar = findViewById(R.id.progressDetailMovie);
            progressBar.setVisibility(View.VISIBLE);

            btnSaveMovie = findViewById(R.id.btn_submits);
            btnSaveMovie.setOnClickListener(this);

            btnDeleteMovie = findViewById(R.id.btn_deletes);
            btnDeleteMovie.setOnClickListener(this);

            if (myMovies.isOnfavorites()) {
                flag = getIntent().getIntExtra(EXTRA_POSITION, 0);
                isFav = true;
                btnSaveMovie.setVisibility(View.GONE);

            } else {
                movieFavorite = new Movies();
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


            movieFavorite.setId(position);
            movieFavorite.setOriginal_title(title);
            movieFavorite.setVote_average(vote);
            movieFavorite.setVote_count(vote_counts);
            movieFavorite.setRelease_date(release_dates);
            movieFavorite.setOverview(overviews);
            movieFavorite.setPoster_path(url_images);

            Intent intent = new Intent();
            intent.putExtra(EXTRA_MOVIE_FAVORITE, movieFavorite);
            intent.putExtra(EXTRA_POSITION, flag);

            /*if data not save earlier*/
            if (!isFav) {
                long result = movieFavoriteHelper.insertIntoMovie(movieFavorite);

                if (result > 0) {
                    movieFavorite.setId((int) result);
                    setResult(RESULT_ADD, intent);
                    Toast.makeText(MoviesDetailActivity.this, getString(R.string.succes_add_data), Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(MoviesDetailActivity.this, getString(R.string.failed_add_data), Toast.LENGTH_SHORT).show();
                }
            }

            /*When delete data*/
        } else if (view.getId() == R.id.btn_deletes) {
            movieFavoriteHelper = MovieFavoriteHelper.getInstance(getApplicationContext());
            long result = movieFavoriteHelper.deleteIntoMovie(position);
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
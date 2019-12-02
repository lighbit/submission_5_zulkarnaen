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
import com.example.submission5.database.TVShowFavoriteHelper;
import com.example.submission5.model.TvShow;

/**
 * @author zulkarnaen
 */
public class TVShowDetailActivity extends AppCompatActivity implements View.OnClickListener {

    TextView original_name, vote_count, vote_average, first_air_date, overview;
    ImageView imagePhoto;
    String urlPhoto;
    Button btnSaveTV, btnDeleteTv;

    public static final String EXTRA_TV_SHOW = "extra_tv";
    public static final String EXTRA_TV_FAVORITE = "extra_tv_favorite";
    public static final String EXTRA_POSITION_DEFAULT = "extra_position";

    private TvShow myFavoriteTvShow;
    private ProgressBar myProgressBar;
    private int flags, positionDefault;
    private boolean isFav = false;
    public static final int RESULT_ADD = 101;

    private TVShowFavoriteHelper myFavoriteTvShowHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tv_show);

        final TvShow tvShow = getIntent().getParcelableExtra(EXTRA_TV_SHOW);
        positionDefault = getIntent().getIntExtra(EXTRA_POSITION_DEFAULT, 0);

        myFavoriteTvShowHelper = TVShowFavoriteHelper.getInstance(getApplicationContext());
        myFavoriteTvShowHelper.openTvShowDatabase();

        myFavoriteTvShow = getIntent().getParcelableExtra(EXTRA_TV_SHOW);


        assert tvShow != null;
        if (tvShow.isOnfavorites()) {

            myProgressBar = findViewById(R.id.progressDetailMovie);
            myProgressBar.setVisibility(View.INVISIBLE);

            original_name = findViewById(R.id.txt_name);
            original_name.setText(tvShow.getOriginal_name());

            vote_average = findViewById(R.id.rating);
            vote_average.setText(tvShow.getVote_average());

            overview = findViewById(R.id.tv_item_sinopsis);
            overview.setText(tvShow.getOverview());

            first_air_date = findViewById(R.id.tv_item_tahun);
            first_air_date.setText(tvShow.getFirst_air_date());

            vote_count = findViewById(R.id.tv_vote_count);
            vote_count.setText(tvShow.getVote_count());

            String url = "https://image.tmdb.org/t/p/original" + tvShow.getPoster_path();
            imagePhoto = findViewById(R.id.img_photo);
            Glide.with(TVShowDetailActivity.this).load(url).into(imagePhoto);
            Glide.with(TVShowDetailActivity.this)
                    .load(url)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(imagePhoto);

            btnSaveTV = findViewById(R.id.btn_submit);
            btnSaveTV.setOnClickListener(this);

            btnDeleteTv = findViewById(R.id.btn_delete);
            btnDeleteTv.setOnClickListener(this);

            if (tvShow.isOnfavorites()) {
                flags = getIntent().getIntExtra(EXTRA_POSITION_DEFAULT, 0);
                isFav = true;
                btnSaveTV.setVisibility(View.GONE);
            } else {
                myFavoriteTvShow = new TvShow();
                btnDeleteTv.setVisibility(View.GONE);
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
                            url_image = "https://image.tmdb.org/t/p/w185" + tvShow.getPoster_path();

                            urlPhoto = tvShow.getPoster_path();

                            original_name.setText(tvShow.getOriginal_name());
                            vote_count.setText(tvShow.getVote_count());
                            vote_average.setText(tvShow.getVote_average());
                            first_air_date.setText(tvShow.getFirst_air_date());
                            overview.setText(tvShow.getOverview());

                            Glide.with(TVShowDetailActivity.this)
                                    .load(url_image)
                                    .placeholder(R.color.colorPrimary)
                                    .dontAnimate()
                                    .into(imagePhoto);

                            myProgressBar.setVisibility(View.INVISIBLE);

                            myFavoriteTvShowHelper = TVShowFavoriteHelper.getInstance(getApplicationContext());
                            myFavoriteTvShowHelper.openTvShowDatabase();

                            myFavoriteTvShow = getIntent().getParcelableExtra(EXTRA_TV_FAVORITE);
                            if (tvShow.isOnfavorites()) {
                                flags = getIntent().getIntExtra(EXTRA_POSITION_DEFAULT, 0);
                                isFav = true;
                                btnSaveTV.setVisibility(View.GONE);

                            } else {
                                myFavoriteTvShow = new TvShow();
                                btnDeleteTv.setVisibility(View.GONE);
                            }

                        }
                    });
                }
            }).start();

            original_name = findViewById(R.id.txt_name);
            vote_average = findViewById(R.id.rating);
            overview = findViewById(R.id.tv_item_sinopsis);
            first_air_date = findViewById(R.id.tv_item_tahun);
            vote_count = findViewById(R.id.tv_vote_count);
            imagePhoto = findViewById(R.id.img_photo);

            myProgressBar = findViewById(R.id.progressDetailMovie);
            myProgressBar.setVisibility(View.VISIBLE);

            btnSaveTV = findViewById(R.id.btn_submit);
            btnSaveTV.setOnClickListener(this);

            btnDeleteTv = findViewById(R.id.btn_delete);
            btnDeleteTv.setOnClickListener(this);

            if (tvShow.isOnfavorites()) {
                flags = getIntent().getIntExtra(EXTRA_POSITION_DEFAULT, 0);
                isFav = true;
                btnSaveTV.setVisibility(View.GONE);
            } else {
                myFavoriteTvShow = new TvShow();
                btnDeleteTv.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.btn_submit) {
            String title = original_name.getText().toString().trim();
            String vote = vote_average.getText().toString().trim();
            String vote_counts = vote_count.getText().toString().trim();
            String overviews = overview.getText().toString().trim();
            String release = first_air_date.getText().toString().trim();

            String url_image = urlPhoto.trim();

            myFavoriteTvShow.setId(positionDefault);
            myFavoriteTvShow.setOriginal_name(title);
            myFavoriteTvShow.setVote_average(vote);
            myFavoriteTvShow.setVote_count(vote_counts);
            myFavoriteTvShow.setOverview(overviews);
            myFavoriteTvShow.setFirst_air_date(release);

            myFavoriteTvShow.setPoster_path(url_image);

            Intent intent = new Intent();
            intent.putExtra(EXTRA_TV_FAVORITE, myFavoriteTvShow);
            intent.putExtra(EXTRA_POSITION_DEFAULT, flags);

            if (!isFav) {

                long result = myFavoriteTvShowHelper.insertIntoTvShow(myFavoriteTvShow);

                if (result > 0) {
                    myFavoriteTvShow.setId((int) result);
                    setResult(RESULT_ADD, intent);
                    Toast.makeText(TVShowDetailActivity.this, getString(R.string.succes_add_data_tv), Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(TVShowDetailActivity.this, getString(R.string.failed_add_data_tv), Toast.LENGTH_SHORT).show();
                }
            }

        } else if (view.getId() == R.id.btn_delete) {
            myFavoriteTvShowHelper = TVShowFavoriteHelper.getInstance(getApplicationContext());
            long result = myFavoriteTvShowHelper.deleteIntoTvShow(positionDefault);
            if (result > 0) {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(TVShowDetailActivity.this, getString(R.string.notif_failed_delete), Toast.LENGTH_SHORT).show();
            }

        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

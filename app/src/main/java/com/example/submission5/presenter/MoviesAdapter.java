package com.example.submission5.presenter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.submission5.R;
import com.example.submission5.model.Movies;
import com.example.submission5.view.MoviesDetailActivity;

import java.util.ArrayList;

/**
 * @author zulkarnaen
 */
public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.CategoryViewHolder> {

    private Context context;
    private ArrayList<Movies> mData = new ArrayList<>();

    /*get Data Movies*/
    private ArrayList<Movies> getDataMovies() {
        return mData;
    }

    private boolean isFavorite;

    /*if favorite set true*/
    public void isOnFavoriteMovies(boolean value) {
        this.isFavorite = value;
    }

    private String word;

    /*set word like choose*/
    public void setWordMovies(String word) {
        this.word = word;
    }

    /*set Context*/
    public MoviesAdapter(Context context) {
        this.context = context;
    }

    /*set New Data Movies if not available*/
    public void setDataMovies(ArrayList<Movies> items) {
        mData.clear();
        mData.addAll(items);
        notifyDataSetChanged();
    }

    /*counting*/
    @Override
    public int getItemCount() {
        return mData.size();
    }


    /*View Category View Holder*/
    class CategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPhoto;
        TextView ttvTitle, tvRelease, tvOverview, tvVoteCount;

        /*Set Default*/
        CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            ttvTitle = itemView.findViewById(R.id.txt_name);
            tvRelease = itemView.findViewById(R.id.txt_tahun);
            tvOverview = itemView.findViewById(R.id.txt_description);
            tvVoteCount = itemView.findViewById(R.id.txt_genres);
            imgPhoto = itemView.findViewById(R.id.img_photo);
        }
    }

    /*Create View Holder*/
    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemRow = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_movies, viewGroup, false);
        return new MoviesAdapter.CategoryViewHolder(itemRow);
    }

    /*on Bind View Holder*/
    @Override
    public void onBindViewHolder(@NonNull final CategoryViewHolder categoryViewHolder, final int i) {

        categoryViewHolder.ttvTitle.setText(getDataMovies().get(i).getOriginal_title());
        categoryViewHolder.tvRelease.setText(getDataMovies().get(i).getRelease_date());
        categoryViewHolder.tvOverview.setText(getDataMovies().get(i).getOverview());
        categoryViewHolder.tvVoteCount.setText(getDataMovies().get(i).getVote_count());

        String uri = "https://image.tmdb.org/t/p/original" + getDataMovies().get(i).getPoster_path();
        Glide.with(categoryViewHolder.itemView.getContext())
                .load(uri)
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
                .into(categoryViewHolder.imgPhoto);

        categoryViewHolder.itemView.setOnClickListener(new CustomOnItemClickListener(i, new CustomOnItemClickListener.OnItemClickCallback() {
            /*Call Data when Data in Click*/
            @Override
            public void onItemClicked(View view, int position) {
                Toast.makeText(context, word + " " + getDataMovies().get(position).getOriginal_title(), Toast.LENGTH_SHORT).show();
                Intent move = new Intent(context, MoviesDetailActivity.class);
                Movies movie = new Movies();
                movie.setOriginal_title(getDataMovies().get(i).getOriginal_title());
                movie.setVote_average(getDataMovies().get(i).getVote_average());
                movie.setVote_count(getDataMovies().get(i).getVote_count());
                movie.setRelease_date(getDataMovies().get(i).getRelease_date());
                movie.setOverview(getDataMovies().get(i).getOverview());
                movie.setPoster_path(getDataMovies().get(i).getPoster_path());
                movie.setOnfavorites(isFavorite);

                move.putExtra(MoviesDetailActivity.EXTRA_MOVIE, movie);
                move.putExtra(MoviesDetailActivity.EXTRA_POSITION, getDataMovies().get(i).getId());

                context.startActivity(move);
            }
        }));

    }
}

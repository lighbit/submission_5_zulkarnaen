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
import com.example.submission5.model.TvShow;
import com.example.submission5.view.TVShowDetailActivity;


import java.util.ArrayList;

/**
 * @author zulkarnaen
 */
public class TvShowAdapter extends RecyclerView.Adapter<TvShowAdapter.CategoryViewHolder> {

    private Context context;
    private ArrayList<TvShow> tvShowData = new ArrayList<>();

    private String word;

    public void setWord(String word) {
        this.word = word;
    }

    private boolean isFavorite;

    public void isOnFavoriteTvShow(boolean value) {
        this.isFavorite = value;
    }

    private ArrayList<TvShow> getDataTvShow() {
        return tvShowData;
    }

    public TvShowAdapter(Context context) {
        this.context = context;
    }

    public void setDataTvshow(ArrayList<TvShow> items) {
        tvShowData.clear();
        tvShowData.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return tvShowData.size();
    }


    class CategoryViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_title, tv_first_air_dates, tv_overview, tv_counts;
        private ImageView tv_photo;

        CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.txt_name);
            tv_counts = itemView.findViewById(R.id.txt_genres);
            tv_first_air_dates = itemView.findViewById(R.id.txt_tahun);
            tv_overview = itemView.findViewById(R.id.txt_description);
            tv_photo = itemView.findViewById(R.id.img_photo);
        }
    }


    @NonNull
    @Override
    public TvShowAdapter.CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemRow = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_tv_show, viewGroup, false);
        return new TvShowAdapter.CategoryViewHolder(itemRow);
    }

    @Override
    public void onBindViewHolder(@NonNull final TvShowAdapter.CategoryViewHolder categoryViewHolder, final int i) {


        categoryViewHolder.tv_title.setText(getDataTvShow().get(i).getOriginal_name());
        categoryViewHolder.tv_counts.setText(getDataTvShow().get(i).getVote_count());
        categoryViewHolder.tv_first_air_dates.setText(getDataTvShow().get(i).getFirst_air_date());
        categoryViewHolder.tv_overview.setText(getDataTvShow().get(i).getOverview());

        String uri = "https://image.tmdb.org/t/p/original" + getDataTvShow().get(i).getPoster_path();

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
                .into(categoryViewHolder.tv_photo);

        categoryViewHolder.itemView.setOnClickListener(new CustomOnItemClickListener(i, new CustomOnItemClickListener.OnItemClickCallback() {
            @Override
            public void onItemClicked(View view, int position) {
                Toast.makeText(context, word + " " + getDataTvShow().get(position).getOriginal_name(), Toast.LENGTH_SHORT).show();
                Intent move = new Intent(context, TVShowDetailActivity.class);
                TvShow tv = new TvShow();

                tv.setOriginal_name(getDataTvShow().get(i).getOriginal_name());
                tv.setVote_count(getDataTvShow().get(i).getVote_count());
                tv.setVote_average(getDataTvShow().get(i).getVote_average());
                tv.setFirst_air_date(getDataTvShow().get(i).getFirst_air_date());
                tv.setOverview(getDataTvShow().get(i).getOverview());
                tv.setPoster_path(getDataTvShow().get(i).getPoster_path());
                tv.setOnfavorites(isFavorite);

                move.putExtra(TVShowDetailActivity.EXTRA_TV_SHOW, tv);
                move.putExtra(TVShowDetailActivity.EXTRA_POSITION, getDataTvShow().get(i).getId());
                context.startActivity(move);
            }
        }));
    }
}

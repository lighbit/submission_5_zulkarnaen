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

    private Context myContext;
    private ArrayList<TvShow> myTvShowData = new ArrayList<>();

    private String word;

    public void setWord(String word) {
        this.word = word;
    }

    private boolean isFavorite;

    public void isOnFavoriteTvShow(boolean value) {
        this.isFavorite = value;
    }

    private ArrayList<TvShow> getDataTvShow() {
        return myTvShowData;
    }

    public TvShowAdapter(Context myContext) {
        this.myContext = myContext;
    }

    public void setDataTvShow(ArrayList<TvShow> items) {
        myTvShowData.clear();
        myTvShowData.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return myTvShowData.size();
    }


    class CategoryViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_title, tv_first_air_dates, tv_overview, tv_counts;
        private ImageView tv_photo;

        CategoryViewHolder(@NonNull View myItemView) {
            super(myItemView);
            tv_title = myItemView.findViewById(R.id.txt_name);
            tv_counts = myItemView.findViewById(R.id.txt_genres);
            tv_first_air_dates = myItemView.findViewById(R.id.txt_tahun);
            tv_overview = myItemView.findViewById(R.id.txt_description);
            tv_photo = myItemView.findViewById(R.id.img_photo);
        }
    }


    @NonNull
    @Override
    public TvShowAdapter.CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup myViewGroup, int i) {
        View itemRow = LayoutInflater.from(myViewGroup.getContext()).inflate(R.layout.item_tv_show, myViewGroup, false);
        return new TvShowAdapter.CategoryViewHolder(itemRow);
    }

    @Override
    public void onBindViewHolder(@NonNull final TvShowAdapter.CategoryViewHolder viewHolderCategory, final int i) {


        viewHolderCategory.tv_title.setText(getDataTvShow().get(i).getOriginal_name());
        viewHolderCategory.tv_counts.setText(getDataTvShow().get(i).getVote_count());
        viewHolderCategory.tv_first_air_dates.setText(getDataTvShow().get(i).getFirst_air_date());
        viewHolderCategory.tv_overview.setText(getDataTvShow().get(i).getOverview());

        String uri = "https://image.tmdb.org/t/p/original" + getDataTvShow().get(i).getPoster_path();

        Glide.with(viewHolderCategory.itemView.getContext())
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
                .into(viewHolderCategory.tv_photo);

        viewHolderCategory.itemView.setOnClickListener(new CustomItemListenerClick(i, new CustomItemListenerClick.OnItemClickCallback() {
            @Override
            public void onItemClicked(View view, int position) {
                Toast.makeText(myContext, word + " " + getDataTvShow().get(position).getOriginal_name(), Toast.LENGTH_SHORT).show();
                Intent move = new Intent(myContext, TVShowDetailActivity.class);
                TvShow tv = new TvShow();

                tv.setOriginal_name(getDataTvShow().get(i).getOriginal_name());
                tv.setVote_count(getDataTvShow().get(i).getVote_count());
                tv.setVote_average(getDataTvShow().get(i).getVote_average());
                tv.setFirst_air_date(getDataTvShow().get(i).getFirst_air_date());
                tv.setOverview(getDataTvShow().get(i).getOverview());
                tv.setPoster_path(getDataTvShow().get(i).getPoster_path());
                tv.setOnfavorites(isFavorite);

                move.putExtra(TVShowDetailActivity.EXTRA_TV_SHOW, tv);
                move.putExtra(TVShowDetailActivity.EXTRA_POSITION_DEFAULT, getDataTvShow().get(i).getId());
                myContext.startActivity(move);
            }
        }));
    }
}

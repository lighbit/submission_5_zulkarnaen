package com.example.submission5.myView;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.submission5.R;
import com.example.submission5.myModel.TvShow;
import com.example.submission5.myModelImpl.TvShowServiceImpl;
import com.example.submission5.myPresenter.TvShowAdapter;

import java.util.ArrayList;
import java.util.Objects;

/**
 * @author zulkarnaen
 */
public class TVShowFragment extends Fragment {

    private TvShowAdapter myTvShowAdapter;
    private ProgressBar myProgressBar;
    private TvShowServiceImpl myTvShowServiceImpl;


    public TVShowFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*main Model instance*/
        myTvShowServiceImpl = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(TvShowServiceImpl.class);
    }

    private Observer<ArrayList<TvShow>> getDataTvShow = new Observer<ArrayList<TvShow>>() {
        @Override
        public void onChanged(ArrayList<TvShow> tvShows) {
            if (tvShows != null) {
                myTvShowAdapter.setWord(getResources().getString(R.string.choose));
                myTvShowAdapter.setDataTvShow(tvShows);
                myTvShowAdapter.isOnFavoriteTvShow(false);
                myProgressBar.setVisibility(View.GONE);
            }
        }
    };

    private void showLoading() {
        myProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        myTvShowAdapter = new TvShowAdapter(getActivity());
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.rv_category2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(myTvShowAdapter);

        TvShowServiceImpl tvShowViewModel = ViewModelProviders.of(this).get(TvShowServiceImpl.class);
        tvShowViewModel.getTvShow().observe(this, getDataTvShow);
        tvShowViewModel.setTvShow(Objects.requireNonNull(this.getContext()));

        myProgressBar = view.findViewById(R.id.progressBarTv);
        myProgressBar.bringToFront();

        showLoading();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        /*Get Observer then set Data*/
        myTvShowServiceImpl.getTvShow().observe(Objects.requireNonNull(getActivity()), getDataTvShow);
        myTvShowServiceImpl.setTvShow(Objects.requireNonNull(this.getContext()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        /*remove Observer*/
        myTvShowServiceImpl.getTvShow().removeObserver(getDataTvShow);
    }
}

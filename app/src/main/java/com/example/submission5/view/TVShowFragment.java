package com.example.submission5.view;


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
import com.example.submission5.model.TvShow;
import com.example.submission5.modelImpl.TvShowServiceImpl;
import com.example.submission5.presenter.TvShowAdapter;

import java.util.ArrayList;
import java.util.Objects;

/**
 * @author zulkarnaen
 */
public class TVShowFragment extends Fragment {

    private TvShowAdapter tvShowAdapter;
    private ProgressBar progressBar;
    private TvShowServiceImpl tvShowImplService;


    public TVShowFragment() {
        // Required empty public constructor
    }

    private Observer<ArrayList<TvShow>> getTvShow = new Observer<ArrayList<TvShow>>() {
        @Override
        public void onChanged(ArrayList<TvShow> tvShows) {
            if (tvShows != null) {
                tvShowAdapter.setWord(getResources().getString(R.string.choose));
                tvShowAdapter.setDataTvshow(tvShows);
                tvShowAdapter.isOnFavoriteTvShow(false);
                progressBar.setVisibility(View.GONE);
            }
        }
    };

    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        tvShowAdapter = new TvShowAdapter(getActivity());
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.rv_category2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(tvShowAdapter);

        TvShowServiceImpl tvShowViewModel = ViewModelProviders.of(this).get(TvShowServiceImpl.class);
        tvShowViewModel.getTvShow().observe(this, getTvShow);
        tvShowViewModel.setTvShow(Objects.requireNonNull(this.getContext()));

        progressBar = view.findViewById(R.id.progressBarTv);
        progressBar.bringToFront();

        showLoading();

        return view;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*main Model instance*/
        tvShowImplService = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(TvShowServiceImpl.class);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        /*Get Observer then set Data*/
        tvShowImplService.getTvShow().observe(Objects.requireNonNull(getActivity()), getTvShow);
        tvShowImplService.setTvShow(Objects.requireNonNull(this.getContext()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        /*remove Observer*/
        tvShowImplService.getTvShow().removeObserver(getTvShow);
    }
}

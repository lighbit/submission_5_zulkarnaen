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
import com.example.submission5.model.Movies;
import com.example.submission5.modelImpl.MoviesServiceImpl;
import com.example.submission5.presenter.MoviesAdapter;

import java.util.ArrayList;
import java.util.Objects;

import static com.example.submission5.MainActivity.INTENT_SEARCH;

/**
 * @author zulkarnaen
 */
public class SearchActivityMoviesFragment extends Fragment {


    private ProgressBar progressBar;
    private MoviesAdapter moviesAdapter;
    private MoviesServiceImpl moviesModel;
    private String paramSearch = "";

    public SearchActivityMoviesFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Bundle bundle = this.getArguments();
        if (bundle != null) this.paramSearch = bundle.getString(INTENT_SEARCH);
        else {
            paramSearch = "";
        }

        moviesModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(MoviesServiceImpl.class);
    }


    private Observer<ArrayList<Movies>> getMovies = new Observer<ArrayList<Movies>>() {
        @Override
        public void onChanged(ArrayList<Movies> moviesItem) {
            if (moviesItem != null) {
                moviesAdapter.setWordMovies(getResources().getString(R.string.choose));
                moviesAdapter.setDataMovies(moviesItem);
                moviesAdapter.isOnFavoriteMovies(false);
                progressBar.setVisibility(View.GONE);
            }
        }
    };

    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        moviesAdapter = new MoviesAdapter(getActivity());
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.rv_category);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(moviesAdapter);

        MoviesServiceImpl moviesViewModel = ViewModelProviders.of(this).get(MoviesServiceImpl.class);
        moviesViewModel.getMovies().observe(this, getMovies);
        moviesViewModel.setMoviesData(Objects.requireNonNull(this.getContext()));

        progressBar = view.findViewById(R.id.progressBarMovies);
        progressBar.bringToFront();

        showLoading();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        moviesModel.getMovies().observe(Objects.requireNonNull(getActivity()), getMovies);
        moviesModel.setSearchMoviesWhenSearch(paramSearch, Objects.requireNonNull(this.getContext()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        moviesModel.getMovies().removeObserver(getMovies);
    }


}

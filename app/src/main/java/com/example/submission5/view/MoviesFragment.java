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

/**
 * @author zulkarnaen
 */
public class MoviesFragment extends Fragment {
    private MoviesAdapter moviesAdapter;
    private ProgressBar progressBar;
    private MoviesServiceImpl moviesModel;

    public MoviesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*get Activity*/
        moviesModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(MoviesServiceImpl.class);
    }

    private Observer<ArrayList<Movies>> getDataMovies = new Observer<ArrayList<Movies>>() {
        @Override
        public void onChanged(ArrayList<Movies> movies) {
            if (movies != null) {
                moviesAdapter.setWordMovies(getResources().getString(R.string.choose));
                moviesAdapter.setDataMovies(movies);
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
        moviesViewModel.getMovies().observe(this, getDataMovies);
        moviesViewModel.setMoviesData(Objects.requireNonNull(this.getContext()));

        progressBar = view.findViewById(R.id.progressBarMovies);
        progressBar.bringToFront();

        showLoading();

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        /*Get Observer then set Data*/
        moviesModel.getMovies().observe(Objects.requireNonNull(getActivity()), getDataMovies);
        moviesModel.setMoviesData(Objects.requireNonNull(this.getContext()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        /*remove Observer*/
        moviesModel.getMovies().removeObserver(getDataMovies);
    }
}

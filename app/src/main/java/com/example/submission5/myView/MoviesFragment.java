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
import com.example.submission5.myModel.Movies;
import com.example.submission5.myModelImpl.MoviesServiceImpl;
import com.example.submission5.myPresenter.MoviesAdapter;

import java.util.ArrayList;
import java.util.Objects;

/**
 * @author zulkarnaen
 */
public class MoviesFragment extends Fragment {
    private MoviesAdapter myMoviesAdapter;
    private ProgressBar myProgressBar;
    private MoviesServiceImpl myMoviesModel;

    public MoviesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*get Activity*/
        myMoviesModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(MoviesServiceImpl.class);
    }

    private Observer<ArrayList<Movies>> getDataMovies = new Observer<ArrayList<Movies>>() {
        @Override
        public void onChanged(ArrayList<Movies> movies) {
            if (movies != null) {
                myMoviesAdapter.setWordMovies(getResources().getString(R.string.choose));
                myMoviesAdapter.setDataMovies(movies);
                myMoviesAdapter.isOnFavoriteMovies(false);
                myProgressBar.setVisibility(View.GONE);
            }
        }
    };

    private void showLoading() {
        myProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        myMoviesAdapter = new MoviesAdapter(getActivity());
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.rv_category);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(myMoviesAdapter);

        MoviesServiceImpl moviesViewModel = ViewModelProviders.of(this).get(MoviesServiceImpl.class);
        moviesViewModel.getDataMovies().observe(this, getDataMovies);
        moviesViewModel.setMoviesData(Objects.requireNonNull(this.getContext()));

        myProgressBar = view.findViewById(R.id.progressBarMovies);
        myProgressBar.bringToFront();

        showLoading();

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        /*Get Observer then set Data*/
        myMoviesModel.getDataMovies().observe(Objects.requireNonNull(getActivity()), getDataMovies);
        myMoviesModel.setMoviesData(Objects.requireNonNull(this.getContext()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        /*remove Observer*/
        myMoviesModel.getDataMovies().removeObserver(getDataMovies);
    }
}

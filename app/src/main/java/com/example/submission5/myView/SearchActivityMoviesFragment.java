package com.example.submission5.myView;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
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

import static com.example.submission5.MainActivity.MY_INTENT_SEARCH;

/**
 * @author zulkarnaen
 */
public class SearchActivityMoviesFragment extends Fragment {

    private ProgressBar myProgressBar;
    private MoviesAdapter myMoviesAdapter;
    private MoviesServiceImpl myMoviesServiceImpl;
    private String paramSearch = "";

    public SearchActivityMoviesFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle instanceSaved) {
        super.onCreate(instanceSaved);


        Bundle bundle = this.getArguments();
        if (bundle != null) this.paramSearch = bundle.getString(MY_INTENT_SEARCH);
        else {
            paramSearch = "";
        }

        myMoviesServiceImpl = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(MoviesServiceImpl.class);
    }


    private Observer<ArrayList<Movies>> getMovies = new Observer<ArrayList<Movies>>() {
        @Override
        public void onChanged(ArrayList<Movies> moviesItem) {
            if (moviesItem != null) {
                myMoviesAdapter.setWordMovies(getResources().getString(R.string.choose));
                myMoviesAdapter.setDataMovies(moviesItem);
                myMoviesAdapter.isOnFavoriteMovies(false);
                myProgressBar.setVisibility(View.GONE);
            }
        }
    };

    private void showLoading() {
        myProgressBar.setVisibility(View.VISIBLE);
    }


    @Override
    public View onCreateView(LayoutInflater inflateLayout, ViewGroup viewGroup, Bundle instanceSaved) {

        return inflateLayout.inflate(R.layout.fragment_home, viewGroup, false);
    }

    @Override
    public void onViewCreated(@NonNull View viewHandler, @Nullable Bundle instanceSaved) {
        super.onViewCreated(viewHandler, instanceSaved);

        myMoviesAdapter = new MoviesAdapter(getActivity());
        RecyclerView recyclerView = viewHandler.findViewById(R.id.rv_category);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(myMoviesAdapter);

        myProgressBar = viewHandler.findViewById(R.id.progressBarMovies);
        myProgressBar.bringToFront();

        showLoading();

    }


    @Override
    public void onActivityCreated(@Nullable Bundle instanceSaved) {
        super.onActivityCreated(instanceSaved);

        myMoviesServiceImpl.getDataMovies().observe(Objects.requireNonNull(getActivity()), getMovies);
        myMoviesServiceImpl.setSearchMoviesWhenSearch(paramSearch, Objects.requireNonNull(this.getContext()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        myMoviesServiceImpl.getDataMovies().removeObserver(getMovies);
    }


}

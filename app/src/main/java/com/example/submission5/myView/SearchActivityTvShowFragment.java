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
import com.example.submission5.myModel.TvShow;
import com.example.submission5.myModelImpl.TvShowServiceImpl;
import com.example.submission5.myPresenter.TvShowAdapter;

import java.util.ArrayList;
import java.util.Objects;

import static com.example.submission5.MainActivity.MY_INTENT_SEARCH;

/**
 * @author zulkarnaen
 */
public class SearchActivityTvShowFragment extends Fragment {

    private ProgressBar myProgressBar;
    private TvShowAdapter myTvShowAdapter;
    private TvShowServiceImpl myTvShowServiceImpl;
    private String paramSearch = "";

    public SearchActivityTvShowFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle instanceSaved) {
        super.onCreate(instanceSaved);


        Bundle bundle = this.getArguments();
        if (bundle != null) this.paramSearch = bundle.getString(MY_INTENT_SEARCH);
        else {
            paramSearch = "";
        }

        myTvShowServiceImpl = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(TvShowServiceImpl.class);
    }

    private Observer<ArrayList<TvShow>> getTvShow = new Observer<ArrayList<TvShow>>() {
        @Override
        public void onChanged(ArrayList<TvShow> tvShowItem) {
            if (tvShowItem != null) {
                myTvShowAdapter.setWord(getResources().getString(R.string.choose));
                myTvShowAdapter.setDataTvShow(tvShowItem);
                myTvShowAdapter.isOnFavoriteTvShow(false);
                myProgressBar.setVisibility(View.GONE);
            }
        }
    };

    private void showLoading() {
        myProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflateLayout, ViewGroup viewGroup, Bundle instanceSaved) {

        return inflateLayout.inflate(R.layout.fragment_dashboard, viewGroup, false);
    }

    @Override
    public void onViewCreated(@NonNull View viewHandler, @Nullable Bundle insatnaceSaved) {
        super.onViewCreated(viewHandler, insatnaceSaved);

        myTvShowAdapter = new TvShowAdapter(getActivity());
        RecyclerView recyclerView = viewHandler.findViewById(R.id.rv_category2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(myTvShowAdapter);

        myProgressBar = viewHandler.findViewById(R.id.progressBarTv);
        myProgressBar.bringToFront();

        showLoading();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle instanceSaved) {
        super.onActivityCreated(instanceSaved);

        myTvShowServiceImpl.getTvShow().observe(Objects.requireNonNull(getActivity()), getTvShow);
        myTvShowServiceImpl.setSearchTvWhenSearch(paramSearch, Objects.requireNonNull(this.getContext()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        myTvShowServiceImpl.getTvShow().removeObserver(getTvShow);
    }
}

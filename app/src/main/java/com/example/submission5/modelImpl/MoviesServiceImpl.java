package com.example.submission5.modelImpl;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.submission5.R;
import com.example.submission5.model.Movies;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;

/**
 * @author zulkarnaen
 */
public class MoviesServiceImpl extends ViewModel {

    /*MY API*/
    private static final String MY_API_KEY = "d10721892b71839178ae7c4597123c84";
    private MutableLiveData<ArrayList<Movies>> myListMovies = new MutableLiveData<>();

    /*get all data from api*/
    public void setMoviesData(Context myContext) {
        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<Movies> listItems = new ArrayList<>();

        String language = myContext.getString(R.string.API);

        String url = "https://api.themoviedb.org/3/movie/popular?api_key=" + MY_API_KEY + language;

        client.get(url, new AsyncHttpResponseHandler() {
            /*if onSuccess*/
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray list = responseObject.getJSONArray("results");

                    for (int i = 0; i < list.length(); i++) {
                        JSONObject weather = list.getJSONObject(i);
                        Movies movieItems = new Movies(weather);
                        listItems.add(movieItems);
                    }
                    myListMovies.postValue(listItems);
                } catch (Exception e) {
                    Log.d("Exception", Objects.requireNonNull(e.getMessage()));
                }
            }

            /*if onFailures*/
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("onFailure", Objects.requireNonNull(error.getMessage()));
            }
        });
    }

    /*get all data from api when search*/
    public void setSearchMoviesWhenSearch(String param, Context myContext) {
        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<Movies> listItems = new ArrayList<>();

        String language = myContext.getString(R.string.API);

        String url = "https://api.themoviedb.org/3/search/movie?api_key=" + MY_API_KEY + language + "&query=" + param;

        client.get(url, new AsyncHttpResponseHandler() {
            /*if onSuccess*/
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray list = responseObject.getJSONArray("results");

                    for (int i = 0; i < list.length(); i++) {
                        JSONObject movie = list.getJSONObject(i);
                        Movies movieItems = new Movies(movie);
                        listItems.add(movieItems);
                    }
                    myListMovies.postValue(listItems);

                } catch (Exception e) {
                    Log.d("Exception", Objects.requireNonNull(e.getMessage()));
                }
            }

            /*if onFailures*/
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("onFailure", Objects.requireNonNull(error.getMessage()));
            }
        });
    }

    /*set that data on this live Data*/
    public LiveData<ArrayList<Movies>> getDataMovies() {
        return myListMovies;
    }
}

package com.example.myfavorit;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.example.myfavorit.adapter.MoviesAdapter;

import static com.example.myfavorit.database.DatabaseContract.CONTENT_URI;

/**
 * @author zulkarnaen
 */
public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private MoviesAdapter myMoviesAdapter;
    private final int LOAD_ID = 110;

    /*Load on Create LISTING some data*/
    @Override
    protected void onCreate(Bundle myInstanceSaved) {
        super.onCreate(myInstanceSaved);
        setContentView(R.layout.fragment_home);

        myMoviesAdapter = new MoviesAdapter(this, null, true);

        /*set On list*/
        ListView listView = findViewById(R.id.rv_category);
        listView.setAdapter(myMoviesAdapter);

        /*set loader Manager*/
        LoaderManager.getInstance(this).initLoader(LOAD_ID, null, this);

    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
        return new CursorLoader(this, CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        myMoviesAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        myMoviesAdapter.swapCursor(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LoaderManager.getInstance(this).restartLoader(LOAD_ID, null, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LoaderManager.getInstance(this).destroyLoader(LOAD_ID);
    }
}

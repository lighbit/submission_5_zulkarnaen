package com.example.submission5.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.example.submission5.R;
import com.example.submission5.model.ResultsItem;

import java.util.concurrent.ExecutionException;

import static com.example.submission5.database.DatabaseContract.CONTENT_URI;

/**
 * @author zulkarnaen
 */
public class MyRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context myContext;
    private Cursor listWidget;

    /*When create set CONTENT URI*/
    @Override
    public void onCreate() {
        listWidget = myContext.getContentResolver().query(
                CONTENT_URI,
                null,
                null,
                null,
                null
        );
    }

    @Override
    public int getCount() {
        return listWidget.getCount();
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    MyRemoteViewsFactory(Context applicationContext) {
        myContext = applicationContext;
    }


    /*get Some Item if position not valid call throw*/
    private ResultsItem getSomeItem(int position) {
        if (!listWidget.moveToPosition(position)) {
            throw new IllegalStateException("Some Position not valid");
        }

        return new ResultsItem(listWidget);
    }

    @Override
    public RemoteViews getViewAt(int i) {
        ResultsItem myItem = getSomeItem(i);
        RemoteViews rv = new RemoteViews(myContext.getPackageName(), R.layout.favourite_widget_item);

        Bitmap bitmap = null;
        try {
            bitmap = Glide.with(myContext)
                    .asBitmap()
                    .load("http://image.tmdb.org/t/p/w500" + myItem.getPoster_path())
                    .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        rv.setImageViewBitmap(R.id.imageView, bitmap);

        Bundle extras = new Bundle();
        extras.putInt(MyFavoriteWidget.EXTRA_ITEM, i);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);

        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent);
        return rv;
    }

}

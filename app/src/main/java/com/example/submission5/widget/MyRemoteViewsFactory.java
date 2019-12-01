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
    private Cursor myListWidget;

    /*When create set CONTENT URI*/
    @Override
    public void onCreate() {
        myListWidget = myContext.getContentResolver().query(
                CONTENT_URI,
                null,
                null,
                null,
                null
        );
    }

    @Override
    public int getCount() {
        return myListWidget.getCount();
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
        if (!myListWidget.moveToPosition(position)) {
            throw new IllegalStateException("Some Position not valid");
        }

        return new ResultsItem(myListWidget);
    }

    @Override
    public RemoteViews getViewAt(int i) {
        ResultsItem myItem = getSomeItem(i);
        RemoteViews myRemoteViews = new RemoteViews(myContext.getPackageName(), R.layout.my_favorite_widget_item);

        String url_image = "https://image.tmdb.org/t/p/w185" + myItem.getPoster_path();

        Bitmap bitMapImage = null;
        try {

            bitMapImage
                    = Glide.with(myContext)
                    .asBitmap()
                    .load(url_image)
                    .placeholder(R.color.colorAccent)
                    .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get();

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        myRemoteViews.setImageViewBitmap(R.id.imageView, bitMapImage);

        Bundle extras = new Bundle();
        extras.putInt(MyFavoriteWidget.EXTRA_ITEM, i);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);

        myRemoteViews.setOnClickFillInIntent(R.id.imageView, fillInIntent);
        return myRemoteViews;
    }

}

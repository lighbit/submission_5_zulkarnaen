package com.example.submission5.myWidget;

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
import com.example.submission5.myModel.Notification;

import java.util.concurrent.ExecutionException;

import static com.example.submission5.myDatabase.DatabaseContract.CONTENT_URI;

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

    MyRemoteViewsFactory(Context myApplicationContext) {
        myContext = myApplicationContext;
    }


    /*get Some Item if position not valid call throw*/
    private Notification getSomeItemOnMovies(int position) {
        if (!myListWidget.moveToPosition(position)) {
            throw new IllegalStateException("Invalid Position in Widget");
        }

        return new Notification(myListWidget);
    }

    @Override
    public RemoteViews getViewAt(int i) {
        Notification myItem = getSomeItemOnMovies(i);
        RemoteViews myRemoteViews = new RemoteViews(myContext.getPackageName(), R.layout.my_favorite_widget_item);

        String url_image = "https://image.tmdb.org/t/p/w185" + myItem.getPoster_path();

        Bitmap bitMapImage = null;
        try {

            bitMapImage
                    = Glide.with(myContext)
                    .asBitmap()
                    .load(url_image)
                    .placeholder(R.color.colorPrimary)
                    .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get();

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            e.getMessage();
        }

        Bundle extras = new Bundle();
        extras.putInt(MyFavoriteWidget.EXTRA_ITEM, i);

        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);

        myRemoteViews.setImageViewBitmap(R.id.imageView, bitMapImage);
        myRemoteViews.setOnClickFillInIntent(R.id.imageView, fillInIntent);
        return myRemoteViews;
    }

}

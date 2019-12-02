package com.example.submission5.myWidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.submission5.R;

import java.util.Objects;

/**
 * @author zulkarnaen
 */
public class MyFavoriteWidget extends AppWidgetProvider {

    public static final String TOAST_ACTION = "com.example.submission5.TOAST_ACTION";
    public static final String EXTRA_ITEM = "com.example.submission5.EXTRA_ITEM";
    private final static int NOTIFICATION_REQUEST_CODE = 200;

    /*when receive that widget*/
    @Override
    public void onReceive(Context myContext, Intent myIntents) {
        if (Objects.equals(myIntents.getAction(), TOAST_ACTION)) {
            int myIndexView = myIntents.getIntExtra(EXTRA_ITEM, 0);
            Toast.makeText(myContext, myContext.getString(R.string.choose) + myIndexView, Toast.LENGTH_SHORT).show();
        }
        super.onReceive(myContext, myIntents);
    }

    /*update some widget when set up widget in layout your smart phone*/
    static void updateSomeWidget(Context myContext, AppWidgetManager myAppWidgetManager, int myAppWidgetID) {
        /*Create intent in Widget Service*/
        Intent myIntents = new Intent(myContext, MyWidgetService.class);
        myIntents.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, myAppWidgetID);
        myIntents.setData(Uri.parse(myIntents.toUri(Intent.URI_INTENT_SCHEME)));

        /*call MyFavoriteWidget to create toast action and extra app widget*/
        Intent myOtherIntent = new Intent(myContext, MyFavoriteWidget.class);
        myOtherIntent.setAction(MyFavoriteWidget.TOAST_ACTION);
        myOtherIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, myAppWidgetID);

        /*Create Remote Views in your xml*/
        RemoteViews myRemoteViews = new RemoteViews(myContext.getPackageName(), R.layout.my_favorite_widget);
        myRemoteViews.setRemoteAdapter(R.id.image_inside, myIntents);
        myRemoteViews.setEmptyView(R.id.image_inside, R.id.empty_view);

        /*Update*/
        PendingIntent myPendingIntent = PendingIntent.getBroadcast(myContext, NOTIFICATION_REQUEST_CODE, myOtherIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        myRemoteViews.setPendingIntentTemplate(R.id.image_inside, myPendingIntent);

        myAppWidgetManager.updateAppWidget(myAppWidgetID, myRemoteViews);
    }

    /*call when update*/
    @Override
    public void onUpdate(Context context, AppWidgetManager myAppWidgetManager, int[] myAppWidgetIds) {
        for (int myAppWidgetId : myAppWidgetIds) {
            updateSomeWidget(context, myAppWidgetManager, myAppWidgetId);
            Toast.makeText(context, "Widget has been updated! ", Toast.LENGTH_SHORT).show();
        }
    }
}

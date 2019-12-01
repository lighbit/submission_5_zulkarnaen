package com.example.submission5.widget;

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

    /*when receive that widget*/
    @Override
    public void onReceive(Context myContext, Intent myIntents) {
        if (Objects.equals(myIntents.getAction(), TOAST_ACTION)) {
            int viewIndex = myIntents.getIntExtra(EXTRA_ITEM, 0);
            Toast.makeText(myContext, myContext.getString(R.string.choose) + viewIndex, Toast.LENGTH_SHORT).show();
        }
        super.onReceive(myContext, myIntents);
    }

    /*update some widget when set up widget in layout your smart phone*/
    static void updateSomeWidget(Context context, AppWidgetManager appWidgetManager, int idAppWidget) {
        /*Create intent in Widget Service*/
        Intent i = new Intent(context, MyWidgetService.class);
        i.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, idAppWidget);
        i.setData(Uri.parse(i.toUri(Intent.URI_INTENT_SCHEME)));
        i.setData(Uri.parse(i.toUri(Intent.URI_INTENT_SCHEME)));

        /*Create Remote Views in your xml*/
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.my_favorite_widget);
        rv.setRemoteAdapter(R.id.image_inside, i);
        rv.setEmptyView(R.id.image_inside, R.id.empty_view);

        /*call MyFavoriteWidget to create toast action and extra app widget*/
        Intent ti = new Intent(context, MyFavoriteWidget.class);
        ti.setAction(MyFavoriteWidget.TOAST_ACTION);
        ti.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, idAppWidget);

        /*Update*/
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, ti, PendingIntent.FLAG_UPDATE_CURRENT);
        rv.setPendingIntentTemplate(R.id.image_inside, pi);

        appWidgetManager.updateAppWidget(idAppWidget, rv);
    }

    /*call when update*/
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateSomeWidget(context, appWidgetManager, appWidgetId);
        }
    }
}

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
    public void onReceive(Context context, Intent i) {
        if (Objects.equals(i.getAction(), TOAST_ACTION)) {
            int viewIndex = i.getIntExtra(EXTRA_ITEM, 0);
            Toast.makeText(context, context.getString(R.string.choose) + viewIndex, Toast.LENGTH_SHORT).show();
        }
        super.onReceive(context, i);
    }

    /*update some widget when set up widget in layout your smart phone*/
    static void updateSomeWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        /*Create intent in Widget Service*/
        Intent i = new Intent(context, MyWidgetService.class);
        i.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        i.setData(Uri.parse(i.toUri(Intent.URI_INTENT_SCHEME)));
        i.setData(Uri.parse(i.toUri(Intent.URI_INTENT_SCHEME)));

        /*Create Remote Views in your xml*/
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.favourite_widget);
        rv.setRemoteAdapter(R.id.stack_view, i);
        rv.setEmptyView(R.id.stack_view, R.id.empty_view);

        /*call MyFavoriteWidget to create toast action and extra app widget*/
        Intent ti = new Intent(context, MyFavoriteWidget.class);
        ti.setAction(MyFavoriteWidget.TOAST_ACTION);
        ti.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

        /*Update*/
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, ti, PendingIntent.FLAG_UPDATE_CURRENT);
        rv.setPendingIntentTemplate(R.id.stack_view, pi);

        appWidgetManager.updateAppWidget(appWidgetId, rv);
    }

    /*call when update*/
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateSomeWidget(context, appWidgetManager, appWidgetId);
        }
    }

    /*enable*/
    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }
    /*disable*/
    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

}

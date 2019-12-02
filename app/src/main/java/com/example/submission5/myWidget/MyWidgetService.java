package com.example.submission5.myWidget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * @author zulkarnaen
 */
public class MyWidgetService extends RemoteViewsService {

    /*on create*/
    @Override
    public void onCreate() {
        super.onCreate();
    }

    /*Set Remote View Factory for widget*/
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent myIntents) {
        return new MyRemoteViewsFactory(this.getApplicationContext());
    }
}

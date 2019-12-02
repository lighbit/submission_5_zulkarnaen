package com.example.submission5.myNotification;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatDelegate;

/**
 * @author zulkarnaen
 */
public class AppCompatService extends PreferenceActivity {

    /*FOR WHAT U USE FOR default mode or night mode instead*/
    private AppCompatDelegate defaultModeActivity;

    /*OnCreate handler*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getDelegate().installViewFactory();
        getDelegate().onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
    }

    /*getDelegate for other service*/
    private AppCompatDelegate getDelegate() {
        if (defaultModeActivity == null) {
            defaultModeActivity = AppCompatDelegate.create(this, null);
        }
        return defaultModeActivity;
    }

    public ActionBar getSupportActionBar() {
        return getDelegate().getSupportActionBar();
    }

}

package com.example.submission5;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * @author zulkarnaen
 */
public class SplashActivity extends Activity {

    // TODO: GIVE DELAY FOR SPLASH SCREEN
    private static final int MY_SPLASH_TIME = 2 * 1000;// 3 seconds delay

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        // TODO: GIVE TRY CATCH
        try {
            new Handler().postDelayed(new Runnable() {

                public void run() {

                    Intent intent = new Intent(SplashActivity.this,
                            MainActivity.class);
                    startActivity(intent);

                    SplashActivity.this.finish();
                }
            }, MY_SPLASH_TIME);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        this.finish();
        super.onBackPressed();
    }
}
package com.example.submission5.notification;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;

import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.submission5.R;
import com.example.submission5.model.ResultsItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author zulkarnaen
 */
public class DailyUpcomingMovies extends BroadcastReceiver {

    /*MY API KEY*/
    private static final String MY_API_KEY = "d10721892b71839178ae7c4597123c84";
    private static int idNotification = 2000;
    private RequestQueue queueNotification;
    List<ResultsItem> listNotification;


    /*when notification on send notification will be show in you notification*/
    private void sendNewReleaseNotification(Context context, String header, String title, int id, List<ResultsItem> MovieResults) {
        /*set to notification*/
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

        int count = 1;
        for (ResultsItem movie : MovieResults) {
            /*Inside box title notification*/
            inboxStyle.addLine(count++ + ". " + context.getString(R.string.release_text1)
                    + " " + movie.getOriginal_title());
        }

        /*Set title, text and other what you need in your phone when notification available*/
        Uri uriTone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_favorite)
                .setContentTitle(header)
                .setContentText(title)
                .setStyle(inboxStyle)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setVibrate(new long[]{1500, 1500, 1500, 1500, 1500})
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setSound(uriTone);

        /*Different setting for android oreo*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(context.getString(R.string.id_notification),
                    "NOTIFICATION_CHANNEL_NAME", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.YELLOW);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            builder.setChannelId(context.getString(R.string.id_notification));
            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel);
        }
        assert notificationManager != null;
        notificationManager.notify(id, builder.build());
    }

    /*get data when call api*/
    public void getData(final Context context, String url) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        final String today = dateFormat.format(date);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("results");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject data = jsonArray.getJSONObject(i);
                        ResultsItem movieItem = new ResultsItem();
                        movieItem.setOriginal_title(data.getString("original_title"));
                        movieItem.setVote_average(data.getString("vote_average"));
                        movieItem.setVote_count(data.getString("vote_count"));
                        movieItem.setOverview(data.getString("overview"));
                        movieItem.setPoster_path(data.getString("poster_path"));
                        if (data.getString("release_date").equals(today)) {
                            listNotification.add(movieItem);
                        }
                    }

                    String header = context.getString(R.string.header_daily);
                    String title = context.getString(R.string.today_release);
                    new ResultsItem();
                    sendNewReleaseNotification(context, header, title, idNotification, listNotification);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        queueNotification.add(request);
    }

    /*do it in background when data come*/
    @SuppressLint("StaticFieldLeak")
    public class getTaskForMovies extends AsyncTask<String, Void, Void> {

        private Context context;

        getTaskForMovies(Context context) {
            this.context = context;
        }

        @Override
        protected Void doInBackground(String... strings) {
            getData(context, strings[0]);
            return null;
        }
    }

    /*call API to base for movie new update*/
    private void callApiMovie(Context context) {
        getTaskForMovies getDataAsync = new getTaskForMovies(context);
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String getDateFormat = dateFormat.format(date);
        getDataAsync.execute("https://api.themoviedb.org/3/discover/movie?api_key=" + MY_API_KEY
                + "&primary_release_date.gte=" + getDateFormat + "&primary_release_date.lte=" + getDateFormat);
    }

    /*handle when data is coming*/
    @Override
    public void onReceive(Context context, Intent intent) {
        listNotification = new ArrayList<>();
        queueNotification = Volley.newRequestQueue(context);
        callApiMovie(context);

    }

    /*set pending when alarm change on or of*/
    private static PendingIntent getPending(Context context) {
        Intent intent = new Intent(context, DailyUpcomingMovies.class);
        return PendingIntent.getBroadcast(context, 1011, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    /*You can disable alarm when you don't needed*/
    public void cancelAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        assert alarmManager != null;
        alarmManager.cancel(getPending(context));

        /*toast will be show when you off that daily notification*/
        Toast.makeText(context, R.string.up_notif_off, Toast.LENGTH_SHORT).show();
    }

    /*Set alarm every monday, maybe after your break in your house or kostan
    you can see some new movies for tomorrow with your girlfriend ;) or.. alone i guest */
    public void settingAlarm(Context context) {
        int delay = 0;
        cancelAlarm(context);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, DailyUpcomingMovies.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                100, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, 2);
        calendar.set(Calendar.HOUR_OF_DAY, 6);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        /*Different setting for android below Marshmallow and above*/
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            assert alarmManager != null;
            alarmManager.setInexactRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis() + delay,
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent
            );
        } else {
            assert alarmManager != null;
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis() + delay, pendingIntent);
        }
        idNotification += 1;
        /*toast will be show when you on that daily notification*/
        Toast.makeText(context, R.string.up_notif_on, Toast.LENGTH_SHORT).show();
    }


}

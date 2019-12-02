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
import com.example.submission5.model.Notification;

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
    private RequestQueue myQueueNotifications;
    List<Notification> listMyDailyNotification;


    /*when notification on send notification will be show in you notification*/
    private void sendNewReleaseNotification(Context myContext, String myHeader, String title, int id, List<Notification> MovieResults) {
        /*set to notification*/
        NotificationManager notificationManager = (NotificationManager) myContext.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

        int count = 1;
        for (Notification movie : MovieResults) {
            /*Inside box title notification*/
            inboxStyle.addLine(count++ + ". " + myContext.getString(R.string.release_text1)
                    + " " + movie.getOriginal_title());
        }

        /*Set title, text and other what you need in your phone when notification available*/
        Uri uriTone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(myContext)
                .setSmallIcon(R.drawable.ic_favorite)
                .setContentTitle(myHeader)
                .setContentText(title)
                .setStyle(inboxStyle)
                .setColor(ContextCompat.getColor(myContext, android.R.color.transparent))
                .setVibrate(new long[]{1500, 1500, 1500, 1500, 1500})
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setSound(uriTone);

        /*Different setting for android oreo*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(myContext.getString(R.string.id_notification),
                    "NOTIFICATION_CHANNEL_NAME", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.YELLOW);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            builder.setChannelId(myContext.getString(R.string.id_notification));
            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel);
        }
        assert notificationManager != null;
        notificationManager.notify(id, builder.build());
    }

    /*get data when call api*/
    public void getDataApi(final Context context, String url) {
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
                        Notification movieItem = new Notification();
                        movieItem.setOriginal_title(data.getString("original_title"));
                        movieItem.setPoster_path(data.getString("poster_path"));
                        if (data.getString("release_date").equals(today)) {
                            listMyDailyNotification.add(movieItem);
                        }
                    }

                    String header = context.getString(R.string.header_daily);
                    String title = context.getString(R.string.today_release);
                    new Notification();
                    sendNewReleaseNotification(context, header, title, idNotification, listMyDailyNotification);

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
        myQueueNotifications.add(request);
    }

    /*do it in background when data come*/
    @SuppressLint("StaticFieldLeak")
    public class getTaskForMovies extends AsyncTask<String, Void, Void> {

        private Context myContext;

        getTaskForMovies(Context myContext) {
            this.myContext = myContext;
        }

        @Override
        protected Void doInBackground(String... strings) {
            getDataApi(myContext, strings[0]);
            return null;
        }
    }

    /*call API to base for movie new update*/
    private void callApiMovie(Context context) {
        getTaskForMovies getDataAsync = new getTaskForMovies(context);
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        /*set string*/
        String my_url = "https://api.themoviedb.org/3/discover/movie?api_key=";
        String graterThan = "&primary_release_date.gte=";
        String lessThan = "&primary_release_date.lte=";

        /*set today*/
        Date date = new Date();
        String getDateFormat = dateFormat.format(date);
        getDataAsync.execute(my_url + MY_API_KEY + graterThan + getDateFormat + lessThan + getDateFormat);
    }

    /*handle when data is coming*/
    @Override
    public void onReceive(Context context, Intent intent) {
        listMyDailyNotification = new ArrayList<>();
        myQueueNotifications = Volley.newRequestQueue(context);
        callApiMovie(context);

    }

    /*set pending when alarm change on or of*/
    private static PendingIntent getPending(Context context) {
        Intent myIntents = new Intent(context, DailyUpcomingMovies.class);
        return PendingIntent.getBroadcast(context, 1011, myIntents, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    /*You can disable alarm when you don't needed*/
    public void cancelMyAlarm(Context myContext) {
        AlarmManager myAlarmManager = (AlarmManager) myContext.getSystemService(Context.ALARM_SERVICE);
        assert myAlarmManager != null;
        myAlarmManager.cancel(getPending(myContext));

        /*toast will be show when you off that daily notification*/
        Toast.makeText(myContext, R.string.up_notif_off, Toast.LENGTH_SHORT).show();
    }

    /*Set alarm on 8 am, maybe before go to wok or school in your house or kostan
    you can see some new movies for tomorrow with your girlfriend ;) or.. alone i guest */
    public void setMyAlarm(Context myContext) {
        int delay = 0;
        cancelMyAlarm(myContext);
        AlarmManager myAlarmManager = (AlarmManager) myContext.getSystemService(Context.ALARM_SERVICE);
        Intent myIntents = new Intent(myContext, DailyUpcomingMovies.class);
        PendingIntent myPendingIntents = PendingIntent.getBroadcast(myContext,
                100, myIntents, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
//        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
//            calendar.add(Calendar.DATE, 1);
//        }
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        /*Different setting for android below Marshmallow and above*/
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            assert myAlarmManager != null;
            myAlarmManager.setInexactRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis() + delay,
                    AlarmManager.INTERVAL_DAY,
                    myPendingIntents
            );
        } else {
            assert myAlarmManager != null;
            myAlarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis() + delay, myPendingIntents);
        }
        idNotification += 1;
        /*toast will be show when you on that daily notification*/
        Toast.makeText(myContext, R.string.up_notif_on, Toast.LENGTH_SHORT).show();
    }


}

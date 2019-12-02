package com.example.submission5.myNotification;

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
import android.os.Build;
import android.widget.Toast;


import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.submission5.MainActivity;
import com.example.submission5.R;

import java.util.Calendar;
import java.util.Objects;

/**
 * @author zulkarnaen
 */
public class DailyPushNotificationForMovie extends BroadcastReceiver {

    /*when notification is coming, you can change text in here*/
    @Override
    public void onReceive(Context myContext, Intent myIntents) {

        sendDailyNotification(myContext, myContext.getString(R.string.header_daily), myContext.getString(R.string.desk_daily));
    }

    /*set pending when alarm change on or of*/
    private static PendingIntent getPendingIntent(Context myContext) {
        Intent myIntents = new Intent(myContext, DailyPushNotificationForMovie.class);
        return PendingIntent.getBroadcast(myContext, R.string.id_notification, myIntents, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    /*You can disable alarm when you don't needed*/
    public void cancelMyAlarm(Context myContext) {
        AlarmManager myAlarmManager = (AlarmManager) myContext.getSystemService(Context.ALARM_SERVICE);
        Objects.requireNonNull(myAlarmManager).cancel(getPendingIntent(myContext));

        /*toast will be show when you off that daily notification*/
        Toast.makeText(myContext, R.string.daily_notif_off, Toast.LENGTH_SHORT).show();
    }


    /*Set alarm every 7 am, maybe after your break fast you can see some movies ;) */
    public void setMyAlarm(Context myContext) {
        cancelMyAlarm(myContext);
        AlarmManager myAlarmManager = (AlarmManager) myContext.getSystemService(Context.ALARM_SERVICE);
        /*Set this time you needed for notification alarm*/
        Intent myIntents = new Intent(myContext, DailyPushNotificationForMovie.class);

        PendingIntent myPendingIntents = PendingIntent.getBroadcast(myContext, 0, myIntents, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 7);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        /*repeating again*/
        assert myAlarmManager != null;
        myAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, myPendingIntents);

        /*Different setting for android below Marshmallow and above*/
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            Objects.requireNonNull(myAlarmManager).setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, getPendingIntent(myContext));
        } else {
            myAlarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), getPendingIntent(myContext));
        }

        /*toast will be show when you on that daily notification*/
        Toast.makeText(myContext, R.string.daily_notif_on, Toast.LENGTH_SHORT).show();
    }

    /*Send notification in configuration set ON*/
    private void sendDailyNotification(Context myContext, String title, String desc) {
        NotificationManager myNotificationManager = (NotificationManager) myContext.getSystemService(
                Context.NOTIFICATION_SERVICE);
        Intent myIntents = new Intent(myContext, MainActivity.class);

        PendingIntent myPendingIntents = PendingIntent.getActivity(myContext, R.string.id_notification, myIntents,
                PendingIntent.FLAG_UPDATE_CURRENT);

        /*Set title, text and other what you need in your phone when notification available*/
        Uri uriTone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(myContext)
                .setSmallIcon(R.drawable.ic_movie)
                .setContentTitle(title)
                .setContentText(desc)
                .setContentIntent(myPendingIntents)
                .setColor(ContextCompat.getColor(myContext, android.R.color.transparent))
                .setVibrate(new long[]{1500, 1500, 1500, 1500, 1500})
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setSound(uriTone);

        /*Different setting for android oreo*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel myNotificationChannels = new NotificationChannel(
                    myContext.getString(R.string.id_notification), myContext.getString(R.string.channel_name),
                    NotificationManager.IMPORTANCE_HIGH);
            myNotificationChannels.enableLights(true);
            myNotificationChannels.setLightColor(Color.YELLOW);
            myNotificationChannels.enableVibration(true);
            myNotificationChannels.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

            builder.setChannelId(myContext.getString(R.string.id_notification));
            assert myNotificationManager != null;
            myNotificationManager.createNotificationChannel(myNotificationChannels);
        }
        assert myNotificationManager != null;
        myNotificationManager.notify(R.string.id_notification, builder.build());

    }

}

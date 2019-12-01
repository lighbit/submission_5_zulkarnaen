package com.example.submission5.notification;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.provider.Settings;

import com.example.submission5.R;

/**
 * @author zulkarnaen
 */
public class MenuSetting extends AppCompatService {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MainFragmentPreference()).commit();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public static class MainFragmentPreference extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

        /*Reminder for Upcoming Movies*/
        DailyUpcomingMovies myDailyUpcomingMovies = new DailyUpcomingMovies();
        /*Reminder for daily Movies*/
        DailyPushNotificationForMovie myDailyPushNotificationForMovies = new DailyPushNotificationForMovie();
        /*Change Reminder*/
        SwitchPreference changeReminder;
        SwitchPreference changeReminderToday;

        /*function if some change in user*/
        @Override
        public boolean onPreferenceChange(Preference myPreference, Object myObject) {
            String key = myPreference.getKey();
            boolean value = (boolean) myObject;
            if (key.equals(getString(R.string.today_reminder))) {
                if (value) {
                    myDailyPushNotificationForMovies.setMyAlarm(getActivity());
                } else {
                    myDailyPushNotificationForMovies.cancelMyAlarm(getActivity());
                }
            } else {
                if (value) {
                    myDailyUpcomingMovies.setMyAlarm(getActivity());
                } else {
                    myDailyUpcomingMovies.cancelMyAlarm(getActivity());
                }
            }
            return true;
        }


        /*sesuaikan dengan ketentuan*/
        @Override
        public void onCreate(final Bundle myInstanceSaved) {

            /*merubah notifikasi on atau off pada menu setting*/
            super.onCreate(myInstanceSaved);
            addPreferencesFromResource(R.xml.main_menu_settings);
            changeReminder = (SwitchPreference) findPreference(getString(R.string.today_reminder));
            changeReminder.setOnPreferenceChangeListener(this);
            changeReminderToday = (SwitchPreference) findPreference(getString(R.string.key_release_reminder));
            changeReminderToday.setOnPreferenceChangeListener(this);
            Preference myPref = findPreference(getString(R.string.key_lang));

            /*Mengganti bahasa pada menu settings*/
            myPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    startActivity(new Intent(Settings.ACTION_LOCALE_SETTINGS));
                    return true;
                }
            });
        }
    }

}

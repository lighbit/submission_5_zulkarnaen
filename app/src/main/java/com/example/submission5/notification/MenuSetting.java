package com.example.submission5.notification;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.provider.Settings;

import com.example.submission5.R;
import com.example.submission5.model.ResultsItem;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zulkarnaen
 */
public class MenuSetting extends AppCompatService {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MainPreferenceFragment()).commit();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public static class MainPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

        /*Reminder for Upcoming Movies*/
        DailyUpcomingMovies dailyUpcomingMovies = new DailyUpcomingMovies();
        /*Reminder for daily Movies*/
        DailyPushNotificationForMovie dailyPushNotificationForMovies = new DailyPushNotificationForMovie();
        /*Change Reminder*/
        SwitchPreference changeReminder;
        SwitchPreference changeReminderToday;
        /*throw notification Listing to result item*/
        List<ResultsItem> notificationListing;

        /*function if some change in user*/
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            String key = preference.getKey();
            boolean value = (boolean) newValue;
            if (key.equals(getString(R.string.today_reminder))) {
                if (value) {
                    dailyPushNotificationForMovies.setAlarm(getActivity());
                } else {
                    dailyPushNotificationForMovies.cancelAlarm(getActivity());
                }
            } else {
                if (value) {

                    dailyUpcomingMovies.settingAlarm(getActivity());
                } else {
                    dailyUpcomingMovies.cancelAlarm(getActivity());
                }
            }
            return true;
        }


        /*sesuaikan dengan ketentuan*/
        @Override
        public void onCreate(final Bundle savedInstanceState) {

            /*merubah notifikasi on atau off pada menu setting*/
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.main_pref);
            notificationListing = new ArrayList<>();
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

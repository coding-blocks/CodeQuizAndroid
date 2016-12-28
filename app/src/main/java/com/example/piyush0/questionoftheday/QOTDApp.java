package com.example.piyush0.questionoftheday;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.evernote.android.job.JobManager;
import com.example.piyush0.questionoftheday.job.DemoJobCreator;
import com.example.piyush0.questionoftheday.job.DemoSyncJob;
import com.example.piyush0.questionoftheday.utils.FontsOverride;

import io.realm.Realm;
import retrofit2.Retrofit;

/**
 * Created by piyush0 on 06/12/16.
 */

public class QOTDApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FontsOverride.setDefaultFont(this, "SANS_SERIF", "fonts/" + FontsOverride.FONT_PROXIMA_NOVA);
        Realm.init(this);
        JobManager.create(this).addJobCreator(new DemoJobCreator());

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (!prefs.getBoolean("firstTime", false)) {

            DemoSyncJob demoSyncJob = new DemoSyncJob();
            demoSyncJob.schedulePeriodicJob(getApplicationContext());

            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstTime", true);
            editor.commit();
        }
    }
}

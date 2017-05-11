package com.inkswipe.SocialSociety;

import android.app.Application;
import android.content.Context;

import com.flurry.android.FlurryAgent;

/**
 * Created by ajinkya.d on 10/5/2016.
 */
public class MyApplication  extends Application {
    //private Tracker mTracker;

    @Override
    public void onCreate() {
        super.onCreate();

        new FlurryAgent.Builder()
                .withLogEnabled(false)
                .build(this, "QTPRGMHYD693T2MFCTN2");
    }
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //MultiDex.install(this);
        // configure Flurry

    }
   /* synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
//            mTracker = analytics.newTracker(R.xml.global_tracker);
        }
        return mTracker;
    }*/
}



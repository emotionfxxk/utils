package com.vivid.utils.demo;

import android.content.Context;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.Map;

/**
 * Created by SeanXu on 2015/11/4.
 */
public class GAManager {
    private final static String TAG = "GAManager";

    /**
     * The Analytics singleton. The field is set in onCreate method override when the application
     * class is initially created.
     */
    private static GoogleAnalytics analytics;

    /**
     * The default app tracker. The field is from onCreate callback when the application is
     * initially created.
     */
    private static Tracker tracker;
    private static boolean initialized = false;

    public static void init(Context context) {
        if (!initialized) {
            analytics = GoogleAnalytics.getInstance(context);
            tracker = analytics.newTracker("UA-63223111-4");
            tracker.enableExceptionReporting(true);
            tracker.enableAdvertisingIdCollection(true);
            tracker.enableAutoActivityTracking(true);
            initialized = true;
        }
    }

    /**
     * Access to the global Analytics singleton. If this method returns null you forgot to either
     * set android:name="&lt;this.class.name&gt;" attribute on your application element in
     * AndroidManifest.xml or you are not setting this.analytics field in onCreate method override.
     */
    public static GoogleAnalytics analytics() {
        return analytics;
    }

    /**
     * The default app tracker. If this method returns null you forgot to either set
     * android:name="&lt;this.class.name&gt;" attribute on your application element in
     * AndroidManifest.xml or you are not setting this.tracker field in onCreate method override.
     */
    public static Tracker tracker() {
        return tracker;
    }

    public static void reportUserId(String userId) {
        Map<String, String> event = new HitBuilders.EventBuilder()
                .setCategory("UserId")
                .setAction(userId)
                .setLabel(userId)
                .build();
        tracker.send(event);
    }

}

package com.nnd.flipwords;

import android.app.Application;

import com.nnd.flipwords.data.ApplicationComponent;
import com.nnd.flipwords.data.DaggerApplicationComponent;
import com.nnd.flipwords.data.NetworkModule;

import timber.log.Timber;

/**
 * Created by Android dev on 7/13/17.
 */

public class FlipWordsApp extends Application {
    private static ApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());

        createNewComponent();
    }

    private static void createNewComponent() {
        component = DaggerApplicationComponent.builder().networkModule(new NetworkModule()).build();
    }

    public static ApplicationComponent getComponent() {
        if (component == null) createNewComponent();

        return component;
    }
}

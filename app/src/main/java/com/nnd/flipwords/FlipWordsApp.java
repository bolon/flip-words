package com.nnd.flipwords;

import android.app.Application;
import android.content.Context;

import com.nnd.flipwords.data.ApplicationComponent;
import com.nnd.flipwords.data.DaggerApplicationComponent;
import com.nnd.flipwords.data.DataModule;
import com.nnd.flipwords.data.NetworkModule;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import timber.log.Timber;

/**
 * Created by Android dev on 7/13/17.
 */

public class FlipWordsApp extends Application {
    private static ApplicationComponent component;

    private static void createNewComponent(Context context) {
        component = DaggerApplicationComponent.builder()
                .networkModule(new NetworkModule())
                .dataModule(new DataModule(context))
                .build();
    }

    public static ApplicationComponent getComponent(Context context) {
        if (component == null) createNewComponent(context);

        return component;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());

        createNewComponent(this);
        Realm.init(this);
    }
}

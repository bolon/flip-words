package com.nnd.flipwords.data;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Android dev on 8/2/17.
 */

@Module
public class DataModule {
    Context context;

    public DataModule(Context context) {
        this.context = context;
    }

    @Provides
    Realm provideRealm() {
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().deleteRealmIfMigrationNeeded()
                .build();

        return Realm.getInstance(realmConfiguration);
    }
}

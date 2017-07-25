package com.nnd.flipwords.data;

import com.nnd.flipwords.presenter.MainActivity;
import com.nnd.flipwords.presenter.SplashScreenActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Android dev on 7/14/17.
 */

@Singleton
@Component(modules = NetworkModule.class)
public interface ApplicationComponent {
    void inject(MainActivity m);
    void inject(SplashScreenActivity s);
}

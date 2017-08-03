package com.nnd.flipwords.data;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import retrofit2.HttpException;
import timber.log.Timber;

/**
 * Created by Android dev on 8/3/17.
 */

public class APIResolver implements Function<Observable<? extends Throwable>, Observable<?>> {
    private int maxRetries;
    private long retryDelayMillis;
    private int retryCount;

    public APIResolver(int maxRetries, long retryDelayMillis) {
        this.maxRetries = maxRetries;
        this.retryDelayMillis = retryDelayMillis;
        this.retryCount = 0;
    }

    @Override
    public Observable<?> apply(Observable<? extends Throwable> attmpts) {
        return attmpts.flatMap((Function<Throwable, ObservableSource<?>>) t -> {
            if (++retryCount < maxRetries) {
                return Observable.timer(retryDelayMillis, TimeUnit.MILLISECONDS);
            }
            return Observable.error(t);
        });
    }
}

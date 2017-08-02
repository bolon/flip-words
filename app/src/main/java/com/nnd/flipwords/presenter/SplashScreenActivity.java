package com.nnd.flipwords.presenter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.nnd.flipwords.FlipWordsApp;
import com.nnd.flipwords.R;
import com.nnd.flipwords.Utils;
import com.nnd.flipwords.data.WordsInterface;
import com.nnd.flipwords.data.mw.ResponseWord;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class SplashScreenActivity extends AppCompatActivity implements Callback<ResponseWord> {
    private static int TOTAL_TO_FETCH = 10;

    @Inject @Named("def") WordsInterface wordsAPI;
    @Inject @Named("rx") WordsInterface wordsAPIRX;
    @Inject Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        FlipWordsApp.getComponent(getApplicationContext()).inject(this);
        RealmResults<ResponseWord> words = realm.where(ResponseWord.class).findAll();

        if (words.isEmpty()) fetchMoreWords();
        else proceed(words.first());
    }

    void fetchMoreWords() {
        Timber.i("Fetch more words...");
        realm.beginTransaction();

        AtomicInteger counter = new AtomicInteger(0);
        AtomicBoolean isStillHere = new AtomicBoolean(true);

        Observable<List<String>> obsDate = Observable.fromArray(Utils.getRandomDate(TOTAL_TO_FETCH));
        obsDate.flatMapIterable(dates -> dates)
                .flatMap(date -> wordsAPIRX.getWordOfTheDayRX(date))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(word -> {
                    if (counter.getAndIncrement() > 5 && isStillHere.get()) {
                        isStillHere.set(false);
                        proceed(realm.where(ResponseWord.class).findFirst());
                    }
                    realm.copyToRealmOrUpdate(word);
                })
                .doOnComplete(() -> realm.commitTransaction())
                .doOnError(t -> Timber.e(t.getMessage()))
                .subscribe();
    }

    /**
     * Next pls
     * @param word
     */
    void proceed(ResponseWord word) {
        startActivity(MainActivity.createIntent(this, word));
        this.finish();
    }

    @Override
    public void onResponse(Call<ResponseWord> call, Response<ResponseWord> response) {
        try {
            proceed(response.body());
        } catch (NullPointerException ex) {
            proceed(null);
        }
    }

    @Override
    public void onFailure(Call<ResponseWord> call, Throwable t) {
        Timber.e("Call : " + call.request().url().toString());
        Timber.e(call.request().url().encodedQuery());
        t.printStackTrace();
        proceed(null);
    }
}

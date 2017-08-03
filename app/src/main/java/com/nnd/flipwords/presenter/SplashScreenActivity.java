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
import timber.log.Timber;

public class SplashScreenActivity extends AppCompatActivity {
    private static int TOTAL_TO_FETCH = 15;

    @Inject @Named("def") WordsInterface wordsAPI;
    @Inject @Named("rx") WordsInterface wordsAPIRX;
    @Inject Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        FlipWordsApp.getComponent(getApplicationContext()).inject(this);
        RealmResults<ResponseWord> words = realm.where(ResponseWord.class).findAll();

        if (words.size() - 2 < 1) fetchMoreWords();
        else proceed();
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
                .doOnNext(word -> realm.copyToRealmOrUpdate(word))
                .doOnComplete(() -> {
                    realm.commitTransaction();
                    proceed();
                })
                .doOnError(t -> Timber.e(t.getMessage()))
                .subscribe();
    }

    /**
     * Next pls
     */
    void proceed() {
        startActivity(MainActivity.createIntent(this));
        this.finish();
    }
}

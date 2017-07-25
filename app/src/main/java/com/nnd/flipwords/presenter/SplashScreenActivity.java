package com.nnd.flipwords.presenter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.nnd.flipwords.FlipWordsApp;
import com.nnd.flipwords.R;
import com.nnd.flipwords.Utils;
import com.nnd.flipwords.data.WordsInterface;
import com.nnd.flipwords.data.mw.ResponseWord;

import javax.inject.Inject;
import javax.inject.Named;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class SplashScreenActivity extends AppCompatActivity implements Callback<ResponseWord> {
    @Inject WordsInterface wordsAPI;
    @Inject @Named("wordnik_key") String wordnikKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        FlipWordsApp.getComponent().inject(this);
        wordsAPI.getWordOfTheDay(Utils.getRandomDate(), wordnikKey).enqueue(this);
    }

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
        Timber.e(call.request().url().toString());
        t.printStackTrace();
        proceed(null);
    }
}

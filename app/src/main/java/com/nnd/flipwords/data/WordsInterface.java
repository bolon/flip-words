package com.nnd.flipwords.data;

import com.nnd.flipwords.data.mw.ResponseWord;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Android dev on 7/14/17.
 */

public interface WordsInterface {
    @GET("wordOfTheDay")
    Call<ResponseWord> getWordOfTheDay(@Query("date") String date);

    @GET("wordOfTheDay")
    Observable<ResponseWord> getWordOfTheDayRX(@Query("date") String date);
}

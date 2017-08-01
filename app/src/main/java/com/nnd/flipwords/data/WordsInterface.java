package com.nnd.flipwords.data;

import com.nnd.flipwords.data.mw.ResponseWord;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Android dev on 7/14/17.
 */

public interface WordsInterface {
    @GET("wordOfTheDay")
    Call<ResponseWord> getWordOfTheDay(@Query("date") String date);
}

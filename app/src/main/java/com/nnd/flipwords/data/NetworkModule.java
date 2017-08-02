package com.nnd.flipwords.data;

import com.nnd.flipwords.BuildConfig;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;
import timber.log.Timber;

/**
 * Created by Android dev on 7/14/17.
 */

@Module
public class NetworkModule {
    //Merriam - Webster dict
    private final static String BASE_API_URL = "http://www.dictionaryapi.com/api/v1/references/";
    private final static String API_DICT_TYPE = "collegiate/";
    private final static String API_MIME_TYPE = "xml/";
    private final static String API_URL = BASE_API_URL + API_DICT_TYPE + API_MIME_TYPE;

    //Wordnik
    private final static String BASE_API_URL_WORDNIK = "http://api.wordnik.com:80/v4/";
    private final static String API_WORDS = "words.json/";
    private final static String API_URL_WORDNIK = BASE_API_URL_WORDNIK + API_WORDS;

    public NetworkModule() {}

    @Provides
    @Named("wordnik_key")
    String provideAPIKey() {
        return BuildConfig.API_KEY_WORDNIK;
    }

    @Provides
    @Singleton
    @Named("intercept_w_api_key")
    OkHttpClient provideCallFactoryIntercept(@Named("wordnik_key") String apiKey) {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder().addInterceptor(chain -> {
            Request request = chain.request();

            HttpUrl appenddedURL = request.url()
                    .newBuilder()
                    .addQueryParameter("api_key", apiKey)
                    .build();

            request = request.newBuilder().url(appenddedURL).build();

            return chain.proceed(request);
        });
        return okHttpClientBuilder.build();
    }

    @Provides
    @Singleton
    SimpleXmlConverterFactory provideSimpleXMLConverterFactory() {
        return SimpleXmlConverterFactory.create();
    }

    @Provides
    @Singleton
    GsonConverterFactory provideGsonConverterFactory() {
        return GsonConverterFactory.create();
    }

    @Provides
    @Singleton
    @Named("rxCFactory")
    Retrofit provideRetrofitRX(GsonConverterFactory gsonConverterFactory, @Named("intercept_w_api_key") OkHttpClient okHttpClient) {
        return new Retrofit.Builder().baseUrl(API_URL_WORDNIK)
                .addConverterFactory(gsonConverterFactory)
                .callFactory(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Provides
    @Named("json_based")
    @Singleton
    Retrofit provideRetrofitJSON(GsonConverterFactory converterFactory, @Named("intercept_w_api_key") OkHttpClient okHttpClient) {
        return new Retrofit.Builder().baseUrl(API_URL_WORDNIK)
                .addConverterFactory(converterFactory)
                .callFactory(okHttpClient)
                .build();
    }

    @Provides
    @Singleton
    @Named("def")
    WordsInterface provideWordsDefInterface(@Named("json_based") Retrofit retrofit) {
        return retrofit.create(WordsInterface.class);
    }

    @Provides
    @Singleton
    @Named("rx")
    WordsInterface provideWordsDefInterfaceRX(@Named("rxCFactory") Retrofit retrofit) {
        return retrofit.create(WordsInterface.class);
    }
}

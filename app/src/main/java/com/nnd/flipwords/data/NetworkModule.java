package com.nnd.flipwords.data;

import com.nnd.flipwords.BuildConfig;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

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
    Call.Factory provideCallFactory() {
        return new OkHttpClient().newBuilder().build();
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
    @Named("xml_based")
    @Singleton
    Retrofit provideRetrofitXML(SimpleXmlConverterFactory converterFactory, Call.Factory callFactory) {
        return new Retrofit.Builder().baseUrl(API_URL)
                .addConverterFactory(converterFactory)
                .callFactory(callFactory)
                .build();
    }

    @Provides
    @Named("json_based")
    @Singleton
    Retrofit provideRetrofitJSON(GsonConverterFactory converterFactory, Call.Factory callFactory) {
        return new Retrofit.Builder().baseUrl(API_URL_WORDNIK)
                .addConverterFactory(converterFactory)
                .callFactory(callFactory)
                .build();
    }

    @Provides
    WordsInterface provideWordsDefInterface(@Named("json_based") Retrofit retrofit) {
        return retrofit.create(WordsInterface.class);
    }
}

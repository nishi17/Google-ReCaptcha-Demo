package com.demo.demo_recaptcha.Retrofit;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitAdapter {

    public static Retrofit retrofit;

    public static RetrofitServers retrofitServers;

    public final static RetrofitServers getFlyrrServices() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        if (RetrofitAdapter.retrofitServers == null) {
            retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl("https://www.google.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            ;
            RetrofitAdapter.retrofitServers = retrofit.create(RetrofitServers.class);
            return RetrofitAdapter.retrofitServers;
        }
        return RetrofitAdapter.retrofitServers;
    }
}

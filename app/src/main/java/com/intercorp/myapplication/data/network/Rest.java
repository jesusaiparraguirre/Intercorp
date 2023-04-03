package com.intercorp.myapplication.data.network;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.DateTypeAdapter;
import com.intercorp.myapplication.data.network.IntercorpServices;
import com.squareup.okhttp.OkHttpClient;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;


/**
 * Created by italo on 7/07/18.
 */

public class Rest {
    private final IntercorpServices webServices;

    private static final String BASE_URL = "https://intercorp-46c0f-default-rtdb.firebaseio.com";

    public Rest(){
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .registerTypeAdapter(Date.class, new DateTypeAdapter())
                .create();

        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setReadTimeout(360, TimeUnit.SECONDS);
        okHttpClient.setConnectTimeout(360, TimeUnit.SECONDS);

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(BASE_URL)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setClient(new OkClient(okHttpClient))
                .setConverter(new GsonConverter(gson))
                .build();

        webServices = restAdapter.create(IntercorpServices.class);
    }


    public Rest(IntercorpServices webServices) {
        this.webServices = webServices;
    }

    public IntercorpServices getWebservices(){
        return webServices;
    }
}

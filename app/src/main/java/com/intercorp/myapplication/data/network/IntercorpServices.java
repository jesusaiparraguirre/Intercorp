package com.intercorp.myapplication.data.network;


import com.google.gson.JsonObject;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Header;
import retrofit.http.POST;


/**
 * Created by italo on 9/06/18.
 */

public interface  IntercorpServices {

    @POST("/.json")
    void generalMethod( @Body JsonObject request, Callback<JsonObject> intercorpCallback);

}

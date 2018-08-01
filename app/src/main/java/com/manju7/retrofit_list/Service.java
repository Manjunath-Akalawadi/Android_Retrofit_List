package com.manju7.retrofit_list;

import com.google.gson.JsonArray;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Service {

    @GET("/bins/alte8")
    Call<JsonArray> readItemsArray();
}

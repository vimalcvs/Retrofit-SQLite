package com.test.myapplication.service;

import com.test.myapplication.model.CallbackMain;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {
    @GET("api.json")
    Call<CallbackMain> getData();
}

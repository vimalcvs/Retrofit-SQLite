package com.test.vimal.service;

import com.test.vimal.main.CallbackMain;
import com.test.vimal.room.CallbackRoom;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {


    @GET("api.json")
    Call<CallbackMain> getData();

    @GET("api.json")
    Call<CallbackRoom> getDataRoom();

}

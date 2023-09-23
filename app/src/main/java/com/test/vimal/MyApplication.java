package com.test.vimal;

import android.app.Application;

import com.google.android.material.color.DynamicColors;
import com.test.vimal.room.db.RoomDB;


public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        RoomDB.getInstance(this);

        DynamicColors.applyToActivitiesIfAvailable(this);
    }
}
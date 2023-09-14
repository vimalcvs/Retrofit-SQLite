package com.test.vimal;

import android.app.Application;

import com.google.android.material.color.DynamicColors;


public class MyApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        DynamicColors.applyToActivitiesIfAvailable(this);
    }
}
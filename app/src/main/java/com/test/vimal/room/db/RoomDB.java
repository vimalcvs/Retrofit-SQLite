package com.test.vimal.room.db;

import static com.test.vimal.service.Constant.DATABASE_NAME;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.test.vimal.room.ModelRoom;

@Database(entities = {ModelRoom.class}, version = 1, exportSchema = false)
public abstract class RoomDB extends RoomDatabase {

    private static RoomDB instance;

    public abstract RoomDao myDao();

    public static synchronized void getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), RoomDB.class, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
    }

}

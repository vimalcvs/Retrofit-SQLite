package com.test.vimal.room.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.test.vimal.room.ModelRoom;

@Database(entities = {ModelRoom.class}, version = 2)
public abstract class RoomDB extends RoomDatabase {

    private static RoomDB instance;

    public static synchronized RoomDB getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), RoomDB.class, "my_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public abstract RoomDao myDao();
}

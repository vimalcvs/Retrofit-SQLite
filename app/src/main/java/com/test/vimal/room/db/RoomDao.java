package com.test.vimal.room.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.test.vimal.room.ModelRoom;

import java.util.List;

@Dao
public interface RoomDao {

    @Query("SELECT * FROM model_room")
    LiveData<List<ModelRoom>> getAllData();

    @Query("SELECT * FROM model_room WHERE id = :roomId")
    LiveData<List<ModelRoom>> getDataById(String roomId);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertData(List<ModelRoom> data);


}

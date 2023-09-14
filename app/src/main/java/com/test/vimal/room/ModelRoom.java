package com.test.vimal.room;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "model_room")
public class ModelRoom  implements Serializable {

    @PrimaryKey
    @NonNull
    public String id;
    public String name;
    public String description;
    public String img;

    public ModelRoom(@NonNull String id, String name, String img, String description) {
        this.id = id;
        this.name = name;
        this.img = img;
        this.description = description;
    }
}

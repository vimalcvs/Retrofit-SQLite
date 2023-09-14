package com.test.vimal.room;


import static com.test.vimal.service.Constant.DATABASE_NAME;
import static com.test.vimal.service.Constant.EXTRA_KEY;

import android.os.Bundle;
import android.text.Html;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.room.Room;

import com.test.vimal.R;
import com.test.vimal.databinding.ActivityDetailBinding;
import com.test.vimal.room.db.RoomDB;

import java.util.List;

public class ActivityRoomDetail extends AppCompatActivity {

    private ActivityDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());

        ModelRoom modelRoom = (ModelRoom) getIntent().getSerializableExtra(EXTRA_KEY);

        String roomId;
        if (modelRoom != null) {
            roomId = modelRoom.id;
            binding.toolbar.setTitle(modelRoom.name);
        } else {
            Toast.makeText(this, "No data available", Toast.LENGTH_SHORT).show();
            return;
        }

        RoomDB roomDB = Room.databaseBuilder(this, RoomDB.class, DATABASE_NAME).build();

        LiveData<List<ModelRoom>> modelRoomData = roomDB.myDao().getDataById(roomId);
        modelRoomData.observe(this, modelRooms -> {
            if (modelRooms != null && !modelRooms.isEmpty()) {
                binding.tvDetails.setText(Html.fromHtml(modelRooms.get(0).description));
            } else {
                binding.tvDetails.setText(R.string.no_data_available);
            }
        });
    }
}
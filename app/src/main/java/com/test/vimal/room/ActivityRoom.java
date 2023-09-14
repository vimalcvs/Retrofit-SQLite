package com.test.vimal.room;

import static com.test.vimal.service.Constant.SUCCESS;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.room.Room;

import com.test.vimal.R;
import com.test.vimal.databinding.ActivityMainBinding;
import com.test.vimal.databinding.LayoutErrorBinding;
import com.test.vimal.main.ActivityMain;
import com.test.vimal.room.db.RoomDB;
import com.test.vimal.service.ApiInterface;
import com.test.vimal.service.RestAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Response;

public class ActivityRoom extends AppCompatActivity {

    private Call<CallbackRoom> call;
    private ActivityMainBinding binding;
    private LayoutErrorBinding bindings;
    private AdapterRoom adapter;
    private RoomDB roomDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        bindings = binding.ivInclude;

        binding.toolbar.setNavigationOnClickListener(v -> startActivity(new Intent(this, ActivityMain.class)));


        roomDB = Room.databaseBuilder(this, RoomDB.class, "my-database").build();

        binding.rvRecycler.setLayoutManager(new StaggeredGridLayoutManager(3, 1));
        adapter = new AdapterRoom(new ArrayList<>(), this);
        binding.rvRecycler.setAdapter(adapter);


        roomDB.myDao().getAllData().observe(this, (Observer<List<ModelRoom>>) myEntities -> {
            if (myEntities.isEmpty()) {
                apiResponse();
            } else {
                adapter.setData(myEntities);
            }
        });

        binding.srSwipe.setOnRefreshListener(this::apiResponse);
        bindings.btError.setOnClickListener(v -> {
            apiResponse();
            bindings.llError.setVisibility(View.GONE);
        });

    }

    private void apiResponse() {
        bindings.pvProgress.setVisibility(View.VISIBLE);
        ApiInterface anApiInterface = RestAdapter.getClient().create(ApiInterface.class);
        call = anApiInterface.getDataRoom();
        call.enqueue(new retrofit2.Callback<>() {
            @Override
            public void onResponse(@NonNull Call<CallbackRoom> call, @NonNull Response<CallbackRoom> response) {
                bindings.pvProgress.setVisibility(View.GONE);
                bindings.llError.setVisibility(View.GONE);
                binding.srSwipe.setRefreshing(false);
                CallbackRoom resp = response.body();
                if (resp != null && resp.status.equals(SUCCESS)) {
                    insertDataIntoRoom(resp.data);
                } else {
                    bindings.pvProgress.setVisibility(View.GONE);
                    binding.srSwipe.setRefreshing(false);
                    bindings.llError.setVisibility(View.VISIBLE);
                    bindings.tvError.setText(R.string.no_data_available);
                    bindings.ivError.setImageResource(R.drawable.icon_data_empty);
                }
            }

            @Override
            public void onFailure(@NonNull Call<CallbackRoom> call, @NonNull Throwable t) {
                binding.srSwipe.setRefreshing(false);
                bindings.pvProgress.setVisibility(View.GONE);
                bindings.llError.setVisibility(View.VISIBLE);
                bindings.ivError.setImageResource(R.drawable.icon_data_net);
                bindings.tvError.setText(R.string.no_internet_connection);
            }

        });
    }

    private void insertDataIntoRoom(List<ModelRoom> data) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> roomDB.myDao().insertData(data));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (call != null && call.isExecuted()) {
            call.cancel();
        }
    }
}

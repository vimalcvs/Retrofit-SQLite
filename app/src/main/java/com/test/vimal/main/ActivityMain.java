package com.test.vimal.main;

import static com.test.vimal.service.Constant.EXTRA_KEY;
import static com.test.vimal.service.Constant.SUCCESS;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.test.vimal.R;
import com.test.vimal.databinding.ActivityMainBinding;
import com.test.vimal.databinding.LayoutErrorBinding;
import com.test.vimal.room.ActivityRoom;
import com.test.vimal.service.ApiInterface;
import com.test.vimal.service.RestAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class ActivityMain extends AppCompatActivity {

    private Call<CallbackMain> call;
    private DatabaseHandler databaseHandler;
    private List<ModelMain> modelMainList;
    private ActivityMainBinding binding;
    private LayoutErrorBinding bindings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        bindings = binding.ivInclude;

        binding.toolbar.setNavigationOnClickListener(v -> startActivity(new Intent(this, ActivityRoom.class)));

        databaseHandler = new DatabaseHandler(ActivityMain.this);
        modelMainList = new ArrayList<>();

        loadData();

        binding.srSwipe.setOnRefreshListener(this::loadData);
        bindings.btError.setOnClickListener(v -> {
            apiResponse();
            bindings.pvProgress.setVisibility(View.VISIBLE);
            bindings.llError.setVisibility(View.GONE);
        });

    }

    private void loadData() {
        bindings.pvProgress.setVisibility(View.VISIBLE);
        binding.srSwipe.setRefreshing(true);
        if (databaseHandler.isTableEmpty()) {
            apiResponse();
        } else {
            modelMainList.clear();
            modelMainList = databaseHandler.listContacts();
            binding.rvRecycler.setLayoutManager(new StaggeredGridLayoutManager(3, 1));
            binding.rvRecycler.setAdapter(new AdapterMain(modelMainList, this));

            bindings.pvProgress.setVisibility(View.GONE);
            bindings.llError.setVisibility(View.GONE);
            binding.srSwipe.setRefreshing(false);
        }
    }

    private void apiResponse() {
        ApiInterface anApiInterface = RestAdapter.getClient().create(ApiInterface.class);
        call = anApiInterface.getData();
        call.enqueue(new retrofit2.Callback<>() {
            @Override
            public void onResponse(@NonNull Call<CallbackMain> call, @NonNull Response<CallbackMain> response) {
                bindings.pvProgress.setVisibility(View.GONE);
                bindings.llError.setVisibility(View.GONE);
                binding.srSwipe.setRefreshing(false);
                CallbackMain resp = response.body();
                if (resp != null && resp.status.equals(SUCCESS)) {
                    databaseHandler.insertDetails(resp.data);
                    loadData();
                } else {
                    bindings.pvProgress.setVisibility(View.GONE);
                    binding.srSwipe.setRefreshing(false);
                    bindings.llError.setVisibility(View.VISIBLE);
                    bindings.tvError.setText(R.string.no_data_available);
                    bindings.ivError.setImageResource(R.drawable.icon_data_empty);
                }
            }

            @Override
            public void onFailure(@NonNull Call<CallbackMain> call, @NonNull Throwable t) {
                binding.srSwipe.setRefreshing(false);
                bindings.pvProgress.setVisibility(View.GONE);
                bindings.llError.setVisibility(View.VISIBLE);
                bindings.ivError.setImageResource(R.drawable.icon_data_net);
                bindings.tvError.setText(R.string.no_internet_connection);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (call != null && call.isExecuted()) {
            call.cancel();
        }
    }
}

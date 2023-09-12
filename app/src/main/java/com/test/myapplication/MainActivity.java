package com.test.myapplication;

import static com.test.myapplication.model.Constant.SUCCESS;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.test.myapplication.databinding.ActivityMainBinding;
import com.test.myapplication.db.DatabaseHandler;
import com.test.myapplication.model.AdapterMain;
import com.test.myapplication.model.CallbackMain;
import com.test.myapplication.model.ModelMain;
import com.test.myapplication.service.ApiInterface;
import com.test.myapplication.service.RestAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private DatabaseHandler databaseHandler;
    private ArrayList<ModelMain> modelMainList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        databaseHandler = new DatabaseHandler(this);
        modelMainList = new ArrayList<>();

        binding.srSwipe.setOnRefreshListener(this::loadData);
        loadData();
    }

    private void loadData() {
        binding.pvProgress.setVisibility(View.VISIBLE);
        binding.srSwipe.setRefreshing(true);

        if (databaseHandler.isTableEmpty()) {
            apiResponse();
        } else {
            modelMainList = databaseHandler.listContacts();
            binding.rvRecycler.setLayoutManager(new StaggeredGridLayoutManager(3, 1));
            binding.rvRecycler.setAdapter(new AdapterMain(modelMainList, this));

            binding.pvProgress.setVisibility(View.GONE);
            binding.srSwipe.setRefreshing(false);
        }
    }

    private void apiResponse() {

        ApiInterface anApiInterface = RestAdapter.getClient().create(ApiInterface.class);
        Call<CallbackMain> call = anApiInterface.getData();
        call.enqueue(new retrofit2.Callback<>() {
            @Override
            public void onResponse(@NonNull Call<CallbackMain> call, @NonNull Response<CallbackMain> response) {
                binding.pvProgress.setVisibility(View.GONE);
                binding.srSwipe.setRefreshing(false);
                CallbackMain resp = response.body();
                if (resp != null && resp.status.equals(SUCCESS)) {
                    databaseHandler.insertDetails(resp.data);
                    loadData();
                } else {
                    binding.pvProgress.setVisibility(View.GONE);
                    binding.srSwipe.setRefreshing(false);
                    Toast.makeText(MainActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CallbackMain> call, @NonNull Throwable t) {
                binding.srSwipe.setRefreshing(false);
                binding.pvProgress.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Network request failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

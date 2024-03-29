package com.test.vimal.main;


import static com.test.vimal.service.Constant.EXTRA_KEY;

import android.os.Bundle;
import android.text.Html;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.test.vimal.R;
import com.test.vimal.databinding.ActivityDetailBinding;

import java.util.List;

public class ActivityMainDetail extends AppCompatActivity {

    public DatabaseHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityDetailBinding binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());

        ModelMain modelMain = (ModelMain) getIntent().getSerializableExtra(EXTRA_KEY);
        dbHandler = new DatabaseHandler(ActivityMainDetail.this);

        if (modelMain != null) {
            List<ModelMain> list = dbHandler.readData(modelMain.id);
            if (!list.isEmpty()) {
                binding.toolbar.setTitle(modelMain.name);
                binding.tvDetails.setText(Html.fromHtml(list.get(0).description));
            } else {
                binding.tvDetails.setText(R.string.no_data_available);
            }
        } else {
            Toast.makeText(this, "No data available", Toast.LENGTH_SHORT).show();
        }
    }

}

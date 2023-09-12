package com.test.myapplication.model;

import static com.test.myapplication.model.Constant.EXTRA_KEY;
import static com.test.myapplication.model.Constant.IMAGE_PATH;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.test.myapplication.ActivityDetail;
import com.test.myapplication.databinding.ItemListBinding;

import java.util.List;

public class AdapterMain extends RecyclerView.Adapter<AdapterMain.ViewHolder> {

    private final List<ModelMain> list;
    Context context;

    public AdapterMain(List<ModelMain> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModelMain modelMain = list.get(position);
        holder.binding.tvTitle.setText(modelMain.name);

        Picasso.get()
                .load(IMAGE_PATH + modelMain.img)
                .into(holder.binding.ivImage);

        holder.binding.cvCard.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(context, ActivityDetail.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(EXTRA_KEY, modelMain);
                context.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final ItemListBinding binding;

        public ViewHolder(ItemListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}

package com.example.bakeryapplication.Controllers;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bakeryapplication.Activities.ViewAllActivity;
import com.example.bakeryapplication.Helpers.BakeryHelper;
import com.example.bakeryapplication.Models.CategoriesFragmentModel;
import com.example.bakeryapplication.R;
import com.example.bakeryapplication.databinding.CategoryViewHolderBinding;

import java.util.ArrayList;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder> {
private Context context ;
    private ArrayList<CategoriesFragmentModel> items ;
    private CategoryViewHolderBinding binding ;


    public CategoriesAdapter(Context context, ArrayList<CategoriesFragmentModel> items) {
        this.context = context;
        this.items = items;
    }
    @NonNull
    @Override
    public CategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding =CategoryViewHolderBinding.inflate(LayoutInflater.from(context),parent,false);
        return new CategoriesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesViewHolder holder, int position) {
        CategoriesFragmentModel currentItem = items.get(position);

        if (currentItem.getImageUrl() != null)
            Glide.with(context).load(Uri.parse(currentItem.getImageUrl())).into(holder.bin.ImageCategoryVh);
        holder.bin.categoryNameTv.setText(currentItem.getName());


        holder.itemView.setOnClickListener(v -> {
            Intent catIntent = new Intent(context, ViewAllActivity.class);
            catIntent.putExtra("type",currentItem.getType());
            startActivity(context,catIntent,null);
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class CategoriesViewHolder extends RecyclerView.ViewHolder {
        CategoryViewHolderBinding bin ;
        public CategoriesViewHolder(@NonNull CategoryViewHolderBinding itemView) {
            super(itemView.getRoot());
            bin = itemView ;
         }
    }
}

package com.example.bakeryapplication.Controllers;



import static androidx.core.content.ContextCompat.startActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bakeryapplication.Activities.ViewAllActivity;
import com.example.bakeryapplication.Models.HomeFragmentCategoriesModel;
import com.example.bakeryapplication.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class HomeCategorisesAdapter extends RecyclerView.Adapter<HomeCategorisesAdapter.CategoriesRvViewHolder> {

    private final ArrayList<HomeFragmentCategoriesModel> items;
    private int rowIndex = -1;
Context context ;
    public HomeCategorisesAdapter(ArrayList<HomeFragmentCategoriesModel> static_models, Context context) {
        this.items = static_models;
        this.context = context ;
    }


    @NonNull
    @Override
    public CategoriesRvViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_catigories_rv_item, parent, false);
        return new CategoriesRvViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesRvViewHolder holder, @SuppressLint("RecyclerView") int position) {
        HomeFragmentCategoriesModel currentItem = items.get(position);
        if (currentItem.getImage() != null && ! currentItem.getImage().isEmpty())
            Glide.with(context).load(Uri.parse(currentItem.getImage())).into(holder.categoriseIv);
        else
            holder.categoriseIv.setImageResource(R.drawable.m);

        holder.categoriesTv.setText(currentItem.getTitle());
        holder.categoriesLinearLayout.setOnClickListener(v -> {
            rowIndex = position;
            notifyDataSetChanged();
            Intent viewAllIntent = new Intent(context, ViewAllActivity.class);
            viewAllIntent.putExtra("type",currentItem.getType());
            startActivity(context,viewAllIntent,null);
        });

        if (rowIndex == position) {
            holder.categoriesLinearLayout.setBackgroundResource(R.drawable.rv_item_selected);

        } else {
            holder.categoriesLinearLayout.setBackgroundResource(R.drawable.static_rv_bg);
        }


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class CategoriesRvViewHolder extends RecyclerView.ViewHolder {

        RoundedImageView categoriseIv;
        TextView categoriesTv;
        LinearLayout categoriesLinearLayout;

        public CategoriesRvViewHolder(@NonNull View itemView) {
            super(itemView);

            categoriseIv = itemView.findViewById(R.id.custom_staticRv_layout_iv);
            categoriesTv = itemView.findViewById(R.id.custom_staticRv_layout_tv);
            categoriesLinearLayout = itemView.findViewById(R.id.custom_staticRv_layout_linearLayout);
        }
    }
}

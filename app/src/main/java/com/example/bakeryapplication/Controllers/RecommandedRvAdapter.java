package com.example.bakeryapplication.Controllers;

import static androidx.core.content.ContextCompat.startActivity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bakeryapplication.Activities.AlterProductActivity;
import com.example.bakeryapplication.CartDetailsActivity;
import com.example.bakeryapplication.Helpers.BakeryHelper;
import com.example.bakeryapplication.Helpers.FirebaseHelper;
import com.example.bakeryapplication.Models.ProductModel;
import com.example.bakeryapplication.R;
import com.example.bakeryapplication.databinding.RecommandedRvItemBinding;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

public class RecommandedRvAdapter extends RecyclerView.Adapter<RecommandedRvAdapter.RecommandedRvViewHolder> {

    public static final String RECOMMANDED_ITEM_KEY = "recommandedItem";
    ArrayList<ProductModel> recommandedItems;
    Context context;

    public RecommandedRvAdapter(ArrayList<ProductModel> recommandedItems, Context context) {
        this.recommandedItems = recommandedItems;
        this.context = context;
    }

    @NonNull
    @Override
    public RecommandedRvViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecommandedRvItemBinding rRvBinding = RecommandedRvItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        RecommandedRvViewHolder viewHolder = new RecommandedRvViewHolder(rRvBinding);
        return viewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecommandedRvViewHolder holder, int position) {
        ProductModel currentItem = recommandedItems.get(position);
        if (currentItem.getPic() != null && !currentItem.getPic().isEmpty()) {
            setImageFromCloud(currentItem.getName(),holder.bin.recommandedRvItemImage);
        }
        else
            holder.bin.recommandedRvItemImage.setImageResource(R.drawable.k);

        holder.bin.recommandedRvItemItemNameTv.setText(currentItem.getName());
        holder.bin.recommandedRvItemPriceTv.setText("JOD" + currentItem.getFee());
        holder.bin.recommandedRvItemPlusCircle
                .setOnClickListener(v -> {
                    Intent intent = new Intent(context, CartDetailsActivity.class);
                    BakeryHelper.putProductWithIntent(intent, currentItem,position);
                    startActivity(context, intent, null);
                });
        holder.bin.getRoot().setOnLongClickListener(v -> {
            Intent in = new Intent(context, AlterProductActivity.class);
            in.putExtra("productId", currentItem);
            context.startActivity(in);
            return true;
        });



    }

    private void setImageFromCloud(String itemName, ImageView imageItem) {
        FirebaseHelper.getRecommandedImages(itemName).getDownloadUrl()
                .addOnSuccessListener(uri -> {
                    Glide.with(context).load(uri).into(imageItem);

                });
    }

    @Override
    public int getItemCount() {
        return recommandedItems.size();
    }

    public class RecommandedRvViewHolder extends RecyclerView.ViewHolder {
        RecommandedRvItemBinding bin;

        public RecommandedRvViewHolder(@NonNull RecommandedRvItemBinding rvBinding) {
            super(rvBinding.getRoot());
            bin = rvBinding;


        }
    }

}

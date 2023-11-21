package com.example.bakeryapplication.Controllers;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static androidx.core.content.ContextCompat.startActivity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.nfc.Tag;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bakeryapplication.Activities.AlterProductActivity;
import com.example.bakeryapplication.CartDetailsActivity;
import com.example.bakeryapplication.Helpers.BakeryHelper;
import com.example.bakeryapplication.Helpers.FirebaseHelper;
import com.example.bakeryapplication.Models.ProductModel;
import com.example.bakeryapplication.databinding.ViewAllRvItemBinding;

import java.util.ArrayList;
public class ViewAllAdapter extends RecyclerView.Adapter<ViewAllAdapter.ViewAllViewHolder> {

    private Context context;
    private ArrayList<ProductModel> items;
    int positionDeletedItem ;
    private int quantityProduct;
    public ViewAllAdapter(Context context, ArrayList<ProductModel> items) {
        this.context = context;
        this.items = items;
    }
    @NonNull
    @Override
    public ViewAllViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewAllRvItemBinding binding = ViewAllRvItemBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewAllViewHolder(binding);
    }
    @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
    @Override
    public void onBindViewHolder(@NonNull ViewAllViewHolder holder, int position) {
        ProductModel currentItem = items.get(position);
        FirebaseHelper.setProductPicture(context,holder.b.catRvItemIv,currentItem.getName());
        holder.b.catRvItemNameTv.setText(currentItem.getName());
        holder.b.catRvItemPriceTv.setText(currentItem.getFee() + " jod");
        holder.b.quantityProduct.setText(String.valueOf(currentItem.getQuantity()));
        holder.b.productPlusCounter.setOnClickListener(v -> {
            quantityProduct = currentItem.getQuantity();
            quantityProduct++;
            holder.b.quantityProduct.setText(String.valueOf(quantityProduct));
            notifyDataSetChanged();
            currentItem.setQuantity(quantityProduct);

        });
        holder.b.productMinusCounter.setOnClickListener(v -> {

            if (quantityProduct >= 1) {
                quantityProduct--;
                holder.b.quantityProduct.setText(String.valueOf(quantityProduct));
                notifyDataSetChanged();
                currentItem.setQuantity(quantityProduct);

            }
        });
        holder.b.orderNowTv.setOnClickListener(v -> {
            if (Integer.parseInt(holder.b.quantityProduct.getText().toString()) > 0) {
                Intent intent = new Intent(context, CartDetailsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                BakeryHelper.putProductWithIntent(intent, currentItem, position);
                startActivity(context, intent, null);
            }
        });
        holder.b.getRoot().setOnLongClickListener(v -> {
            Intent intent = new Intent(context , AlterProductActivity.class);
            BakeryHelper.putProductWithIntent(intent,currentItem,position);
            context.startActivity(intent);
            return false;
        });



    }
    @Override
    public int getItemCount() {
        return items.size();
    }
    public static class ViewAllViewHolder extends RecyclerView.ViewHolder {
        ViewAllRvItemBinding b;

        public ViewAllViewHolder(@NonNull ViewAllRvItemBinding itemView) {
            super(itemView.getRoot());
            b = itemView;
        }
    }


}

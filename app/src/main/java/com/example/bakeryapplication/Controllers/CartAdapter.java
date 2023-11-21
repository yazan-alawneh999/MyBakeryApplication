package com.example.bakeryapplication.Controllers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bakeryapplication.Helpers.BakeryHelper;
import com.example.bakeryapplication.Helpers.CartManger;
import com.example.bakeryapplication.Helpers.FirebaseHelper;
import com.example.bakeryapplication.Models.CartModel;
import com.example.bakeryapplication.R;
import com.example.bakeryapplication.databinding.CartFragmentViewHolderBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private static final String TAG = "yazan";
    public static final String PASS_DATA_ACTION = "pass_data";
    private ArrayList<CartModel> items;
    private double totalAmount;
    private int quantity;
    Intent intent;
    Context context;
    CartManger cartManger;


    public CartAdapter(ArrayList<CartModel> items, Context context) {
        this.items = items;
        this.intent = new Intent(PASS_DATA_ACTION);
        this.context = context;
        this.cartManger = new CartManger(context);


    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CartFragmentViewHolderBinding cartBinding =
                CartFragmentViewHolderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CartViewHolder(cartBinding);
    }

    @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartModel currentItem = items.get(position);
        // bind item info
        FirebaseHelper.setProductPicture(context, holder.cartBinding.cartViewHolderIv, currentItem.getName());
        holder.cartBinding.cartViewHolderItemFee.setText(currentItem.getFee() + " JOD");
        holder.cartBinding.cartViewHolderLabelTv.setText(currentItem.getName());
        holder.cartBinding.cartViewHolderTotalFee.setText(currentItem.getTotalPrice() + " JOD");
        holder.cartBinding.quantityCart.setText(String.valueOf(currentItem.getQuantity()));
        // plusCounter
        holder.cartBinding.cartViewHolderPlusCounter.setOnClickListener(v ->
        {
            quantity = currentItem.getQuantity();
            quantity++;
            totalAmount = currentItem.getFee() * quantity;
            holder.cartBinding.quantityCart.setText(String.valueOf(quantity));
            holder.cartBinding.cartViewHolderTotalFee.setText(totalAmount + "JOD");
            currentItem.setQuantity(quantity);
            currentItem.setTotalPrice(totalAmount);
            updateItemInFirebase(currentItem.getId(), currentItem);

        });
        // minusCounter
        holder.cartBinding.cartViewHolderMinusCounter.setOnClickListener(v ->
        {
            quantity = currentItem.getQuantity();
            if (quantity == 1) {
                deleteItemFromFirebase(currentItem.getId(), currentItem);
            } else {
                quantity = quantity - 1;
                totalAmount = currentItem.getFee() * quantity;
                holder.cartBinding.quantityCart.setText(String.valueOf(quantity));
                holder.cartBinding.cartViewHolderTotalFee.setText(totalAmount + " JOD");
                currentItem.setQuantity(quantity);
                currentItem.setTotalPrice(totalAmount);
                updateItemInFirebase(currentItem.getId(), currentItem);
                Log.d(TAG, "onClick: minus quantity success");

            }


        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void deleteItemFromFirebase(String id, CartModel item) {
        FirebaseHelper.getAddCartRef()
                .document(id)
                .delete()
                .addOnCompleteListener(task ->
                        {
                            if (task.isSuccessful()) {
                                items.remove(item);
                                notifyDataSetChanged();
                                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                                Log.d(TAG, "onClick: remove item success ");
                            }
                        }
                );
    }

    public void updateItemInFirebase(String itemLocation, CartModel newItem) {
        if (itemLocation != null) {
            FirebaseHelper.getAddCartRef()
                    .document(itemLocation)
                    .set(newItem).addOnSuccessListener(unused ->
                    {
                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                        Log.d(TAG, "onClick: update item success ");

                    });
        } else
            BakeryHelper.showToast(context, "id null ");

    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        CartFragmentViewHolderBinding cartBinding;

        public CartViewHolder(@NonNull CartFragmentViewHolderBinding itemView) {
            super(itemView.getRoot());
            cartBinding = itemView;
        }
    }
}

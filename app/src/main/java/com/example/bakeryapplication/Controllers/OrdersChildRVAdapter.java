package com.example.bakeryapplication.Controllers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakeryapplication.Models.CartModel;
import com.example.bakeryapplication.databinding.OrdersChiledRvHolderBinding;


import java.util.ArrayList;

public class OrdersChildRVAdapter extends RecyclerView.Adapter<OrdersChildRVAdapter.ChildeRVHolder>{
    ArrayList<CartModel> cartItems ;
    Context context ;
    OrdersChiledRvHolderBinding chb ;

    public OrdersChildRVAdapter(ArrayList<CartModel> cartItems, Context context) {
        this.cartItems = cartItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ChildeRVHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        chb = OrdersChiledRvHolderBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ChildeRVHolder(chb);
    }

    @Override
    public void onBindViewHolder(@NonNull ChildeRVHolder holder, int position) {
        CartModel currentItem = cartItems.get(position);
        holder.vhb.itemNameTv.setText(currentItem.getName());
        holder.vhb.quantityTv.setText(currentItem.getQuantity()+"");
        holder.vhb.totalFeeTv.setText(currentItem.getTotalPrice()+" JOD");

    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public class ChildeRVHolder extends RecyclerView.ViewHolder {
        OrdersChiledRvHolderBinding vhb ;
        public ChildeRVHolder(@NonNull OrdersChiledRvHolderBinding itemView) {
            super(itemView.getRoot());
            vhb = itemView ;
        }
    }
}

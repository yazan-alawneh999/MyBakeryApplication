package com.example.bakeryapplication.Controllers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakeryapplication.Models.CartModel;
import com.example.bakeryapplication.Models.OrderModel;
import com.example.bakeryapplication.databinding.OrdersRvHolderBinding;


import java.util.ArrayList;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.MainViewHolder>{
    ArrayList<OrderModel> orders ;
    Context context ;
    OrdersChildRVAdapter adapter ;
    OrdersRvHolderBinding mb ;
    int totalOrders ;

    public OrdersAdapter(ArrayList<OrderModel> orders, Context context) {
        this.orders = orders;
        this.context = context;
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mb = OrdersRvHolderBinding.inflate(LayoutInflater.from(context),parent,false);
        return new MainViewHolder(mb);
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        OrderModel currentOrder = orders.get(position);
        for (CartModel item : currentOrder.getCartItems()){
            totalOrders += item.getTotalPrice() ;
        }

        holder.bvh.totalOrdersVH.setText(totalOrders + "JOD");
        holder.bvh.orderNumberTv.setText("#"+currentOrder.getId());
        holder.bvh.customerNameTv.setText(currentOrder.getCustomerName());
        holder.bvh.customerPhone.setText(currentOrder.getCustomerPhone());

        // child recyclerview setting
        adapter = new OrdersChildRVAdapter(currentOrder.getCartItems() , context) ;
        RecyclerView.LayoutManager manager = new LinearLayoutManager(context);
        holder.bvh.recyclerView2.setAdapter(adapter);
        holder.bvh.recyclerView2.setLayoutManager(manager);
        holder.bvh.recyclerView2.setHasFixedSize(true);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class MainViewHolder extends RecyclerView.ViewHolder {
        OrdersRvHolderBinding bvh ;

        public MainViewHolder(@NonNull OrdersRvHolderBinding itemView) {
            super(itemView.getRoot());
            bvh = itemView ;
        }
    }
}

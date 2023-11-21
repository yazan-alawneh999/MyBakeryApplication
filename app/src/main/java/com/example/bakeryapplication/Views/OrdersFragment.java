package com.example.bakeryapplication.Views;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bakeryapplication.Controllers.OrdersAdapter;
import com.example.bakeryapplication.Models.CartModel;
import com.example.bakeryapplication.Models.OrderModel;
import com.example.bakeryapplication.R;
import com.example.bakeryapplication.databinding.FragmentOrdersBinding;

import java.util.ArrayList;


public class OrdersFragment extends Fragment {

    FragmentOrdersBinding ob ;
    ArrayList<OrderModel> orders ;
    ArrayList<CartModel> cartItems ;

    OrdersAdapter adapter ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ob = FragmentOrdersBinding.inflate(LayoutInflater.from(container.getContext()),container,false) ;
        // set Recyclerview
        setMainRecyclerview() ;

        return ob.getRoot() ;
    }

    private void setMainRecyclerview() {


    }
}
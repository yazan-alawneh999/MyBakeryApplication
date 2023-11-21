package com.example.bakeryapplication.Views;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bakeryapplication.Activities.PlacedOrderActivity;
import com.example.bakeryapplication.Controllers.CartAdapter;
import com.example.bakeryapplication.Helpers.FirebaseHelper;
import com.example.bakeryapplication.Models.CartModel;
import com.example.bakeryapplication.databinding.FragmentCartBinding;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class CartFragment extends Fragment {

    public static final String CART_LIST = "cart_list";
    private ArrayList<CartModel> items;
    private CartAdapter adapter;
    private FragmentCartBinding b;
    private double totalAmount = 0.0;

    private double tax;
    private double total;

    public CartFragment() {
        // Required empty public constructor
    }

    @SuppressLint({"UseRequireInsteadOfGet", "NotifyDataSetChanged"})
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentCartBinding.inflate(inflater, container, false);
        // progress settings
        setInProgress(true);
        // recyclerview settings
        items = new ArrayList<>();
        adapter = new CartAdapter(items,requireContext());
        b.cartFragmentRv.setLayoutManager(new LinearLayoutManager(container.getContext()));
        b.cartFragmentRv.setAdapter(adapter);
        b.cartFragmentRv.setHasFixedSize(true);
        setCartList();
        // check button settings
        b.cartFragmentChickBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), PlacedOrderActivity.class);
            intent.putExtra(CART_LIST, items);
            startActivity(intent);
        });
        // receive total Amount change from cart adapter broad cast
        LocalBroadcastManager.getInstance(Objects.requireNonNull(getContext()))
                .registerReceiver(receiverData, new IntentFilter(CartAdapter.PASS_DATA_ACTION));
       // receive order placed broad cast to set cart empty
        LocalBroadcastManager.getInstance(getContext())
                .registerReceiver(clearCartReceiver, new IntentFilter(PlacedOrderActivity.CLEAR_CART_BROADCAST));


        return b.getRoot();
    }


    @SuppressLint("SetTextI18n")
    private void calculateCard() {
        double taxPercent = 0.02;
        double delivery = 2.00;
        FirebaseHelper.getAddCartRef()
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        totalAmount = 0;
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            CartModel item = document.toObject(CartModel.class);
                            totalAmount += (item.getTotalPrice() * 100.0) / 100.0;
                        }
                        tax = Math.round(totalAmount * taxPercent * 100.0) / 100.0;
                        total = Math.round((totalAmount + delivery) * 100.0) / 100.0 + Math.round(tax * 100.0) / 100.0;

                        b.cartFragmentTotalLayoutTaxValueTv.setText(tax + " jod");
                        b.cartFragmentTotalLayoutTotalItemValueTv.setText(totalAmount + " jod");
                        b.cartFragmentTotalLayoutDeliveryServiceValueTv.setText(delivery + " jod");
                        b.cartFragmentTotalFeeValueTv.setText(total + " jod");
                    }
                    else
                        setCartEmpty(true);
                });


    }

    @SuppressLint("NotifyDataSetChanged")
    private void setCartList() {
        FirebaseHelper.getAddCartRef()
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        Log.d("yazan", "setCartList: cart list not empty ");
                        for (DocumentSnapshot document : queryDocumentSnapshots) {
                            String id = document.getReference().getId() ;
                            Log.d("yazan", "setCartList: itemId : "+ id );
                            CartModel item = document.toObject(CartModel.class);
                            item.setId(id);
                            items.add(item);
                            adapter.notifyDataSetChanged();
                            calculateCard();
                        }
                    setInProgress(false);
                    }
                    else {
                        setCartEmpty(true);
                    }
                });


    }

    public BroadcastReceiver receiverData = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            totalAmount = 0;
            calculateCard();

        }
    };
    public BroadcastReceiver clearCartReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
         setCartEmpty(true);
        }
    };
    public void setInProgress(boolean isProgress) {
        if (isProgress) {
            b.progressBar.setVisibility(View.VISIBLE);
            b.cartScrollView.setVisibility(View.GONE);
        } else {
            b.progressBar.setVisibility(View.GONE);
            b.cartScrollView.setVisibility(View.VISIBLE);
        }
    }

    public void setCartEmpty(boolean isCartEmpty){
        if (isCartEmpty){
            b.cartScrollView.setVisibility(View.GONE);
            b.cartEmptyLayout.setVisibility(View.VISIBLE);
            b.progressBar.setVisibility(View.GONE);
        }
    }
}


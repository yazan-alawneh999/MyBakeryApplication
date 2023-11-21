package com.example.bakeryapplication.Helpers;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.bakeryapplication.CartDetailsActivity;
import com.example.bakeryapplication.Models.CartModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public class CartManger
{
    public static final String TAG = "yazan";
    private boolean isExist = false;
    private String itemLocation;
    Context context;
    ArrayList<CartModel> itemsInCart;


    public CartManger(Context context)
    {
        this.context = context;
        this.itemsInCart = new ArrayList<>();

    }
    public void insertItem(CartModel cartItem)
    {
        if (!itemsInCart.isEmpty())
        // insert item but check if exist first
        {
            Log.d(TAG, "insertItem: itemsInCart is not empty");
            for (CartModel item : itemsInCart) {
                if (cartItem.getName().equals(item.getName()))
                {
                    Log.d(TAG, "insertItem: yes item is exist ");
                    isExist = true;
                    if (isExist)
                        Log.d(TAG, "insertItem:  isExist = true ");
                    else
                        Log.d(TAG, "insertItem:  isExist = false ");
                    itemLocation = item.getId();
                    Log.d(TAG, "insertItem: item location =  " + itemLocation);
                    return;
                }
            }
            if (isExist)
            // update item data set
            {
                Log.d(TAG, "insertItem: item is exist in  "+ itemLocation);
                FirebaseHelper.getUserCartRef().document(itemLocation)
                        .set(cartItem)
                        .addOnSuccessListener(unused -> {
                            BakeryHelper.showToast(context, " itemUpdated");

                        });

            }
            else
            // add item data set
            {
                FirebaseHelper.getAddCartRef()
                        .add(cartItem)
                        .addOnSuccessListener(documentReference -> {
                            cartItem.setId(documentReference.getId());
                            itemsInCart.add(cartItem);
                            BakeryHelper.showToast(context, "item added");

                        });
            }
        }
        else
        // add item data set in cart empty case
        {
            addItemToCart(cartItem);
        }


    }
    public void fillCartItems(CartModel item)
    {

        FirebaseHelper.getUserCartRef()
                .get()
                .addOnSuccessListener(queryDocumentSnapshots ->
                {
                    if (!queryDocumentSnapshots.isEmpty())
                    // not empty cart case
                    // check item existence
                    {
                        Log.d(TAG, "get fromFirebase Success");
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots)
                        {
                            Log.d(TAG, "getItemsInCart: document exist ");
                            CartModel cartModel = document.toObject(CartModel.class);
                            if (document.getReference().getId() != null) {
                                Log.d(TAG, "document id for new Item  = " + document.getReference().getId());
                                cartModel.setId(document.getReference().getId());
                            } else {
                                Log.d(TAG, "getItemsInCart:documentId = null  ");
                            }
                            itemsInCart.add(cartModel);
                            insertItem(item);

                        }

                    }
                    else
                    // empty cart case
                    // add item to cart
                    {
                        addItemToCart(item);
                    }

                });
        if (itemsInCart.size() > 0)
            Log.d(TAG, "  :cart items is not empty   ");
        else
            Log.d(TAG, "  :cart items is  empty   ");

    }
    public void addItemToCart(CartModel item)
    {
        FirebaseHelper.getAddCartRef()
                .add(item)
                .addOnSuccessListener(documentReference -> {
                    item.setId(documentReference.getId());
                    itemsInCart.add(item);
                    Log.d(TAG, "addItemToCart: item added ");

                })

                .addOnFailureListener(e -> {
                    Log.d(TAG, "addItemToCart: failed because :"+ e);

                });

    }




}


package com.example.bakeryapplication.Activities;

import static com.example.bakeryapplication.Views.CartFragment.CART_LIST;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.bakeryapplication.CartDetailsActivity;
import com.example.bakeryapplication.Helpers.CartManger;
import com.example.bakeryapplication.Helpers.FirebaseHelper;
import com.example.bakeryapplication.Models.CartModel;
import com.example.bakeryapplication.Models.UserModel;
import com.example.bakeryapplication.R;
import com.example.bakeryapplication.Views.CartFragment;
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
import java.util.HashMap;
import java.util.Objects;

public class PlacedOrderActivity extends AppCompatActivity
{
    public static final String CLEAR_CART_BROADCAST = "clear_cart";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placed_order);

        ArrayList<CartModel> cartList = (ArrayList<CartModel>) getIntent().getSerializableExtra(CartFragment.CART_LIST);

        if (cartList != null && cartList.size() > 0) {
            for (CartModel item : cartList) {
                final HashMap<String, Object> orderMap = new HashMap<>();
                orderMap.put("name", item.getName());
                orderMap.put("reqUserId", FirebaseHelper.getCurrentUserId());
                orderMap.put("quantity", item.getQuantity());
                orderMap.put("totalPrice", item.getTotalPrice());
                orderMap.put("pic", item.getPic());


               FirebaseHelper.getUserOrderRef()
                        .add(orderMap)
                        .addOnCompleteListener(task ->
                        {
                            if (task.isSuccessful()) {
//                                showNotification();
                                Toast.makeText(PlacedOrderActivity.this, " order successfully", Toast.LENGTH_SHORT).show();
//                                clearCartItems(cartList);
                            }
                        });
            }
        }
    }
    public void showNotification()
    {
        FirebaseHelper.getCurrentUserRef().get()
                .addOnSuccessListener(documentSnapshot -> {
                    UserModel currentUser = documentSnapshot.toObject(UserModel.class);
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"placed order channel ");


                });

    }

    //    private void clearCartItems(ArrayList<CartModel> cartList) {
//        if (cartList != null && cartList.size() > 0) {
//            for (CartModel item : cartList) {
//                db
//                        .collection(CartDetailsActivity.CURRENT_USER_COLLECTION)
//                        .document(Objects.requireNonNull(auth.getCurrentUser()).getUid())
//                        .collection(CartDetailsActivity.ADD_TO_CART_COLLECTION)
//                        .document(item.getId())
//                        .delete()
//                        .addOnSuccessListener(unused -> {
//                            LocalBroadcastManager.getInstance(getApplicationContext())
//                                    .sendBroadcast(new Intent(CLEAR_CART_BROADCAST));
//                            Log.d(CartManger.TAG, "clearCartItems: success ! ");
//
//
//                        })
//                        .addOnFailureListener(e -> {
//                            Log.d(CartManger.TAG, "clearCartItems: failed Cased by :" + e);
//
//                        });
//
//
//            }
//
//
//        }
//    }
}
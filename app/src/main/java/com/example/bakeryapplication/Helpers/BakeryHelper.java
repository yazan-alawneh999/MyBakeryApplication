package com.example.bakeryapplication.Helpers;

import static android.content.Intent.getIntent;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.bakeryapplication.Models.ProductModel;
import com.example.bakeryapplication.Views.MainActivity;
import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;

public class BakeryHelper {

    final static String PRODUCT_PASS_KEY = "PRODUCT";
    public static final String POSITION = "position";
    public static final String DELETE = "delete product";

    public static void showToast(Context context, String Tmessage) {
        Toast.makeText(context, Tmessage, Toast.LENGTH_SHORT).show();
    }

    public static void setProfileImage(Context context, Uri Image, ImageView imageView) {
        if (Image != null) {
            Glide.with(context)
                    .load(Image)
                    .apply(RequestOptions.circleCropTransform())
                    .into(imageView);
        }


    }

    @SuppressLint("SimpleDateFormat")
    public static String timestampToString(Timestamp time) {
        return new SimpleDateFormat("HH:mm").format(time.toDate());

    }

    public static void putProductWithIntent(Intent productIntent, ProductModel product,int product_position) {
        productIntent.putExtra(PRODUCT_PASS_KEY, product);
        productIntent.putExtra(POSITION,product_position);

    }

    public static ProductModel getProductFromIntent(Intent intent) {
        if (intent.getSerializableExtra(PRODUCT_PASS_KEY) != null)
            return (ProductModel) intent.getSerializableExtra(PRODUCT_PASS_KEY);
        return null ;


    }

    public static void returnToHome(Context context){

        context.startActivity(new Intent(context,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

    }



}

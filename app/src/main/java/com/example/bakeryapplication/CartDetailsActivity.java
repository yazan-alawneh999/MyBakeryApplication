package com.example.bakeryapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import com.bumptech.glide.Glide;
import com.example.bakeryapplication.Controllers.RecommandedRvAdapter;
import com.example.bakeryapplication.Helpers.BakeryHelper;
import com.example.bakeryapplication.Helpers.CartManger;
import com.example.bakeryapplication.Helpers.FirebaseHelper;
import com.example.bakeryapplication.Models.CartModel;
import com.example.bakeryapplication.Models.ProductModel;
import com.example.bakeryapplication.Models.RecommandedRvModel;
import com.example.bakeryapplication.Models.ViewAllModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firestore.bundle.BundleElement;


import java.util.HashMap;
import java.util.Objects;

public class CartDetailsActivity extends AppCompatActivity {

    private RecommandedRvModel item;
    private com.example.bakeryapplication.databinding.ActivityCartDetailsBinding b;
    private int numberOfOrder ;
    private double Total;
    CartManger cartManger;
    CartModel cartItem;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // window settings
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // xml inflate by view binding
        b = com.example.bakeryapplication.databinding.ActivityCartDetailsBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        // set tool bar
        setCartToolbar(b.cartTb);
        // init cart manager obj
        cartManger = new CartManger(getApplicationContext());
        // get product
        ProductModel product = BakeryHelper.getProductFromIntent(getIntent());
        if (product != null)
        // set product info
        {
            // calculate total cart
            numberOfOrder = product.getQuantity();
            Total = numberOfOrder * product.getFee();
            setDataItem(product);
            // plus button
            b.plusCounter.setOnClickListener(v -> {
                numberOfOrder += 1;
                Total = numberOfOrder * product.getFee();
                b.quantityTv.setText(String.valueOf(numberOfOrder));
                b.totalPrice.setText("JOD "+Total);

            });
            // minus button
            b.minusCounter.setOnClickListener(v -> {
                if (numberOfOrder > 1) {
                    numberOfOrder -= 1;
                    Total = numberOfOrder * product.getFee();
                    b.quantityTv.setText(String.valueOf(numberOfOrder));
                    b.totalPrice.setText("JOD "+Total );
                }
                else BakeryHelper.returnToHome(this);
            });
            // add to cart button
            b.addToCart.setOnClickListener(v -> {
                cartItem = new CartModel(product.getName(), product.getFee(), numberOfOrder, Total, product.getPic());
                cartManger.fillCartItems(cartItem);
                BakeryHelper.returnToHome(getApplicationContext());
            });

        }

    }

    private void setCartToolbar(Toolbar cartTb) {
        setSupportActionBar(cartTb);
        cartTb.setNavigationOnClickListener(v -> {
            BakeryHelper.returnToHome(this);
        });

    }
    @SuppressLint("SetTextI18n")
    public void setDataItem(ProductModel product) {

        FirebaseHelper.setProductPicture(getApplicationContext(),b.cartDetailsImageView,product.getName());
        b.titleTv.setText(product.getName());
        b.feeTv.setText( "JOD "+product.getFee());
        b.starsTv.setText("" + product.getRate());
        b.timeTv.setText(product.getPrepareTime() + "minute");
        b.calories.setText("" + product.getCalories());
        b.totalPrice.setText("JOD "+ Total);
        b.descriptionTv.setText(product.getDescription());
        b.quantityTv.setText(String.valueOf(numberOfOrder));


    }
}

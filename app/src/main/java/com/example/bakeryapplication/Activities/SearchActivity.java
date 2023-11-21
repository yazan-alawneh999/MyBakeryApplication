package com.example.bakeryapplication.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.os.UserManager;
import android.view.View;
import android.view.WindowManager;

import com.example.bakeryapplication.Controllers.SearchProductAdapter;
import com.example.bakeryapplication.Helpers.BakeryHelper;
import com.example.bakeryapplication.Helpers.FirebaseHelper;
import com.example.bakeryapplication.Models.ProductModel;
import com.example.bakeryapplication.Models.UserModel;
import com.example.bakeryapplication.R;
import com.example.bakeryapplication.databinding.ActivitySearchBinding;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.firestore.Query;

public class SearchActivity extends AppCompatActivity {
    ActivitySearchBinding sBin;
    SearchProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // xml binding
        sBin = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(sBin.getRoot());
        //remove status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //set toolbar
        setSearchToolbar();
        // set search et
        setSearchEt();

    }


    private void setSearchEt() {
        sBin.searchActivityEt.requestFocus();
        sBin.searchIB.setOnClickListener(v -> {
            String txtSearch = sBin.searchActivityEt.getText().toString();
            if (txtSearch.isEmpty() || txtSearch.length() < 3) {
                sBin.searchActivityEt.setError("Invalid product name");
                return;
            }
            setUpSearchRecyclerView(txtSearch);


        });
    }

    private void setUpSearchRecyclerView(String txtSearch) {
        Query query = FirebaseHelper.getNewProductsDocs()
                .whereGreaterThanOrEqualTo("name", txtSearch);

        FirestoreRecyclerOptions<ProductModel> options = new FirestoreRecyclerOptions.Builder<ProductModel>()
                .setQuery(query, ProductModel.class).build();

        adapter = new SearchProductAdapter(options, getApplicationContext());
        sBin.searchRV.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        sBin.searchRV.setAdapter(adapter);
        adapter.startListening();


    }

    private void setSearchToolbar() {
        setSupportActionBar(sBin.searchTb);
        sBin.searchTb.setNavigationOnClickListener(v ->
        {
            BakeryHelper.returnToHome(this);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (adapter != null)
            adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null)
            adapter.startListening();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (adapter != null)
            adapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null)
            adapter.startListening();
    }
}
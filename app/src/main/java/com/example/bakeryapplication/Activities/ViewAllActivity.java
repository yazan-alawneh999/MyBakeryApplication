package com.example.bakeryapplication.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.bakeryapplication.Controllers.ViewAllAdapter;
import com.example.bakeryapplication.Helpers.BakeryHelper;
import com.example.bakeryapplication.Helpers.FirebaseHelper;
import com.example.bakeryapplication.Models.ProductModel;
import com.example.bakeryapplication.R;
import com.example.bakeryapplication.databinding.ActivityViewAllBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class ViewAllActivity extends AppCompatActivity {
    private static final String KAAK_TYPE = "kaak";
    private static final String MAAMOUL_TYPE = "maamoul";
    private static final String HARAIES_TYPE = "Haraies";
    private static final String PASTRIES_TYPE = "Pastries";
    private static final String TYPE_LABEL = "type";
    private ActivityViewAllBinding b;
    private ViewAllAdapter adapter;
    private ArrayList<ProductModel> items;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // fragment view binding
        b = ActivityViewAllBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        //set tool bar
        setToolbar();
        // invoke view products of category adapter
        setAdapter(this);

    }
    @SuppressLint("NotifyDataSetChanged")
    private void setAdapter(Context context) {

        items = new ArrayList<>();
        adapter = new ViewAllAdapter(context, items);
        b.viewAllRv.setLayoutManager(new LinearLayoutManager(context));
        b.viewAllRv.setAdapter(adapter);
        b.viewAllRv.setHasFixedSize(true);

        //get type intent
        String type = getIntent().getStringExtra(TYPE_LABEL);
        // getting kaak
        if (type != null && type.equalsIgnoreCase(KAAK_TYPE)) {
            FirebaseHelper.getKaakCategory()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documents : task.getResult()) {
                                String id = documents.getReference().getId();
                                ProductModel item = documents.toObject(ProductModel.class);
                                item.setId(id);
                                items.add(item);
                                adapter.notifyDataSetChanged();
                            }
                        }

                    });
        }
        // getting maamoul
        if (type != null && type.equalsIgnoreCase(MAAMOUL_TYPE)) {
            FirebaseHelper.getMaamoulCategory()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documents : task.getResult()) {
                                String id = documents.getReference().getId();
                                ProductModel item = documents.toObject(ProductModel.class);
                                item.setId(id);
                                items.add(item);
                                adapter.notifyDataSetChanged();
                            }
                        }

                    });
        }
        // getting Haraies
        if (type != null && type.equalsIgnoreCase(HARAIES_TYPE)) {
            FirebaseHelper.getHaraiesCategory()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documents : task.getResult()) {
                                String id = documents.getReference().getId();
                                ProductModel item = documents.toObject(ProductModel.class);
                                item.setId(id);
                                items.add(item);
                                adapter.notifyDataSetChanged();
                            }
                        }

                    });
        }
        // getting Pastries
        if (type != null && type.equalsIgnoreCase(PASTRIES_TYPE)) {
            FirebaseHelper.getPastriesCategory()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documents : task.getResult()) {
                                String id = documents.getReference().getId() ;
                                ProductModel item = documents.toObject(ProductModel.class);
                                item.setId(id);
                                items.add(item);
                                adapter.notifyDataSetChanged();
                            }
                        }

                    });
        }
    }
    private void setToolbar(){
        setSupportActionBar(b.toolbar2);
        Objects.requireNonNull(getSupportActionBar())
                .setDisplayHomeAsUpEnabled(true);
        b.toolbar2.setNavigationOnClickListener(v -> BakeryHelper.returnToHome(this));

    }
}



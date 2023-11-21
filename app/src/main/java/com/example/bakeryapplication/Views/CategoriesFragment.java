package com.example.bakeryapplication.Views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bakeryapplication.Controllers.CategoriesAdapter;
import com.example.bakeryapplication.Controllers.ViewAllAdapter;
import com.example.bakeryapplication.Helpers.FirebaseHelper;
import com.example.bakeryapplication.Models.CategoriesFragmentModel;
import com.example.bakeryapplication.Models.ViewAllModel;
import com.example.bakeryapplication.databinding.FragmentCategoriesBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class CategoriesFragment extends Fragment {
    private FragmentCategoriesBinding binding;
    private ArrayList<CategoriesFragmentModel> items;
    private CategoriesAdapter adapter;


    public CategoriesFragment() {
        // Required empty public constructor
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCategoriesBinding.inflate(LayoutInflater.from(container.getContext()), container, false);

        // invoke categories labels adapter
        setAdapter(container.getContext());

        return binding.getRoot();

    }

    @SuppressLint("NotifyDataSetChanged")
    private void setAdapter(Context context) {
        items = new ArrayList<>();
        adapter = new CategoriesAdapter(context, items);
        binding.categoriesFragmentRv.setAdapter(adapter);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true);
        binding.categoriesFragmentRv.setLayoutManager(manager);
        binding.categoriesFragmentRv.setHasFixedSize(true);

        FirebaseHelper.getFragmentCategoriesModelRef()
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            CategoriesFragmentModel item = documentSnapshot.toObject(CategoriesFragmentModel.class);
                            items.add(item);
                            adapter.notifyDataSetChanged();
                        }
                    } else
                        Log.d("yazan", "Error: " + task.getException());

                });

    }

}


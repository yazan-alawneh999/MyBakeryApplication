package com.example.bakeryapplication.Views;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bakeryapplication.Activities.SearchActivity;
import com.example.bakeryapplication.Controllers.HomeCategorisesAdapter;
import com.example.bakeryapplication.Controllers.RecommandedRvAdapter;
import com.example.bakeryapplication.Helpers.BakeryHelper;
import com.example.bakeryapplication.Helpers.FirebaseHelper;
import com.example.bakeryapplication.Models.HomeFragmentCategoriesModel;
import com.example.bakeryapplication.Models.ProductModel;
import com.example.bakeryapplication.Models.RecommandedRvModel;
import com.example.bakeryapplication.Models.UserModel;
import com.example.bakeryapplication.databinding.FragmentHomeBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;


public class HomeFragment extends Fragment {
    FragmentHomeBinding homeBinding;
    private ArrayList<HomeFragmentCategoriesModel> categoriesItems;
    private ArrayList<ProductModel> recommandedItems;
    private HomeCategorisesAdapter categoriesAdapter;
    private RecommandedRvAdapter recommandedAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }
    @SuppressLint("NotifyDataSetChanged")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // inflate fragment with view binding
        homeBinding = FragmentHomeBinding.inflate(inflater, container, false);
        // set user info
        onUserInfoUpdate();
        // set progressbar
        setInProgress(true);
        // categories Rv configuration
        setCategoriesAdapter(container.getContext());
        // Recommanded RecyclerView configuration
        setRecommandedRvAdapter(container.getContext());
        // set search bar functionality
        setSearchFunction();
        return homeBinding.getRoot();
    }

    private void setSearchFunction()
    {
        homeBinding.homeFragmentSearchEt.setOnFocusChangeListener((v, hasFocus) ->
        {
            if (hasFocus) {
                Intent intent = new Intent(requireContext(), SearchActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }
    @SuppressLint("NotifyDataSetChanged")
    private void setRecommandedRvAdapter(Context context)
    {
        recommandedItems = new ArrayList<>();
        homeBinding.homeFragmentRecommandedRv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        recommandedAdapter = new RecommandedRvAdapter(recommandedItems, context);
        homeBinding.homeFragmentRecommandedRv.setAdapter(recommandedAdapter);
        homeBinding.homeFragmentRecommandedRv.setHasFixedSize(true);

        FirebaseHelper.getNewProductsDocs()
                .get()
                .addOnCompleteListener(task ->
                {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String id = document.getReference().getId() ;
                            Log.d("yazan", "setRecommandedRvAdapter: "+ id );
                            ProductModel item = document.toObject(ProductModel.class);
                            item.setId(id);
                            if (item.isRecommanded())
                                recommandedItems.add(item);
                            recommandedAdapter.notifyDataSetChanged();
                            setInProgress(false);
                        }
                    } else {
                        BakeryHelper.showToast(getActivity(), "Error" + task.getException());
                    }
                });
    }
    @SuppressLint("NotifyDataSetChanged")
    private void setCategoriesAdapter(Context context)
    {

        categoriesItems = new ArrayList<>();
        categoriesAdapter = new HomeCategorisesAdapter(categoriesItems, context);
        homeBinding.homeFragmentCategoriesRv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        homeBinding.homeFragmentCategoriesRv.setAdapter(categoriesAdapter);
        homeBinding.homeFragmentCategoriesRv.setHasFixedSize(true);

        FirebaseHelper.getCategoriesRvReference()
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            HomeFragmentCategoriesModel item = documentSnapshot.toObject(HomeFragmentCategoriesModel.class);
                            categoriesItems.add(item);
                            categoriesAdapter.notifyDataSetChanged();

                        }

                    } else {
                        BakeryHelper.showToast(getContext(), "Error" + task.getException());
                    }

                });
    }
    public void onUserInfoUpdate()
    {
        FirebaseHelper.getCurrentUserRef()
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    UserModel user = documentSnapshot.toObject(UserModel.class);
                    if (user != null) {
                        if (user.getImage() != null)
                            BakeryHelper.setProfileImage(getContext(), Uri.parse(user.getImage()), homeBinding.homeFragmentProfileIv);
                        homeBinding.homeFragmentUsernameTv.setText(user.getName());
                    }
                });

    }
    public void setInProgress(boolean inProgress)
    {
        if (inProgress) {
            homeBinding.loginScrollView.setVisibility(View.GONE);
            homeBinding.loginPrbar.setVisibility(View.VISIBLE);
        } else {
            homeBinding.loginScrollView.setVisibility(View.VISIBLE);
            homeBinding.loginPrbar.setVisibility(View.GONE);
        }
    }
}
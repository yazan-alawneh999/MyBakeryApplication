package com.example.bakeryapplication.Controllers;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bakeryapplication.CartDetailsActivity;
import com.example.bakeryapplication.Helpers.BakeryHelper;
import com.example.bakeryapplication.Models.ProductModel;
import com.example.bakeryapplication.R;
import com.example.bakeryapplication.databinding.SearchProductViewHolderBinding;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class SearchProductAdapter extends FirestoreRecyclerAdapter<ProductModel,SearchProductAdapter.ProductSearchViewHolder> {

  Context context ;
  SearchProductViewHolderBinding sBinVH ;
    public SearchProductAdapter(@NonNull FirestoreRecyclerOptions<ProductModel> options,Context context ) {
        super(options);
        this.context = context ;
    }


    @NonNull
    @Override
    public ProductSearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        sBinVH = SearchProductViewHolderBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ProductSearchViewHolder(sBinVH);
    }
    @Override
    protected void onBindViewHolder(@NonNull ProductSearchViewHolder holder, int position, @NonNull ProductModel model) {
        if (model.getPic() != null)
            Glide.with(context)
                    .load(model.getPic())
                    .into((ImageView) holder.bindSearchVH.getRoot().findViewById(R.id.includedLayout).findViewById(R.id.productCardIv)) ;
        if (model.getName() != null ) {
            TextView name = holder.bindSearchVH.getRoot().findViewById(R.id.includedLayout).findViewById(R.id.productProductNameTv);
            name.setText(model.getName());
        }
            TextView fee =  holder.bindSearchVH.getRoot().findViewById(R.id.includedLayout).findViewById(R.id.productFee);
            fee.setText("JOD "+model.getFee());

            holder.bindSearchVH.getRoot().setOnClickListener(v ->
            {
                Intent intent = new Intent(context, CartDetailsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                BakeryHelper.putProductWithIntent(intent,model,position);
                context.startActivity(intent);
            });



    }


    class  ProductSearchViewHolder extends RecyclerView.ViewHolder {
        SearchProductViewHolderBinding bindSearchVH ;
         public ProductSearchViewHolder(@NonNull SearchProductViewHolderBinding itemView) {
             super(itemView.getRoot());
             bindSearchVH= itemView ;
         }
     }
}

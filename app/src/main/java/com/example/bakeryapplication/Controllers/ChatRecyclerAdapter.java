package com.example.bakeryapplication.Controllers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakeryapplication.Helpers.BakeryHelper;
import com.example.bakeryapplication.Helpers.FirebaseHelper;
import com.example.bakeryapplication.Models.ChatMessagesModel;
import com.example.bakeryapplication.databinding.ChatMessageRvRowBinding;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ChatRecyclerAdapter extends FirestoreRecyclerAdapter<ChatMessagesModel, ChatRecyclerAdapter.ChatMessageViewHolder> {
    Context context ;
    ChatMessageRvRowBinding msBin ;
    public ChatRecyclerAdapter(@NonNull FirestoreRecyclerOptions<ChatMessagesModel> options,Context context ) {
        super(options);
        this.context = context ;
    }

    @Override
    protected void onBindViewHolder(@NonNull ChatMessageViewHolder holder, int position, @NonNull ChatMessagesModel model) {
        if (model.getSenderId().equals(FirebaseHelper.getCurrentUserId())){
            msBin.leftMessageLayout.setVisibility(View.GONE);
            msBin.rightMessageLayout.setVisibility(View.VISIBLE);
            msBin.rightTextMessage.setText(model.getMessage());
            msBin.rightTimeMessage.setText(BakeryHelper.timestampToString(model.getSendTime()));
        }else {
            msBin.rightMessageLayout.setVisibility(View.GONE);
            msBin.leftMessageLayout.setVisibility(View.VISIBLE);
            msBin.leftTextMessage.setText(model.getMessage());
            msBin.leftTimeMessage.setText(BakeryHelper.timestampToString(model.getSendTime()));
        }


    }

    @NonNull
    @Override
    public ChatMessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // xml binding
        msBin = ChatMessageRvRowBinding.inflate(LayoutInflater.from(context),parent,false);
        return new ChatMessageViewHolder(msBin);
    }

    class ChatMessageViewHolder extends RecyclerView.ViewHolder {
        ChatMessageRvRowBinding binH;

        public ChatMessageViewHolder(@NonNull ChatMessageRvRowBinding itemView) {
            super(itemView.getRoot());
            binH = itemView;
        }
    }
}


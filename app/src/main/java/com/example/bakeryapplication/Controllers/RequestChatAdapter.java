package com.example.bakeryapplication.Controllers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakeryapplication.Activities.ChatActivity;
import com.example.bakeryapplication.Activities.RequestChatActivity;
import com.example.bakeryapplication.Helpers.BakeryHelper;
import com.example.bakeryapplication.Helpers.FirebaseHelper;
import com.example.bakeryapplication.Models.ChatRoomModel;
import com.example.bakeryapplication.Models.UserModel;
import com.example.bakeryapplication.databinding.RecentChatHolderBinding;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;


public class RequestChatAdapter extends FirestoreRecyclerAdapter<ChatRoomModel, RequestChatAdapter.RequestChatHolder> {
    Context context;

    RecentChatHolderBinding recBin;
    UserModel otherUser;


    public RequestChatAdapter(@NonNull FirestoreRecyclerOptions<ChatRoomModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull RequestChatHolder holder, int position, @NonNull ChatRoomModel model) {
        // get user info
        FirebaseHelper.getOtherUserFromChatRoom(model.getUserIds())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    boolean lastMessageByMe = model.getLastMessageSenderId().equals(FirebaseHelper.getCurrentUserId());
                    // set user info to request message holder
                    otherUser = documentSnapshot.toObject(UserModel.class);
                    if (otherUser.getImage() != null)
                        BakeryHelper.setProfileImage(context, Uri.parse(otherUser.getImage()), holder.binH.recentChatIv);
                    holder.binH.requestNameUser.setText(otherUser.getName());
                    holder.binH.requestTimeUser.setText(BakeryHelper.timestampToString(model.getLastTimeStamp()));
                    if (lastMessageByMe)
                        holder.binH.requestMessageUser.setText("You: " + model.getLastMessage());
                    else
                        holder.binH.requestMessageUser.setText(model.getLastMessage());

                });

        //set listner for intent to chat activity with pass other user info
        holder.binH.getRoot().setOnClickListener(v -> {
           String otherId =  FirebaseHelper.getOtherUserFromChatRoom(model.getUserIds()).getId();
            Intent adminChatIntent = new Intent(context, ChatActivity.class);
            adminChatIntent.putExtra(ChatActivity.IS_ADMIN_KEY, "1");
            FirebaseHelper.passReqUserChatRoomAsIntent(adminChatIntent,model.getChatRoomId(), otherId , otherUser.getFcmtoken());
            adminChatIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(adminChatIntent);
        });
    }

    @NonNull
    @Override
    public RequestChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        recBin = RecentChatHolderBinding.inflate(LayoutInflater.from(context), parent, false);
        return new RequestChatHolder(recBin);
    }


    class RequestChatHolder extends RecyclerView.ViewHolder {
        RecentChatHolderBinding binH;

        public RequestChatHolder(@NonNull RecentChatHolderBinding itemView) {
            super(itemView.getRoot());
            binH = itemView;
        }
    }
}


package com.example.bakeryapplication.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.example.bakeryapplication.Controllers.ChatRecyclerAdapter;
import com.example.bakeryapplication.Helpers.BakeryHelper;
import com.example.bakeryapplication.Helpers.FirebaseHelper;
import com.example.bakeryapplication.Models.ChatMessagesModel;
import com.example.bakeryapplication.Models.ChatRoomModel;
import com.example.bakeryapplication.Models.UserModel;
import com.example.bakeryapplication.R;
import com.example.bakeryapplication.Views.MainActivity;
import com.example.bakeryapplication.databinding.ActivityChatBinding;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatActivity extends AppCompatActivity {
    private ActivityChatBinding chat;
    private String chatRoomId;

    private ChatRoomModel chatRoom;

    public static final String IS_ADMIN_KEY = "isAdminKey";
    String isAdmin;
    String reqUserId;
    ChatRecyclerAdapter chatsAdapter;
    String otherToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // full screen setting
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // xml Binding
        chat = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(chat.getRoot());
        // back arrow
        chat.backChatNavView.setOnClickListener((v) -> {
            if (isAdmin == null) {
                // set  user back
                Intent intent = new Intent(ChatActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            } else {
                // set  admin back
                Intent intent = new Intent(ChatActivity.this, RequestChatActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }

        });
        // get admin key word from intent
        isAdmin = getIntent().getStringExtra(IS_ADMIN_KEY);
        //check admin access
        if (isAdmin == null) {
            // set chat user UI
            setUserInfo();
        } else {
            // set chat admin UI
            setAdminInfo();
        }
        // create or use chatRoom
        openChatRoom();
        // upload message to firebase
        chat.sendNavIv.setOnClickListener(v -> {
            String message = chat.sendMessageEt.getText().toString();
            if (message.isEmpty()) {
                return;
            }
            sendMessageToAdmin(message);
            sendNotification(message);

        });

        // set chat adapter 
        setChatAdapter();


    }
    private void sendNotification(String message) {
        if (getIntent() != null)
            otherToken = getIntent().getStringExtra(FirebaseHelper.OTHER_USER_TOKEN);
        FirebaseHelper.getCurrentUserRef()
                .get().addOnSuccessListener(documentSnapshot -> {
                    UserModel currentUser = documentSnapshot.toObject(UserModel.class);
                    try {
                        JSONObject jsonObject = new JSONObject();

                        JSONObject notificationObject = new JSONObject();
                        notificationObject.put("title",currentUser.getName());
                        notificationObject.put("body",message);

                        JSONObject dataObj = new JSONObject();
                        dataObj.put("userId",FirebaseHelper.getCurrentUserId());
                        dataObj.put("chatRoomId",chatRoomId);

                        jsonObject.put("notification",notificationObject);
                        jsonObject.put("data",dataObj);
                        jsonObject.put("to",otherToken);
                        callAPI(jsonObject);

                    }catch (Exception e){

                    }

                });


    }
    public void callAPI(JSONObject jsonObject)
    {
        MediaType JSON = MediaType.get("application/json");
        OkHttpClient client = new OkHttpClient();
        String url = "https://fcm.googleapis.com/fcm/send";
        RequestBody body = RequestBody.create(jsonObject.toString(), JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .header("Authorization", "Bearer AAAAAHDTx_A:APA91bFy7b6LtSddyiMfBewmTpbZ5pNUN98dlw5IKGnQcIBN5QPKzBD0k_tXdOKmbyKL-W1dSzXCDrzhCrGorMJv7cUdB6lFyXDOu7U0UgRGrHWAURuDdz07tBKrykpNpjK-N4VVBSg3")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

            }
        });
    }

    private void setChatAdapter() {
        Query query = FirebaseHelper.getChatRoomMessages(chatRoomId)
                .orderBy("sendTime", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<ChatMessagesModel> chatsOptions = new FirestoreRecyclerOptions.Builder<ChatMessagesModel>()
                .setQuery(query, ChatMessagesModel.class).build();

        chatsAdapter = new ChatRecyclerAdapter(chatsOptions, getApplicationContext());
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        manager.setReverseLayout(true);
        chat.chatRv.setLayoutManager(manager);
        chat.chatRv.setAdapter(chatsAdapter);
        chatsAdapter.startListening();
        chatsAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                chat.chatRv.smoothScrollToPosition(0);

            }
        });


    }

    private void setAdminInfo() {
        //get user id from request chat Intent
        reqUserId = getIntent().getStringExtra(FirebaseHelper.REQ_USER_ID);
        //set admin chat room
        chatRoomId = getIntent().getStringExtra(FirebaseHelper.CHAT_ROOM_ID_KEY);
        //get user info to set for admin
        FirebaseHelper.getUserCollectionRef()
                .document(reqUserId)
                .get().addOnSuccessListener(documentSnapshot -> {
                    UserModel reqUser = documentSnapshot.toObject(UserModel.class);
                    //set user info
                    if (reqUser.getImage() != null)
                        BakeryHelper.setProfileImage(getApplicationContext(), Uri.parse(reqUser.getImage()), chat.chatProfileImageView);
                    chat.chatUsernameToolbar.setText(reqUser.getName());

                });


    }

    private void setUserInfo() {
        chatRoomId = FirebaseHelper.chatRoomId(FirebaseHelper.getCurrentUserId(), FirebaseHelper.getAdminId());
        chat.chatProfileImageView.setImageResource(R.drawable.bakery_login_logo);
        chat.chatUsernameToolbar.setText("Manar Bakery");

    }

    private void openChatRoom() {
        FirebaseHelper.getChatRoomRef(chatRoomId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    chatRoom = documentSnapshot.toObject(ChatRoomModel.class);
                    if (chatRoom == null) {
                        chatRoom = new ChatRoomModel(chatRoomId, Arrays.asList(FirebaseHelper.getCurrentUserId(), FirebaseHelper.getAdminId())
                                , "",
                                Timestamp.now());
                    }
                    FirebaseHelper.getChatRoomRef(chatRoomId).set(chatRoom);


                });
    }

    private void sendMessageToAdmin(String message) {
        // save last message info
        chatRoom.setLastTimeStamp(Timestamp.now());
        chatRoom.setLastMessageSenderId(FirebaseHelper.getCurrentUserId());
        chatRoom.setLastMessage(message);
        FirebaseHelper.getChatRoomRef(chatRoomId).set(chatRoom);
        // create chat message
        ChatMessagesModel chatMessage = new ChatMessagesModel(FirebaseHelper.getCurrentUserId(), message, Timestamp.now());
        // add chat to chat room
        FirebaseHelper.getChatRoomMessages(chatRoomId)
                .add(chatMessage)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        chat.sendMessageEt.setText("");

                    }
                });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (chatsAdapter != null)
            chatsAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (chatsAdapter != null)
            chatsAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (chatsAdapter != null)
            chatsAdapter.startListening();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (chatsAdapter != null)
            chatsAdapter.stopListening();
    }
}
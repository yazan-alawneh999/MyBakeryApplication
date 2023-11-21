package com.example.bakeryapplication.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.bakeryapplication.Controllers.RequestChatAdapter;
import com.example.bakeryapplication.Helpers.FirebaseHelper;
import com.example.bakeryapplication.Models.ChatRoomModel;
import com.example.bakeryapplication.R;
import com.example.bakeryapplication.Views.MainActivity;
import com.example.bakeryapplication.databinding.ActivityRequestChatBinding;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.database.Query;

public class RequestChatActivity extends AppCompatActivity {
    ActivityRequestChatBinding reqBin;
    RequestChatAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // bind reqChat xml
        reqBin = ActivityRequestChatBinding.inflate(getLayoutInflater());
        setContentView(reqBin.getRoot());
        // set req user messages adapter
        setUpAdapter();
        // set req ui  tool bar
        setSupportActionBar(reqBin.requestToolBar);
        // set back arrow toolbar
        reqBin.requestToolBar.setNavigationOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

        });
    }

    private void setUpAdapter() {
        com.google.firebase.firestore.Query query = FirebaseHelper.getAllChatRoomsRef()
                .orderBy("lastTimeStamp", com.google.firebase.firestore.Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<ChatRoomModel> options = new FirestoreRecyclerOptions.Builder<ChatRoomModel>()
                .setQuery(query, ChatRoomModel.class).build();
        adapter = new RequestChatAdapter(options,RequestChatActivity.this);
        adapter.startListening();
      LinearLayoutManager manager =new LinearLayoutManager(getApplicationContext());
        reqBin.recentChatRv.setLayoutManager(manager);
        reqBin.recentChatRv.setAdapter(adapter);


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
            adapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null)
            adapter.startListening();
    }
}
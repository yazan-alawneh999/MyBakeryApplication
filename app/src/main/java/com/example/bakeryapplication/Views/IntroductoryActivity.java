package com.example.bakeryapplication.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.bakeryapplication.Activities.ChatActivity;
import com.example.bakeryapplication.Helpers.FirebaseHelper;
import com.example.bakeryapplication.Models.UserModel;
import com.example.bakeryapplication.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

public class IntroductoryActivity extends AppCompatActivity {
    ImageView logo, bg_iv, manar_iv;

    LottieAnimationView lottieAnimationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_introductory);

        if (getIntent().getExtras() != null) {
            // from notification
            String userId = getIntent().getStringExtra("userId");
            String chatRoomId = getIntent().getStringExtra("chatRoomId");
            FirebaseHelper.getUserCollectionRef()
                    .document(userId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        UserModel sender = documentSnapshot.toObject(UserModel.class);
                        Intent intent = new Intent(this, ChatActivity.class);
                        intent.putExtra(ChatActivity.IS_ADMIN_KEY, "1");
                        FirebaseHelper.passReqUserChatRoomAsIntent(intent, chatRoomId, userId, sender.getFcmtoken());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                    });

        } else {

            new Handler()
                    .postDelayed(() ->
                                    startActivity(new Intent(IntroductoryActivity.this, LoginActivity.class))
                            , 6000);

        }

        bg_iv = findViewById(R.id.intro_background_iv);
        logo = findViewById(R.id.intro_logo);
        manar_iv = findViewById(R.id.intro_appNameTv);
        lottieAnimationView = findViewById(R.id.intro_animation_logo);

        bg_iv.animate().translationY(-3000).setDuration(1000).setStartDelay(4000);
        logo.animate().translationY(-1600).setDuration(1000).setStartDelay(4000);
        manar_iv.animate().translationY(2000).setDuration(1000).setStartDelay(4000);
        lottieAnimationView.animate().translationY(2000).setDuration(1000).setStartDelay(4000);


    }
}
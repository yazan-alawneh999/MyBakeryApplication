package com.example.bakeryapplication.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import com.example.bakeryapplication.Controllers.ViewPagerFragmentAdapter;
import com.example.bakeryapplication.databinding.ActivityLoginBinding;
import com.google.android.material.tabs.TabLayout;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding loginBinding;
    TabLayout tabLayout;
    ViewPagerFragmentAdapter adapter;
    float v = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        loginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(loginBinding.getRoot());

        tabLayout = loginBinding.loginTabLayout;
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        adapter = new ViewPagerFragmentAdapter(getSupportFragmentManager());
        adapter.addTab(new MyTabs("Signup",SignUpFragment.getNewInstance()));
        adapter.addTab(new MyTabs("Login",LoginTabFargment.getNewInstance()));
        loginBinding.loginViewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(loginBinding.loginViewPager);

        loginBinding.loginFabFacebook.setTranslationY(300);
        loginBinding.loginFabTwitter.setTranslationY(300);
        loginBinding.loginFabGoogle.setTranslationY(300);
        tabLayout.setTranslationX(300);

        loginBinding.loginFabGoogle.setAlpha(v);
        loginBinding.loginFabFacebook.setAlpha(v);
        loginBinding.loginFabTwitter.setAlpha(v);
        tabLayout.setAlpha(v);

        loginBinding.loginFabFacebook.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        loginBinding.loginFabGoogle.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(600).start();
        loginBinding.loginFabTwitter.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(800).start();
        tabLayout.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(100).start();

    }

}
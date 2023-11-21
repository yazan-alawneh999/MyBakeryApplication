package com.example.bakeryapplication.Views;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bakeryapplication.Activities.ChatActivity;
import com.example.bakeryapplication.Activities.AlterProductActivity;
import com.example.bakeryapplication.Activities.RequestChatActivity;
import com.example.bakeryapplication.Activities.ViewAllActivity;
import com.example.bakeryapplication.Helpers.BakeryHelper;
import com.example.bakeryapplication.Helpers.FirebaseHelper;
import com.example.bakeryapplication.Models.UserModel;
import com.example.bakeryapplication.R;
import com.example.bakeryapplication.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final int PERMISSION_REQ_CODE = 2;

    private ImageView header_iv;
    private TextView header_userName, header_userEmail, adminLabel;
    private FragmentManager fragmentManager;
    private ActivityMainBinding mainBinding;
    UserModel currentUserInfo;
    public  ActivityResultLauncher<String> requestPermissionLauncher ;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // inflate main activity with view binding
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(mainBinding.getRoot());
        // set toolbar
        setSupportActionBar(mainBinding.toolbar);
        //set navigation drawer
        setNavigationDrawer();
        // navigation header access
        initializeHeaderView();
        // get FCMToken
        getFCMToken();
        requestPermissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission()
                , isGranted -> {
                    if (isGranted) {
                        // FCM SDK (and your app) can post notifications.
                    } else {
                    }
                });
        
    }

    @Override
    public void onStart() {
        super.onStart();
        // customise navigation header view
        onUserInfoUpdate();
        // check userAccess
        checkUserAccess();

    }
    private void askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED) {
                // FCM SDK (and your app) can post notifications.

            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }
    private void checkUserAccess() {
        FirebaseHelper.getCurrentUserRef()
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.getString("isAdmin") == null) {
                        mainBinding.navigationDrawer
                                .getMenu()
                                .findItem(R.id.bottom_navigation_newProduct)
                                .setVisible(false);
                        mainBinding.navigationDrawer
                                .getMenu()
                                .findItem(R.id.adminOrders)
                                .setVisible(false);
                    } else
                        adminLabel.setVisibility(View.VISIBLE);

                });
    }
    private void setNavigationDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                mainBinding.drawerLayout, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        mainBinding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        mainBinding.navigationDrawer
                .setNavigationItemSelectedListener(this);

        Objects.requireNonNull(getSupportActionBar())
                .setDisplayHomeAsUpEnabled(true);

        fragmentManager = getSupportFragmentManager();
        openFragment(new HomeFragment());
        mainBinding.navigationDrawer.setCheckedItem(R.id.bottom_navigation_home);

        mainBinding.toolbar.setNavigationOnClickListener(v ->
                mainBinding.drawerLayout.openDrawer(GravityCompat.START));

    }
    private void initializeHeaderView() {
        View v = mainBinding.navigationDrawer.getHeaderView(0);

        header_iv = v.findViewById(R.id.header_iv);
        header_userName = v.findViewById(R.id.navHeader_username_tv);
        header_userEmail = v.findViewById(R.id.navHeader_email_tv);
        adminLabel = v.findViewById(R.id.adminTextLabel);

    }
    private void onUserInfoUpdate() {
        FirebaseHelper.getCurrentUserRef()
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    currentUserInfo = documentSnapshot.toObject(UserModel.class);
                    if (currentUserInfo != null) {
                        if (currentUserInfo.getImage() != null)
                            BakeryHelper.setProfileImage(MainActivity.this, Uri.parse(currentUserInfo.getImage()), header_iv);
                        header_userEmail.setText(currentUserInfo.getEmail());
                        header_userName.setText(currentUserInfo.getName());
                    }


                });
    }
    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.mainActivityFragmentContainer, fragment);
        transaction.commit();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (mainBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            // close nav drawer
            mainBinding.drawerLayout.closeDrawer(GravityCompat.START);
        }



    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.bottom_navigation_home) {
            openFragment(new HomeFragment());
            mainBinding.drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        } else if (itemId == R.id.adminOrders) {
            openFragment(new OrdersFragment());
            mainBinding.drawerLayout.closeDrawer(GravityCompat.START);

            return true;
        } else if (itemId == R.id.bottom_navigation_categories) {
            openFragment(new CategoriesFragment());
            mainBinding.drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        } else if (itemId == R.id.bottom_navigation_cart) {
            openFragment(new CartFragment());
            mainBinding.drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        } else if (itemId == R.id.bottom_navigation_profile) {
            openFragment(new ProfileFragment());
            mainBinding.drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        } else if (itemId == R.id.bottom_navigation_chat) {

            FirebaseHelper.getCurrentUserRef()
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.getString("isAdmin") == null) {
                            Intent chatIntent = new Intent(this, ChatActivity.class);
                            chatIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(chatIntent);
                        } else {
                            Intent reqMessageIntent = new Intent(this, RequestChatActivity.class);
                            reqMessageIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(reqMessageIntent);
                        }
                    });
        } else if (itemId == R.id.bottom_navigation_newProduct) {

            Intent newProductIntent = new Intent(this, AlterProductActivity.class);
            newProductIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(newProductIntent);


        }


        return false;
    }

public void getFCMToken(){
    FirebaseMessaging.getInstance().getToken()
            .addOnSuccessListener(s -> {
                FirebaseHelper.getCurrentUserRef().update("fcmtoken",s);

            });
}
}



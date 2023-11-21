package com.example.bakeryapplication.Views;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bakeryapplication.Helpers.BakeryHelper;
import com.example.bakeryapplication.Helpers.FirebaseHelper;
import com.example.bakeryapplication.Models.UserModel;
import com.example.bakeryapplication.databinding.FragmentProfileBinding;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.messaging.FirebaseMessaging;


public class ProfileFragment extends Fragment {
    // class attributes
    UserModel currentUserModel;
    private FragmentProfileBinding b;
    Uri profilImageUri;
    ActivityResultLauncher<Intent> imagePikerLauncher;
    String username, userEmail, userPass, userPhone;
    // class constructor
    public ProfileFragment() {
        // Required empty public constructor
    }
    // class functions
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imagePikerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null && data.getData() != null) {
                            profilImageUri = data.getData();
                            BakeryHelper.setProfileImage(getContext()
                                    , profilImageUri,
                                    b.profileFragmentCircleImageView);
                        }
                    }

                }
        );
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentProfileBinding.inflate(LayoutInflater.from(container.getContext()), container, false);
        //loading profile
        setInLoadProgress(true);
        getUserData();
        //set update user progress
        setInProgress(false);
        // update image
        b.profileFragmentCircleImageView.setOnClickListener(
                v -> {
            ImagePicker.with(this)
                    .cropSquare()
                    .compress(512)
                    .maxResultSize(512, 512)
                    .createIntent(intent -> {
                        imagePikerLauncher.launch(intent);
                        return null;
                    });
        });
        // update user info
        b.profileFragmentUpdateButton.setOnClickListener(
                v -> {
            setInProgress(true);
            try {
                // update userInfo
                updateUserInfo();
            }
            catch (Exception e) {

                UserModel newUser = getNewUserInfo();
                newUser.setImage(currentUserModel.getImage());
                setUser(newUser);


            }

        });
        // logout button
        b.profileLogoutBtn.setOnClickListener((v -> {
            FirebaseMessaging.getInstance().deleteToken()
                    .addOnSuccessListener(unused -> {
                        FirebaseHelper.logout();
                        Intent logout = new Intent(getContext(), IntroductoryActivity.class);
                        logout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(logout);
                    });

        }));
        return b.getRoot();

    }
    private void setInProgress(boolean inProgress) {
        if (inProgress) {
            b.profileUpdatePBr.setVisibility(View.VISIBLE);
            b.profileFragmentUpdateButton.setVisibility(View.GONE);
        } else {
            b.profileUpdatePBr.setVisibility(View.GONE);
            b.profileFragmentUpdateButton.setVisibility(View.VISIBLE);
        }
    }
    private void setInLoadProgress(boolean inLoad) {
        if (inLoad) {
            b.profileProgressBr.setVisibility(View.VISIBLE);
            b.profileScrollView.setVisibility(View.GONE);
        } else {
            b.profileProgressBr.setVisibility(View.GONE);
            b.profileScrollView.setVisibility(View.VISIBLE);
        }
    }
    private void updateUserInfo() {

        FirebaseHelper.getProfilePictureReference()
                .putFile(profilImageUri)
                .addOnCompleteListener(task -> {
                    FirebaseHelper.getProfilePictureReference()
                            .getDownloadUrl()
                            .addOnSuccessListener(uri -> {

                                UserModel newUser = getNewUserInfo();
                                newUser.setImage(uri.toString());
                                setUser(newUser);



                            });


                });
    }
    private void setUser(UserModel newUser) {
        FirebaseHelper.getCurrentUserRef()
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.getString("isAdmin") != null) {


                        FirebaseHelper.getCurrentUserRef()
                                .set(newUser)
                                .addOnSuccessListener(unused -> {
                                    FirebaseHelper.getCurrentUserRef()
                                                    .update("isAdmin","1")
                                            .addOnSuccessListener(unused1 -> {
                                                setInProgress(false);
                                                BakeryHelper.showToast(getContext(), "updated");
                                            });

                                });
                    }
                    FirebaseHelper.getCurrentUserRef()
                            .set(newUser)
                            .addOnSuccessListener(unused -> {
                                setInProgress(false);
                                BakeryHelper.showToast(getContext(), "updated");
                            });



                });

    }
    private UserModel getNewUserInfo() {
        username = b.profileFragmentNameEt.getText().toString();
        userEmail = b.profileFragmentEmailEt.getText().toString();
        userPhone = b.profileFragmentPhoneEt.getText().toString();
        userPass = b.profileFragmentAddressEt.getText().toString();
        return new UserModel(username, userEmail, userPass, userPhone);

    }
    private void getUserData() {
        FirebaseHelper.getCurrentUserRef()
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    currentUserModel = documentSnapshot.toObject(UserModel.class);
                    if (currentUserModel != null) {
                        b.profileFragmentNameEt.setText(currentUserModel.getName());
                        b.profileFragmentEmailEt.setText(currentUserModel.getEmail());
                        b.profileFragmentPhoneEt.setText(currentUserModel.getPhone());
                        b.profileFragmentAddressEt.setText(currentUserModel.getPassword());
                        if (currentUserModel.getImage() != null)
                            BakeryHelper.setProfileImage(getContext(), Uri.parse(currentUserModel.getImage()), b.profileFragmentCircleImageView);
                    }
                    setInLoadProgress(false);
                });
    }


}

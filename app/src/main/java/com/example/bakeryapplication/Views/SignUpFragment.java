package com.example.bakeryapplication.Views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bakeryapplication.Helpers.BakeryHelper;
import com.example.bakeryapplication.Helpers.FirebaseHelper;
import com.example.bakeryapplication.Models.UserModel;
import com.example.bakeryapplication.R;


public class SignUpFragment extends Fragment {
    EditText email_et, pass_et, username_et, phone_et;
    Button signupBtn;
    String username;
    String userEmail, password, userPhone;

    public SignUpFragment() {
    }

    public static SignUpFragment getNewInstance() {
        return new SignUpFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        email_et = view.findViewById(R.id.signUp_fragment_email_et);
        pass_et = view.findViewById(R.id.signUp_fragment_password_et);
        username_et = view.findViewById(R.id.signUp_fragment_name_et);
        phone_et = view.findViewById(R.id.signUp_fragment_phone_et);
        signupBtn = view.findViewById(R.id.signup_fragment_sign_btn);

        signupBtn.setOnClickListener(v -> {
            validInput();
            createUser();
        });


    }

    private void validInput() {
        username = username_et.getText().toString();
        userEmail = email_et.getText().toString();
        password = pass_et.getText().toString();
        userPhone = phone_et.getText().toString();

        if (TextUtils.isEmpty(username)) {
            Toast.makeText(getActivity(), "Name is Empty !", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(userEmail)) {
            Toast.makeText(getActivity(), "email is Empty !", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getActivity(), "password  is Empty !", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() < 6) {
            Toast.makeText(getActivity(), "password  must be grater than 6 char !", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(userPhone)) {
            Toast.makeText(getActivity(), "phone is Empty !", Toast.LENGTH_SHORT).show();
            return;
        }
    }
    private void createUser() {
        // initialize user
        username = username_et.getText().toString();
        userEmail = email_et.getText().toString();
        password = pass_et.getText().toString();
        userPhone = phone_et.getText().toString();
        UserModel user = new UserModel(username, userEmail, password, userPhone);
        // signup user with firebase
        FirebaseHelper.getAuthInstance()
                .createUserWithEmailAndPassword(userEmail, password)
                .addOnCompleteListener(task -> {
                    // upload userInfo to fireStore database
                    if (task.isSuccessful()) {

                        FirebaseHelper.setCurrentUserInfo(user);
                        BakeryHelper.showToast(this.getActivity(),"Registration Successful ");
                    }
                    else {
                        Log.d("yazan", "createUser: "+task.getException() );
                        BakeryHelper.showToast(requireContext()," error " + task.getException());

                    }

                });


    }
}
package com.example.bakeryapplication.Views;

import android.content.Intent;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bakeryapplication.Helpers.FirebaseHelper;
import com.example.bakeryapplication.R;
import com.google.firebase.auth.FirebaseAuth;


public class LoginTabFargment extends Fragment {
    private EditText email, pass;
    private Button loginBtn;
    private TextView forgetPass_tv;
    private FirebaseAuth auth;
    private String userEmail, password;
    private ProgressBar progressBar;
    private final float v = 0;

    public LoginTabFargment() {
    }

    public static LoginTabFargment getNewInstance() {
        return new LoginTabFargment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login_fargment, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        email = view.findViewById(R.id.login_fragment_email_et);
        pass = view.findViewById(R.id.login_fragment_pass_et);
        forgetPass_tv = view.findViewById(R.id.login_fragment_forgetPass_tv);
        loginBtn = view.findViewById(R.id.login_fragment_log_btn);
        progressBar = view.findViewById(R.id.loginProgressbar);

        setInProgress(false);

        loginFragmintAnimate(v) ;


        loginBtn.setOnClickListener(v -> {
            login();
        });

    }

    private void loginFragmintAnimate(float v) {

        email.setTranslationX(800);
        pass.setTranslationX(800);
        forgetPass_tv.setTranslationX(800);
        loginBtn.setTranslationX(800);


        email.setAlpha(v);
        pass.setAlpha(v);
        forgetPass_tv.setAlpha(v);
        loginBtn.setAlpha(v);

        email.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        pass.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        forgetPass_tv.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        loginBtn.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();
    }

    private void validateInput() {
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
    }

    private void login() {
        userEmail = email.getText().toString();
        password = pass.getText().toString();

        validateInput();

        FirebaseHelper.getAuthInstance()
                .signInWithEmailAndPassword(userEmail, password)
                .addOnCompleteListener(task ->
                {
                    if (task.isSuccessful()) {
                        setInProgress(true);
                        Intent loginIntent = new Intent(getActivity(), MainActivity.class);
                        startActivity(loginIntent);

                    } else {
                        setInProgress(false);
                        clearFields();
                        Toast.makeText(getActivity(), "Error" + task.getException(), Toast.LENGTH_SHORT).show();
                        Log.d("yazan", "login: Error " + task.getException());
                    }
                });
    }

    public void setInProgress(boolean inProgress) {
        if (inProgress) {
            progressBar.setVisibility(View.VISIBLE);
            loginBtn.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            loginBtn.setVisibility(View.VISIBLE);
        }
    }
    public void clearFields(){
        email.setText("");
        pass.setText("");
    }

}
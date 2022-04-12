package com.example.anygift.login;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavHost;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.anygift.R;
import com.example.anygift.feed.MainActivity;
import com.example.anygift.Retrofit.LoginResult;
import com.example.anygift.Retrofit.RetrofitInterface;
import com.example.anygift.model.Model;
import com.example.anygift.model.ModelFirebase;
import com.example.anygift.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import java.util.HashMap;

public class LoginFragment extends Fragment {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    View view;
    Snackbar mySnackbar;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressBar pb;

    Button signIn_btn, signUp_btn, forgotP_btn;
    TextInputEditText email, password;
    String email_user, password_user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        //getActivity().setTitle("AnyGift - Login");
        view = inflater.inflate(R.layout.fragment_login, container, false);
        signIn_btn = view.findViewById(R.id.Login_signIn_btn);
        signUp_btn = view.findViewById(R.id.Login_btn_signUp);
        forgotP_btn = view.findViewById(R.id.Login_btn_forgotp);
        email = view.findViewById(R.id.Login_email_input);
        password = view.findViewById(R.id.Login_password_input);
        pb = view.findViewById(R.id.login_prob);
        pb.setVisibility(View.INVISIBLE);

        signIn_btn.setTypeface(Typeface.SANS_SERIF);
        signUp_btn.setTypeface(Typeface.SANS_SERIF);
        forgotP_btn.setTypeface(Typeface.SANS_SERIF);

        signIn_btn.setOnClickListener(v -> {
            checkUser();
        });

        signUp_btn.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(LoginFragmentDirections.actionLoginFragmentToSignUpFragment());
        });

        forgotP_btn.setOnClickListener(v -> {
            forgotPassword();
        });
        return view;
    }

    public void login(){
        //TODO Login using Retrofit
        HashMap<String,String> map = LoginResult.mapToLogin(email.getText().toString(),password.getText().toString());
        Model.instance.login(map, new Model.userLoginListener() {
            @Override
            public void onComplete(LoginResult loginResult,String message) {
                //use this loginResult
                if (loginResult == null)
                    Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                else{
                    Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                    Model.instance.signedUser_new = loginResult;
                    Log.d("TAG", "login successful");
                    goToFeedActivity();
                   // Navigation.findNavController(view).navigate(R.id.action_global_feedFragment);

                }
            }
        });
    }

    public void checkUser() {
        email_user = email.getText().toString();
        password_user = password.getText().toString();
        if (TextUtils.isEmpty(email_user) && email_user.matches(emailPattern)) {
            email.setError("please enter correct email");
            return;
        }
        if (TextUtils.isEmpty(password_user)) {
            password.setError("please enter correct  password");
            return;
        }
        //connect via http request
        pb.setVisibility(View.VISIBLE);
        login();

//        mAuth.signInWithEmailAndPassword(email_user, password_user).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if (task.isSuccessful()) {
//                    signIn_btn.setEnabled(false);
//                    signUp_btn.setEnabled(false);
//                    forgotP_btn.setEnabled(false);
//                    pb.setVisibility(View.INVISIBLE);
//                    mySnackbar = Snackbar.make(view, "Login successful :)", BaseTransientBottomBar.LENGTH_LONG);
//                    mySnackbar.show();
//                    Log.d("TAG", "login successful");
//                    goToFeedActivity();
//                   // Navigation.findNavController(view).navigate(R.id.action_global_feedFragment);
//                } else
//                    //Log.d("TAG","Login failed");
//                    Toast.makeText(getContext(), "Error! " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
//                pb.setVisibility(View.INVISIBLE);
//            }
//        });


    }

    public void goToFeedActivity(){
                pb.setVisibility(View.INVISIBLE);
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
    };

    public void forgotPassword() {
        email_user = email.getText().toString();
        mAuth.sendPasswordResetEmail(email_user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    mySnackbar = Snackbar.make(view, "Email sent, check your mailbox :)", BaseTransientBottomBar.LENGTH_LONG);
                    mySnackbar.show();

                } else
                    Log.d("TAG", "failed");
            }
        });
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.clear();
    }
}


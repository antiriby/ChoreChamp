package com.AMTV.ChoreChamp;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView register;
    private EditText editTextEmail, editTextPassword;
    private Button signIn;

    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    private FirebaseUser user;
    private String userId;
    private boolean isAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        register = (TextView) findViewById(R.id.txtLoginRegister);
        register.setOnClickListener(this);

        signIn = (Button) findViewById(R.id.btnLoginLogin);
        signIn.setOnClickListener(this);

        editTextEmail = (EditText) findViewById(R.id.txtLoginEmail);
        editTextPassword = (EditText) findViewById(R.id.txtLoginPassword);

        progressBar = (ProgressBar) findViewById(R.id.loginProBar);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txtLoginRegister:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.btnLoginLogin:
                userLogin();
                break;
        }
    }

    private void userLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (email.isEmpty()) {
            editTextEmail.setError("Email is required!");
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please enter a valid email!");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Password is required!");
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editTextPassword.setError("Invalid password length. Minimum length must be 6 characters");
            editTextPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);


        //Check Firebase Authentication
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //redirect to user profile/homepage
                    //TODO check if ProfileActivity object already exists. If so, DO NOT CREATE ANOTHER ACTIVITY
                    // Double clicking on the login button creates two profile activities so find a wa to solve this issue

                    user = FirebaseAuth.getInstance().getCurrentUser();
                    userId = user.getUid();
                    MyApplication.setUserId(user.getUid());

                    MyApplication.setDbReference(FirebaseDatabase.getInstance().getReference());

                    MyApplication.getDbReference().child("Users").child(userId).child("role").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Something went wrong!", Toast.LENGTH_LONG).show();
                            } else {
                                String result = (String) task.getResult().getValue();
                                if (result.equals("Admin")) {
                                    MyApplication.setAdmin(true);
                                } else {
                                    MyApplication.setAdmin(false);
                                }
                            }
                        }
                    });

                    MyApplication.getDbReference().child("Users").child(userId).child("householdId").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Something went wrong. Please try Again!", Toast.LENGTH_LONG).show();
                            } else {
                                MyApplication.setHouseholdId(String.valueOf(task.getResult().getValue()));
//                                startActivity(new Intent(LoginActivity.this, ProfileActivity.class));
                                startActivity(new Intent(LoginActivity.this, RewardActivity.class));
                            }
                        }
                    });
                } else {
                    Toast.makeText(LoginActivity.this, "Failed to login! Please check credentials", Toast.LENGTH_LONG).show();
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}
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
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseUser user;
    private DatabaseReference dbReference;
    private FirebaseAuth mAuth;

    private String userId;
    private TextView  registerUser;
    private EditText editTextHouseholdName, editTextName, editTextEmail, editTextFamilyPassword, editTextConfirmPassword;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        dbReference = FirebaseDatabase.getInstance("https://chorechamp-a0443-default-rtdb.firebaseio.com").getReference();

        registerUser = (Button) findViewById(R.id.btnRegister);
        registerUser.setOnClickListener(this);

        editTextHouseholdName = (EditText) findViewById(R.id.txtRegisterHouseholdName);
        editTextName = (EditText) findViewById(R.id.txtRegisterName);
        editTextEmail = (EditText) findViewById(R.id.txtRegisterEmail);
        editTextFamilyPassword = (EditText) findViewById(R.id.txtRegisterFamilyPassword);
        editTextConfirmPassword = (EditText) findViewById(R.id.txtRegisterConfirmPassword);

        progressBar = (ProgressBar) findViewById(R.id.registrationProgressBar);



    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {

            case R.id.btnRegister:
                registerUser();
                break;
        }

    }

    private void registerUser() {
        String householdName = editTextHouseholdName.getText().toString().trim();
        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String familyPassword = editTextFamilyPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();
        Boolean adminRole = true;

        if (householdName.isEmpty()){
            editTextHouseholdName.setError("First name is required!");
            editTextHouseholdName.requestFocus();
            return;
        }

        if (name.isEmpty()){
            editTextName.setError("Last name is required!");
            editTextName.requestFocus();
            return;
        }

        if (email.isEmpty()){
            editTextEmail.setError("Email Address is required!");
            editTextEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please provide a valid email address (i.e. @gmail.com");
            editTextEmail.requestFocus();
            return;
        }

        if (familyPassword.isEmpty()){
            editTextFamilyPassword.setError("Password is required!");
            editTextFamilyPassword.requestFocus();
            return;
        }

        if (familyPassword.length() < 6){
            editTextFamilyPassword.setError("Minimum password length is 6 characters");
            editTextFamilyPassword.requestFocus();
            return;
        }

        if(!confirmPassword.equals(familyPassword)){
            editTextConfirmPassword.setError("Passwords do not match. Please try again.");
            editTextConfirmPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, familyPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // Create the household id using push()
                        if (task.isSuccessful()){

                            DatabaseReference householdRef =  dbReference.child("Households").push();
                            User user = new User(name, email, adminRole, householdRef.getKey());
                            dbReference.child("Users").child(mAuth.getCurrentUser().getUid()).setValue(user);

                            Household household = new Household(householdName,familyPassword,user);

                            Intent addMembers = new Intent(RegisterActivity.this, AddMemberActivity.class);
                            addMembers.putExtra("Household", household);
                            addMembers.putExtra("HouseholdID", householdRef.getKey());
                            addMembers.putExtra("CurrentUser", user);
                            addMembers.putExtra("FamilyPassword", familyPassword);

                            startActivity(addMembers);

                        } else {
                            Toast.makeText(RegisterActivity.this, "Something went wrong. Please try Again!", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }
}
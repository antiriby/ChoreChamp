package com.AMTV.ChoreChamp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class AddMemberActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextName, editTextEmail;
    private Button addAnotherMember, nextButton;
    private Switch admin;

    private String householdID, familyPassword;
    private User currentUser;
    private Household household;
    private ArrayList<User> members;

    private FirebaseAuth mAuth;
    private DatabaseReference dbReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);

        editTextName = (EditText) findViewById(R.id.txtAddMemberName);
        editTextEmail = (EditText) findViewById(R.id.txtAddMemberEmail);

        addAnotherMember = (Button) findViewById(R.id.btnAddMemberAddAnother);
        addAnotherMember.setOnClickListener(this);

        nextButton = (Button) findViewById(R.id.btnAddMemberNext);
        nextButton.setOnClickListener(this);

        admin = (Switch) findViewById(R.id.switchAddMemberAdmin);
        admin.setOnClickListener(this);

        householdID = (getIntent().getStringExtra("HouseholdID")).substring(1);
        familyPassword = getIntent().getStringExtra("FamilyPassword");
        currentUser = (User)getIntent().getSerializableExtra("CurrentUser");
        household = (Household) getIntent().getSerializableExtra("Household");
        members = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();
        dbReference = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAddMemberAddAnother:
                addNewMember();
                break;
            case R.id.btnAddMemberNext:
                createHousehold();
                Intent login = new Intent(this, LoginActivity.class);
                login.putExtra("HouseholdId", householdID);
                login.putExtra("CurrentUser", currentUser);
                login.putExtra("Household", household);
                startActivity(login);
                break;
        }
    }

    private void addNewMember() {

        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        Boolean role = admin.isChecked();


        User user = new User(name, email, role, householdID);
        // Push new member to database
        mAuth.createUserWithEmailAndPassword(email,familyPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            dbReference.child("Users").child(mAuth.getUid()).setValue(user);
                            editTextName.getText().clear();
                            editTextEmail.getText().clear();

                            // Add new member to the member ArrayList
                            members.add(user);
                            Toast.makeText(AddMemberActivity.this, "Member was successfully added to household!", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(AddMemberActivity.this, "Something went wrong. Please try Again!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


    private void createHousehold() {
        if(!editTextName.getText().toString().trim().isEmpty() &&
                !editTextEmail.getText().toString().trim().isEmpty()){
            addNewMember();
        }

        // Update member list for household object including the admin/current user
        members.add(currentUser);
        household.setMembers(members);

        // Push the household to database
        dbReference.child("Households").child(householdID).setValue(household);
    }
}
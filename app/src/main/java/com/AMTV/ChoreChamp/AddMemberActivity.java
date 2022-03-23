package com.AMTV.ChoreChamp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;


public class AddMemberActivity extends AppCompatActivity implements View.OnClickListener {

    final int ICON_WIDTH = 300;
    final int ICON_HEIGHT = 300;
    final int DEFAULT_ICON_ID = MyApplication.getRedThumbId();

    private int profileIconId = DEFAULT_ICON_ID;

    private EditText editTextName, editTextEmail;
    private Button addAnotherMember, nextButton;
    private Switch admin;

    private String householdID, familyPassword;
    private User currentUser;
    private Household household;
    private boolean shouldCreateHousehold;

    private HashMap<String,User> members;

    private FirebaseAuth mAuth;
    private DatabaseReference dbReference;

    private ImageView profileIcon;
    private Button redButton, orangeButton, goldButton, greenButton, blueButton, violetButton;
    private ArrayList<Button> iconColors = new ArrayList<>();
    private ArrayList<ImageView>icons = new ArrayList<>();
    // TODO: Add icon image views and buttons to AddMemberActivity and XML file

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);

        getExtras();

        editTextName = (EditText) findViewById(R.id.txtAddMemberName);
        editTextEmail = (EditText) findViewById(R.id.txtAddMemberEmail);

        addAnotherMember = (Button) findViewById(R.id.btnAddMemberAddAnother);
        addAnotherMember.setOnClickListener(this);

        nextButton = (Button) findViewById(R.id.btnAddMemberFinish);
        nextButton.setOnClickListener(this);

        admin = (Switch) findViewById(R.id.switchAddMemberAdmin);
        admin.setOnClickListener(this);

        //Setup Icon View
        profileIcon = (ImageView)findViewById(R.id.imgAddMemberIconView);
        profileIcon.setImageResource(DEFAULT_ICON_ID);

        //Setup Icon Color Buttons
        redButton = (Button) findViewById(R.id.btnAddMemberRed);
        redButton.setOnClickListener(this);
        orangeButton = (Button) findViewById(R.id.btnAddMemberOrange);
        orangeButton.setOnClickListener(this);
        goldButton = (Button) findViewById(R.id.btnAddMemberGold);
        goldButton.setOnClickListener(this);
        greenButton = (Button) findViewById(R.id.btnAddMemberGreen);
        greenButton.setOnClickListener(this);
        blueButton = (Button) findViewById(R.id.btnAddMemberBlue);
        blueButton.setOnClickListener(this);
        violetButton = (Button) findViewById(R.id.btnAddMemberViolet);
        violetButton.setOnClickListener(this);

        iconColors.add(redButton);
        iconColors.add(orangeButton);
        iconColors.add(goldButton);
        iconColors.add(greenButton);
        iconColors.add(blueButton);
        iconColors.add(violetButton);
        members = new HashMap<>();

        mAuth = FirebaseAuth.getInstance();
        dbReference = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAddMemberAddAnother:
                addNewMember();
                break;
            case R.id.btnAddMemberFinish:

                if(shouldCreateHousehold) {
                    createHousehold();
                } else {
                    updateHousehold();
                }
                break;
            case R.id.btnAddMemberRed:
                profileIconId = MyApplication.getRedThumbId();
                selectProfileIcon();
                break;
            case R.id.btnAddMemberOrange:
                profileIconId = MyApplication.getOrangeThumbId();
                selectProfileIcon();
                break;
            case R.id.btnAddMemberGold:
                profileIconId = MyApplication.getGoldThumbId();
                selectProfileIcon();
                break;
            case R.id.btnAddMemberGreen:
                profileIconId = MyApplication.getGreenThumbId();
                selectProfileIcon();
                break;
            case R.id.btnAddMemberBlue:
                profileIconId = MyApplication.getBlueThumbId();
                selectProfileIcon();
                break;
            case R.id.btnAddMemberViolet:
                profileIconId = MyApplication.getVioletThumbId();
                selectProfileIcon();
                break;
        }
    }

    private void selectProfileIcon(){
        //TODO: only select icon if it's not already being used by another member in the household
        //TODO: display error Toast if icon is already taken
        profileIcon.setImageResource(profileIconId);
        profileIcon.getLayoutParams().width = ICON_WIDTH;
        profileIcon.getLayoutParams().height = ICON_HEIGHT;
        profileIcon.requestLayout();
    }

    private void addNewMember() {

        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        Boolean role = admin.isChecked();

        if (name.isEmpty()){
            editTextName.setError("Name is required!");
            editTextName.requestFocus();
            return;
        }

        if (email.isEmpty() ){
            editTextEmail.setError("Email Address is required!");
            editTextEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please provide a valid email address (i.e. @gmail.com");
            editTextEmail.requestFocus();
            return;
        }

        // Push new member to database
        mAuth.createUserWithEmailAndPassword(email,familyPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String userId = FirebaseAuth.getInstance().getUid();
                            User user = new User(name, email, role, householdID, userId, profileIconId);

                            dbReference.child("Users").child(userId).setValue(user);
                            FirebaseAuth.getInstance().signOut();

                            editTextName.getText().clear();
                            editTextEmail.getText().clear();

                            // Add new member to the member ArrayList
                            members.put(user.getUid(), user);
                            Toast.makeText(AddMemberActivity.this, "Member was successfully added to household!", Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(AddMemberActivity.this, "Something went wrong. Please try Again!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void createHousehold() {
        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();

        if (name.isEmpty()){
            editTextName.setError("Name is required!");
            editTextName.requestFocus();
            return;
        }

        if (email.isEmpty() ){
            editTextEmail.setError("Email Address is required!");
            editTextEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please provide a valid email address (i.e. @gmail.com");
            editTextEmail.requestFocus();
            return;
        }

        // Update member list for household object including the admin/current user
        members.put(currentUser.getUid(), currentUser);
        household.setMembers(members);

        // Push the household to database
        dbReference.child("Households").child(householdID).setValue(household);

        Intent login = new Intent(this, LoginActivity.class);
        login.putExtra("HouseholdId", householdID);
        login.putExtra("CurrentUser", currentUser);
        login.putExtra("Household", household);
        startActivity(login);
    }

    public void updateHousehold(){
        dbReference.child("Households").child(householdID).child("members").setValue(members);
        Intent home = new Intent(this, HomeActivity.class);
        home.putExtra("HouseholdId", householdID);
        home.putExtra("CurrentUser", currentUser);
        home.putExtra("Household", household);
        startActivity(home);
        finish();
    }

    private void getExtras(){
        householdID = getIntent().getStringExtra("HouseholdID");
        familyPassword = getIntent().getStringExtra("FamilyPassword");
        currentUser = (User) getIntent().getSerializableExtra("CurrentUser");
        household = (Household) getIntent().getSerializableExtra("Household");
        shouldCreateHousehold = getIntent().getBooleanExtra("CreateHousehold", false);

        mAuth = FirebaseAuth.getInstance();
        dbReference = FirebaseDatabase.getInstance().getReference();

        if(householdID == null){
            householdID = MyApplication.getHouseholdId();
        }

        if(familyPassword == null){
            familyPassword = MyApplication.getFamilyPassword();
        }

        if(members == null){
            dbReference.child("Households").child(householdID).child("members").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot snap : snapshot.getChildren()){
                        User member = snap.getValue(User.class);
                        members.put(member.getUid(), member);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(AddMemberActivity.this, "Couldn't add member", Toast.LENGTH_SHORT).show();
                }
            });
        }

//        if(household == null){
//            dbReference.child("Households").child(householdID).addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    household = (Household) snapshot.getValue(Household.class);
//                    if(household == null){
//                        Toast.makeText(AddMemberActivity.this, "Problem with retrieving household data", Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//                    Toast.makeText(AddMemberActivity.this, "Something went wrong, try again later.", Toast.LENGTH_SHORT).show();
//                }
//            });
//
//        }
    }
}
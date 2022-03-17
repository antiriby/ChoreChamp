package com.AMTV.ChoreChamp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {



    final int FIXED_ICON_WIDTH = 400;
    final int FIXED_ICON_HEIGHT = 400;
    private int ORIGINAL_ICON_WIDTH;
    private int ORIGINAL_ICON_HEIGHT;
    private FirebaseUser currentUser;
    private DatabaseReference dbReference;
    private FirebaseAuth mAuth;

    private String userId, profileIconId;
    private TextView  registerUser;
    private ImageView profileIcon, redThumb, orangeThumb, goldThumb, greenThumb, blueThumb, violetThumb, selectedProfileIcon;
    private EditText editTextHouseholdName, editTextName, editTextEmail, editTextFamilyPassword, editTextConfirmPassword;
    private Button redButton, orangeButton, goldButton, greenButton, blueButton, violetButton;
    private ArrayList<Button> iconColors = new ArrayList<>();
    private ArrayList<ImageView>icons = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        dbReference = FirebaseDatabase.getInstance("https://chorechamp-a0443-default-rtdb.firebaseio.com").getReference();

        registerUser = (Button) findViewById(R.id.btnRegister);
        registerUser.setOnClickListener(this);

        //Setup Icons
        profileIcon = (ImageView)findViewById(R.id.imgRegisterRedThumb);
        redThumb = (ImageView)findViewById(R.id.imgRegisterRedThumb);
        orangeThumb = (ImageView)findViewById(R.id.imgRegisterOrangeThumb);
        goldThumb = (ImageView)findViewById(R.id.imgRegisterGoldThumb);
        greenThumb = (ImageView)findViewById(R.id.imgRegisterGreenThumb);
        blueThumb = (ImageView)findViewById(R.id.imgRegisterBlueThumb);
        violetThumb = (ImageView)findViewById(R.id.imgRegisterVioletThumb);

        icons.add(redThumb);
        icons.add(orangeThumb);
        icons.add(goldThumb);
        icons.add(greenThumb);
        icons.add(blueThumb);
        icons.add(violetThumb);

        selectedProfileIcon = redThumb;

        ORIGINAL_ICON_WIDTH = profileIcon.getLayoutParams().width;
        ORIGINAL_ICON_HEIGHT = profileIcon.getLayoutParams().height;

        //Setup Icon Color Buttons
        redButton = (Button) findViewById(R.id.btnRegisterRed);
        redButton.setOnClickListener(this);
        orangeButton = (Button) findViewById(R.id.btnRegisterOrange);
        orangeButton.setOnClickListener(this);
        goldButton = (Button) findViewById(R.id.btnRegisterGold);
        goldButton.setOnClickListener(this);
        greenButton = (Button) findViewById(R.id.btnRegisterGreen);
        greenButton.setOnClickListener(this);
        blueButton = (Button) findViewById(R.id.btnRegisterBlue);
        blueButton.setOnClickListener(this);
        violetButton = (Button) findViewById(R.id.btnRegisterViolet);
        violetButton.setOnClickListener(this);

        iconColors.add(redButton);
        iconColors.add(orangeButton);
        iconColors.add(goldButton);
        iconColors.add(greenButton);
        iconColors.add(blueButton);
        iconColors.add(violetButton);


        // Setup Edit Text Views
        editTextHouseholdName = (EditText) findViewById(R.id.txtRegisterHouseholdName);
        editTextName = (EditText) findViewById(R.id.txtRegisterName);
        editTextEmail = (EditText) findViewById(R.id.txtRegisterEmail);
        editTextFamilyPassword = (EditText) findViewById(R.id.txtRegisterFamilyPassword);
        editTextConfirmPassword = (EditText) findViewById(R.id.txtRegisterConfirmPassword);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {

            case R.id.btnRegister:
                registerUser();
                break;
            case R.id.btnRegisterRed:
//                changeProfileIcon();
//                redThumb.setVisibility(View.VISIBLE);
//                selectedProfileIcon = redThumb;
                profileIcon.setBackgroundResource(R.mipmap.red_thumb_foreground);
                profileIcon.getLayoutParams().width = ORIGINAL_ICON_WIDTH;
                profileIcon.getLayoutParams().height = ORIGINAL_ICON_HEIGHT;
                profileIcon.requestLayout();
                profileIconId = "R.mipmap.red_thumb_foreground";
                break;
            case R.id.btnRegisterOrange:
//                changeProfileIcon();
//                orangeThumb.setVisibility(View.VISIBLE);
//                selectedProfileIcon = orangeThumb;
                profileIcon.setBackgroundResource(R.mipmap.orange_thumb_foreground);
                profileIcon.getLayoutParams().width = ORIGINAL_ICON_WIDTH;
                profileIcon.getLayoutParams().height = ORIGINAL_ICON_HEIGHT;
                profileIcon.requestLayout();
                profileIconId = "R.mipmap.orange_thumb_foreground";
                break;
            case R.id.btnRegisterGold:
//                changeProfileIcon();
//                goldThumb.setVisibility(View.VISIBLE);
//                selectedProfileIcon = goldThumb;
                profileIcon.setBackgroundResource(R.mipmap.gold_thumb_foreground);
                profileIcon.getLayoutParams().width = ORIGINAL_ICON_WIDTH;
                profileIcon.getLayoutParams().height = ORIGINAL_ICON_HEIGHT;
                profileIcon.requestLayout();
                profileIconId = "R.mipmap.gold_thumb_foreground";
                break;
            case R.id.btnRegisterGreen:
//                changeProfileIcon();
//                greenThumb.setVisibility(View.VISIBLE);
//                selectedProfileIcon = greenThumb;
                profileIcon.setBackgroundResource(R.mipmap.green_thumb_foreground);
                profileIcon.getLayoutParams().width = ORIGINAL_ICON_WIDTH;
                profileIcon.getLayoutParams().height = ORIGINAL_ICON_HEIGHT;
                profileIcon.requestLayout();
                profileIconId = "R.mipmap.green_thumb_foreground";
                break;
            case R.id.btnRegisterBlue:
//                changeProfileIcon();
//                blueThumb.setVisibility(View.VISIBLE);
//                selectedProfileIcon = blueThumb;
                profileIcon.setBackgroundResource(R.mipmap.blue_thumb_foreground);
                profileIcon.getLayoutParams().width = FIXED_ICON_WIDTH;
                profileIcon.getLayoutParams().height = FIXED_ICON_HEIGHT;
                profileIcon.requestLayout();
                profileIconId = "R.mipmap.blue_thumb_foreground";
                break;
            case R.id.btnRegisterViolet:
//                changeProfileIcon();
//                violetThumb.setVisibility(View.VISIBLE);
//                selectedProfileIcon = violetThumb;
                profileIcon.setBackgroundResource(R.mipmap.violet_thumb_foreground);
                profileIcon.getLayoutParams().width = FIXED_ICON_WIDTH;
                profileIcon.getLayoutParams().height = FIXED_ICON_HEIGHT;
                profileIcon.requestLayout();
                profileIconId = "R.mipmap.violent_thumb_foreground";
                break;
        }

    }

    private void changeProfileIcon() {
        for(ImageView icon : icons) {
            icon.setVisibility(View.INVISIBLE);
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

        mAuth.createUserWithEmailAndPassword(email, familyPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // Create the household id using push()
                        if (task.isSuccessful()){

                            DatabaseReference householdRef =  dbReference.child("Households").push();

                            currentUser = FirebaseAuth.getInstance().getCurrentUser();
                            userId = currentUser.getUid();
                            Bitmap bitImage = BitmapFactory.decodeResource(getResources(),R.mipmap.red_thumb);

                            String householdId = householdRef.getKey().substring(1);
                            User user = new User(name, email, adminRole, householdId, userId,profileIconId);
                            dbReference.child("Users").child(userId).setValue(user);
                            FirebaseAuth.getInstance().signOut();

                            Household household = new Household(householdName, familyPassword, user);

                            Intent addMembers = new Intent(RegisterActivity.this, AddMemberActivity.class);
                            addMembers.putExtra("Household", household);
                            addMembers.putExtra("HouseholdID", householdId);
                            addMembers.putExtra("CurrentUser", user);
                            addMembers.putExtra("FamilyPassword", familyPassword);

                            startActivity(addMembers);

                        } else {
                            Toast.makeText(RegisterActivity.this, "Something went wrong. Please try Again!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
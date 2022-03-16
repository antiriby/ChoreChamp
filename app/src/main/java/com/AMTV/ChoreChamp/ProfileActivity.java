package com.AMTV.ChoreChamp;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseUser user;
    private DatabaseReference dbReference;

    private String userId;
    private Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        user = FirebaseAuth.getInstance().getCurrentUser();
        dbReference = FirebaseDatabase.getInstance().getReference();
        userId = user.getUid();

        final TextView firstNameTextView = (TextView) findViewById(R.id.txtProfileFirstName);
        final TextView lastNameTextView = (TextView) findViewById(R.id.txtProfileLastName);
        final TextView emailTextView = (TextView) findViewById(R.id.txtProfileEmail);
        final TextView householdSizeTextView = (TextView) findViewById(R.id.txtProfileHouseholdSize);

        dbReference.child("Users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                System.out.println(snapshot.getValue() == null);
                if(userProfile != null) {
                    String firstName = userProfile.getName();
                    String lastName = userProfile.getRole();
                    String email = userProfile.getEmail();
                    String householdId = userProfile.getHouseholdId();

                    firstNameTextView.setText("First Name: "+ firstName);
                    lastNameTextView.setText("Last Name: "+ lastName);
                    emailTextView.setText("Email: " + email);
                    householdSizeTextView.setText("Household ID: " + householdId);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, "Something went wrong.", Toast.LENGTH_LONG).show();;
            }
        });

        logout = (Button) findViewById(R.id.btnLogout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                FirebaseAuth.getInstance().signOut();
                finish();
            }
        });
    }
}
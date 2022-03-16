package com.AMTV.ChoreChamp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddEditRewardActivity extends AppCompatActivity implements MemberAssignmentAdapterListener {

    Button btnSave;
    FirebaseUser user;
    String userId, householdId;
    DatabaseReference householdReference;
    DatabaseReference userReference;
    DatabaseReference dbReference;


    private EditText editTextName, editTextPoints, editTextDescription;
    private ArrayList<User> members = new ArrayList<>();
    private ArrayList<String> images = new ArrayList<>();
    private ArrayList<String> assigneeIds = new ArrayList<>();

    private MemberAssignmentAdapter adapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_reward);

        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();

        userReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
        userReference.child("householdId").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(!task.isSuccessful()){
                    Toast.makeText(AddEditRewardActivity.this, "Something went wrong. Please try Again!", Toast.LENGTH_LONG).show();
                }else{
                    householdId = String.valueOf(task.getResult().getValue());
                    householdReference = FirebaseDatabase.getInstance().getReference().child("Households");

                    fillMembersArray();
                    initRecyclerView();

                    btnSave = findViewById(R.id.btnAddEditSave);

                    editTextName = (EditText) findViewById(R.id.addEditRewardName);
                    editTextPoints = (EditText) findViewById(R.id.addEditRewardPoints);
                    editTextDescription = (EditText) findViewById(R.id.addEditRewardDescription);

                    btnSave.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            updateReward();
                        }
                    });
                }
            }
        });


    }

    private void updateReward() {
        String name = editTextName.getText().toString().trim();
        String pointsStr = editTextPoints.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();

        if (name.isEmpty()) {
            editTextName.setError("Reward name is required!");
            editTextName.requestFocus();
            return;
        }

        if (pointsStr.isEmpty()) {
            editTextPoints.setError("Number of points is required!");
            editTextPoints.requestFocus();
            return;
        }

        int points = Integer.parseInt(pointsStr);

        Map<String, Object> childUpdates = new HashMap<>();

        for(String assignee : assigneeIds){
            String key = householdReference.child(householdId).child("Rewards").push().getKey();
            Reward reward = new Reward(name, points, assignee, description);

            childUpdates.put("/"+householdId+"/Rewards/"+key, reward);

        }

        householdReference.updateChildren(childUpdates)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Intent intent = new Intent(AddEditRewardActivity.this, RewardActivity.class);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddEditRewardActivity.this, "Something went wrong. Please try Again!", Toast.LENGTH_LONG).show();
                    }
                });

    }

    private void fillMembersArray(){
          DatabaseReference membersRef = householdReference.child(householdId).child("members");
          membersRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    members.clear();
                    for (DataSnapshot snapshot1 : snapshot.getChildren()){
                        User ld = snapshot1.getValue(User.class);
                        members.add(ld);
                    }

                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(AddEditRewardActivity.this, "Failed to get household members", Toast.LENGTH_LONG).show();
                }
          });
    }

    private void initRecyclerView(){
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView = findViewById(R.id.addEditRewardMemberList);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MemberAssignmentAdapter(members, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onMemberClicked(Intent intent){
        String addMember = intent.getStringExtra("add_member");
        String removeMember = intent.getStringExtra("remove_member");

        if(addMember != null){
            assigneeIds.add(addMember);
        }

        if(removeMember != null){
            assigneeIds.remove(removeMember);
        }
    }
}
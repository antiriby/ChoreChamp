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
import java.util.Date;

public class AddEditTaskActivity extends AppCompatActivity implements MemberAssignmentAdapterListener {
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
        setContentView(R.layout.activity_task);

        userId = MyApplication.getUserId();

        userReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

        householdId = MyApplication.getHouseholdId();
        householdReference = FirebaseDatabase.getInstance().getReference().child("Households");

        fillMembersArray();
        initRecyclerView();

        btnSave = findViewById(R.id.btnAddEditSave);

        editTextName = (EditText) findViewById(R.id.addEditTaskName);
        editTextPoints = (EditText) findViewById(R.id.addEditTaskPoints);
        editTextDescription = (EditText) findViewById(R.id.addEditTaskDescription);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateTask();
            }
        });
    }

    private void updateTask() {
        String name = editTextName.getText().toString().trim();
        String pointsStr = editTextPoints.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        Date taskDate = new Date();

        Frequency taskFrequency;

        if (name.isEmpty()) {
            editTextName.setError("Task name is required!");
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
            String key = householdReference.child(householdId).child("availableTasks").push().getKey();
//            Reward reward = new Reward(name, points, assignee, description);
            Task task = new Task(name,description,points,taskDate);

            householdReference.child(householdId).child("availableTasks").child(key).setValue(task);

            MyApplication.getDbReference().child("Users").child(assignee).child("availableTasks").child(key).setValue(task);
        }

        Intent intent = new Intent(AddEditTaskActivity.this, TaskActivity.class);
        startActivity(intent);
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
                Toast.makeText(AddEditTaskActivity.this, "Failed to get household members", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initRecyclerView(){
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView = findViewById(R.id.addEditTaskMemberList);
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

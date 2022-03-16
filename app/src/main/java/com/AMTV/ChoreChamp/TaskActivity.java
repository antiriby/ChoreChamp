package com.AMTV.ChoreChamp;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class TaskActivity extends AppCompatActivity {

    DatabaseReference dbReference;
    FirebaseUser user;
    String userId, householdId;

    boolean isAdmin = false;

    Button btnAdd;
    List<com.AMTV.ChoreChamp.Task> taskList;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(MyApplication.isAdmin()){
            setContentView(R.layout.activity_admin_task);

        }else{
            setContentView(R.layout.activity_task);
        }

        recyclerView = findViewById(R.id.taskList);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new TaskAdapter(taskList, TaskActivity.this);
        recyclerView.setAdapter(mAdapter);

        userId = MyApplication.getUserId();

        householdId = MyApplication.getHouseholdId();

        // Admins can see all of household tasks, non-admins can only see their tasks
        if(MyApplication.isAdmin()) {
            dbReference = FirebaseDatabase.getInstance().getReference().child("Households").child(householdId).child("availabletasks");
            dbReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    taskList.clear();
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        com.AMTV.ChoreChamp.Task ld = snapshot1.getValue(com.AMTV.ChoreChamp.Task.class);
                        taskList.add(ld);
                    }

                    mAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(TaskActivity.this, "Failed to update tasks list", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            dbReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("availabletasks");
            dbReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    taskList.clear();
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        com.AMTV.ChoreChamp.Task ld = snapshot1.getValue(Task.class);
                        taskList.add(ld);
                    }

                    mAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(TaskActivity.this, "Failed to update tasks list", Toast.LENGTH_LONG).show();
                }
            });
        }





        // Only shows + button if admin
        if(MyApplication.isAdmin()){
            btnAdd = findViewById(R.id.btnTaskAdd);

            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(TaskActivity.this, AddEditTaskActivity.class);
                    startActivity(intent);
                }
            });
        }

    }



//        @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_task);
//    }
}
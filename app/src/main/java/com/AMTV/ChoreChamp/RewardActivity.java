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
import java.util.Arrays;
import java.util.List;

public class RewardActivity extends AppCompatActivity {
    DatabaseReference dbReference;
    FirebaseUser user;
    String userId, householdId;

    Button btnAdd;
    List<Reward> rewardList = new ArrayList<>();

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward);

        recyclerView = findViewById(R.id.rewardsList);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new RewardsAdapter(rewardList, RewardActivity.this);
        recyclerView.setAdapter(mAdapter);

        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();
        FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("householdId").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
           @Override
           public void onComplete(@NonNull Task<DataSnapshot> task) {
               if (!task.isSuccessful()) {
                   Toast.makeText(RewardActivity.this, "Something went wrong. Please try Again!", Toast.LENGTH_LONG).show();
               } else {
                   householdId = String.valueOf(task.getResult().getValue());

                   dbReference = FirebaseDatabase.getInstance().getReference().child("Households").child(householdId).child("Rewards");
                   dbReference.addValueEventListener(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot snapshot) {
                           rewardList.clear();
                           for (DataSnapshot snapshot1 : snapshot.getChildren()){
                               Reward ld = snapshot1.getValue(Reward.class);
                               rewardList.add(ld);
                           }

                           mAdapter.notifyDataSetChanged();
                       }

                       @Override
                       public void onCancelled(@NonNull DatabaseError error) {
                           Toast.makeText(RewardActivity.this, "Failed to update rewards list", Toast.LENGTH_LONG).show();
                       }
                   });
               }
           }
       });


//        fillRewardsList();
        btnAdd = findViewById(R.id.btnRewardAdd);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RewardActivity.this, AddEditRewardActivity.class);
                startActivity(intent);
            }
        });






    }

//    private void fillRewardsList() {
//        // TODO get rewards from database
//
//        Reward r1 = new Reward("Chocolate",10);
//        Reward r2 = new Reward("Watch movie", 20);
//        Reward r3 = new Reward("New toy", 100);
//
//        rewardList.addAll(Arrays.asList(new Reward[] {r1, r2, r3}));
//    }

}

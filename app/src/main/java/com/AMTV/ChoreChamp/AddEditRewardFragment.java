package com.AMTV.ChoreChamp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddEditRewardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddEditRewardFragment extends Fragment {

    Button btnSave;
    ImageButton btnBack;
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

    public AddEditRewardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AddEditRewardFragment.
     */
    public static AddEditRewardFragment newInstance() {
        AddEditRewardFragment fragment = new AddEditRewardFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView;

        rootView = inflater.inflate(R.layout.activity_add_edit_reward, container, false);

        userId = MyApplication.getUserId();

        userReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

        householdId = MyApplication.getHouseholdId();
        householdReference = FirebaseDatabase.getInstance().getReference().child("Households");

        fillMembersArray();
        layoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView = rootView.findViewById(R.id.addEditRewardMemberList);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MemberAssignmentAdapter(members, this.getContext(), listener);
        recyclerView.setAdapter(adapter);

        btnSave = rootView.findViewById(R.id.btnAddEditSave);

        editTextName = (EditText) rootView.findViewById(R.id.addEditRewardName);
        editTextPoints = (EditText) rootView.findViewById(R.id.addEditRewardPoints);
        editTextDescription = (EditText) rootView.findViewById(R.id.addEditRewardDescription);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateReward();
            }
        });


        // Back button
        btnBack = rootView.findViewById(R.id.btnAddEditRewardBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ThirdFragment rewardFragment = new ThirdFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fram, rewardFragment).commit();
            }
        });


        return rootView;
    }

    MemberAssignmentAdapterListener listener = new MemberAssignmentAdapterListener() {
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
    };

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

        Map<String, Object> childUpdates = new HashMap<>();

        for(String assignee : assigneeIds){
            String key = householdReference.child(householdId).child("availableRewards").push().getKey();
            Reward reward = new Reward(name, pointsStr, assignee, key, description, "false");

            householdReference.child(householdId).child("availableRewards").child(key).setValue(reward);

            MyApplication.getDbReference().child("Users").child(assignee).child("availableRewards").child(key).setValue(reward);
        }

        ThirdFragment thirdFragment = new ThirdFragment();
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fram, thirdFragment).commit();

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
                Toast.makeText(getActivity(), "Failed to get household members", Toast.LENGTH_LONG).show();
            }
        });

    }



}
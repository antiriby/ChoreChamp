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
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.internal.Objects;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FourthFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FourthFragment extends Fragment implements View.OnClickListener {

    private DatabaseReference dbReference;
    private TextView nameTextView, pointsTextView;
    private ImageView profileImgView;
    private User currentUser;
    private Button logoutButton;

    private String householdId, userId;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager layoutManager;

    private List<Object> historyObjects = new ArrayList<>();


    public FourthFragment() {
        // Required empty public constructor
    }
    
    // TODO: Rename and change types and number of parameters
    public static FourthFragment newInstance(String param1, String param2) {
        FourthFragment fragment = new FourthFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) { }

        dbReference = MyApplication.getDbReference();
        householdId = MyApplication.getHouseholdId();
        userId = MyApplication.getUserId();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_fourth, container, false);
        nameTextView = rootView.findViewById(R.id.txtProfileName);
        pointsTextView = rootView.findViewById(R.id.txtProfilePoints);
        profileImgView = rootView.findViewById(R.id.imgProfileIcon);

        logoutButton = rootView.findViewById(R.id.btnProfileLogout);
        logoutButton.setOnClickListener(this);

        recyclerView = rootView.findViewById(R.id.recyclerProfileHistory);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this.getContext());

        recyclerView.setLayoutManager(layoutManager);


        mAdapter = new ProfileHistoryAdapter(getContext(),currentUser, historyObjects);
        recyclerView.setAdapter(mAdapter);


        //Add completed tasks field to User
        dbReference.child("Users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentUser = snapshot.getValue(User.class);
                nameTextView.setText(currentUser.getName());
                pointsTextView.setText(currentUser.getPoints() + "pts");
                profileImgView.setImageResource(currentUser.getProfileIconId());
                //TODO: Display task/reward history
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(),"Error loading profile. Please try again later.", Toast.LENGTH_LONG).show();
            }
        });

        getHistory();

        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnProfileLogout:
                Intent intent = new Intent(getActivity(), WelcomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
    }

    public void getHistory(){
        historyObjects.clear();
        // Get available rewards from database
        dbReference = FirebaseDatabase.getInstance().getReference().child("Households").child(householdId).child("availableRewards");
        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Reward ld = snapshot1.getValue(Reward.class);
                    String assignedUser = ld.getUid();

                    if(assignedUser.equals(userId) && ld.getClaimed().equals("true")){
                        historyObjects.add(ld);
                    }
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });

        // Get available tasks from database
        dbReference = FirebaseDatabase.getInstance().getReference().child("Households").child(householdId).child("availableTasks");
        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Task ld = snapshot1.getValue(Task.class);
                    String assignedUser = ld.getUid();

                    if(assignedUser.equals(userId) && ld.getIsComplete().equals("true")){
                        historyObjects.add(ld);
                    }
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
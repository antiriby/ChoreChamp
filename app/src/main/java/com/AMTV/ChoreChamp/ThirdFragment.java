package com.AMTV.ChoreChamp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ThirdFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ThirdFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";

    DatabaseReference dbReference;
    String userId, householdId;
    Button btnAdd;
    List<Reward> rewardList = new ArrayList<>();

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    public ThirdFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ThirdFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ThirdFragment newInstance() {
        ThirdFragment fragment = new ThirdFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView;

        if(MyApplication.isAdmin()){
            rootView = inflater.inflate(R.layout.activity_admin_reward, container, false);

        }else{
            rootView = inflater.inflate(R.layout.activity_reward, container, false);
        }

        recyclerView = rootView.findViewById(R.id.rewardsList);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this.getContext());

        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new RewardsAdapter(rewardList, this.getContext());
        recyclerView.setAdapter(mAdapter);

        userId = MyApplication.getUserId();

        householdId = MyApplication.getHouseholdId();

        // Admins can see all of household rewards, non-admins can only see their rewards
        if(MyApplication.isAdmin()) {
            dbReference = FirebaseDatabase.getInstance().getReference().child("Households").child(householdId).child("availableRewards");
            dbReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    rewardList.clear();
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        Reward ld = snapshot1.getValue(Reward.class);
                        rewardList.add(ld);
                    }

                    mAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    //Toast.makeText(RewardActivity.this, "Failed to update rewards list", Toast.LENGTH_LONG).show();
                    Toast.makeText(getActivity(), "Failed to update rewards list", Toast.LENGTH_LONG).show();

                }
            });
        } else {
            dbReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("availableRewards");
            dbReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    rewardList.clear();
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        Reward ld = snapshot1.getValue(Reward.class);
                        rewardList.add(ld);
                    }

                    mAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getActivity(), "Failed to update rewards list", Toast.LENGTH_LONG).show();

                }
            });
        }

        // Only shows + button if admin
        if(MyApplication.isAdmin()){
            btnAdd = rootView.findViewById(R.id.btnRewardAdd);

            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Intent intent = new Intent(RewardActivity.this, AddEditRewardActivity.class);
                    Intent intent = new Intent(getActivity(), AddEditRewardActivity.class);

                    startActivity(intent);
                }
            });
        }
        return rootView;
    }
}
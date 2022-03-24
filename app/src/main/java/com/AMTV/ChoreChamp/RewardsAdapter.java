package com.AMTV.ChoreChamp;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

public class RewardsAdapter extends RecyclerView.Adapter<RewardsAdapter.MyViewHolder> {

    List<Reward> rewardList;
    HashMap<String,User> membersList;
    Context context;
    DatabaseReference userReference, rewardReference, householdReference;
    int userPoints;

    public RewardsAdapter(List<Reward> rewardList, HashMap<String,User> membersList, Context context) {
        this.rewardList = rewardList;
        this.membersList = membersList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reward,parent,false);
        MyViewHolder holder = new MyViewHolder(view);

        rewardReference = FirebaseDatabase.getInstance().getReference().child("Households").child(MyApplication.getHouseholdId()).child("availableRewards");
        householdReference = FirebaseDatabase.getInstance().getReference().child("Households").child(MyApplication.getHouseholdId());
        userReference = FirebaseDatabase.getInstance().getReference().child("Users");

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tv_rewardName.setText(rewardList.get(position).getName());
        holder.tv_rewardPoints.setText(String.valueOf(rewardList.get(position).getPoints()) + " pts");
        if(MyApplication.isAdmin()){
            holder.profilePic.setVisibility(View.VISIBLE);
        }else{
            holder.profilePic.setVisibility(View.INVISIBLE);
        }
        holder.profilePic.setImageResource(membersList.get(rewardList.get(position).getUid()).getProfileIconId());
//        holder.cb_rewardCheck.setButtonTintList(new ColorStateList());
        holder.cb_rewardCheck.setChecked(rewardList.get(position).getClaimed().equals("true"));
        holder.cb_rewardCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Reward currentReward = rewardList.get(position);
                userPoints = Integer.parseInt(membersList.get(currentReward.getUid()).getPoints());
//                userReference.child(currentReward.getUid()).child("points").addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        userPoints = Integer.parseInt((String) snapshot.getValue());
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });

                int currRewardPoints = Integer.parseInt(currentReward.getPoints());

                // If claimed, change to unclaimed
                if(currentReward.getClaimed().equals("true")) {
                    rewardReference.child(currentReward.getRewardId()).child("claimed").setValue("false");
                    userReference.child(currentReward.getUid()).child("availableRewards").child(currentReward.getRewardId()).child("claimed").setValue("false");

                    // Set points in households
                    householdReference.child("members").child(currentReward.getUid()).child("points").setValue("" + (userPoints + currRewardPoints));

                    // Remove from claimed rewards
                    householdReference.child("claimedRewards").child(currentReward.getUid()).removeValue();

                    // Set points in users
                    userReference.child(currentReward.getUid()).child("points").setValue("" + (userPoints + currRewardPoints));

                }else{ // unclaimed, change to claimed
                    //Check if user has enough points
                    if(userPoints >= currRewardPoints) {
                        rewardReference.child(rewardList.get(position).getRewardId()).child("claimed").setValue("true");
                        userReference.child(currentReward.getUid()).child("availableRewards").child(currentReward.getRewardId()).child("claimed").setValue("true");

                        // Set points in households
                        householdReference.child("members").child(currentReward.getUid()).child("points").setValue("" + (userPoints - Integer.parseInt(currentReward.getPoints())));

                        // Add to claimed rewards
                        householdReference.child("claimedRewards").child(currentReward.getUid()).setValue(currentReward.getUid());

                        // Set points in users
                        userReference.child(currentReward.getUid()).child("points").setValue("" + (userPoints - Integer.parseInt(currentReward.getPoints())));
                    }else{
                        holder.cb_rewardCheck.setChecked(false);
                        Toast.makeText(view.getContext(), "You don't have enough points!", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return rewardList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_rewardName;
        TextView tv_rewardPoints;
        CheckBox cb_rewardCheck;
        ImageView profilePic;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_rewardName = itemView.findViewById(R.id.RewardListRewardName);
            tv_rewardPoints = itemView.findViewById(R.id.RewardListRewardPoints);
            cb_rewardCheck = itemView.findViewById(R.id.rewardCheckBox);
            profilePic = itemView.findViewById(R.id.rewardProfilePic);
        }
    }
}

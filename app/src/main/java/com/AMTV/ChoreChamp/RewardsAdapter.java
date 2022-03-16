package com.AMTV.ChoreChamp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RewardsAdapter extends RecyclerView.Adapter<RewardsAdapter.MyViewHolder> {

    List<Reward> rewardList;
    Context context;

    public RewardsAdapter(List<Reward> rewardList, Context context) {
        this.rewardList = rewardList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reward,parent,false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tv_rewardName.setText(rewardList.get(position).getName());
        holder.tv_rewardPoints.setText(String.valueOf(rewardList.get(position).getPoints()));
    }

    @Override
    public int getItemCount() {
        return rewardList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_rewardName;
        TextView tv_rewardPoints;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_rewardName = itemView.findViewById(R.id.RewardListRewardName);
            tv_rewardPoints = itemView.findViewById(R.id.RewardListRewardPoints);
        }
    }
}

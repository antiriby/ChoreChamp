package com.AMTV.ChoreChamp;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class ProfileHistoryAdapter extends RecyclerView.Adapter<ProfileHistoryAdapter.historyViewHolder> implements Serializable {

    private Context context;
    private User userProfile;
    private List<Object> historyList;

    public ProfileHistoryAdapter(@NonNull Context context, @NonNull User userProfile, List<Object>historyList) {
        super();
        this.context = context;
        this.userProfile = userProfile;
        this.historyList = historyList;
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    @NonNull
    @Override
    public ProfileHistoryAdapter.historyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.profile_history, parent, false);
        ProfileHistoryAdapter.historyViewHolder holder = new ProfileHistoryAdapter.historyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileHistoryAdapter.historyViewHolder holder, int position) {
        int positiveColor = context.getColor(R.color.positive_points);
        int negativeColor = context.getColor(R.color.negative_points);

        if(historyList.get(position) instanceof Reward){
            Reward reward = (Reward) historyList.get(position);
            holder.name.setText(reward.getName());
            holder.points.setText("-" + reward.getPoints() + " pts");
            holder.points.setTextColor(negativeColor);
        } else if (historyList.get(position) instanceof Task){
            Task task = (Task) historyList.get(position);
            holder.name.setText(task.getName());
            holder.points.setText("+" + task.getPoints() + " pts");
            holder.points.setTextColor(positiveColor);
        }
    }

    class historyViewHolder extends RecyclerView.ViewHolder {
        TextView name, points;

        public historyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.txtProfileHistoryName);
            points = itemView.findViewById(R.id.txtProfileHistoryPoints);
        }
    }
}
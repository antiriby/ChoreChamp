package com.AMTV.ChoreChamp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;

public class ProfileHistoryAdapter extends RecyclerView.Adapter<ProfileHistoryAdapter.historyViewHolder> implements Serializable {

    private Context context;
    private User userProfile;

    public ProfileHistoryAdapter(@NonNull Context context, @NonNull User userProfile) {
        super();
        this.context = context;
        this.userProfile = userProfile;
    }

    @Override
    public int getItemCount() {
        return 1;
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
        holder.name.setText(userProfile.getName());
        holder.points.setText(userProfile.getPoints() + " pts");
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
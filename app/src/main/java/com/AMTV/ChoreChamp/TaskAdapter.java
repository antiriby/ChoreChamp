package com.AMTV.ChoreChamp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.MyViewHolder> {
    List<Task> taskList;
    Context context;
    DatabaseReference userReference, rewardReference, householdReference;
    int userPoints;


    public TaskAdapter(List<Task> taskList, Context context) {
        this.taskList = taskList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task,parent,false);
        MyViewHolder holder = new MyViewHolder(view);

        rewardReference = FirebaseDatabase.getInstance().getReference().child("Households").child(MyApplication.getHouseholdId()).child("availableTasks");
        householdReference = FirebaseDatabase.getInstance().getReference().child("Households").child(MyApplication.getHouseholdId());
        userReference = FirebaseDatabase.getInstance().getReference().child("Users");

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tv_taskName.setText(taskList.get(position).getName());
        holder.tv_taskPoints.setText(String.valueOf(taskList.get(position).getPoints()) + "pts");
        holder.cb_taskCheck.setChecked(taskList.get(position).getIsComplete().equals("true"));
        holder.cb_taskCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Task currentTask = taskList.get(position);

                userReference.child(currentTask.getUid()).child("points").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        userPoints = Integer.parseInt((String) snapshot.getValue());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                int currTaskPoints = Integer.parseInt(currentTask.getPoints());

                // If claimed, change to unclaimed
                if(currentTask.getIsComplete().equals("true")) {
                    rewardReference.child(currentTask.getTaskId()).child("claimed").setValue("false");

                    // Set points in households
                    householdReference.child("members").child(currentTask.getUid()).child("points").setValue("" + (userPoints + currTaskPoints));

                    // Set points in users
                    userReference.child(currentTask.getUid()).child("points").setValue("" + (userPoints + currTaskPoints));

                }else{ // unclaimed, change to claimed
                    //Check if user has enough points
                    if(userPoints >= currTaskPoints) {
                        rewardReference.child(taskList.get(position).getTaskId()).child("claimed").setValue("true");

                        // Set points in households
                        householdReference.child("members").child(currentTask.getUid()).child("points").setValue("" + (userPoints - Integer.parseInt(currentTask.getPoints())));

                        // Set points in users
                        userReference.child(currentTask.getUid()).child("points").setValue("" + (userPoints - Integer.parseInt(currentTask.getPoints())));
                    }else{
                        Toast.makeText(view.getContext(), "You don't have enough points!", Toast.LENGTH_LONG);
                    }
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_taskName;
        TextView tv_taskPoints;
        CheckBox cb_taskCheck;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_taskName = itemView.findViewById(R.id.TaskListTaskName);
            tv_taskPoints = itemView.findViewById(R.id.TaskListTaskPoints);
            cb_taskCheck = itemView.findViewById(R.id.taskCheckBox);

        }
    }
}

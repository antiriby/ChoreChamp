package com.AMTV.ChoreChamp;

import android.content.Context;
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
public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.MyViewHolder> {
    List<Task> taskList;
    Context context;
    DatabaseReference userReference, rewardReference, householdReference;
    int userPoints;
    HashMap<String, User> membersList = new HashMap<>();


    public TaskAdapter(List<Task> taskList, HashMap<String,User> membersList, Context context) {
        this.taskList = taskList;
        this.membersList = membersList;
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

        if(MyApplication.isAdmin()){
            holder.profilePic.setVisibility(View.VISIBLE);
        }else{
            holder.profilePic.setVisibility(View.INVISIBLE);
        }
        holder.profilePic.setImageResource(membersList.get(taskList.get(position).getUid()).getProfileIconId());
        holder.cb_taskCheck.setChecked(taskList.get(position).getIsComplete().equals("true"));
        holder.cb_taskCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Task currentTask = taskList.get(position);

                userPoints = Integer.parseInt(membersList.get(currentTask.getUid()).getPoints());

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

                // If completed, change to incomplete
                if(currentTask.getIsComplete().equals("true")) {
                    rewardReference.child(currentTask.getTaskId()).child("isComplete").setValue("false");
                    userReference.child(currentTask.getUid()).child("availableTasks").child(currentTask.getTaskId()).child("isComplete").setValue("false");

                    // Set points in households
                    householdReference.child("members").child(currentTask.getUid()).child("points").setValue("" + (userPoints - currTaskPoints));

                    // Remove from complete tasks
                    householdReference.child("completedTasks").child(currentTask.getUid()).removeValue();

                    // Set points in users
                    userReference.child(currentTask.getUid()).child("points").setValue("" + (userPoints - currTaskPoints));

                }else{ // incomplete, change to completed
                    rewardReference.child(taskList.get(position).getTaskId()).child("isComplete").setValue("true");
                    userReference.child(currentTask.getUid()).child("availableTasks").child(currentTask.getTaskId()).child("isComplete").setValue("true");

                    // Set points in households
                    householdReference.child("members").child(currentTask.getUid()).child("points").setValue("" + (userPoints + Integer.parseInt(currentTask.getPoints())));

                    // Add to completed tasks
                    householdReference.child("completedTasks").child(currentTask.getUid()).setValue(currentTask.getUid());

                    // Set points in users
                    userReference.child(currentTask.getUid()).child("points").setValue("" + (userPoints + Integer.parseInt(currentTask.getPoints())));
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
        ImageView profilePic;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_taskName = itemView.findViewById(R.id.TaskListTaskName);
            tv_taskPoints = itemView.findViewById(R.id.TaskListTaskPoints);
            cb_taskCheck = itemView.findViewById(R.id.taskCheckBox);
            profilePic = itemView.findViewById(R.id.taskProfilePic);

        }
    }
}

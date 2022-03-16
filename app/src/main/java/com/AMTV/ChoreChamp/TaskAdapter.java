package com.AMTV.ChoreChamp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.MyViewHolder> {
    List<Task> taskList;
    Context context;

    public TaskAdapter(List<Task> taskList, Context context) {
        this.taskList = taskList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task,parent,false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tv_taskName.setText(taskList.get(position).getName());
        holder.tv_taskPoints.setText(String.valueOf(taskList.get(position).getPoints()) + "pts");
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_taskName;
        TextView tv_taskPoints;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_taskName = itemView.findViewById(R.id.TaskListTaskName);
            tv_taskPoints = itemView.findViewById(R.id.TaskListTaskPoints);
        }
    }
}

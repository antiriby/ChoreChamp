package com.AMTV.ChoreChamp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class TaskAssignmentAdapter extends RecyclerView.Adapter<TaskAssignmentAdapter.ViewHolder>{

    private ArrayList<User> names = new ArrayList<>();
    //    private ArrayList<String> img = new ArrayList<>(); //TODO add user images
    private Context context;
    private ArrayList<User> assignees = new ArrayList<>();
    private MemberAssignmentAdapterListener listener;

    public TaskAssignmentAdapter(ArrayList<User> names, Context context, MemberAssignmentAdapterListener listener){
        this.names = names;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TaskAssignmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.family_member_assignment_list, parent, false);
        return new TaskAssignmentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskAssignmentAdapter.ViewHolder holder, int position) {
        holder.tv_name.setText(names.get(position).getName());

        holder.profilePic.setImageResource(names.get(position).getProfileIconId());

        holder.iv_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.iv_image.getBorderWidth() > 0){
                    holder.iv_image.setBorderWidth(0);

                    Intent intent = new Intent();
                    intent.putExtra("remove_member", names.get(holder.getBindingAdapterPosition()).getUid());
                    listener.onMemberClicked(intent);

                }else{
                    holder.iv_image.setBorderWidth(3);
                    Intent intent = new Intent();
                    intent.putExtra("add_member", names.get(holder.getBindingAdapterPosition()).getUid());
                    listener.onMemberClicked(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView iv_image;
        TextView tv_name;
        CardView cv_card;
        ImageView profilePic;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_image = itemView.findViewById(R.id.imgAddEditRewardMember);
            tv_name = itemView.findViewById(R.id.txtAddEditRewardMemberName);
            cv_card = itemView.findViewById(R.id.addEditRewardCard);
            profilePic = itemView.findViewById(R.id.imgAddEditRewardMember);
        }
    }
}

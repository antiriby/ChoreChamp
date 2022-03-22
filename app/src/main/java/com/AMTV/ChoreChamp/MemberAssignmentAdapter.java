package com.AMTV.ChoreChamp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Member;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class MemberAssignmentAdapter extends RecyclerView.Adapter<MemberAssignmentAdapter.ViewHolder>{

    private ArrayList<User> names = new ArrayList<>();
//    private ArrayList<String> img = new ArrayList<>(); //TODO add user images
    private Context context;
    private ArrayList<User> assignees = new ArrayList<>();
    private MemberAssignmentAdapterListener listener;

    public MemberAssignmentAdapter(ArrayList<User> names, Context context, MemberAssignmentAdapterListener listener){
        this.names = names;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.family_member_assignment_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tv_name.setText(names.get(position).getName());

        holder.iv_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.iv_image.getBorderColor() == Color.RED){
                    holder.iv_image.setBorderColor(Color.BLACK);
                    holder.tv_name.setTextColor(Color.BLACK);

                    Intent intent = new Intent();
                    intent.putExtra("remove_member", names.get(holder.getBindingAdapterPosition()).getUid());
                    listener.onMemberClicked(intent);

                }else{
                    holder.iv_image.setBorderColor(Color.RED);
                    holder.tv_name.setTextColor(Color.RED);
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

        public ViewHolder(View itemView) {
            super(itemView);
            iv_image = itemView.findViewById(R.id.imgAddEditRewardMember);
            tv_name = itemView.findViewById(R.id.txtAddEditRewardMemberName);
            cv_card = itemView.findViewById(R.id.addEditRewardCard);
        }
    }
}

package com.AMTV.ChoreChamp;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;


public class UserAdapter extends RecyclerView.Adapter<UserAdapter.usersViewHolder> implements Serializable {

    private Context context;
    private ArrayList<User> userList;
    public UserAdapter(@NonNull Context context, @NonNull ArrayList<User> userList)
    {
        super();
        this.context = context;
        this.userList = userList;
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    @NonNull
    @Override
    public usersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.household_list_item, parent, false);
        usersViewHolder holder = new usersViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull usersViewHolder holder, int position) {
        holder.name.setText(userList.get(position).getName());
        //TODO: add points to User Class and display in RecyclerView
        //holder.points.setText(userList.get(position).getPoints());
    }

    class usersViewHolder extends RecyclerView.ViewHolder {
        TextView name, points;
        public usersViewHolder(@NonNull View itemView)
        {
            super(itemView);
            name = itemView.findViewById(R.id.txtHouseholdListName);
            //points = itemView.findViewById(R.id.txtHouseholdListPoints);
        }
    }
}

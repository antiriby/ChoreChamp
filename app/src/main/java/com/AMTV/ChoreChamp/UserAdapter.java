package com.AMTV.ChoreChamp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.io.Serializable;


public class UserAdapter extends FirebaseRecyclerAdapter<User,UserAdapter.usersViewHolder> implements Serializable {

    private Context context;
    public UserAdapter(@NonNull Context context, @NonNull FirebaseRecyclerOptions<User> options)
    {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull UserAdapter.usersViewHolder holder, int position, @NonNull User model) {
        holder.name.setText(model.getName());
        //TODO: add points to User Class and display in RecyclerView
        //holder.points.setText(model.getPoints());
    }

    @NonNull
    @Override
    public usersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.from(parent.getContext())
                .inflate(R.layout.household_list_item, parent, false);
        view.setVisibility(View.VISIBLE);
        return new UserAdapter.usersViewHolder(view);
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

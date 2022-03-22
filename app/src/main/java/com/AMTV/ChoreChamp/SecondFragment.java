package com.AMTV.ChoreChamp;

import android.content.Intent;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SecondFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SecondFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;

    DatabaseReference dbReference;
    FirebaseUser user;
    String userId, householdId;

    boolean isAdmin = false;

    //Button btnAdd;
    List<Task> taskList = new ArrayList<>();

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private ImageButton btnAdd;

    public SecondFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SecondFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SecondFragment newInstance() {

        SecondFragment fragment = new SecondFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_second2, container, false);


        if(MyApplication.isAdmin()){
            rootView = inflater.inflate(R.layout.activity_admin_task, container, false);
        }else{
            rootView = inflater.inflate(R.layout.activity_task, container, false);
        }

        recyclerView = rootView.findViewById(R.id.taskList);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this.getContext());

        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new TaskAdapter(taskList, this.getContext());
        recyclerView.setAdapter(mAdapter);

        userId = MyApplication.getUserId();

        householdId = MyApplication.getHouseholdId();

        if(MyApplication.isAdmin()) {
            dbReference = FirebaseDatabase.getInstance().getReference().child("Households").child(householdId).child("availableTasks");
            dbReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    taskList.clear();
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        Task ld = snapshot1.getValue(Task.class);
                        taskList.add(ld);
                    }

                    mAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getActivity(), "Failed to update task list", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            dbReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("availableTasks");
            dbReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    taskList.clear();
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        Task ld = snapshot1.getValue(Task.class);
                        taskList.add(ld);
                    }

                    mAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getActivity(), "Failed to update task list", Toast.LENGTH_LONG).show();
                }
            });
        }

        if(MyApplication.isAdmin()) {
            btnAdd = rootView.findViewById(R.id.btnTaskAdd);

            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), AddEditTaskActivity.class);

                    startActivity(intent);
                }
            });
        }
        // Inflate the layout for this fragment

        TextView emptyMessage = rootView.findViewById(R.id.emptyTaskListMessage);




        btnAdd = (ImageButton) rootView.findViewById(R.id.btnAddTask);

        if(MyApplication.isAdmin()){
            btnAdd.setVisibility(View.VISIBLE);

            //TODO Uncomment when task list stuff added
//        if(taskList.size() > 0){
//            emptyMessage.setVisibility(View.INVISIBLE);
//        }else{
            emptyMessage.setVisibility(View.VISIBLE);
//        }
        }else{
            btnAdd.setVisibility(View.INVISIBLE);
            emptyMessage.setVisibility(View.INVISIBLE);
        }


        return rootView;
    }
}
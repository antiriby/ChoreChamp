package com.AMTV.ChoreChamp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddEditTaskActivity extends Fragment {
    Button btnSave;
    ImageButton btnBack;
    FirebaseUser user;
    String userId, householdId;
    DatabaseReference householdReference;
    DatabaseReference userReference;
    DatabaseReference dbReference;

    private EditText editTaskName, editTaskPoints, editTaskDescription, editTaskDate;
    private ArrayList<User> members = new ArrayList<>();
    private ArrayList<String> images = new ArrayList<>();
    private ArrayList<String> assigneeIds = new ArrayList<>();
    private SimpleDateFormat taskDateFormat = new SimpleDateFormat("MM/dd/yyyy");
    Calendar c = Calendar.getInstance();

    private MemberAssignmentAdapter adapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;

    public AddEditTaskActivity() {

    }

    public static AddEditTaskActivity newInstance() {
        AddEditTaskActivity fragment = new AddEditTaskActivity();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_task);

//        userId = MyApplication.getUserId();
//
//        userReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
//
//        householdId = MyApplication.getHouseholdId();
//        householdReference = FirebaseDatabase.getInstance().getReference().child("Households");
//
//        fillMembersArray();
//        initRecyclerView();
//
//        btnSave = findViewById(R.id.btnAddEditSave);
//
//        editTaskName = (EditText) findViewById(R.id.addEditTaskName);
//        editTaskPoints = (EditText) findViewById(R.id.addEditTaskPoints);
//        editTaskDescription = (EditText) findViewById(R.id.addEditTaskDescription);
//        editTaskDate = (EditText) findViewById(R.id.editTaskDate);
//
//        btnSave.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                updateTask();
//            }
//        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        View rootView;

        rootView = inflater.inflate(R.layout.activity_add_edit_task,container,false);

        userId = MyApplication.getUserId();

        userReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

        householdId = MyApplication.getHouseholdId();
        householdReference = FirebaseDatabase.getInstance().getReference().child("Households");

        fillMembersArray();
//        initRecyclerView();

        btnSave = rootView.findViewById(R.id.btnAddEditSave);

        editTaskName = (EditText) rootView.findViewById(R.id.addEditTaskName);
        editTaskPoints = (EditText) rootView.findViewById(R.id.addEditTaskPoints);
        editTaskDescription = (EditText) rootView.findViewById(R.id.addEditTaskDescription);
        editTaskDate = (EditText) rootView.findViewById(R.id.editTaskDate);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateTask();
            }
        });

//        btnBack = rootView.findViewById()
        return rootView;
    }

    MemberAssignmentAdapterListener listener = new MemberAssignmentAdapterListener() {
        @Override
        public void onMemberClicked(Intent intent) {
            String addMember = intent.getStringExtra("add_member");
            String removeMember = intent.getStringExtra("remove_member");

            if (addMember != null){
                assigneeIds.add(addMember);
            }

            if (removeMember != null){
                assigneeIds.remove(removeMember);
            }
        }
    };

    private void updateTask() {
        String name = editTaskName.getText().toString().trim();
        String pointsStr = editTaskPoints.getText().toString().trim();
        String description = editTaskDescription.getText().toString().trim();
        String dateString = editTaskDate.getText().toString().trim();
        Date date = new Date(dateString);
        c.setTime(date);



        if (name.isEmpty()) {
            editTaskName.setError("Task name is required!");
            editTaskName.requestFocus();
            return;
        }

        if (pointsStr.isEmpty()) {
            editTaskPoints.setError("Number of points is required!");
            editTaskPoints.requestFocus();
            return;
        }

        if (dateString.isEmpty()) {
            editTaskDate.setError("Task completion date is required!");
            editTaskDate.requestFocus();
            return;
        }



        int points = Integer.parseInt(pointsStr);

        Map<String, Object> childUpdates = new HashMap<>();

        for(String assignee : assigneeIds){
            String key = householdReference.child(householdId).child("availableTasks").push().getKey();
            Task task = new Task(name,description,points,date);

            householdReference.child(householdId).child("availableTasks").child(key).setValue(task);

            MyApplication.getDbReference().child("Users").child(assignee).child("availableTasks").child(key).setValue(task);
        }

//        Intent intent = new Intent(AddEditTaskActivity.this, TaskActivity.class);
//        startActivity(intent);
        SecondFragment secondFragment = new SecondFragment();
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fram, secondFragment).commit();
    }

    private void fillMembersArray(){
        DatabaseReference membersRef = householdReference.child(householdId).child("members");
        membersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                members.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    User ld = snapshot1.getValue(User.class);
                    members.add(ld);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(AddEditTaskActivity.this, "Failed to get household members", Toast.LENGTH_LONG).show();
                Toast.makeText(getActivity(), "Failed to get household members", Toast.LENGTH_LONG).show();
            }
        });
    }

//    private void initRecyclerView(){
//        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//        recyclerView = findViewById(R.id.addEditTaskMemberList);
//        recyclerView.setLayoutManager(layoutManager);
//        adapter = new MemberAssignmentAdapter(members, this, listener);
//        recyclerView.setAdapter(adapter);
//    }

//    @Override
//    public void onMemberClicked(Intent intent){
//        String addMember = intent.getStringExtra("add_member");
//        String removeMember = intent.getStringExtra("remove_member");
//
//        if(addMember != null){
//            assigneeIds.add(addMember);
//        }
//
//        if(removeMember != null){
//            assigneeIds.remove(removeMember);
//        }
//    }


}

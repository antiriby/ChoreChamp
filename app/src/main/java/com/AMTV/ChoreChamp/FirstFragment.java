package com.AMTV.ChoreChamp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FirstFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FirstFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private DatabaseReference householdRef;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseRecyclerOptions<User> options;
    private ArrayList<User>userList;
    private ImageButton btnAdd;


    public FirstFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
     * @return A new instance of fragment FirstFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FirstFragment newInstance() {
        FirstFragment fragment = new FirstFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            //adapter = (UserAdapter) getArguments().getSerializable(ADAPTER);
        }
        householdRef = FirebaseDatabase.getInstance().getReference()
                .child("Households")
                .child(MyApplication.getHouseholdId())
                .child("members");

        options = new FirebaseRecyclerOptions.Builder<User>()
                .setQuery(householdRef, User.class)
                .build();

        userList = new ArrayList<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_first, container, false);

        recyclerView = rootView.findViewById(R.id.recyclerFragmentHouseholdList);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this.getContext());

        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new UserAdapter(this.getContext(), userList);
        recyclerView.setAdapter(mAdapter);

        householdRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for(DataSnapshot snap : snapshot.getChildren()){
                    User member = snap.getValue(User.class);
                    userList.add(member);
                }

                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Failed to retrieve household members. Please try again later.", Toast.LENGTH_LONG).show();
            }
        });
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        btnAdd = (ImageButton) rootView.findViewById(R.id.btnAddFamilyMember);

        if(MyApplication.isAdmin()){
            btnAdd.setVisibility(View.VISIBLE);
            
            // TODO add onClickListener

        }else{
            btnAdd.setVisibility(View.INVISIBLE);
        }


        return rootView;
    }
}
package com.AMTV.ChoreChamp;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FirstFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FirstFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String CURRENT_USER = "currentUser";
    private static final String ADAPTER = "adapter";

    // TODO: Rename and change types of parameters
    private User currentUser;
    private RecyclerView recyclerView;
    private DatabaseReference householdRef;
    private UserAdapter adapter;

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
    public static FirstFragment newInstance(User currentUser, UserAdapter adapter) {
        FirstFragment fragment = new FirstFragment();
        Bundle args = new Bundle();
        args.putSerializable(CURRENT_USER,currentUser);
        args.putSerializable(ADAPTER,adapter);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentUser = (User) getArguments().getSerializable(CURRENT_USER);
            //adapter = (UserAdapter) getArguments().getSerializable(ADAPTER);
        }
        householdRef = FirebaseDatabase.getInstance().getReference()
                .child("Households")
                .child(currentUser.getHouseholdId())
                .child("members");

//        FirebaseRecyclerOptions<User> options = new FirebaseRecyclerOptions.Builder<User>()
//                .setQuery(householdRef, User.class)
//                .build();
//
//        adapter = new UserAdapter(getActivity(),options);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_first, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerFragmentHouseholdList);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this.getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(llm);

        FirebaseRecyclerOptions<User> options = new FirebaseRecyclerOptions.Builder<User>()
                .setQuery(householdRef, User.class)
                .build();

        adapter = new UserAdapter(getActivity(),options);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter.notifyDataSetChanged();
//        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerFragmentHouseholdList);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//
//        FirebaseRecyclerOptions<User> options = new FirebaseRecyclerOptions.Builder<User>()
//                .setQuery(householdRef, User.class)
//                .build();
//
//        adapter = new UserAdapter(getActivity(),options);
//        recyclerView.setAdapter(adapter);
//
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public UserAdapter getAdapter(){
        return adapter;
    }
}
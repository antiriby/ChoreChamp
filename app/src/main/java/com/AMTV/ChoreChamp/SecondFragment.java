package com.AMTV.ChoreChamp;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SecondFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SecondFragment extends Fragment {

    private ImageButton btnAdd;

    public SecondFragment() {
        // Required empty public constructor
    }


    public static SecondFragment newInstance(String param1, String param2) {
        SecondFragment fragment = new SecondFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_second2, container, false);

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
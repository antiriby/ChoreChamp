package com.AMTV.ChoreChamp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomeActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    private BottomNavigationView bottomNavigationView;
    private RecyclerView recyclerView;
    private UserAdapter adapter;

    private DatabaseReference householdRef;
    final FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        householdRef = FirebaseDatabase.getInstance().getReference().child("Households").child(MyApplication.getHouseholdId()).child("members");

        bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setOnItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.family);
    }
    FirstFragment firstFragment = new FirstFragment();
    SecondFragment secondFragment = new SecondFragment();
    ThirdFragment thirdFragment = new ThirdFragment();
    FourthFragment fourthFragment = new FourthFragment();

    //Bottom Navigation selection
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.family:
                firstFragment = FirstFragment.newInstance();
                fragmentManager.beginTransaction().replace(R.id.bottomNavFragment, firstFragment).commit();
                return true;
            case R.id.tasks:
                fragmentManager.beginTransaction().replace(R.id.bottomNavFragment, secondFragment).commit();
                return true;
            case R.id.rewards:
                fragmentManager.beginTransaction().replace(R.id.bottomNavFragment, thirdFragment).commit();
                return true;
            case R.id.profile:
                fragmentManager.beginTransaction().replace(R.id.bottomNavFragment, fourthFragment).commit();
                return true;
        }
        return false;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}
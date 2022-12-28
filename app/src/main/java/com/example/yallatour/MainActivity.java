package com.example.yallatour;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import fragments.MainFragment;
import fragments.NearbyListFragment;
import fragments.ProfileFragment;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{


    BottomNavigationView bottomNavigationView;
    //selected tab number, we have 3 tabs so value must be lie between 1-3
    // we are setting default value 1. because by default first tab will be selected
    private int selectedTabNumber = 1 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottom_navigation);


        // selecting first fragment bu default
        getSupportFragmentManager().beginTransaction().setReorderingAllowed(true) .replace(R.id.fragmentContainer , MainFragment.class , null)
                        .commit();
        bottomNavigationView.setSelectedItemId(R.id.item_home);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_home:
                getSupportFragmentManager().beginTransaction().setReorderingAllowed(true) .replace(R.id.fragmentContainer , MainFragment.class , null)
                        .commit();
                return true;
            case R.id.item_nearby:
                getSupportFragmentManager().beginTransaction().setReorderingAllowed(true) .replace(R.id.fragmentContainer , NearbyListFragment.class , null)
                        .commit();
                return true;
            case R.id.item_profile:
                getSupportFragmentManager().beginTransaction().setReorderingAllowed(true) .replace(R.id.fragmentContainer , ProfileFragment.class , null)
                        .commit();
                return true;

        }
        return false;
    }
}
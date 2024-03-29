package com.example.meshwar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import fragments.MainFragment;

public class Test extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


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

        }
        return false;
    }
}
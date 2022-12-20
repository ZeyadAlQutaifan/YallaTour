package com.example.yallatour;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import adapters.PlaceAdapter;
import modules.Photos;

public class Test extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        RecyclerView recyclerView = findViewById(R.id.main_container);
        recyclerView.setHasFixedSize(true);
        List<Photos> mList = new ArrayList<>();
        mList.add(new Photos(R.drawable.img1));
        mList.add(new Photos(R.drawable.img2));
        mList.add(new Photos(R.drawable.img1));
        mList.add(new Photos(R.drawable.img1));
        mList.add(new Photos(R.drawable.img2));
        mList.add(new Photos(R.drawable.img1));
        mList.add(new Photos(R.drawable.img1));
        mList.add(new Photos(R.drawable.img2));
        mList.add(new Photos(R.drawable.img1));
        mList.add(new Photos(R.drawable.img1));
        mList.add(new Photos(R.drawable.img2));
        mList.add(new Photos(R.drawable.img1));
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
     //   PlaceAdapter adapter = new PlaceAdapter(getApplication() ,mList);
   //     recyclerView.setAdapter(adapter);
    }
}
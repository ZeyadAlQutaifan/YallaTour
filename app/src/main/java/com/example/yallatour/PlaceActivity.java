package com.example.yallatour;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;
import java.util.List;

import adapters.CommentAdapter;
import modules.Comment;
import util.Constant;
import util.Global;

public class PlaceActivity extends AppCompatActivity {

    List<SlideModel> imageList = new ArrayList<>();// Create image list


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);
        imageList.add(new SlideModel("https://i.ytimg.com/vi/g0rIH15vZvk/maxresdefault.jpg", ScaleTypes.CENTER_CROP));
        imageList.add(new SlideModel("https://th.bing.com/th/id/R.99308f4a5313a34e1e4c07588ad9264a?rik=PE9beiz2zJ%2bh4w&pid=ImgRaw&r=0", ScaleTypes.CENTER_CROP));
        imageList.add(new SlideModel("https://th.bing.com/th/id/OIP.TogPx8eYNedwxr-St7e_gAHaE9?pid=ImgDet&rs=1", ScaleTypes.CENTER_CROP));
        ImageSlider imageSlider = findViewById(R.id.slider);
//       // RecyclerView recyclerView = findViewById(R.id.recyclerView1);
//        List<Comment> list = new ArrayList<>();
//        for (int i = 0; i < 20; i++) {
//            list.add(new Comment());
//        }
//        CommentAdapter adapter = new CommentAdapter(list);
//        recyclerView.setAdapter(adapter);
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(
//                new LinearLayoutManager(this));
        imageSlider.setImageList(imageList);
    }

    public void navigateToPlace(View view) {
        Global.updateDashboard(Constant.INCREASE_NAVIGATION);
    }
}
package fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.yallatour.R;

import java.util.ArrayList;
import java.util.List;

import util.Constant;
import util.Global;

public class PlaceFragment extends Fragment {


    List<SlideModel> imageList = new ArrayList<>();// Create image list
    private TextView txtDistance;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_place, container, false);
        txtDistance = view.findViewById(R.id.txtDistance);
        imageList.add(new SlideModel("https://bit.ly/2YoJ77H", ScaleTypes.CENTER_CROP));
        imageList.add(new SlideModel("https://bit.ly/2BteuF2", ScaleTypes.CENTER_CROP));
        imageList.add(new SlideModel("https://bit.ly/3fLJf72", ScaleTypes.CENTER_CROP));
        ImageSlider imageSlider = view.findViewById(R.id.slider);
        imageSlider.setImageList(imageList);

        return view;
    }


}
package com.example.yallatour;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import modules.Place;
import util.Constant;
import util.Global;

public class PlaceActivity extends AppCompatActivity {

  Place place ;
    private List<SlideModel> imageList = new ArrayList<>();
private TextView txtTitle, txtDexcription, txtDistance , txtCity , txtRate;
private ImageView imgMain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);

        place = (Place) getIntent().getSerializableExtra(Constant.PASSING_OBJECT_KEY);
        txtTitle = findViewById(R.id.txtTitle);
        txtDexcription = findViewById(R.id.txtDescription);
        txtDistance = findViewById(R.id.txtDistance);
        imgMain = findViewById(R.id.imgMain);
        txtCity = findViewById(R.id.txtCity);
        txtRate = findViewById(R.id.txtRate);


        for (int i = 0 ; i<place.getImages().size() ; i++){
            if(place.getImages().get(i) != null && !place.getImages().get(i).isEmpty()) {
                imageList.add(new SlideModel(place.getImages().get(i), ScaleTypes.CENTER_CROP));
            }
        }
        Glide.with(getApplicationContext()).load(place.getImages().get(0)).centerCrop().into(imgMain);
        txtTitle.setText(Global.getNullString(place.getTitle()));
        txtDexcription.setText(Global.getNullString(place.getDescription()));
        txtDistance.setText(String.format("%.2f" , Global.distFrom(Constant.LATITUDE , Constant.LONGITUDE , place.getLat() , place.getLng()) )+ " KM");
        txtRate.setText(String.format("%.2f" , place.getRate()));
        txtCity.setText(Global.getNullCity(place.getCity()));
        showWeatherStatus();
        ImageSlider imageSlider = findViewById(R.id.slider);

        imageSlider.setImageList(imageList);
    }
    private void showWeatherStatus() {
        // TODO Call open weather map, show result in frameWeatherData frame
        String weatherURL = Constant.WEATHER_URI +
                Constant.LATITUDE_BLOCK.replace("{#}", String.valueOf(place.getLat())) +
                Constant.AND +
                Constant.LONGITUDE_BLOCK.replace("{#}", String.valueOf(place.getLng())) +
                Constant.AND +
                Constant.KEY_BLOCK.replace("{#}", Constant.WEATHER_KEY);

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        // this is the error listener method which
// we will call if we get any error from API.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, weatherURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONObject main = response.getJSONObject("main");
                    String temp = main.getString("temp");
                    String city = response.getString("name");
                    Log.v(Constant.TAG_V , "tenp ==>" + temp + "\n city ==> " + city);


                } catch (JSONException e) {
                    // if we do not extract data from json object properly.
                    // below line of code is use to handle json exception
                    e.printStackTrace();
                }
            }
        }, error -> {
            // below line is use to display a toast message along with our error.
            Toast.makeText(getApplicationContext(), "Fail to get data..", Toast.LENGTH_SHORT).show();
        });
        // at last we are adding our json
        // object request to our request
        // queue to fetch all the json data.
        queue.add(jsonObjectRequest);

    }

    public void navigateToPlace(View view) {

        Uri gmmIntentUri = Uri.parse("geo:"+place.getLat()+"," + place.getLng());
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
            Global.updateDashboard(Constant.INCREASE_NAVIGATION);

        }
    }
}
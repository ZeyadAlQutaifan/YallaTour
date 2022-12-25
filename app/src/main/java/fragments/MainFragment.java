package fragments;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.yallatour.AllPlacesActivity;
import com.example.yallatour.PlaceActivity;
import com.example.yallatour.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.Query;

import org.json.JSONException;
import org.json.JSONObject;

import GPS.GPSTracker;
import modules.Place;
import util.Constant;
import util.Global;

public class MainFragment extends Fragment {


    FirebaseRecyclerAdapter<Place, MainFragment.MostViewHolder>   firebaseRecyclerAdapter;
private RecyclerView  mostViewsRecycler;
    private FrameLayout frameWeatherData, frameRequestLocation;
    private Button btnRequestLocationPermission;
    private TextView txtCityName, txtTemp , txtViewAll;
    double longitude = 0, latitude = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);



        mostViewsRecycler = view.findViewById(R.id.mostViewsRecycler);
        txtViewAll = view.findViewById(R.id.txtViewAll);

        txtViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity() , AllPlacesActivity.class));
            }
        });
        frameWeatherData = view.findViewById(R.id.frameWeatherData);
        frameRequestLocation = view.findViewById(R.id.frameRequestLocation);
        requestLocationPermission();
        txtCityName = view.findViewById(R.id.txtCityName);
        txtTemp = view.findViewById(R.id.txtTemp);
        btnRequestLocationPermission = view.findViewById(R.id.btnRequestLocationPermission);

        btnRequestLocationPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestLocationPermission();
            }
        });



mostViewsRecycler.setHasFixedSize(true);
mostViewsRecycler.setLayoutManager(new LinearLayoutManager(getActivity(),  LinearLayoutManager.HORIZONTAL ,false));


        return view;
    }


    @Override
    public void onStop() {
        super.onStop();
        firebaseRecyclerAdapter.stopListening();
    }

    @Override
    public void onStart() {
        super.onStart();

        loadMostViewedPlaces();
    }

    private void loadMostViewedPlaces() {
        Query query = Constant.places;
        FirebaseRecyclerOptions<Place>  options = new FirebaseRecyclerOptions.Builder<Place>().setQuery(query , Place.class).build();
     firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Place,MainFragment.MostViewHolder >(options){

            @NonNull
            @Override
            public MainFragment.MostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_most_viewed, parent, false);

                return new MainFragment.MostViewHolder(v);
            }

            @Override
            protected void onBindViewHolder(@NonNull MainFragment.MostViewHolder holder, int position, @NonNull Place model) {


                holder.txtTitle.setText(Global.getNullString(model.getTitle()).length() > 22?  Global.getNullString(model.getTitle()).substring(0,20)+".." :Global.getNullString(model.getTitle()));
                Glide.with(getActivity())
                        .load(Global.getPlaceImageNotFound(model.getImages().get(0)))
                        .centerCrop()
                        .into(holder.imageView);

                holder.container.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity() , PlaceActivity.class);
                        intent.putExtra(Constant.PASSING_OBJECT_KEY, model);
                        startActivity(intent);
                        String id = String.valueOf(getSnapshots().getSnapshot(holder.getBindingAdapterPosition()).getKey());
                        Log.v(Constant.TAG_V , "==>" + id);
                    }
                });
            }
        };
        mostViewsRecycler.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();

    }



    @Override
    public void onResume() {
        super.onResume();

        getLocation();
    }


    private synchronized void requestLocationPermission() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
            //     ActivityCompat.requestPermissions(getActivity() , new String []{Manifest.permission.ACCESS_FINE_LOCATION} , request_code);

        } else {
            frameWeatherData.setVisibility(View.VISIBLE);
            frameRequestLocation.setVisibility(View.INVISIBLE);

        }
        notifyAll();

    }

    private void getLocation() {
        GPSTracker tracker = new GPSTracker(getActivity());
        if (!tracker.canGetLocation()) {
            tracker.showSettingsAlert();
        } else {
            latitude = tracker.getLatitude();
            longitude = tracker.getLongitude();
            Constant.LATITUDE = latitude;
            Constant.LONGITUDE = longitude;
            showWeatherStatus();
        }
    }

    private void showWeatherStatus() {
        // TODO Call open weather map, show result in frameWeatherData frame
        String weatherURL = Constant.WEATHER_URI +
                Constant.LATITUDE_BLOCK.replace("{#}", String.valueOf(latitude)) +
                Constant.AND +
                Constant.LONGITUDE_BLOCK.replace("{#}", String.valueOf(longitude)) +
                Constant.AND +
                Constant.KEY_BLOCK.replace("{#}", Constant.WEATHER_KEY);

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        // this is the error listener method which
// we will call if we get any error from API.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, weatherURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONObject main = response.getJSONObject("main");
                    String temp = main.getString("temp");
                    String city = response.getString("name");
                    double C = (Double.parseDouble(temp) - 32) * (9 / 5);
                    txtTemp.setText(String.format("%.2f" , C) + " C");
                    txtCityName.setText(city);

                } catch (JSONException e) {
                    // if we do not extract data from json object properly.
                    // below line of code is use to handle json exception
                    e.printStackTrace();
                }
            }
        }, error -> {
            // below line is use to display a toast message along with our error.
            Toast.makeText(getActivity(), "Fail to get data..", Toast.LENGTH_SHORT).show();
        });
        // at last we are adding our json
        // object request to our request
        // queue to fetch all the json data.
        queue.add(jsonObjectRequest);

    }


    private ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
        if (isGranted) {
            frameWeatherData.setVisibility(View.VISIBLE);
            frameRequestLocation.setVisibility(View.INVISIBLE);

            // Permission is granted. Continue the action or workflow in your
            // app.
        } else {
            frameWeatherData.setVisibility(View.INVISIBLE);
            frameRequestLocation.setVisibility(View.VISIBLE);
        }
    });

public static class MostViewHolder extends  RecyclerView.ViewHolder{

        TextView txtTitle ;

        ImageView imageView ;

        CardView container ;
        public MostViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            imageView = itemView.findViewById(R.id.imageView);
            container = itemView.findViewById(R.id.container);
        }
    }
}
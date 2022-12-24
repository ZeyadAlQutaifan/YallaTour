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
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.yallatour.PlaceActivity;
import com.example.yallatour.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import GPS.GPSTracker;
import adapters.PlaceAdapter;
import dashboard.ShowPLacesActivity;
import modules.Photos;
import modules.Place;
import util.Constant;
import util.Global;

public class MainFragment extends Fragment {


    private Query query;
    private FirebaseRecyclerOptions<Place> options;
    private FirebaseRecyclerAdapter<Place, MainFragment.PlaceViewHolder> firebaseRecyclerAdapter;
private RecyclerView recyclerView;
    private FloatingActionButton fabAdd;
    private FrameLayout frameWeatherData, frameRequestLocation;
    private Button btnRequestLocationPermission;
    private TextView txtCityName, txtTemp;
    double longitude = 0, latitude = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        fabAdd = view.findViewById(R.id.fabAdd);
        if (Constant.isAdmin) {
            fabAdd.setVisibility(View.VISIBLE);
        } else {
            fabAdd.setVisibility(View.GONE);
        }
        recyclerView = view.findViewById(R.id.main_container);
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


        recyclerView.setHasFixedSize(true);



        List<Place> places = new ArrayList<>();




        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));


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
        query = Constant.places;
        options = new FirebaseRecyclerOptions.Builder<Place>().setQuery(query , Place.class).build();
        firebaseRecyclerAdapter
                = new FirebaseRecyclerAdapter<Place,MainFragment.PlaceViewHolder >(options){

            @NonNull
            @Override
            public MainFragment.PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main_list, parent, false);

                return new MainFragment.PlaceViewHolder(v);
            }

            @Override
            protected void onBindViewHolder(@NonNull MainFragment.PlaceViewHolder holder, int position, @NonNull Place model) {

                if (position%2 ==0){
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 256*5);
                    holder.imageView.setLayoutParams(layoutParams);
                }else{
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 200*5);
                    holder.imageView.setLayoutParams(layoutParams);
                }
                holder.txtTitle.setText(Global.getNullString(model.getTitle()));
                Glide.with(getActivity())
                        .load(Global.getPlaceImageNotFound(model.getImages().get(0)))
                        .centerCrop()
                        .into(holder.imageView);

                holder.container.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity() , PlaceActivity.class);
                        intent.putExtra(Constant.PASSING_PLACE_KEY , model);
                        startActivity(intent);
                        String id = String.valueOf(getSnapshots().getSnapshot(holder.getBindingAdapterPosition()).getKey());
                        Log.v(Constant.TAG_V , "==>" + id);
                    }
                });
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
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
                    txtTemp.setText(temp + "C");
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

    public static class PlaceViewHolder extends  RecyclerView.ViewHolder{

        TextView txtTitle ;

        RoundedImageView imageView ;

        CardView container ;
        public PlaceViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            imageView = itemView.findViewById(R.id.imageView);
            container = itemView.findViewById(R.id.container);
        }
    }
}
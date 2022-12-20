package fragments;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.yallatour.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import GPS.GPSTracker;
import adapters.PlaceAdapter;
import modules.Photos;
import modules.Place;
import util.Constant;
import util.Global;

public class MainFragment extends Fragment {
    private final static int request_code = 101;

private FloatingActionButton fabAdd;
    private FrameLayout frameWeatherData, frameRequestLocation;
    private Button btnRequestLocationPermission;
private TextView txtCityName , txtTemp ;
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
        RecyclerView recyclerView = view.findViewById(R.id.main_container);
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
        List<String> urls = new ArrayList<>();
        urls.add("https://th.bing.com/th/id/OIP._Dm-xl4kqIY-Mh-9D43JzwHaJ5?pid=ImgDet&rs=1");
        urls.add("https://www.blacktomato.com/wp-content/uploads/2019/03/Petra-Monastery.jpg");
        urls.add("https://th.bing.com/th/id/OIP.rPpsGHyx90hU-E3_h9_PbQHaE7?pid=ImgDet&rs=1");


        List<Place> places = new ArrayList<>();
        places.add(new Place("Petra" , urls , Constant.LOREN_EXAMPLE , 35.22222 , 64.22222));
        urls.add(0 , "https://th.bing.com/th/id/R.b2f936c2240ff12609f17c6311a89a94?rik=mbLYgGIwSWPuYQ&riu=http%3a%2f%2fpromotravel-eg.com%2fwp-content%2fuploads%2f2018%2f01%2fgpic1.jpg&ehk=5GlSOtkYnrgLmi%2f80WbkLTiiQa8Tb0Yi%2b%2br3YcIdliE%3d&risl=&pid=ImgRaw&r=0");
        places.add(new Place("Wadi Rum" , urls , Constant.LOREN_EXAMPLE , 35.22222 , 64.22222));
        places.add(new Place("Petra" , urls , Constant.LOREN_EXAMPLE , 35.22222 , 64.22222));
        urls.add(0 , "https://th.bing.com/th/id/R.b2f936c2240ff12609f17c6311a89a94?rik=mbLYgGIwSWPuYQ&riu=http%3a%2f%2fpromotravel-eg.com%2fwp-content%2fuploads%2f2018%2f01%2fgpic1.jpg&ehk=5GlSOtkYnrgLmi%2f80WbkLTiiQa8Tb0Yi%2b%2br3YcIdliE%3d&risl=&pid=ImgRaw&r=0");
        places.add(new Place("Wadi Rum" , urls , Constant.LOREN_EXAMPLE , 35.22222 , 64.22222));
        places.add(new Place("Petra" , urls , Constant.LOREN_EXAMPLE , 35.22222 , 64.22222));
        urls.add(0 , "https://th.bing.com/th/id/R.b2f936c2240ff12609f17c6311a89a94?rik=mbLYgGIwSWPuYQ&riu=http%3a%2f%2fpromotravel-eg.com%2fwp-content%2fuploads%2f2018%2f01%2fgpic1.jpg&ehk=5GlSOtkYnrgLmi%2f80WbkLTiiQa8Tb0Yi%2b%2br3YcIdliE%3d&risl=&pid=ImgRaw&r=0");
        places.add(new Place("Wadi Rum" , urls , Constant.LOREN_EXAMPLE , 35.22222 , 64.22222));
        places.add(new Place("Petra" , urls , Constant.LOREN_EXAMPLE , 35.22222 , 64.22222));
        urls.add(0 , "https://th.bing.com/th/id/R.b2f936c2240ff12609f17c6311a89a94?rik=mbLYgGIwSWPuYQ&riu=http%3a%2f%2fpromotravel-eg.com%2fwp-content%2fuploads%2f2018%2f01%2fgpic1.jpg&ehk=5GlSOtkYnrgLmi%2f80WbkLTiiQa8Tb0Yi%2b%2br3YcIdliE%3d&risl=&pid=ImgRaw&r=0");
        places.add(new Place("Wadi Rum" , urls , Constant.LOREN_EXAMPLE , 35.22222 , 64.22222));



        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        PlaceAdapter adapter = new PlaceAdapter(getContext() , places);
        recyclerView.setAdapter(adapter);


        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
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
            Constant.LATITUDE = latitude ;
            Constant.LONGITUDE =  longitude ;
            showWeatherStatus();
        }
    }

    private  void showWeatherStatus() {
        // TODO Call open weather map, show result in frameWeatherData frame
        String weatherURL = Constant.WEATHER_URI +
                Constant.LATITUDE_BLOCK.replace("{#}", String.valueOf(latitude)) +
                Constant.AND +
                Constant.LONGITUDE_BLOCK.replace("{#}", String.valueOf(longitude)) +
                Constant.AND +
                Constant.KEY_BLOCK.replace("{#}", Constant.WEATHER_KEY);

double disTest = Global.distFrom(latitude , longitude , 32.3428, 36.1970);
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


}
package dashboard;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.fragment.app.FragmentActivity;

import com.example.meshwar.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.compat.Place;
import com.google.android.libraries.places.compat.ui.PlaceAutocompleteFragment;
import com.google.android.libraries.places.compat.ui.PlaceSelectionListener;

import util.Constant;

public class LocationPickerActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LatLng sydney = new LatLng(-8.579892, 116.095239);
    private MapFragment mapFragment;

    double lat , lon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_location_picker);
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        setupAutoCompleteFragment();
    }


    private void setupAutoCompleteFragment() {
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                sydney = place.getLatLng();
                lat = sydney.latitude;
                lon = sydney.longitude;
                mapFragment.getMapAsync(LocationPickerActivity.this);
            }

            @Override
            public void onError(Status status) {
                Log.e("Error", status.getStatusMessage());
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 8.5f));
        mMap.addMarker(new MarkerOptions()
                .position(sydney)
                .title("www.kodetr.com")
                .snippet("Lokasi dimataram")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mMap != null) {
            mMap.clear();
        }
    }

    public void confirmLocation(View view) {

        Log.v(Constant.TAG_V , "==> lat" + lat + "\t ==> " + lon);
        Intent intent=new Intent();
        intent.putExtra("LAT",lat);
        intent.putExtra("LON",lon);

        setResult(RESULT_OK,intent);
        finish();
    }
}
package com.example.gap;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapLocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LatLng selectedLocation;
    private static final LatLng MUMBAI_DEFAULT = new LatLng(19.0760, 72.8777); // Mumbai coordinates

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_location);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        Button btnConfirm = findViewById(R.id.btnConfirmLocation);
        btnConfirm.setOnClickListener(v -> {
            if (selectedLocation != null) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("latitude", selectedLocation.latitude);
                resultIntent.putExtra("longitude", selectedLocation.longitude);
                setResult(RESULT_OK, resultIntent);
                finish();
            } else {
                Toast.makeText(this, "Please select a location on the map", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Set default location to Mumbai
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(MUMBAI_DEFAULT, 12));
        selectedLocation = MUMBAI_DEFAULT;
        mMap.addMarker(new MarkerOptions().position(MUMBAI_DEFAULT).title("Selected Location"));

        // Allow user to select location by tapping on map
        mMap.setOnMapClickListener(latLng -> {
            mMap.clear();
            selectedLocation = latLng;
            mMap.addMarker(new MarkerOptions().position(latLng).title("Selected Location"));
            Toast.makeText(MapLocationActivity.this, "Location selected", Toast.LENGTH_SHORT).show();
        });

        // Enable location controls
        try {
            mMap.setMyLocationEnabled(false); // Set to true if you add location permission
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }
}
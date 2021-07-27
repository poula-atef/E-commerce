package com.example.e_commerce.UI;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.e_commerce.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationListener listener;
    private LocationManager manager;
    private FrameLayout btn;
    private String addrs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        btn = findViewById(R.id.check);
        listener = new MyLocationListener(this);
        manager = (LocationManager) getSystemService(LOCATION_SERVICE);

        try {
            ActivityCompat.requestPermissions(MapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 5);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
                return;
            }
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 6000, 0, listener);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(30.044, 31.236), 8));

        getMyCurrentLocation();
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                Geocoder coder = new Geocoder(getApplicationContext());
                List<Address> address;
                try {
                    address = coder.getFromLocation(marker.getPosition().latitude, marker.getPosition().longitude, 1);
                    if (address.size() > 0) {
                        addrs = address.get(0).getAddressLine(0);
                        marker.setSnippet(addrs);
                        marker.setTitle("New Location");
                        Toast.makeText(MapActivity.this, "Location Found!!", Toast.LENGTH_SHORT).show();
                        btn.setVisibility(View.VISIBLE);
                        btn.animate().alpha(1.0f).setDuration(200);
                    } else {
                        Toast.makeText(MapActivity.this, "No Address For This Position..", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(MapActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void onComponentClick(View view) {
        int id = view.getId();
        if (id == R.id.check) {
            Intent intent = new Intent(MapActivity.this, CheckOutActivity.class);
            intent.putExtra("address", addrs);
            setResult(RESULT_OK, intent);
            Animatoo.animateSlideRight(this);
            finish();
        }
    }


    void getMyCurrentLocation() {
        mMap.clear();
        Geocoder coder = new Geocoder(getApplicationContext());
        List<Address> address;
        Location loc = null;
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
                return;
            }
            loc = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        if(loc != null){
            LatLng latLng = new LatLng(loc.getLatitude(),loc.getLongitude());
            try{
                address = coder.getFromLocation(latLng.latitude,latLng.longitude,1);
                if(address != null){
                    addrs = address.get(0).getAddressLine(0);
                    mMap.addMarker(new MarkerOptions().position(latLng).title("My Location").snippet(addrs)).setDraggable(true);
                    Toast.makeText(MapActivity.this, "Location Found!!", Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e){
                mMap.addMarker(new MarkerOptions().position(latLng).title("My Location")).setDraggable(true);
            }
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
            btn.setVisibility(View.VISIBLE);
            btn.animate().alpha(1.0f).setDuration(200);
        }else{
            Toast.makeText(this, "Cannot Determine Your Location !!", Toast.LENGTH_SHORT).show();
        }
    }


    class MyLocationListener implements LocationListener{
        private Context context;

        public MyLocationListener(Context context) {
            this.context = context;
        }

        @Override
        public void onProviderEnabled(@NonNull String provider) {
            Toast.makeText(context, "GPS Enabled", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {
            Toast.makeText(context, "GPS Disabled", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onLocationChanged(@NonNull Location location) {
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 5 && grantResults.length == 0) {
            Toast.makeText(this, "You Should accept this permission to open location in map..", Toast.LENGTH_SHORT).show();
        }
    }
}
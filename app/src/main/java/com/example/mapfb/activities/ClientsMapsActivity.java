package com.example.mapfb.activities;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.mapfb.models.Coordinates;
import com.example.mapfb.utils.GpsTracker;
import com.example.mapfb.utils.LatLngInterpolator;
import com.example.mapfb.utils.MarkerAnimation;
import com.example.mapfb.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ClientsMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 125;
    private Location currentLocation;
    private Marker currentLocationMarker;
    private Boolean firstTimeFlag = true;
    DatabaseReference mDatabase;
    Coordinates coordinates;
    String markerColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clients_maps);
        mDatabase= FirebaseDatabase.getInstance().getReference("Coordinates");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent busSelectedIntent = getIntent();
        if (busSelectedIntent.getExtras() != null){
            if (busSelectedIntent.getExtras().getString("busColor").equals("blue"))
                markerColor = "blue";
            else if (busSelectedIntent.getExtras().getString("busColor").equals("purple"))
                markerColor = "purple";
            else if (busSelectedIntent.getExtras().getString("busColor").equals("orange"))
                markerColor = "orange";
            else if (busSelectedIntent.getExtras().getString("busColor").equals("green"))
                markerColor = "green";
        }

        getLocationDataFromFirebase();
//        moveToCurrentLocation();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
//        mMap.setMyLocationEnabled(true);

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isGooglePlayServicesAvailable()) {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            startCurrentLocationUpdates();
        }
    }

    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status)
            return true;
        else {
            if (googleApiAvailability.isUserResolvableError(status))
                Toast.makeText(this, "Please Install google play services to use this application", Toast.LENGTH_LONG).show();
        }
        return false;
    }

    private void startCurrentLocationUpdates() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(3000);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ClientsMapsActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                return;
            }
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, mLocationCallback, Looper.myLooper());
    }

    private final LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            if (locationResult.getLastLocation() == null)
                return;
            currentLocation = locationResult.getLastLocation();
            if (firstTimeFlag && mMap != null) {
                animateCamera(currentLocation);
                firstTimeFlag = false;
            }
//            showMarker(currentLocation);
        }
    };

    private void animateCamera(@NonNull Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    private void showMarker(@NonNull Coordinates currentLocation) {
        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        if (currentLocationMarker == null){
            if (markerColor.equals("blue"))
                currentLocationMarker = mMap.addMarker(new MarkerOptions().icon(bitmapDescriptorFromVector(this,R.drawable.ic_bus_pin_blue)).title("Blue Line Bus").position(latLng));
            else if (markerColor.equals("purple"))
                currentLocationMarker = mMap.addMarker(new MarkerOptions().icon(bitmapDescriptorFromVector(this,R.drawable.ic_bus_pin_purple)).title("Purple Line Bus").position(latLng));
            else if (markerColor.equals("orange"))
                currentLocationMarker = mMap.addMarker(new MarkerOptions().icon(bitmapDescriptorFromVector(this,R.drawable.ic_bus_pin_orange)).title("Orange Line Bus").position(latLng));
            else if (markerColor.equals("green"))
                currentLocationMarker = mMap.addMarker(new MarkerOptions().icon(bitmapDescriptorFromVector(this,R.drawable.ic_bus_pin_green)).title("Green Line Bus").position(latLng));
        } else {
            MarkerAnimation.animateMarkerToGB(currentLocationMarker, latLng, new LatLngInterpolator.Spherical());
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (fusedLocationProviderClient != null)
            fusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
    }

    private void getLocationDataFromFirebase(){

        // Creating new Coordinates node, which returns the unique key value
        // new Coordinates node would be /Coordinates/$coordinatesId/

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Coordinates coordinates = dataSnapshot.getValue(Coordinates.class);

                Log.d("dataReceived", "Latitude " + coordinates.getLatitude() + ", Longitude " + coordinates.getLongitude());
//                Toast.makeText(getApplicationContext(),"latitude "+coordinates.getLatitude()+ " longitude"+coordinates.getLongitude(),Toast.LENGTH_SHORT).show();
                showMarker(coordinates);
            }


            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("dataFailed", "Failed to read value.", error.toException());
            }
        });
    }

    public void onClickGetCurrentLocation(View view) {
        moveToCurrentLocation();
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private void moveToCurrentLocation(){
        GpsTracker gpsTracker = new GpsTracker(ClientsMapsActivity.this);
        if (gpsTracker.isGPSEnabled() && gpsTracker.isNetworkEnabled() && gpsTracker.canGetLocation()){
            LatLng currentLocation = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
            mMap.addMarker(new MarkerOptions().position(currentLocation).icon(bitmapDescriptorFromVector(this,R.drawable.ic_user_location_marker)).title("Current Location"));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,25));
        }
    }
}

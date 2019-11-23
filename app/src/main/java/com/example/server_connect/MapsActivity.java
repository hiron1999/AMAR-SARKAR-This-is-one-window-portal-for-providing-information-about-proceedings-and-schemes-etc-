package com.example.server_connect;


import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity  implements GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        OnMapReadyCallback {

    private GoogleMap mMap;
    private int Request_code = 10;
    private Marker marker;
    private FusedLocationProviderClient fusedLocationProviderClient;
    LatLng mycordinates;
    LocationManager lm;
    LocationCallback locationCallback;
     Location location;

//LocationManager lm  = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        lm=(LocationManager)getSystemService(LOCATION_SERVICE);

     /*  locationCallback =(LocationCallback)new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Location currentlocation=locationResult.getLastLocation();
               mycordinates =new LatLng(currentlocation.getAltitude(),currentlocation.getLongitude());


                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mycordinates,13.0f));
                if(marker==null){
                    marker=mMap.addMarker(new MarkerOptions().position(mycordinates));
                }else {
                    marker.setPosition(mycordinates);
                }
            }
        };*/



    }


private String getcityname(LatLng mycordinates){
        String mycity="";
    Geocoder geocoder=new Geocoder(MapsActivity.this, Locale.getDefault());
    try{
        List<Address> addresses=geocoder.getFromLocation(mycordinates.latitude,mycordinates.longitude,2);
        String adress=addresses.get(0).getLocality();
        Log.d("mylog","Compleet adress"+addresses.toString());
        Log.d("mylog"," adress"+adress);
        Toast.makeText(MapsActivity.this,"adress= "+adress,Toast.LENGTH_LONG).show();
         mycity=addresses.get(0).toString();
    }catch (IOException e){
        e.printStackTrace();
    }
        return mycity;
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
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        // TODO: Before enabling the My Location layer, you must request
        // location permission from the user. This sample does not include
        // a request for location permission.
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Request_code);


        } else {
            mMap.setMyLocationEnabled(true);
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(this, new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if(task.isSuccessful()&& task.getResult()!=null){
                        location=task.getResult();
                    }
                }
            });

        }

        mMap.setOnMyLocationButtonClickListener((GoogleMap.OnMyLocationButtonClickListener) this);
        mMap.setOnMyLocationClickListener((GoogleMap.OnMyLocationClickListener) this);
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        LatLng mycordinates= new LatLng(location.getAltitude(),location.getLongitude());
        String cityname=getcityname(mycordinates);
        Toast.makeText(this, "Current location:\n" + cityname, Toast.LENGTH_LONG).show();
    }

    @SuppressLint("MissingPermission")
    @Override
    public boolean onMyLocationButtonClick() {

        /*  if(lm.isProviderEnabled(LocationManager.GPS_PROVIDER)==false){
                AlertDialog .Builder builder=new AlertDialog.Builder(this);
                builder.setMessage("Turn on Location First!");
                builder.setTitle("Location is Off");
                builder.setIcon(R.drawable.ic_location_off_black_24dp);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),"This feature is not accessable",Toast.LENGTH_LONG).show();
                    }
                });
            }*/

      //


        LatLng mycordinates= new LatLng(location.getLatitude(),location.getLongitude());
        String cityname=getcityname(mycordinates);
        Toast.makeText(MapsActivity.this,cityname,Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == Request_code) {
            if (permissions.length == 1 &&
                    permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //  mMap.setMyLocationEnabled(true);
                Toast.makeText(this, "location enabled", Toast.LENGTH_LONG).show();
            } else {
                // Permission was denied. Display an error message.
                Toast.makeText(this, "location desabled", Toast.LENGTH_LONG).show();

            }
        }
    }


  /*  @SuppressLint("MissingPermission")
    private void requestlocation(){
        Criteria criteria=new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_MEDIUM);
        criteria.setPowerRequirement(Criteria.POWER_MEDIUM);
        String provider=lm.getBestProvider(criteria,true);
        Location location=lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        Log.d("mylog","inrequestlocation");
        if(location!=null&&(System.currentTimeMillis() -location.getTime())<=1000 *2){
            LatLng mycoo=new LatLng(location.getAltitude(),location.getLongitude());
            String cityname=getcityname(mycoo);
            Toast.makeText(this,cityname,Toast.LENGTH_SHORT).show();
        }else{
            LocationRequest locationRequest=new LocationRequest();
            locationRequest.setNumUpdates(5);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            Log.d("mylog","last location too old getting new location!");

            fusedLocationProviderClient=LocationServices.getFusedLocationProviderClient(this);
            fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback, Looper.myLooper());


        }
    }*/

}
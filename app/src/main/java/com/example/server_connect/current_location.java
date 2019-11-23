package com.example.server_connect;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class current_location {
    private FusedLocationProviderClient fusedLocationProviderClient;
    Context context;
    Location location;

    LatLng latLng;
    @TargetApi(Build.VERSION_CODES.M)
    current_location(Context c){

        context=c;



    }



    public List<Address> getcityname(LatLng mycordinates){
        List<Address> addresses=null;
        Geocoder geocoder=new Geocoder(context, Locale.getDefault());
        try{
            addresses=geocoder.getFromLocation(mycordinates.latitude,mycordinates.longitude,1);
            String adress=addresses.get(0).getLocality();
            Log.d("mylog","Compleet adress"+addresses.toString());
            Log.d("mylog"," adress"+adress);

        }catch (IOException e){
            e.printStackTrace();
        }
        return addresses;
    }
}

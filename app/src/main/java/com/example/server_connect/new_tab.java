package com.example.server_connect;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class new_tab extends Fragment {
    private static final String URL_DATA = "https://amarsarkar.000webhostapp.com/Api/fetch_scheme_cl.php";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
 private String area;
    private Location mlocation;
    int Request_code = 1;
    LatLng latLng;
    LocationManager locationManager;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Object LocationListener;
    static int flag2 = 0;
    private List<ListItem> listItems;
    View view;
    Context newcontext;


    public new_tab(Context context) {
        // Required empty public constructor
        newcontext = context;
    }


    @SuppressLint({"NewApi", "MissingPermission"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_new_tab, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.new_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(newcontext));

        //  requestQueue = Volley.newRequestQueue(schemecontext, new HurlStack(null, pinnedSSLSocketFactory()));
        listItems = new ArrayList<>();


        LocationListener=new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {


            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(newcontext);
        locationManager = (LocationManager) newcontext.getSystemService(Context.LOCATION_SERVICE);

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000l, 500.0f, (android.location.LocationListener) LocationListener);
        mlocation=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        loadRecyclerViewData load = new loadRecyclerViewData();
        load.execute();
       return  view;
    }

    private class loadRecyclerViewData extends AsyncTask<String,String,String> {
        final ProgressDialog progressDialog = new ProgressDialog(newcontext);

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Loading.......Please wait");
            progressDialog.show();
            getlocation();
        }



        @Override
        protected String doInBackground(String... strings) {
            StringRequest sRequest=new StringRequest(Request.Method.POST,URL_DATA,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("test", String.valueOf(response));
                            progressDialog.dismiss();
                            try {
                                JSONArray j=new JSONArray(response);

                            try {
                                for(int i=0;i<j.length();i++) {

                                    JSONObject jsonObject = j.getJSONObject(i);
                                    ListItem item = new ListItem(
                                            jsonObject.getString("name"),
                                            jsonObject.getString("application time"),
                                            jsonObject.getString("picsource"),
                                            jsonObject.getString("dept"),
                                             jsonObject.getString("schemeid"),
                                            jsonObject.getString("desc")


                                    );
                                    listItems.add(item);

                                }
                                adapter=new MyAdapter(listItems,newcontext);
                                recyclerView.setAdapter(adapter);


                            } catch (JSONException e) {
                                progressDialog.dismiss();
                                Log.e("test",e.toString());
                                e.printStackTrace();
                            }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }

                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("test",error.toString());
                            progressDialog.dismiss();
                            Toast.makeText(newcontext,error.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }){
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();

                   params.put("location",area.trim());
                    return params;
                }};


            RequestQueue requestQueue= Volley.newRequestQueue(newcontext);//,new HurlStack(null,getSocketFactory(schemecontext)));
            requestQueue.add(sRequest);
            sRequest.setRetryPolicy(new RetryPolicy() {
                @Override
                public int getCurrentTimeout() {
                    return 500000;
                }

                @Override
                public int getCurrentRetryCount() {
                    return 500000;
                }

                @Override
                public void retry(VolleyError error) throws VolleyError {
                    Log.e("mylog",error.toString());
                }
            });
            return null;
        }


    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getlocation(){





            fusedLocationProviderClient.getLastLocation().addOnCompleteListener((Activity) newcontext, new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if(task.isSuccessful()&& task.getResult()!=null){
                        mlocation=task.getResult();

                    }
                }
            });
            latLng=new LatLng(mlocation.getLatitude(),mlocation.getLongitude());

        current_location c=new current_location(newcontext);
        List<Address>addresses=c.getcityname(latLng);
        if(!addresses.isEmpty()){
            area=addresses.get(0).getLocality().toString();
            Toast.makeText(newcontext,"Your current location:\t"+area,Toast.LENGTH_LONG).show();
            Log.e("location",area);

        }
    }

}

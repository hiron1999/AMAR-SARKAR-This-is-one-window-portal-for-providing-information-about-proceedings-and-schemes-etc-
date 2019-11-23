package com.example.server_connect;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

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
public class applications extends Fragment {
    Context activitycontext;
    View view;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
private String URL_DATA="https://amarsarkar.000webhostapp.com/Api/user_applications.php";
    private List<Application_listitem> listItems;

    public applications(Context context) {
        activitycontext=context;
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       view= inflater.inflate(R.layout.fragment_applications, container, false);
        recyclerView=(RecyclerView)view.findViewById(R.id.applicationlist);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(activitycontext));


        listItems=new ArrayList<>();
loadRecyclerViewData load=new loadRecyclerViewData();
load.execute();
       return  view;
    }

    private class loadRecyclerViewData extends AsyncTask<String,String,String> {
        final ProgressDialog progressDialog = new ProgressDialog(activitycontext);

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Loading.......Please wait");
            progressDialog.show();
        }



        @Override
        protected String doInBackground(String... strings) {
            StringRequest stringRequest=new StringRequest(Request.Method.POST,URL_DATA,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("app", String.valueOf(response));
                            progressDialog.dismiss();


                            try {
                              JSONArray  j = new JSONArray(response);

                            for (int i = 0; i < j.length(); i++) {

                                try {
                                    JSONObject jsonObject = j.getJSONObject(i);
                                    Application_listitem application_listitem = new Application_listitem(
                                            jsonObject.getString("name"),
                                            jsonObject.getString("date"),
                                            jsonObject.getString("status")


                                    );
                                    listItems.add(application_listitem);


                                    adapter = new Application_list_adapter(listItems, activitycontext);
                                    recyclerView.setAdapter(adapter);

                                } catch (JSONException e) {
                                    progressDialog.dismiss();
                                    Log.e("test", e.toString());
                                    e.printStackTrace();
                                }

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
                            Toast.makeText(activitycontext,error.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }){


                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        SharedPreferences s = activitycontext.getSharedPreferences("myprefs", activitycontext.MODE_PRIVATE);
                        params.put("id", s.getString("id", ""));


                        return params;
                    }

                      };

            RequestQueue requestQueue= Volley.newRequestQueue(activitycontext);//,new HurlStack(null,getSocketFactory(schemecontext)));
            requestQueue.add(stringRequest);
            stringRequest.setRetryPolicy(new RetryPolicy() {
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


}

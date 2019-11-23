package com.example.server_connect;


import android.app.ProgressDialog;
import android.content.Context;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLSocketFactory;




/**
 * A simple {@link Fragment} subclass.
 */
public class Scheme extends Fragment {

    private static final String URL_DATA="https://amarsarkar.000webhostapp.com/Api/fetch_scheme.php";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
static  int flag1=0;
    private List<ListItem> listItems;
View view;
Context schemecontext;


    public Scheme(Context context) {
        // Required empty public constructor
        schemecontext=context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_scheme, container, false);

        recyclerView=(RecyclerView)view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(schemecontext));

      //  requestQueue = Volley.newRequestQueue(schemecontext, new HurlStack(null, pinnedSSLSocketFactory()));
        listItems=new ArrayList<>();

            loadRecyclerViewData load = new loadRecyclerViewData();
            load.execute();

        return view;
    }
    private class loadRecyclerViewData extends AsyncTask<String,String,String> {
        final ProgressDialog progressDialog = new ProgressDialog(schemecontext);

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Loading.......Please wait");
            progressDialog.show();
        }



        @Override
        protected String doInBackground(String... strings) {
            JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(URL_DATA,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            Log.e("test", String.valueOf(response));
                            progressDialog.dismiss();
                            try {
                                for(int i=0;i<response.length();i++) {

                                    JSONObject jsonObject = response.getJSONObject(i);
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
                                adapter=new MyAdapter(listItems,schemecontext);
                                recyclerView.setAdapter(adapter);


                            } catch (JSONException e) {
                                progressDialog.dismiss();
                                Log.e("test",e.toString());
                                e.printStackTrace();
                            }


                        }

                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("test",error.toString());
                            progressDialog.dismiss();
                            Toast.makeText(schemecontext,error.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    });

            RequestQueue requestQueue= Volley.newRequestQueue(schemecontext);//,new HurlStack(null,getSocketFactory(schemecontext)));
            requestQueue.add(jsonArrayRequest);
            jsonArrayRequest.setRetryPolicy(new RetryPolicy() {
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

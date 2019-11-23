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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class job_tab extends Fragment {

    private static final String URL_DATA="https://amarsarkar.000webhostapp.com/Api/fetch_job.php";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
static int flag=0;
    private List<ListItem> listItems;
    View view;
    Context jobcontext;

    public job_tab(Context context) {
        // Required empty public constructor
       jobcontext=context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_job_tab, container, false);
        recyclerView=(RecyclerView)view.findViewById(R.id.job_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(jobcontext));

        //  requestQueue = Volley.newRequestQueue(schemecontext, new HurlStack(null, pinnedSSLSocketFactory()));
        listItems=new ArrayList<>();

    loadRecyclerViewData load = new loadRecyclerViewData();
    load.execute();

        return  view;
    }

    private class loadRecyclerViewData extends AsyncTask<String,String,String> {
        final ProgressDialog progressDialog = new ProgressDialog(jobcontext);

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
                                            jsonObject.getString("title"),
                                            jsonObject.getString("date_time"),
                                            jsonObject.getString("image"),
                                            jsonObject.getString("status"),
                                             jsonObject.getString("id"),
                                            jsonObject.getString("description")


                                    );
                                    listItems.add(item);

                                }
                                adapter=new MyAdapter(listItems,jobcontext);
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
                            Toast.makeText(jobcontext,error.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    });

            RequestQueue requestQueue= Volley.newRequestQueue(jobcontext);//,new HurlStack(null,getSocketFactory(schemecontext)));
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

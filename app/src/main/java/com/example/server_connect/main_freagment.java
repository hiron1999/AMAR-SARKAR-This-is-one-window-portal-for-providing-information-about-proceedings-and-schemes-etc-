package com.example.server_connect;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class main_freagment extends Fragment  {
Button button;
FragmentManager fragmentManager;
Context activitycontext;
Freagmentpage_Adapter freagmentpageAdapter;
TabLayout tabLayout;
TabItem T1,T2,T3;
ViewPager viewPager;
TextView data;
    View view;
    public main_freagment(Context context) {
       activitycontext=context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         view= inflater.inflate(R.layout.fragment_main_freagment, container, false);

        tabLayout=view.findViewById(R.id.tab_layout);
        T1=view.findViewById(R.id.home_tab);
        T2=view.findViewById(R.id.job_tab);
        T3=view.findViewById(R.id.new_tab);
        viewPager=view.findViewById(R.id.viewpager);
/*fragmentManager=getChildFragmentManager();

        freagmentpageAdapter=new Freagmentpage_Adapter(getFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(freagmentpageAdapter);
        tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if(tab.getPosition()==0){
                    freagmentpageAdapter.notifyDataSetChanged();
                }
                else if(tab.getPosition()==1){
                    freagmentpageAdapter.notifyDataSetChanged();
                }
               else if(tab.getPosition()==2){
                    freagmentpageAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.addOnPageChangeListener( new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


       /*button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activitycontext,"main freagment",Toast.LENGTH_SHORT).show();
            }
        });*/


        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUpViewPager(viewPager);
         tabLayout.setupWithViewPager(viewPager);
         tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
             @Override
             public void onTabSelected(TabLayout.Tab tab) {

             }

             @Override
             public void onTabUnselected(TabLayout.Tab tab) {

             }

             @Override
             public void onTabReselected(TabLayout.Tab tab) {

             }
         });
    }

    private void setUpViewPager(ViewPager viewPager) {
         freagmentpageAdapter=new Freagmentpage_Adapter(getChildFragmentManager(),tabLayout.getTabCount());
         freagmentpageAdapter.addfreagment(new Scheme(activitycontext),"All Scheme");
        freagmentpageAdapter.addfreagment(new new_tab(activitycontext),"Location Based");
        freagmentpageAdapter.addfreagment(new job_tab(activitycontext),"Job");
        viewPager.setAdapter(freagmentpageAdapter);
    }

    void fetchdata() {
        String myurl = "https://cichlid-marbles.hostingerapp.com/Api/connect.php";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(myurl, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = (JSONObject) response.get(i);
                        data.setText(data.getText() + "\n........................\n" + jsonObject.getString("id") + "\n" + jsonObject.getString("name") + "\n" + jsonObject.getString("email") + "\n" + jsonObject.getString("phone"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("volleylog", error);
            }
        });
        com.example.server_connect.AppController.getInstance().addToRequestQueue(jsonArrayRequest);
        Toast.makeText(activitycontext, "server connection sucess", Toast.LENGTH_LONG).show();
    }


}

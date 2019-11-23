package com.example.server_connect;


import android.content.ClipData;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class setings_freagment extends Fragment {
    Context activitycontext;
    ListView listView;
    View view;
    String [] setting_list;


    public setings_freagment(Context context) {
        activitycontext=context;
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      view = inflater.inflate(R.layout.fragment_setings_freagment, container, false);
      listView=view.findViewById(R.id.setting_list);

           setting_list= new String[]{"Account Settings", "Language Settings", "Helper", "About App"};

        ArrayAdapter adapter=new ArrayAdapter(activitycontext,android.R.layout.simple_list_item_1,setting_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(activitycontext,listView.getItemAtPosition(position).toString(),Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

}

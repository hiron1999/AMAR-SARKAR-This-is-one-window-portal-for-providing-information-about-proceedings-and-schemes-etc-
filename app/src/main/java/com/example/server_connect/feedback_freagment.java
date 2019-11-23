package com.example.server_connect;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.okhttp.internal.Platform;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;




/**
 * A simple {@link Fragment} subclass.
 */
public class feedback_freagment extends Fragment {

    Context activitycontext;
    TextView header;
TextInputLayout text;
Button submit;
    View view;
    public feedback_freagment(Context context) {

        activitycontext=context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       view= inflater.inflate(R.layout.fragment_feedback_freagment, container, false);
text=view.findViewById(R.id.feedback_text);
header=view.findViewById(R.id.feedback_header);
submit=view.findViewById(R.id.feedback_submit);

check();
submit.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        submit_feedback();
    }
});

       return view;
    }

    private  void submit_feedback(){
        submit.setVisibility(View.GONE);
        final ProgressDialog progressDialog=new ProgressDialog(activitycontext);
        progressDialog.setMessage("Submitting...");
        progressDialog.show();
        String Url="https://amarsarkar.000webhostapp.com/Api/feedback.php";
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                Log.e("feed",response);
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    if(jsonObject.getString("sucess").equals("1")){
                        progressDialog.dismiss();
                        submit.setVisibility(View.GONE);
                        header.setText("You have alredy submited");

                        text.setVisibility(View.GONE);
                        SharedPreferences s=activitycontext.getSharedPreferences("myprefs",activitycontext.MODE_PRIVATE);
                        SharedPreferences.Editor editor=s.edit();
                        editor.putString("feedback","submited");
                        editor.commit();
                        editor.apply();
                        AlertDialog.Builder a=new AlertDialog.Builder(activitycontext);

                        a.setTitle("Thank you ");
                        a.setMessage("Your feedback is sucessfully submited");
                        a.setIcon(R.drawable.ic_done_black_24dp);
                        a.setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        a.create();
                        a.show();

                    }
                    else {
                        progressDialog.dismiss();
                        submit.setVisibility(View.VISIBLE);
                        AlertDialog.Builder a=new AlertDialog.Builder(activitycontext);
                        a.setTitle("Error");
                        a.setMessage("Submition failed \n Try again");
                        a.setIcon(R.drawable.ic_close_black_24dp);
                        a.setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        a.create();
                        a.show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    submit.setVisibility(View.VISIBLE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressDialog.dismiss();
                submit.setVisibility(View.VISIBLE);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences s=activitycontext.getSharedPreferences("myprefs",activitycontext.MODE_PRIVATE);
                params.put("text",text.getEditText().getText().toString());
                params.put("id",s.getString("id",""));


                return  params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(activitycontext);//, new HurlStack(null,getSocketFactory(activitycontext)));
        requestQueue.add(stringRequest);


    }

    public void initilize_feedback(){

    }


private void check(){
    SharedPreferences s=activitycontext.getSharedPreferences("myprefs",activitycontext.MODE_PRIVATE);
    if(s.getString("feedback","").equals("submited")){
        submit.setVisibility(View.GONE);
        header.setText("You have alredy submited");

        text.setVisibility(View.GONE);


    }
    else{

    }

    }
}

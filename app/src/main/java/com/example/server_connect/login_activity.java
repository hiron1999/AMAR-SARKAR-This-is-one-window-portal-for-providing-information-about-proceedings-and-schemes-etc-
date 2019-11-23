package com.example.server_connect;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.MainThread;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;



public class login_activity extends AppCompatActivity {

    private static final String a="ooooooo";
    private static final String b="ppppppp";
    private static final String c="fffffff";
TextInputLayout t[];
Button login;
    SharedPreferences.Editor editor;
    SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            NotificationChannel channel=new NotificationChannel(a,b, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(c);
            NotificationManager manager=getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);



        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activity);
        t=new TextInputLayout[2];
        t[0]=(TextInputLayout)findViewById(R.id.ph_e);
        t[1]=(TextInputLayout)findViewById(R.id.pass_login);
        login=(Button) findViewById(R.id.login);
        prefs=getSharedPreferences("myprefs",login_activity.MODE_PRIVATE);
       editor=prefs.edit();

         editor.putBoolean("login status",true);

initilixetext();
        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                InputMethodManager inputManager=(InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(v.getWindowToken(),0);
                if(checkfilds()){

                    loginuser();

                }

            }
        });

    }

    private void loginuser(){
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setTitle(getString(R.string.login_process_title));
        progressDialog.setMessage(getString(R.string.wait_dilog));
        progressDialog.show();
        login.setVisibility(View.GONE);
        String loginurl="https://amarsarkar.000webhostapp.com/Api/login.php";
        StringRequest stringRequest=new StringRequest(Request.Method.POST, loginurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("mylog",response);
                try {
                     JSONObject jsonObject=new JSONObject(response);

                    String sucess=jsonObject.getString("sucess");
                    if(sucess.equals("1")){
                       final String name=jsonObject.getString("name");
                        final String email=jsonObject.getString("email");
                        final String id=jsonObject.getString("id");
                        editor.putBoolean("login_status",true);
                        editor.putString("name",name);
                        editor.putString("id",id);
                        editor.putString("email",email);
                        editor.commit();
                        editor.apply();
                       final String msg=jsonObject.getString("msg");


                        progressDialog.dismiss();
                        fetchdata f=new fetchdata();
                        f.execute();




                    }else {
                        progressDialog.dismiss();
                        login.setVisibility(View.VISIBLE);
                        final AlertDialog.Builder builder=new AlertDialog.Builder(login_activity.this);
                        builder.setCancelable(false);
                        builder.setTitle(R.string.login_fail);
                        builder.setMessage(R.string.login_failmsg);
                        builder.setPositiveButton(R.string.button_signin, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                formattxt();
                                Intent i=new Intent(login_activity.this,signup_activity.class);
                                startActivity(i);
                            }
                        });
                        builder.setNegativeButton(getString(R.string.close), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                formattxt();
                            }
                        });
                        builder.create();
                        builder.show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Error" + e.toString(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    Log.e("mylog",e.toString());
                    login.setVisibility(View.VISIBLE);
                }


            }
        },new Response.ErrorListener()

        {
            @Override
            public void onErrorResponse (VolleyError error){
                Toast.makeText(getApplicationContext(), "Register error" + error.toString(), Toast.LENGTH_SHORT).show();
                Log.e("mylog",error.toString());
                progressDialog.dismiss();
                login.setVisibility(View.VISIBLE);
            }}){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("phone_or_email",t[0].getEditText().getText().toString());
                params.put("password",t[1].getEditText().getText().toString());
             return  params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(login_activity.this);//,new HurlStack(null,getSocketFactory(login_activity.this)));
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
    }
////////////////////////profile pic fetch.........................................................


private class fetchdata extends AsyncTask<String,String,String> {

    final ProgressDialog progressbar=new ProgressDialog(login_activity.this);


    @Override
    protected String doInBackground(String... objects) {
        String myurl = "https://amarsarkar.000webhostapp.com/Api/connect.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, myurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("mylog",response);
                try    {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.get("sucess").equals("1")){

                        String imgurl = "https://amarsarkar.000webhostapp.com" + jsonObject.get("imgurl");
                        SharedPreferences s=login_activity.this.getSharedPreferences("myprefs",login_activity.this.MODE_PRIVATE);
                        SharedPreferences.Editor edit=s.edit();
                        edit.putString("propic",imgurl);
                        edit.commit();
                        edit.apply();




                    }

                }catch (JSONException e){
                    e.printStackTrace();


                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

            }
        }){
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                SharedPreferences s=login_activity.this.getSharedPreferences("myprefs",login_activity.this.MODE_PRIVATE);
                params.put("id", s.getString("id",""));


                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(login_activity.this);//, new HurlStack(null,getSocketFactory(activitycontext)));
        requestQueue.add(stringRequest);
        try {
            Thread.sleep(2*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "sucess";
    }

    @Override
    protected void onPreExecute() {

        progressbar.setMessage(getString(R.string.welcome));
        progressbar.setTitle(R.string.signup_sucess);

        progressbar.show();
    }

    @Override
    protected void onPostExecute(String s) {
        formattxt();

        Intent i=new Intent(login_activity.this,MainActivity.class);
        progressbar.dismiss();
        startActivity(i);
        login.setVisibility(View.VISIBLE);
        NotificationCompat.Builder mbuilder=
                new NotificationCompat.Builder(login_activity.this,a)
                        .setSmallIcon(R.drawable.ic_notifications_active_black_24dp)
                        .setContentTitle("Notification")
                        .setContentText("Welcome User")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);



        NotificationManagerCompat mnotificationmgr=NotificationManagerCompat.from(login_activity.this);
        mnotificationmgr.notify(1,mbuilder.build());

    }
}

    private  void initilixetext(){
        t[0].setHint(getString(R.string.phone_or_email));
        t[1].setHint(getString(R.string.password));
        login.setText(R.string.login);
    }
    private boolean checkfilds() {


        boolean result = true;
        for (int i = 0; i <t.length;i++) {
            if(t[i].getEditText().getText().toString().equals("")){
                t[i].getEditText().setError(getString(R.string.helper));
                result=false;
            }

        }
        return  result;
    }
    private void formattxt(){
        t[0].getEditText().setText("");
        t[1].getEditText().setText("");
    }
}

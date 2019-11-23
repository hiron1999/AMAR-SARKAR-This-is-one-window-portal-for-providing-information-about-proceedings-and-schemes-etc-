package com.example.server_connect;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class description extends Activity{
ImageView descp_img;
TextView title,description;
String imgurl,scheme;
Button apply;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.description);
        descp_img=(ImageView)findViewById(R.id.descrip_img);
        description=(TextView)findViewById(R.id.Total_descrip);
        title=(TextView)findViewById(R.id.descrip_title);
          apply=(Button)findViewById(R.id.apply);
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        if(bundle!=null){
            description.setText((String) bundle.get("description"));
            title.setText((String) bundle.get("title"));
            imgurl=(String) bundle.get("imgurl");
            scheme=(String) bundle.get("schemeid");
           PicassoTrustAll.getInstance(getApplicationContext())
                    .load(imgurl)
                    .into(descp_img);

        }

apply.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        apply_scheme apply_scheme=new apply_scheme();
        apply_scheme.execute();
    }
});

    }
private  class apply_scheme extends AsyncTask<String,String,String>{
final ProgressDialog progressDialog=new ProgressDialog(com.example.server_connect.description.this);

    @Override
    protected void onPreExecute() {
progressDialog.setMessage("please wait...");
progressDialog.setTitle("Appling..");
progressDialog.show();
    }


    @Override
    protected String doInBackground(String... strings) {


           //apply.setVisibility(View.GONE);
            //final ProgressDialog progressDialog=new ProgressDialog(description.this);
            progressDialog.setMessage("Submitting...");
            progressDialog.show();
            String Url="https://amarsarkar.000webhostapp.com/Api/applyscheme.php";
            StringRequest stringRequest=new StringRequest(Request.Method.POST, Url, new Response.Listener<String>() {


                @Override
                public void onResponse(String response) {
                    Log.e("apply",response);
                    try {
                        JSONObject jsonObject=new JSONObject(response);
                        if(jsonObject.getString("check").equals("1")) {
                            progressDialog.dismiss();
                            // submit.setVisibility(View.GONE);
                            if (jsonObject.getString("sucess").equals("1")) {

                                AlertDialog.Builder a = new AlertDialog.Builder(description.this);

                                a.setTitle("Thank you ");
                                a.setMessage("Your Applycation is sucessfully submited");
                                a.setIcon(R.drawable.ic_done_black_24dp);
                                a.setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                a.create();
                                a.show();

                            } else {
                                progressDialog.dismiss();

                                AlertDialog.Builder a = new AlertDialog.Builder(description.this);
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
                        }
                        else {
                            progressDialog.dismiss();
                            AlertDialog.Builder a = new AlertDialog.Builder(description.this);
                           a.setTitle("Warning");
                            a.setMessage("You have alredy applied for this\n go to Applications to see the details");
                            a.setIcon(R.drawable.ic_warning_black_24dp);
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

                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    progressDialog.dismiss();
                    apply.setVisibility(View.VISIBLE);
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    SharedPreferences s=description.this.getSharedPreferences("myprefs",description.this.MODE_PRIVATE);

                    params.put("id",s.getString("id",""));
                    params.put("scheme_id",scheme);


                    return  params;
                }
            };
            RequestQueue requestQueue= Volley.newRequestQueue(description.this);//, new HurlStack(null,getSocketFactory(activitycontext)));
            requestQueue.add(stringRequest);




        return null;
    }
}

}

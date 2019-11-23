package com.example.server_connect;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.example.server_connect.job_tab.flag;


/**
 * A simple {@link Fragment} subclass.
 */
public class profile_freagment extends Fragment {

TextView about,editpropic,name,phone,adress,email,reg_time;
ImageView propic;
Button edit_profile,update;
View view;
private Bitmap bitmap;
private  String URL_UPLORD="https://amarsarkar.000webhostapp.com/Api/uplord_photo.php";
    Context activitycontext;

    public profile_freagment(Context context) {
        activitycontext=context;
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       view= inflater.inflate(R.layout.fragment_profile_freagment, container, false);
      about=view.findViewById(R.id.about);
        editpropic=view.findViewById(R.id.edit_propic);
        edit_profile=view.findViewById(R.id.edit_pro);
        propic=view.findViewById(R.id.pro_pic);
        update=view.findViewById(R.id.update_pro);
        name=view.findViewById(R.id.profile_name);
        email=view.findViewById(R.id.profile_email);
        phone=view.findViewById(R.id.profile_phone);
        adress=view.findViewById(R.id.profile_address);
        reg_time=view.findViewById(R.id.reg_time);

        SharedPreferences sp=activitycontext.getSharedPreferences("myprefs",activitycontext.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("flag","0");
        editor.commit();
        editor.apply();
        //propic fetch..............................
        SharedPreferences s=activitycontext.getSharedPreferences("myprefs",activitycontext.MODE_PRIVATE);

        email.setText(s.getString("email",""));
        if(!s.getString("propic","").equals("")){

           PicassoTrustAll.getInstance(activitycontext)
                    .load(s.getString("propic",""))
                    .into(propic);
        }

        editpropic.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
            @Override
            public void onClick(View v) {
             choosefile();
            }
        });

        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(activitycontext,Edit_profile.class);
                startActivity(i);
            }
        });
        if(s.getString("flag","").equals("0")) {
            loadDetails details = new loadDetails();
            details.execute();
            SharedPreferences sp1=activitycontext.getSharedPreferences("myprefs",activitycontext.MODE_PRIVATE);
            SharedPreferences.Editor editor1=sp1.edit();
            editor1.putString("flag","1");
            editor1.commit();
            editor1.apply();

        }
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(activitycontext,verification.class);
                startActivity(i);
            }
        });
        return view;

    }

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
private void choosefile(){
    Intent i=new Intent();
    i.setType("image/*");
    i.setAction(Intent.ACTION_GET_CONTENT);
    startActivityForResult(Intent.createChooser(i,"Select Picture"),1);
}

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1&& resultCode==(-1)&&data!=null&&data.getData()!=null){
            Uri filepath=data.getData();
            try {
                bitmap= MediaStore.Images.Media.getBitmap(activitycontext.getContentResolver(),filepath);
               // propic.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            SharedPreferences s=activitycontext.getSharedPreferences("myprefs",activitycontext.MODE_PRIVATE);

            uplordpic((String) s.getString("id",""),getstringimg(bitmap));
        }
    }
    private void uplordpic(final String id,final String photo){
        final ProgressDialog progressDialog=new ProgressDialog(activitycontext);
        progressDialog.setMessage("Uplording profile picture....");
        progressDialog.show();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL_UPLORD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("mylog",response);
                try {
                    JSONObject jsonObject=new JSONObject(response);

                    String result=jsonObject.getString("sucess");
                    if(result.equals("1")){
                        progressDialog.dismiss();
                        fetchdata();
                        Toast.makeText(activitycontext, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    Log.e("mylog",e.toString());
                    Toast.makeText(activitycontext, e.toString(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               progressDialog.dismiss();
                Log.e("mylog",error.toString());
                Toast.makeText(activitycontext, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>params=new HashMap<>();

                params.put("id",id.trim());
                params.put("photo",photo.trim());

                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(activitycontext);//,new HurlStack(null,getSocketFactory(activitycontext)));
        requestQueue.add(stringRequest);

    }
   public void fetchdata() {
        final ProgressDialog progressDialog=new ProgressDialog(activitycontext);
        progressDialog.setMessage("Image loading....");
        progressDialog.show();
        String myurl = "https://amarsarkar.000webhostapp.com/Api/connect.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, myurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("mylog",response);
            try    {
                    JSONObject jsonObject = new JSONObject(response);
              if(jsonObject.get("sucess").equals("1")){

                  String imgurl = "https://amarsarkar.000webhostapp.com" + jsonObject.get("imgurl");
                  SharedPreferences s=activitycontext.getSharedPreferences("myprefs",activitycontext.MODE_PRIVATE);
                 SharedPreferences.Editor edit=s.edit();
                 edit.putString("propic",imgurl);
                 edit.commit();
                 edit.apply();


                  PicassoTrustAll.getInstance(activitycontext)
                          .load(s.getString("propic",""))
                          .into(propic);
progressDialog.dismiss();
              }

                }catch (JSONException e){
                e.printStackTrace();
                progressDialog.dismiss();

            }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
error.printStackTrace();
progressDialog.dismiss();
            }
        }){
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                SharedPreferences s=activitycontext.getSharedPreferences("myprefs",activitycontext.MODE_PRIVATE);
                params.put("id", s.getString("id",""));


                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(activitycontext);//, new HurlStack(null,getSocketFactory(activitycontext)));
        requestQueue.add(stringRequest);
    }





        public String getstringimg(Bitmap b){
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.JPEG,40,byteArrayOutputStream);
        byte[] imgbytearray=byteArrayOutputStream.toByteArray();
        String encodedimg= Base64.encodeToString(imgbytearray,Base64.DEFAULT);

        return encodedimg;
    }



    //user details........................................................................

    private class loadDetails extends AsyncTask<String,String,String> {
        final ProgressDialog progressDialog = new ProgressDialog(activitycontext);

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Loading.......Please wait");
            progressDialog.show();
        }



        @Override
        protected String doInBackground(String... strings) {
            String URL_DATA="https://amarsarkar.000webhostapp.com/Api/user_details.php";
            StringRequest stringRequest=new StringRequest(Request.Method.POST,URL_DATA,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("app", String.valueOf(response));
                            progressDialog.dismiss();


                            try {
                                JSONArray  j = new JSONArray(response);

                                try {


                                    JSONObject jsonObject = j.getJSONObject(0);

                                    name.setText(jsonObject.getString("name"));
                                    phone.setText(jsonObject.getString("phone"));
                                    adress.setText(jsonObject.getString("adress")+","+jsonObject.getString("postal_code"));
                                    reg_time.setText(jsonObject.getString("registertime"));








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

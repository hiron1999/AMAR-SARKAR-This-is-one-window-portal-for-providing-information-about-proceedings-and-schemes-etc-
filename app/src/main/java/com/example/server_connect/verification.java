package com.example.server_connect;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class verification extends AppCompatActivity {
    TextInputLayout t[];
    TextView up;
    Button btn;
    private Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        t = new TextInputLayout[2];
        t[0] = (TextInputLayout) findViewById(R.id.doc);
        t[1] = (TextInputLayout) findViewById(R.id.idno);

        up = (TextView) findViewById(R.id.upimg);
        btn = (Button) findViewById(R.id.ver);
        up.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
            @Override
            public void onClick(View v) {
                choosefile();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
            @Override
            public void onClick(View v) {
                if(checkfilds()){
                    Update update=new Update();
                    update.execute();
                }
            }
        });

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
                bitmap= MediaStore.Images.Media.getBitmap(verification.this.getContentResolver(),filepath);
                // propic.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            SharedPreferences s=verification.this.getSharedPreferences("myprefs",verification.this.MODE_PRIVATE);


        }
    }

    public String getstringimg(Bitmap b){
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.JPEG,40,byteArrayOutputStream);
        byte[] imgbytearray=byteArrayOutputStream.toByteArray();
        String encodedimg= Base64.encodeToString(imgbytearray,Base64.DEFAULT);

        return encodedimg;
    }


    private class Update extends AsyncTask<String,String,String> {
            final ProgressDialog progressDialog = new ProgressDialog(verification.this);

            @Override
            protected void onPreExecute() {
                progressDialog.setMessage("Loading.......Please wait");
                progressDialog.show();
            }



            @Override
            protected String doInBackground(String... strings) {
                String URL_DATA="https://amarsarkar.000webhostapp.com/Api/verification.php";
                StringRequest stringRequest=new StringRequest(Request.Method.POST,URL_DATA,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.e("app", String.valueOf(response));

                                try {
                                    JSONObject jsonObject=new JSONObject(response);
                                    if(jsonObject.getString("sucess").equals("1")){
                                        progressDialog.dismiss();

                                        formattxt();
                                        AlertDialog.Builder builder=new AlertDialog.Builder(verification.this);

                                        builder.setMessage("Your details updated sucessfully");
                                        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        });
                                        builder.create();
                                        builder.show();
                                    }
                                    else{
                                        AlertDialog.Builder builder=new AlertDialog.Builder(verification.this);

                                        builder.setMessage("Error occured!!!\nTry Again");
                                        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        });
                                        builder.create();
                                        builder.show();

                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(verification.this,e.getMessage(),Toast.LENGTH_LONG).show();
                                }


                            }


                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("test",error.toString());
                                progressDialog.dismiss();
                                Toast.makeText(verification.this,error.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        }){


                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        SharedPreferences s = verification.this.getSharedPreferences("myprefs", verification.this.MODE_PRIVATE);
                        params.put("id", s.getString("id", ""));
                        params.put("doc",t[0].getEditText().getText().toString());
                        params.put("idno",t[1].getEditText().getText().toString());
                        params.put("photo",getstringimg(bitmap).trim());

                        return params;
                    }

                };

                RequestQueue requestQueue= Volley.newRequestQueue(verification.this);//,new HurlStack(null,getSocketFactory(schemecontext)));
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

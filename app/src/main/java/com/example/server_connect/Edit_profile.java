package com.example.server_connect;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Edit_profile extends AppCompatActivity {
    TextInputLayout t[];
    Button save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        t=new TextInputLayout[3];
        t[0]=(TextInputLayout)findViewById(R.id.edit_phone);
        t[1]=(TextInputLayout)findViewById(R.id.edit_adress);
        t[2]=(TextInputLayout)findViewById(R.id.edit_pin);
        save=(Button)findViewById(R.id.save);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkfilds()){
                    Update update=new Update();
                    update.execute();
                    SharedPreferences sp=Edit_profile.this.getSharedPreferences("myprefs",Edit_profile.this.MODE_PRIVATE);
                    SharedPreferences.Editor editor=sp.edit();
                    editor.putString("flag","0");
                    editor.commit();
                    editor.apply();
                }
            }
        });


    }

    private class Update extends AsyncTask<String,String,String> {
        final ProgressDialog progressDialog = new ProgressDialog(Edit_profile.this);

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Loading.......Please wait");
            progressDialog.show();
        }



        @Override
        protected String doInBackground(String... strings) {
            String URL_DATA="https://amarsarkar.000webhostapp.com/Api/updt.php";
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
                                    AlertDialog.Builder builder=new AlertDialog.Builder(Edit_profile.this);

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
                                    AlertDialog.Builder builder=new AlertDialog.Builder(Edit_profile.this);

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
                                Toast.makeText(Edit_profile.this,e.getMessage(),Toast.LENGTH_LONG).show();
                            }


                        }


                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("test",error.toString());
                            progressDialog.dismiss();
                            Toast.makeText(Edit_profile.this,error.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }){


                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    SharedPreferences s = Edit_profile.this.getSharedPreferences("myprefs", Edit_profile.this.MODE_PRIVATE);
                    params.put("id", s.getString("id", ""));
                    params.put("phone", t[0].getEditText().getText().toString());
                    params.put("adress", t[1].getEditText().getText().toString());
                    params.put("postal_code", t[2].getEditText().getText().toString());

                    return params;
                }

            };

            RequestQueue requestQueue= Volley.newRequestQueue(Edit_profile.this);//,new HurlStack(null,getSocketFactory(schemecontext)));
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
        t[2].getEditText().setText("");
    }
}

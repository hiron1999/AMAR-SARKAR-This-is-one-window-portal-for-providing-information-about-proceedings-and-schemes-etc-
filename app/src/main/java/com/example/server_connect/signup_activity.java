package com.example.server_connect;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


import static com.example.server_connect.R.string.confirm_password;
import static com.example.server_connect.R.string.country;
import static com.example.server_connect.R.string.dob;
import static com.example.server_connect.R.string.dobhelp;
import static com.example.server_connect.R.string.email;
import static com.example.server_connect.R.string.helper;
import static com.example.server_connect.R.string.location;
import static com.example.server_connect.R.string.location_helper;
import static com.example.server_connect.R.string.login;
import static com.example.server_connect.R.string.notmatch_pass;
import static com.example.server_connect.R.string.password;
import static com.example.server_connect.R.string.password_helper;
import static com.example.server_connect.R.string.phone;
import static com.example.server_connect.R.string.postal;
import static com.example.server_connect.R.string.state;

public class signup_activity extends AppCompatActivity {
    private static final String a="ooooooo";
    private static final String b="ppppppp";
    private static final String c="fffffff";
Button signup;
TextInputLayout textInputLayouts[];
RadioGroup radioGroup;
RadioButton radioButton;
private DatePickerDialog.OnDateSetListener onDateSetListener;
TextView form_header,gender;

private Location mlocation;
    int Request_code=1;
    LatLng latLng;
    LocationManager locationManager;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Object LocationListener;
    SharedPreferences.Editor editor;
    SharedPreferences prefs;

    @SuppressLint("ResourceType")

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
                showdilog();
//sheared prefferance..........................................................
        prefs=getSharedPreferences("myprefs",login_activity.MODE_PRIVATE);
        editor=prefs.edit();
//initilize resourses...........................................................
        setContentView(R.layout.activity_signup_activity);
        signup=(Button)findViewById(R.id.Signup) ;
        gender=(TextView)findViewById(R.id.gender);
        textInputLayouts=new TextInputLayout[10];

        textInputLayouts[0]=(TextInputLayout)findViewById(R.id.tname);
        textInputLayouts[1]=(TextInputLayout)findViewById(R.id.tphone);
        textInputLayouts[2]=(TextInputLayout)findViewById(R.id.temail);
        textInputLayouts[3]=(TextInputLayout)findViewById(R.id.tlocation);
        textInputLayouts[4]=(TextInputLayout)findViewById(R.id.tpostal);
        textInputLayouts[5]=(TextInputLayout)findViewById(R.id.tdob);
        textInputLayouts[6]=(TextInputLayout)findViewById(R.id.tpassword);
        textInputLayouts[7]=(TextInputLayout)findViewById(R.id.tconpassword);
        textInputLayouts[8]=(TextInputLayout)findViewById(R.id.country);
        textInputLayouts[9]=(TextInputLayout)findViewById(R.id.state);

        radioGroup=(RadioGroup)findViewById(R.id.rg);
        form_header=(TextView)findViewById(R.id.form_header);
LocationListener=new LocationListener() {
    @Override
    public void onLocationChanged(Location location) {


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
};

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
locationManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1000l,500.0f, (android.location.LocationListener) LocationListener);
 mlocation=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);


        signup.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

if(form_authentication()){
    insertuser();
                }
else {
    Toast.makeText(signup_activity.this,"Fill up all the filds",Toast.LENGTH_SHORT).show();
}

            }
        });
        //initilize datepicker dilog......
        onDateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                textInputLayouts[5].getEditText().setText(year +"/"+ month +"/"+dayOfMonth);
            }
        };


    }


    //data insert......................
    private void insertuser(){

       final ProgressDialog progressDialog=new ProgressDialog(this);
       progressDialog.setTitle(getString(R.string.creatAccount_dilog));
       progressDialog.setMessage(getString(R.string.wait_dilog));
       progressDialog.show();
       signup.setVisibility(View.GONE);
      String inserturl="https://amarsarkar.000webhostapp.com/Api/insert.php";
        StringRequest request=new StringRequest(Request.Method.POST, inserturl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("debug",response.toString());
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String check="";
                           check= jsonObject.getString("check");
                    if(check.equals("0")){
                        progressDialog.dismiss();
                        signup.setVisibility(View.VISIBLE);

                        progressDialog.dismiss();
                        final AlertDialog.Builder builder=new AlertDialog.Builder(signup_activity.this);
                        builder.setCancelable(false);
                        builder.setTitle(R.string.signup_failed);
                        builder.setMessage(R.string.acc_exist_msg);
                        builder.setPositiveButton(login, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i=new Intent(signup_activity.this,login_activity.class);
                                startActivity(i);
                            }
                        });
                        builder.setNegativeButton(getString(R.string.close), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                textInputLayouts[1].setError(getString(R.string.helper_exist_msg));
                                textInputLayouts[2].setError(getString(R.string.helper_exist_msg));
                            }
                        });
                        builder.create();
                        builder.show();

                    }
                    else {


                    String sucess=jsonObject.getString("sucess");
                    if(sucess.equals("1")){

                        NotificationCompat.Builder mbuilder=
                                new NotificationCompat.Builder(signup_activity.this,a)
                                        .setSmallIcon(R.drawable.ic_notifications_active_black_24dp)
                                        .setContentTitle("Notification")
                                        .setContentText("Varify Your Profile To Apply Scheme")
                                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);



                        NotificationManagerCompat mnotificationmgr=NotificationManagerCompat.from(signup_activity.this);
                        mnotificationmgr.notify(1,mbuilder.build());
                        Toast.makeText(getApplicationContext(),"signup sucess",Toast.LENGTH_SHORT).show();
                        progressDialog.setMessage("Account sucessfully created!");
                        Intent i=new Intent(signup_activity.this,MainActivity.class);
                        startActivity(i);

                        editor.putString("name",textInputLayouts[0].getEditText().getText().toString());

                        editor.putString("email",textInputLayouts[2].getEditText().getText().toString());
                        editor.commit();
                        editor.apply();
                        progressDialog.dismiss();
                        signup.setVisibility(View.VISIBLE);
                    }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"Register error"+e.toString(),Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    Log.e("mylog",e.toString());
                    signup.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Register error"+error.toString(),Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                signup.setVisibility(View.VISIBLE);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<String, String>();

               /* params.put("name","hiron");
                params.put("phone","7876789");
                params.put("email","dhgcjd");
                params.put("password","dvshvc");
                params.put("dob","2010/06/08");
                params.put("adress","hgdgyy");
                params.put("postal_code","65757");
                params.put("country","india");
                params.put("state","web");
                params.put("sex","male");*/

                params.put("name",textInputLayouts[0].getEditText().getText().toString().trim());
                params.put("phone",textInputLayouts[1].getEditText().getText().toString().trim());
                params.put("email",textInputLayouts[2].getEditText().getText().toString().trim());
                params.put("password",textInputLayouts[6].getEditText().getText().toString().trim());
                params.put("dob",textInputLayouts[5].getEditText().getText().toString().trim());
                params.put("adress",textInputLayouts[3].getEditText().getText().toString().trim());
                params.put("postal_code",textInputLayouts[4].getEditText().getText().toString().trim());
                params.put("country",textInputLayouts[8].getEditText().getText().toString().trim());
                params.put("state",textInputLayouts[9].getEditText().getText().toString().trim());
                params.put("sex",radioButton.getText().toString().trim());
               return params;
            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(signup_activity.this);//,new HurlStack(null,getSocketFactory(signup_activity.this)));
        requestQueue.add(request);
        request.setRetryPolicy(new RetryPolicy() {
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
    //.............................language dilog..
    void showdilog() {
        final String[] list = {"English", "বাংলা"};
        AlertDialog.Builder mbuilder = new AlertDialog.Builder(this);
        mbuilder.setTitle("Choose Your Language");
        mbuilder.setSingleChoiceItems(list, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    setlang("en");

                } else if (which == 1) {
                    setlang("bn");

                }
                dialog.dismiss();
                // Intent i=new Intent(getApplicationContext(),signup_activity.class);
                //startActivity(i);
            }
        });
        AlertDialog mdilog = mbuilder.create();
        mdilog.setCancelable(false);
        mdilog.show();
    }

    private void setlang(String s) {
        Locale locale = new Locale(s);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = (SharedPreferences.Editor) getPreferences(AppCompatActivity.MODE_PRIVATE).edit();
        editor.putString("my_lang", s);
        editor.apply();

       settext();

    }

    private void loadlocate() {
        SharedPreferences preferences = getPreferences(AppCompatActivity.MODE_PRIVATE);
        String languages = preferences.getString("my_lang", "");
        setlang(languages);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onlocation_click(View view) {
        InputMethodManager inputManager=(InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(view.getWindowToken(),0);
        progressbar p=new progressbar(2000,signup_activity.this,"Fetch location","plese wait...");
         p.run();

getlocation();

    }
    @RequiresApi(api = Build.VERSION_CODES.M)
private void getlocation(){



    if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Request_code);


    } else {

        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(this, new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if(task.isSuccessful()&& task.getResult()!=null){
                    mlocation=task.getResult();

                }
            }
        });
        latLng=new LatLng(mlocation.getLatitude(),mlocation.getLongitude());
    }
current_location c=new current_location(signup_activity.this);
        List<Address>addresses=c.getcityname(latLng);
        if(!addresses.isEmpty()){
            textInputLayouts[3].getEditText().setText(addresses.get(0).getSubLocality().toString()+","+addresses.get(0).getLocality().toString());
            textInputLayouts[4].getEditText().setText(addresses.get(0).getPostalCode().toString());
            textInputLayouts[8].getEditText().setText(addresses.get(0).getCountryName().toString());
           textInputLayouts[9].getEditText().setText(addresses.get(0).getAdminArea().toString());
        }
}
    @SuppressLint("ResourceAsColor")
    private void settext() {
        form_header.setText(R.string.form_header);
        gender.setText(R.string.sex);
        signup.setText(R.string.button_signin);
        textInputLayouts[0].setHint(getString(R.string.name));
        textInputLayouts[1].setHint(getString(phone));
        textInputLayouts[2].setHint(getString(email));
        textInputLayouts[3].setHint(getString(location));
        textInputLayouts[4].setHint(getString(postal));
        textInputLayouts[5].setHint(getString(dob));
        textInputLayouts[6].setHint(getString(password));
        textInputLayouts[7].setHint(getString(confirm_password));
        textInputLayouts[8].setHint(getString(country));
        textInputLayouts[9].setHint(getString(state));

        textInputLayouts[6].setHelperText(getString(password_helper));
        textInputLayouts[3].setHelperText(getString(location_helper));
        textInputLayouts[5].setHelperText(getString(dobhelp));
        textInputLayouts[6].setHelperTextColor(ColorStateList.valueOf(R.color.red));
        textInputLayouts[3].setHelperTextColor(ColorStateList.valueOf(R.color.red));
        textInputLayouts[5].setHelperTextColor(ColorStateList.valueOf(R.color.red));
    }

    private boolean form_authentication(){
        int Id=radioGroup.getCheckedRadioButtonId();
        radioButton=(RadioButton) findViewById(Id);
        boolean result=true;
for(int i=0;i<textInputLayouts.length;i++){
    if(textInputLayouts[i].getEditText().getText().toString().equals("")){

        textInputLayouts[i].getEditText().setError(getString(helper));
        textInputLayouts[i].getEditText().setFocusable(true);
        Toast.makeText(this,"empty",Toast.LENGTH_SHORT).show();
result=false;
    }
    }

    if (!textInputLayouts[7].getEditText().getText().toString().equals(textInputLayouts[6].getEditText().getText().toString())) {
        textInputLayouts[7].getEditText().setError(getString(notmatch_pass));
        textInputLayouts[7].getEditText().setFocusable(true);
result=false;
    }
    return result;
}
//datepickerdilog................................................................
    public void datepick(View view){
        Calendar calendar=Calendar.getInstance();
        int Year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH);
        int day=calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog=new DatePickerDialog(signup_activity.this,
                R.style.Theme_MaterialComponents_Light_Dialog_MinWidth,onDateSetListener,Year,month,day);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.show();
        InputMethodManager inputManager=(InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(view.getWindowToken(),0);
    }

}

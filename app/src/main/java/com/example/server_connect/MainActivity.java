package com.example.server_connect;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity<pivate> extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    TextView data, title,nav_name,nav_email;
    ImageView nav_img;
    Button button;
    private Handler handler=new Handler();
    private DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    Toolbar toolbar;
    Bundle check;
    NavigationView navigationView;
    SharedPreferences.Editor editor;
    SharedPreferences prefs;
    View view;
   FrameLayout fragment_container;
LayoutInflater inflater;
    public  FragmentManager fragmentManager;
   public FragmentTransaction fragmentTransaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs=getSharedPreferences("myprefs",MainActivity.MODE_PRIVATE);
        editor=prefs.edit();
        inflater = LayoutInflater.from(getApplicationContext());


        setContentView(R.layout.activity_main);
        data = (TextView) findViewById(R.id.showdata);
        title = (TextView) findViewById(R.id.title);

        button = (Button) findViewById(R.id.go_map);
        Context contex = getApplicationContext();
        //prefs.getString("name","")


        title.setText(getResources().getString(R.string.title));
        button.setText(getResources().getString(R.string.button_text));
data.setMovementMethod(new ScrollingMovementMethod());
//drawer...........................................
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout=(DrawerLayout) findViewById(R.id.drawer);
       toggle=new ActionBarDrawerToggle(MainActivity.this,drawerLayout,toolbar,R.string.nav_open,R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checklocation(getApplicationContext());

            }
        });

        //  loadlocate();

     //   fetchdata();

        //freagment.............................
        fragment_container=(FrameLayout)findViewById(R.id.childfregmrnt_container) ;
        fragmentManager=getSupportFragmentManager();
         fragmentTransaction=fragmentManager.beginTransaction();
 check=savedInstanceState;
        if(findViewById(R.id.freagment_content)!=null){
            if(check!=null){
                return;
            }
          fragmentTransaction=fragmentManager.beginTransaction();
            main_freagment main_freagment=new main_freagment(MainActivity.this);
            fragmentTransaction.add(R.id.freagment_content,main_freagment,null);

            fragmentTransaction.commit();
        }
        navigationView=(NavigationView)findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(MainActivity.this);
        view=navigationView.getHeaderView(0);
        nav_name = view. findViewById(R.id.nav_name);
        nav_email =  view. findViewById(R.id.nav_email);
        nav_img=view.findViewById(R.id.nav_img);
        nav_name.setText(prefs.getString("name",""));
        nav_email.setText(prefs.getString("email",""));

       if(!prefs.getString("propic","").equals("")) {
           Log.e("mylog",prefs.getString("propic",""));
         PicassoTrustAll.getInstance(MainActivity.this)
                   .load(prefs.getString("propic", ""))
                   .into(nav_img);
       }
    }




    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            Log.e("mylog", String.valueOf(fragmentManager.getBackStackEntryCount()));

            if(fragmentManager.getBackStackEntryCount()!=0){

fragmentManager.popBackStack();
            }
            else{
                AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                builder.setCancelable(false);
                builder.setMessage(getString(R.string.logout_alert_msg));
                builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        editor.putBoolean("login_status",false);
                        editor.clear();
                        editor.commit();
                        editor.apply();
                        Intent i=new Intent(MainActivity.this,startingactivity.class);
                        startActivity(i);
                    }
                });
                builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create();
                builder.show();
            }
           //super.onBackPressed();
        }
    }



    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        toggle.syncState();
    }

    //data fetch................................
    void fetchdata() {
        String myurl = "https://amarsarkar.000webhostapp.com/Api/connect.php";
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
        Toast.makeText(getApplicationContext(), "server connection sucess", Toast.LENGTH_LONG).show();
    }



 /*  private void showdilog() {
        final String[] list = {"English", "বাংলা"};
        AlertDialog.Builder mbuilder = new AlertDialog.Builder(MainActivity.this);
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
        mdilog.show();
    }

    private void setlang(String s) {
        Locale locale = new Locale(s);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = (SharedPreferences.Editor) getPreferences(Activity.MODE_PRIVATE).edit();
        editor.putString("my_lang", s);
        editor.apply();
        title.setText(getResources().getString(R.string.title));
        button.setText(getResources().getString(R.string.button_text));
    }

    private void loadlocate() {
        SharedPreferences preferences = getPreferences(Activity.MODE_PRIVATE);
        String languages = preferences.getString("my_lang", "");
        setlang(languages);
    } */

    @TargetApi(Build.VERSION_CODES.P)

    private void checklocation( Context context) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
              if (!islocation_enable(context)) {

                  builder.setMessage("Turn on Location First!");
                  builder.setTitle("Location is Off");
                  builder.setIcon(R.drawable.ic_location_off_black_24dp);

                  builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialog, int which) {

                          Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                          startActivity(intent);

                      }
                  });
                  builder.setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialog, int which) {

                          Toast.makeText(getApplicationContext(), "This feature is not accessable", Toast.LENGTH_LONG).show();

                      }

                  });

                  builder.create();
                  builder.setCancelable(false);
                  builder.show();

              }else {
                  Intent i=new Intent(MainActivity.this,MapsActivity.class);
                  startActivity(i);
              }

    }

    public static boolean islocation_enable(Context context){
        int locationmode=0;
        String locationproviders;

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT) {
            try {
                locationmode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e) {
                 e.printStackTrace();
                 return false;
            }
            return locationmode != Settings.Secure.LOCATION_MODE_OFF;
        }else {

            locationproviders =Settings.Secure.getString(context.getContentResolver(),Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        return !TextUtils.isEmpty(locationproviders);
        }
    }



    private void closedrawer() {
        drawerLayout.closeDrawer(GravityCompat.START);
    }
    private void  oprndrawer(){
        drawerLayout.openDrawer(GravityCompat.START);
    }

    @Override
    public boolean onNavigationItemSelected( MenuItem menuItem) {
        switch (menuItem.getItemId()){



            case R.id.nav_application:
                if(findViewById(R.id.freagment_content)!=null) {
                    if (check != null) {

                    } else {
                        fragmentTransaction = fragmentManager.beginTransaction();
                       applications applications= new applications(MainActivity.this);

                        fragmentTransaction.replace(R.id.freagment_content,applications, null);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                }

                 break;


            case R.id.nav_profile:
                if(findViewById(R.id.freagment_content)!=null) {
                    if (check != null) {

                    } else {
                       fragmentTransaction = fragmentManager.beginTransaction();
                       profile_freagment profile_freagment = new profile_freagment(MainActivity.this);
                        fragmentTransaction.replace(R.id.freagment_content,profile_freagment, null);
                       fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                }
                break;
            case R.id.nav_home:
                if(findViewById(R.id.freagment_content)!=null) {
                    if (check != null) {

                    } else {
                        fragmentTransaction = fragmentManager.beginTransaction();
                        main_freagment main_freagment = new main_freagment(MainActivity.this);
                        fragmentTransaction.replace(R.id.freagment_content, main_freagment, null);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                }
                break;
            case R.id.nav_setings:
                if(findViewById(R.id.freagment_content)!=null) {
                    if (check != null) {

                    } else {
                      fragmentTransaction = fragmentManager.beginTransaction();
                        setings_freagment setings_freagment = new setings_freagment(MainActivity.this);
                        fragmentTransaction.replace(R.id.freagment_content,setings_freagment, null);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                }
                break;
            case R.id.nav_logout:
                AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                builder.setCancelable(false);
                builder.setMessage(getString(R.string.logout_alert_msg));
                builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        editor.putBoolean("login_status",false);
                        editor.clear();
                        editor.commit();
                        editor.apply();
                        Intent i=new Intent(MainActivity.this,startingactivity.class);
                        startActivity(i);
                    }
                });
                builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create();
                builder.show();
                break;
            case R.id.nav_share:
                Toast.makeText(MainActivity.this,"sharing",Toast.LENGTH_SHORT).show();

                Intent i=new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT,"Write Subject");
                i.putExtra(Intent.EXTRA_TEXT,"https://amarsarkar.000webhostapp.com");
                startActivity(Intent.createChooser(i,"share via"));
                break;
            case R.id.nav_feedback:
                if(findViewById(R.id.freagment_content)!=null) {
                    if (check != null) {

                    } else {
                        fragmentTransaction = fragmentManager.beginTransaction();
                        feedback_freagment feedback_freagment = new feedback_freagment(MainActivity.this);
                        fragmentTransaction.replace(R.id.freagment_content,feedback_freagment, null);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                }
                break;

        }
        closedrawer();
        return true;
    }



}



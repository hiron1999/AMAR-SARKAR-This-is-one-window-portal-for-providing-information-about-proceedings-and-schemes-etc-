package com.example.server_connect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class startingactivity extends AppCompatActivity {

Button signup,login;
TextView heading;
     SharedPreferences prefs;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       prefs=getSharedPreferences("myprefs",startingactivity.MODE_PRIVATE);
         
       // editor.putBoolean("login status",false);
       // editor.putString("name","");
       // editor.putString("id","");
      //  editor.putString("email","");

        setContentView(R.layout.activity_startingactivity);
        signup=(Button)findViewById(R.id.go_signup);
        signup=(Button)findViewById(R.id.go_login);
        heading=(TextView)findViewById(R.id.welcome_text);
//check session........................
        //Toast.makeText(startingactivity.this,prefs.getString("name",""),Toast.LENGTH_SHORT).show();
        if(prefs.getBoolean("login_status",false)){
            Intent i=new Intent(startingactivity.this,MainActivity.class);
            startActivity(i);
        }
    }

    public void goto_login(View view) {
        Intent i=new Intent(this,login_activity.class);
        startActivity(i);
    }

    public void goto_signup(View view) {
        Intent i=new Intent(this,signup_activity.class);
        startActivity(i);
    }
}

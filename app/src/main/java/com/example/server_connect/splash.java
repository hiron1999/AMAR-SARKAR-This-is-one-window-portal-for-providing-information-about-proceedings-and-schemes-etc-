package com.example.server_connect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class splash extends AppCompatActivity {

    private ImageView oo;
    private TextView tt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        tt=(TextView)findViewById(R.id.tt);
        oo=(ImageView)findViewById(R.id.oo);
        Animation myanm= AnimationUtils.loadAnimation(this,R.anim.myanim);
        tt.startAnimation(myanm);
        oo.startAnimation(myanm);
        final Intent i = new Intent(this,startingactivity.class);
        Thread timer=new Thread(){
            public void run()
            {
                try{
                    sleep(3000);
                } catch(InterruptedException e){
                    e.printStackTrace();
                }
                finally{
                    startActivity(i);
                    finish();

                }
            }
        };
        timer.start();

    }
}

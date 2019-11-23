package com.example.server_connect;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ProgressBar;

public class progressbar {

   private Context mcontext;
  private   int milsec;
  private String Title,Msg;
    progressbar(int mil, Context context,String title,String msg){
      milsec=mil;
      mcontext=context;
      Title=title;
      Msg=msg;
    }
    public void run(){

        final ProgressDialog progressDialog=new ProgressDialog(mcontext);
        progressDialog.setTitle(Title);
        progressDialog.setMessage(Msg);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(R.drawable.progressbar);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(milsec);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }
        }).start();
        progressDialog.show();
    }
}

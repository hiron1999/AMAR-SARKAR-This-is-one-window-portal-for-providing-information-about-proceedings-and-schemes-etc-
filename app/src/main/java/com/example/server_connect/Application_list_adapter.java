package com.example.server_connect;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public  class Application_list_adapter extends RecyclerView.Adapter<Application_list_adapter.ViewHolder> {

    private List<Application_listitem> listItems;
    private Context context;
    List<Application_listitem>listitemss;

    public Application_list_adapter(List<Application_listitem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
        this.listitemss= new ArrayList<Application_listitem>(listItems);
    }

    @NonNull
    @Override
    public Application_list_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.application_list,parent,false);
        return new Application_list_adapter.ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Application_listitem listItem=listItems.get(position);

        holder.title.setText(listItem.getTitle());
        holder.date.setText(listItem.getDate());
        holder.status.setText(listItem.getStatus());







      /*  holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"You Just Clicked "+ listItem.getHead()+" or You Can Go Back and check details to know more aacording to list",Toast.LENGTH_LONG).show();

                Intent i=new Intent(context,description.class);
                i.putExtra("title",listItem.getHead());
                i.putExtra("description",listItem.getBio());
                i.putExtra("imgurl",listItem.getImageUrl());
                context.startActivity(i);

            }




        });*/

    }



    @Override
    public int getItemCount() {
        return listItems.size();
    }




    public class ViewHolder extends RecyclerView.ViewHolder{


        public TextView title;
        public TextView date;
        public TextView status;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

           title=(TextView)itemView.findViewById(R.id.application_title);
            date=(TextView)itemView.findViewById(R.id.appplication_date);
            status=(TextView)itemView.findViewById(R.id.application_Status);


        }
    }

}

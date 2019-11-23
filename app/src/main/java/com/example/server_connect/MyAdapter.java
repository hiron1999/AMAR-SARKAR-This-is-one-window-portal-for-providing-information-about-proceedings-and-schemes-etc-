package com.example.server_connect;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;



public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>
{
     private List<ListItem> listItems;
     private Context context;
     List<ListItem>listitemss;

    public MyAdapter(List<ListItem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
        this.listitemss=new ArrayList<>(listItems);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View v= LayoutInflater.from(parent.getContext())
               .inflate(R.layout.list_item,parent,false);
       return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ListItem listItem=listItems.get(position);

        holder.textViewHead.setText(listItem.getHead());
        holder.textViewDesc.setText(listItem.getDesc());

final String IMGURL="https://amarsarkar.000webhostapp.com/dashboard/pages/"+listItem.getImageUrl();


      PicassoTrustAll.getInstance(context)
                .load(IMGURL)
                .into(holder.imageView);


        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


               Intent i=new Intent(context,description.class);
               i.putExtra("title",listItem.getHead());
               i.putExtra("description",listItem.getBio());
               i.putExtra("imgurl",IMGURL);
               i.putExtra("schemeid",listItem.getid());
               context.startActivity(i);

                }




        });



    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }




    public class ViewHolder extends RecyclerView.ViewHolder{


        public TextView textViewHead;
        public TextView textViewDesc;

        public ImageView imageView;

        public LinearLayout linearLayout;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewHead=(TextView)itemView.findViewById(R.id.textViewHead);
            textViewDesc=(TextView)itemView.findViewById(R.id.textViewDesc);

            imageView=(ImageView)itemView.findViewById(R.id.imageView);

            linearLayout=(LinearLayout)itemView.findViewById(R.id.linearLayout);

        }
    }

}

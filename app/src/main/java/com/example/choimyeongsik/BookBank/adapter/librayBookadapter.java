package com.example.choimyeongsik.BookBank.adapter;


import android.content.Context;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.choimyeongsik.BookBank.R;
import com.example.choimyeongsik.BookBank.model.datalibraryVO;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class librayBookadapter extends RecyclerView.Adapter<librayBookadapter.ItemViewHolder>{
   private ArrayList<datalibraryVO> mitems;
   Context mContext;

    public librayBookadapter(ArrayList<datalibraryVO> items, Context context) {
       this.mitems = items;
       this.mContext = context;

    }





    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.libray_book, parent, false);
        return new ItemViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder itemViewHolder, final int position) {
        for(int i=0; i<mitems.size(); i++) {
            if(!mitems.contains(mitems.get(position))) {
                mitems.add(mitems.get(position));
            }
        }
        Log.d("안녕", mitems.get(position).getBookImageURL());
        String value = mitems.get(position).getBookImageURL();
        value = value.replaceAll("\\p{Z}", "");
   Glide.with(itemViewHolder.imageView.getContext()).load(value).override(500,500).into(itemViewHolder.imageView);
   itemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View v) {
           Log.d("하이", mitems.get(position).getTitle() + "/" + mitems.get(position).getIsbn13());
            String url = mitems.get(position).getIsbn13();
            url = url.replaceAll("\\p{Z}", "");

       }
   });
    }

    @Override
    public int getItemCount() {
        return mitems.size();
    }


    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public ItemViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView)itemView.findViewById(R.id.imageview);


        }
    }


}

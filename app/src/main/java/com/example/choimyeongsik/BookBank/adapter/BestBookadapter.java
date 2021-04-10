package com.example.choimyeongsik.BookBank.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.choimyeongsik.BookBank.model.BestBookVO;
import com.example.choimyeongsik.BookBank.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BestBookadapter extends RecyclerView.Adapter<BestBookadapter.BestViewHolder> {
    private ArrayList<BestBookVO> mitem;
    public BestBookadapter(ArrayList<BestBookVO> itmes) {
        this.mitem = itmes;
    }

    @NonNull
    @Override
    public BestBookadapter.BestViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.best_book,viewGroup,false);
        return new BestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BestBookadapter.BestViewHolder bestViewHolder, int i) {
        Glide.with(bestViewHolder.imageView.getContext()).load(mitem.get(i).getBest_bookimageURL()).override(500,500).into(bestViewHolder.imageView);
    }

    @Override
    public int getItemCount() {
        return mitem.size();
    }

    public class BestViewHolder extends  RecyclerView.ViewHolder {
        ImageView imageView;
        public BestViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = (ImageView)itemView.findViewById(R.id.best_image);
        }
    }
}

package com.example.choimyeongsik.BookBank.adapter;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.choimyeongsik.BookBank.model.KAKAOBookVO;
import com.example.choimyeongsik.BookBank.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    private ArrayList<KAKAOBookVO> mitem;


    public BookAdapter(ArrayList<KAKAOBookVO> items) {
        this.mitem = items;
    }




    public static class BookViewHolder extends  RecyclerView.ViewHolder {
        TextView booktitle;
        TextView bookpublisher;
        TextView booksale_price;
        ImageView bookimage;
        TextView bookauthors;
        protected  RecyclerView recyclerView;






        BookViewHolder(View view) {
            super(view);
            booktitle = (TextView) view.findViewById(R.id.booktitle);
            bookpublisher = (TextView) view.findViewById(R.id.bookpublisher);

            bookimage = (ImageView) view.findViewById(R.id.bookimage);
            this.recyclerView = (RecyclerView)view.findViewById(R.id.kakabookView);
            bookauthors = (TextView)view.findViewById(R.id.bookauthors);

        }


    }



    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookitem,parent,false);


        return new BookViewHolder(view);

    }




    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        ArrayList<String> authors = mitem.get(position).getAuthors();


        String str = authors.set(0, authors.get(0));
        Log.d("확인", String.valueOf(str));

        BookViewHolder bookViewHolder = (BookViewHolder) holder;
        bookViewHolder.bookauthors.setText(String.valueOf(str));
        bookViewHolder.booktitle.setText(mitem.get(position).getTitle());
        // bookViewHolder.booksale_price.setText(mitem.get(position).getSale_price() +"원");
        bookViewHolder.bookpublisher.setText(mitem.get(position).getPublisher());
        Glide.with(bookViewHolder.bookimage.getContext()).load(mitem.get(position).getThumbnail()).override(300,300).into(bookViewHolder.bookimage);

    }

    @Override
    public int getItemCount() {
        return mitem.size();
    }
}









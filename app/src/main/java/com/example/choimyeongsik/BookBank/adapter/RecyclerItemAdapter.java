package com.example.choimyeongsik.BookBank.adapter;

import android.content.Context;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.choimyeongsik.BookBank.R;
import com.example.choimyeongsik.BookBank.model.RecyclerItem;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
//

public class RecyclerItemAdapter extends RecyclerView.Adapter<RecyclerItemAdapter.ItemViewHolder> {
    ArrayList<RecyclerItem> mitems;
    private ArrayList<RecyclerItem> arrayList;
    public Context mContext;

    public RecyclerItemAdapter(ArrayList<RecyclerItem> items) {
        mitems = items;
        arrayList = new ArrayList<RecyclerItem>();

    }


    public RecyclerItemAdapter(Context context) {
        this.mContext = context;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.bookdbitem, viewGroup, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder itemViewHolder, int position) {
       /*     itemViewHolder.title.setText(mitems.get(position).getTitle());
        itemViewHolder.pirce.setText(mitems.get(position).getPirce());
        itemViewHolder.publisher.setText(mitems.get(position).getPublisher());
        itemViewHolder.auhors.setText(mitems.get(position).getAuthors()); */
        Glide.with(itemViewHolder.dbimage.getContext()).load(mitems.get(position).getThumbnail()).override(300,300).into(itemViewHolder.dbimage);



    }

    @Override
    public int getItemCount() {
        Log.d("아이템카운트", String.valueOf(mitems.size()));
        return mitems.size();
    }

    public  class ItemViewHolder extends  RecyclerView.ViewHolder {

        private TextView title;
        private TextView pirce;
        private TextView publisher;
        private TextView auhors;
        private ImageView dbimage;

        CardView cardView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
       /*     title = (TextView)itemView.findViewById(R.id.dbtitle);
            pirce = (TextView)itemView.findViewById(R.id.dbpirce);
            publisher = (TextView)itemView.findViewById(R.id.dbpublisher);
            auhors = (TextView)itemView.findViewById(R.id.dbauthors); */
            dbimage = (ImageView) itemView.findViewById(R.id.dbimage);

       ;




        }





    }

}

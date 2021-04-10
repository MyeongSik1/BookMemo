package com.example.choimyeongsik.BookBank.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.choimyeongsik.BookBank.R;
import com.example.choimyeongsik.BookBank.model.RecordVo;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ItemViewHolder> {


private Context mContext;
    private ArrayList<RecordVo> mItems;
    private ArrayList<RecordVo> arrayList;

    public RecordAdapter(Context mContext, ArrayList<RecordVo> items) {
      this.mContext =mContext;
        mItems = items;
        arrayList = new ArrayList<RecordVo>();
        arrayList.addAll(mItems);
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.record_item,viewGroup,false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder itemViewHolder, final int i) {
        if (mItems.get(i).getImage() == null) {
            itemViewHolder.imageView.setVisibility(View.GONE);
        } else {
            Bitmap bmp = BitmapFactory.decodeByteArray(mItems.get(i).getImage(), 0, mItems.get(i).getImage().length);
            Glide.with(itemViewHolder.imageView.getContext()).load(bmp).override(300,300).into(itemViewHolder.imageView);
        }
        itemViewHolder.contents.setText(mItems.get(i).getContent());
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }


    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView contents;
        private ImageView imageView;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            contents = (TextView)itemView.findViewById(R.id.contents);
            imageView = (ImageView)itemView.findViewById(R.id.book_image);
        }
    }
}

package com.example.choimyeongsik.BookBank.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.choimyeongsik.BookBank.model.GridItem;
import com.example.choimyeongsik.BookBank.ImageClickActivity;
import com.example.choimyeongsik.BookBank.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GridItemAdapter extends RecyclerView.Adapter<GridItemAdapter.ViewHolder> {
    private ArrayList<GridItem> mitems;
    private Context context;
    public GridItemAdapter(ArrayList<GridItem> items, Context context) {
        this.mitems = items;
        this.context = context;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public GridItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.grid_view,viewGroup,false);
        return new GridItemAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GridItemAdapter.ViewHolder viewHolder, final int i) {
        if (mitems.get(i).getImage() != null) {
            Bitmap bmp = BitmapFactory.decodeByteArray(mitems.get(i).getImage(), 0, mitems.get(i).getImage().length);
            Glide.with(viewHolder.imageView.getContext()).load(bmp).override(300,300).into(viewHolder.imageView);
        }
        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {           // 이미지클릭시 확대
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                Bitmap bit = BitmapFactory.decodeByteArray(mitems.get(i).getImage(), 0,mitems.get(i).getImage().length);
                bit.compress(Bitmap.CompressFormat.JPEG,100,stream);
                byte[] byteArray = stream.toByteArray();
                Context context = v.getContext();
                Intent intent = new Intent(v.getContext(), ImageClickActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("image", byteArray);
                context.startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return mitems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
       private ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = (ImageView)itemView.findViewById(R.id.gridimage);


        }
    }
}

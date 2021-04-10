package com.example.choimyeongsik.BookBank.Fragment;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.choimyeongsik.BookBank.DB.BookDatabase;
import com.example.choimyeongsik.BookBank.model.GridItem;
import com.example.choimyeongsik.BookBank.adapter.GridItemAdapter;
import com.example.choimyeongsik.BookBank.R;
import com.example.choimyeongsik.BookBank.decoration.SpacesItemDecoration;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public  class FragmentGallery extends Fragment {
    public ArrayList <GridItem> mitems;
    public RecyclerView recycler;
    public GridItemAdapter adapter;
    public GridItem gridItem;
    Context mcontext;
    byte[] ks;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.fragment_gallery, container, false);

        TextView textView = (TextView)rootview.findViewById(R.id.image_count);

        recycler = rootview.findViewById(R.id.grid_recycler);

        int spanCount = 2;
        int spacing = 20;
        boolean includeEdge = false;




        recycler.setLayoutManager(new GridLayoutManager(getActivity(),spanCount));
        recycler.addItemDecoration(new SpacesItemDecoration(spanCount,spacing,includeEdge));
        mitems = new ArrayList <GridItem>();
        adapter = new GridItemAdapter(mitems, mcontext);
        recycler.setAdapter(adapter);
        GridArray();
        adapter.notifyDataSetChanged();
        textView.setText(String.valueOf(mitems.size()) + "개의 이미지");

        return rootview;
    }

    public void GridArray() {
        Cursor cs = null;
        BookDatabase helper = new BookDatabase(getActivity());
        SQLiteDatabase db = helper.getWritableDatabase();
        cs = db.rawQuery("select image from tb_book", null);


        if (cs.getCount() > 0 ) {
            while (cs.moveToNext()) {
                ks = cs.getBlob(cs.getColumnIndex("image"));
                if (ks != null) {
                    gridItem = new GridItem(
                            cs.getBlob(cs.getColumnIndex("image")));
                    mitems.add(gridItem);
                }
            }
        }







    }
}






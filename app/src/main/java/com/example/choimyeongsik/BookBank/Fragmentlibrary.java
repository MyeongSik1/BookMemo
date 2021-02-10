package com.example.choimyeongsik.BookBank;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Fragmentlibrary extends Fragment {
    private RecyclerItem mitem;
    private ArrayList<RecyclerItem> mitems;
    public RecyclerView recycler;
    public RecyclerItemAdapter adapter;
    private TextView bookcount;
   ImageView setting_button;
    private Context mcontext;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup rootview = (ViewGroup)inflater.inflate(R.layout.fragment_library,container,false);
        Log.d("나왔당", "onCreateView");
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        mcontext = container.getContext();

    setting_button = (ImageView)rootview.findViewById(R.id.button_setting);
        bookcount = (TextView)rootview.findViewById(R.id.bookcount);
        recycler = (RecyclerView)rootview.findViewById(R.id.main_RecyclerView);
        recycler.setHasFixedSize(true);
        int spanCount = 3;
        int spacing = 20;
        boolean includeEdge = false;
        recycler.setLayoutManager(new GridLayoutManager(getActivity(),spanCount));
        recycler.addItemDecoration(new SpacesItemDecoration(spanCount,spacing,includeEdge));
        RecyclerDecoration spaceDecoration = new RecyclerDecoration(2);
        BookDatabase helper = new BookDatabase(getContext());
        mitems = new ArrayList <RecyclerItem>();
        cursorArray();
        adapter = new RecyclerItemAdapter(mitems);
        adapter.notifyDataSetChanged();
        recycler.setAdapter(adapter);

        bookcount.setText("총" + String.valueOf(mitems.size()) + "권의 책을 추가하였습니다  ");


        recycler.addOnItemTouchListener(new book_search.RecyclerTouchListener(getActivity(), recycler, new book_search.ClickListener() {
            @Override
            public void onClick(View view, int postion) {
                String title  = mitems.get(postion).getTitle();
                Log.d("하이", title);
                String getThumbnail = mitems.get(postion).getThumbnail();
                Intent intent = new Intent(getActivity(),book_record.class);
                intent.putExtra("title", mitems.get(postion).getTitle());
                intent.putExtra("thumbnail",mitems.get(postion).getThumbnail());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int postion) {

            }
        }));

        setting_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Setting.class);
                startActivity(intent);
            }
        });



        return rootview;
    }



    public void cursorArray() {

        Cursor cs = null;
        BookDatabase helper = new BookDatabase(getContext());
        SQLiteDatabase db = helper.getWritableDatabase();
        cs = db.rawQuery("select * from  tb_book", null);




        while (cs.moveToNext()) {
            String check = cs.getString(cs.getColumnIndex("title"));
            if (check != null) {
                mitem = new RecyclerItem(
                        cs.getString(cs.getColumnIndex("title")),
                        cs.getString(cs.getColumnIndex("pirce")),
                        cs.getString(cs.getColumnIndex("publisher")),
                        cs.getString(cs.getColumnIndex("authors")),
                        cs.getString(cs.getColumnIndex("thumbnail")),
                        cs.getString(cs.getColumnIndex("isbn")));


                mitems.add(mitem);

            }
        }
        cs.close();
    }

    public void refrsh() {
        mitems = new ArrayList <RecyclerItem>();  // 임시대처
        cursorArray();
        adapter = new RecyclerItemAdapter(mitems);
        adapter.notifyDataSetChanged();
        recycler.setAdapter(adapter);
    }




}

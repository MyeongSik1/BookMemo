package com.example.choimyeongsik.BookBank;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class book_record extends AppCompatActivity {
    public static final int REQUEST_CODE_WRITE = 1003;
    public static final int REQUEST_CODE_RECORD= 1004;
Button record_button;
    ImageView imageView;
    TextView title;
    Button record_delete;
    String title_;
    public RecyclerView recyclerView;
    public RecordAdapter adapter;
    public Record_Item record_item;
    public ArrayList<Record_Item> mitems;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_record);
        getWindow().setStatusBarColor(Color.parseColor("#D8D8D8"));

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_record);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        title = (TextView)findViewById(R.id.title);
        imageView = (ImageView)findViewById(R.id.thumbnail);
        record_delete = (Button)findViewById(R.id.record_dele);
        Intent intent = getIntent();
        title_ = intent.getExtras().getString("title");
        String thumbnail = intent.getExtras().getString("thumbnail");

        title.setText(title_);
        Glide.with(imageView).load(thumbnail).override(300,300).into(imageView);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);           // 리사이클러뷰 데이터 역순으로 뿌리기
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView = (RecyclerView) findViewById(R.id.record_Recycler);
        recyclerView.setLayoutManager(linearLayoutManager);
        mitems = new ArrayList<Record_Item>();
        adapter = new RecordAdapter(getApplicationContext(),mitems);
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
       recyclerView.setAdapter(adapter);
        cursorArray();
        adapter.notifyDataSetChanged();
         textView = (TextView)findViewById(R.id.memocount);
            int asd = adapter.getItemCount();
            textView.setText( String.valueOf(asd));







        record_button = (Button)findViewById(R.id.record_button);
        record_button.setOnClickListener(new View.OnClickListener() {       // 글작성
            @Override
            public void onClick(View view) {
                String title = title_;
                Intent intent = new Intent(getApplicationContext(),contents_record.class);
                intent.putExtra("title", title);
                startActivityForResult(intent,REQUEST_CODE_WRITE );

            }
        });

        record_delete.setOnClickListener(new View.OnClickListener() {       // 책삭제
            @Override
            public void onClick(View v) {
                Cursor cs = null;
                BookDatabase helper = new BookDatabase(getApplication());
                SQLiteDatabase db = helper.getWritableDatabase();
                db.execSQL("DELETE FROM tb_book WHERE title = '" + title_ + "';");
                BookDatabase helper2 = new BookDatabase(getApplication());
                SQLiteDatabase db2 = helper2.getWritableDatabase();
                db2.execSQL("DELETE FROM tb_book WHERE name = '" + title_ + "';");
                Fragmentlibrary fragmentlibrary = new Fragmentlibrary();

              finish();


            }
        });
        // 리사이클러뷰 클릭이벤트
         recyclerView.addOnItemTouchListener(new book_search.RecyclerTouchListener(getApplicationContext(), recyclerView, new book_search.ClickListener() {
             @Override
             public void onClick(View view, int postion) {
                 String number = mitems.get(postion).getNumber();
                Intent intent1 = new Intent(getApplicationContext(), contents_revise.class);
                intent1.putExtra("number",number);
                startActivityForResult(intent1,REQUEST_CODE_RECORD);
             }

             @Override
             public void onLongClick(View view, int postion) {

             }
         }));


    }








    public void cursorArray() {

        Cursor cs = null;
        BookDatabase helper = new BookDatabase(getApplication());
        SQLiteDatabase db = helper.getWritableDatabase();
        cs=   db.rawQuery("SELECT contents, name, image, _id FROM tb_book", null);


        while (cs.moveToNext()) {
            String na = cs.getString(cs.getColumnIndex("name"));
            if(title_.equals(na)) {
                record_item = new Record_Item(
                        cs.getString(cs.getColumnIndex("name")),
                        cs.getString(cs.getColumnIndex("contents")),
                        cs.getBlob(cs.getColumnIndex("image")),
                        cs.getString(cs.getColumnIndex("_id")));
                mitems.add(record_item);
            }
            else {

            }
        }
        cs.close();
        db.close();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_WRITE) {
           content_update();
        }
        if (requestCode == REQUEST_CODE_RECORD) {
            content_update();
        }
    }
    public void content_update() { // 되돌아왔을때 다시뿌리기
        mitems = new ArrayList<Record_Item>();
        adapter = new RecordAdapter(getApplicationContext(),mitems);
        recyclerView.setAdapter(adapter);
        cursorArray();
        adapter.notifyDataSetChanged();
        int asd = adapter.getItemCount();
        textView.setText( String.valueOf(asd));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

}



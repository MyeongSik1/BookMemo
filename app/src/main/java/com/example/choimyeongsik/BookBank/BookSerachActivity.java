package com.example.choimyeongsik.BookBank;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;


import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;


import com.example.choimyeongsik.BookBank.adapter.BookAdapter;
import com.example.choimyeongsik.BookBank.callback.KakaoRetrofit;
import com.example.choimyeongsik.BookBank.decoration.RecyclerDecoration;
import com.example.choimyeongsik.BookBank.model.Document;
import com.example.choimyeongsik.BookBank.model.DocumentList;
import com.example.choimyeongsik.BookBank.model.KAKAOBookVO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BookSerachActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    final String BOOK  = "Book-search";
    LinearLayoutManager mLayoutManager;
    BookAdapter bookAdapter;
    ProgressDialog asyncDialog;
    ArrayList<KAKAOBookVO> mitems = new ArrayList <>();
    ArrayList<Document> Tr;
     String isbn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_search);
        getWindow().setStatusBarColor(Color.parseColor("#D8D8D8"));
        Intent intent = getIntent();
         String title = intent.getExtras().getString("title");
        Log.d(BOOK, title);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_too);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = findViewById(R.id.kakabookView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        RecyclerDecoration spaceDecoration = new RecyclerDecoration(50);
        mRecyclerView.addItemDecoration(spaceDecoration);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));

      BookTask  bookTask = new BookTask(this); // 카카오 책검색 api
     bookTask.execute(title);










         Retrofit retrofit = new Retrofit.Builder()
                 .baseUrl("https://dapi.kakao.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        KakaoRetrofit retable = retrofit.create(KakaoRetrofit.class);
        Call <DocumentList> call = retable.getDocument("안녕");



        call.enqueue(new Callback <DocumentList>() {
                         @Override
                         public void onResponse(Call <DocumentList> call, Response <DocumentList> response) {
                                    Log.v("안녕2", response.toString());
                           Tr = response.body().documents;
                           Log.d("안녕2", String.valueOf(response.body().documents.size()));
                           Log.d("안녕2", Tr.get(1).getTitle());


                         }

                         @Override
                         public void onFailure(Call <DocumentList> call, Throwable t) {
                                        Log.d("안녕2", "실패");
                         }
                     });


                mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), mRecyclerView, new ClickListener() {
                    @Override
                    public void onClick(View view, int postion) {
                        startinformation(postion, getApplication());
                    }

                    @Override
                    public void onLongClick(View view, int postion) {

                    }
                }));

    }

    class BookTask extends AsyncTask<String, Integer, String> {
        private Context context;


        public BookTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            Intent intent = getIntent();
            if (intent !=  null) {
                asyncDialog = new ProgressDialog(BookSerachActivity.this);
                asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                asyncDialog.setMessage("책 검색중입니다. . ");
                asyncDialog.show();
            } else
            {
                asyncDialog = new ProgressDialog(context);
                asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                asyncDialog.setMessage("책 검색중입니다. . ");
                asyncDialog.show();
            }
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {       // FragmentHome에서 클릭했을경우와 책검색후에 클릭했을경우를 나눔
            Intent intent = getIntent();
            if (intent != null) {
                String HOME = intent.getExtras().getString("HOME");
                if (HOME.equals("HOME")) {
                    bookAdapter = new BookAdapter(mitems);
                    mRecyclerView.setAdapter(bookAdapter);
                    bookAdapter.notifyDataSetChanged();
                }
            } else {
                startinformation(0, context);
            }


          asyncDialog.dismiss();


        }

        @Override
        protected String doInBackground(String... parms) {
            String json;
            BufferedReader br;
            URL url;
            try {
                String address = "https://dapi.kakao.com/v3/search/book?target=title&query="+ URLEncoder.encode(parms[0],"UTF-8")+ ("&size=30");
                url = new URL(address);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.setConnectTimeout(30000);
                conn.setRequestProperty("Authorization","KakaoAK 3e82e4d9bea33046d8289b5e3eb53add");
                br = new BufferedReader( new InputStreamReader(conn.getInputStream()));
                if ( br == null) {
                    Toast.makeText(getApplication(), "데이터 다운로드 실패", Toast.LENGTH_SHORT).show();
                }
                StringBuilder sb = new StringBuilder();
                String line = null;
                while((line = br.readLine()) != null) {
                    sb.append(line);
                }
                br.close();
                ObjectMapper mapper = new ObjectMapper();
                Map<String,Object> map = mapper.readValue(sb.toString(),
                        new TypeReference<Map<String,Object>>() {
                        });
                Object obj = map.get("documents");
                String resultJsonData = mapper.writeValueAsString(obj);
                mitems = mapper.readValue(resultJsonData,
                        new TypeReference<ArrayList<KAKAOBookVO>>() {});
                /* for(KAKAOBookVO book : mitems){
                    Log.i("KAKAOBOOKLog", String.valueOf(book.getAuthors()));
               } */
            } catch (Exception e) {
            }

            return parms[0];
        }
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

    public interface ClickListener {
        void onClick(View view, int postion);
        void onLongClick(View view, int postion);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {
        private GestureDetector gestureDetector;
        private BookSerachActivity.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final BookSerachActivity.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildAdapterPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }
    }



    public void startinformation(int postion, Context context) {  // book_infomation에 데이터 전달
    Intent intent = new Intent(context, BookInformationActivity.class);
    ArrayList<String> authors = mitems.get(postion).getAuthors();
    String str = authors.set(0, authors.get(0)); // [] 빼기
    String title_  = mitems.get(postion).getTitle();
    String sale_price_= mitems.get(postion).getSale_price();
    String datatime_ = mitems.get(postion).getDatetime();
    String thumbnail_ = mitems.get(postion).getThumbnail();
    String authors_  = String.valueOf(authors);
    String main_publisher_ = mitems.get(postion).getPublisher();
    String isbncut = mitems.get(postion).getIsbn();
    String bookurl = mitems.get(postion).getUrl();


    String zsd = Integer.toString(isbncut.length()); // isbn 길이 확인
    int asdz = Integer.parseInt(zsd);


    if (asdz > 14) {
        isbn = isbncut.substring(11); // Isbn 13자리
    } else {
        isbn = null;
    }

    intent.putExtra("title", title_);
    intent.putExtra("sale_price", sale_price_);
    intent.putExtra("datatime", datatime_);
    intent.putExtra("thumbnail", thumbnail_);
    intent.putExtra("authors", str);
    intent.putExtra("main_publisher", main_publisher_);
    intent.putExtra("isbn", isbn);
    intent.putExtra("url", bookurl);
   intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


        context.startActivity(intent);

}


}

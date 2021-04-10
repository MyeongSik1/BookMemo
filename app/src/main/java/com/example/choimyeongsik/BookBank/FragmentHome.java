package com.example.choimyeongsik.BookBank;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.example.choimyeongsik.BookBank.adapter.BestBookadapter;
import com.example.choimyeongsik.BookBank.adapter.librayBookadapter;
import com.example.choimyeongsik.BookBank.model.BestBookVO;
import com.example.choimyeongsik.BookBank.model.datalibraryVO;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
//
import static com.kakao.util.helper.Utility.getPackageInfo;

public class FragmentHome extends Fragment {
    EditText edittext;
    librayBookadapter bookadapter;
    BestBookadapter bestbookadapter;
    Context mContext;






    private RecyclerView mRecyclerView,mRecyclerView2;
    private LinearLayoutManager mLayoutManager,mLayoutManager2;
    ArrayList<datalibraryVO> blist = new ArrayList <>();
    ArrayList<BestBookVO> elist = new ArrayList <>();
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }




    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootview = (ViewGroup)inflater.inflate(R.layout.fragment_home,container,false);
        edittext = (EditText)rootview.findViewById(R.id.edit_book_search);
        mRecyclerView = rootview.findViewById(R.id.recycler_book);
        mRecyclerView2 = rootview.findViewById(R.id.best_book);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView2.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mLayoutManager2 = new LinearLayoutManager(getContext());
        mLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView2.setLayoutManager(mLayoutManager2);

        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.getDefault());
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.getDefault());
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());
        String year = yearFormat.format(currentTime);
        String month = monthFormat.format(currentTime);
        String day = dayFormat.format(currentTime);
         int dayi = Integer.parseInt(day);
         int d,d2;
         String s,s2;
        if (7 < dayi) {                // 도서관 날짜구하기 1주일마다 베스트셀러변경
            d = 8;
            d2 = dayi;
            s = String.format("%02d", d);
            s2 = Integer.toString(d2);
        } else {
            d = 1;
            d2  = 7;
             s = String.format("%02d", d);
             s2 = String.format("%02d", d2);
        }
        if (15 < dayi) {
            d = 16;
            d2 = dayi;
            s = Integer.toString(d);
            s2 = Integer.toString(d2);
        }
        if (23 < dayi) {
            d = 24;
            d2 = dayi;
            s = Integer.toString(d);
            s2 = Integer.toString(d2);
        }
        String time = year + "-" + month + "-" + s + "&endDt=" + year + "-" + month + "-" + s2;
        String booktimer = "http://data4library.kr/api/loanItemSrch?authKey=01ac025572c930c78ee563df2ff9829e85be0b408ad7786afa0b1a2e40709aba&startDt=" +
                time;

       library librarya = new library();      // 도서정보나루 인기도서
        librarya.execute(booktimer);
        WeekBest weekBest = new WeekBest();  //교보문고 베스트셀러
        weekBest.execute();


        edittext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) { // 책검색
                switch ( actionId) {
                    case EditorInfo.IME_ACTION_SEARCH:

                        String title = edittext.getText().toString();
                        Intent intent = new Intent(getActivity(), BookSerachActivity.class);
                        intent.putExtra("HOME", "HOME");
                        intent.putExtra("title", title);
                        startActivity(intent);
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });
        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), mRecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {          // 도서관 인기 메인에서 클릭시
                BookSerachActivity.BookTask  oi = new BookSerachActivity(). new BookTask(mContext);
                String a = blist.get(position).getIsbn13();
                oi.execute(a);
            }
            @Override
            public void onLongClick(View view, int position) {
            }
        }));

        mRecyclerView2.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), mRecyclerView2, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                BookSerachActivity.BookTask  oi = new BookSerachActivity(). new BookTask(mContext); // 교보문고 베스트셀러 클릭시
                String a = elist.get(position).getBest_isbn13();
                oi.execute(a);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


        return rootview;
    }
    class library extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String s) {
            bookadapter = new librayBookadapter(blist, mContext);
            mRecyclerView.setAdapter(bookadapter);

        }
        @Override
        protected String doInBackground(String... zkzk) {

            try {
                URL url = new URL(zkzk[0]);
                DocumentBuilderFactory factory =
                        DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder(); //XML문서 빌더 객체를 생성
                Document document = builder.parse(new InputSource(url.openStream())); //XML문서를 파싱한다.
                document.getDocumentElement().normalize(); // 공백등 제거
                NodeList nodeList = document.getElementsByTagName("doc");

                for(int i=0; i< 15; i++) {
                    Node docs = nodeList.item(i);
                    Log.d("library", String.valueOf(nodeList.getLength()));
                    NodeList cList = docs.getChildNodes();
                    datalibraryVO b = new datalibraryVO();
                    for(int k=0; k<cList.getLength(); k++) {
                        Node item = cList.item(k);
                        String value = item.getNodeName();
                        if(value.equals("bookname")) b.setTitle(item.getTextContent());
                        if (value.equals("authors")) b.setAuthors(item.getTextContent());
                        if (value.equals("isbn13")) b.setIsbn13(item.getTextContent());
                        if (value.equals("bookImageURL")) b.setBookImageURL(item.getTextContent());
                    }
                    blist.add(b);
                }




            } catch (Exception e) {
                e.printStackTrace();
                Log.e("home", e.getMessage());
            }

            return null;
        }

    }

    class WeekBest extends AsyncTask <String, Integer, String> {
        ProgressDialog asyncDialog = new ProgressDialog(mContext);

        @Override
        protected void onPreExecute() {
            asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            asyncDialog.setMessage("주간 베스트 목록을 불러오는중입니다. . ");
            asyncDialog.show();
            super.onPreExecute();



        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            RecyclerView.ItemAnimator animator = mRecyclerView2.getItemAnimator();
            if (animator instanceof SimpleItemAnimator) {
                ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
            }
            bestbookadapter = new BestBookadapter(elist);
            mRecyclerView2.setAdapter(bestbookadapter);
            asyncDialog.dismiss();
        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                org.jsoup.nodes.Document doc = Jsoup.connect
                        ("http://www.kyobobook.co.kr/bestSellerNew/bestseller.laf?orderClick=d79").get();

                Elements contents = doc.select("div.cover").select("a[href]");
                Elements url = contents.select("img[src]");
                for(org.jsoup.nodes.Element ur : url) {
                    BestBookVO s = new BestBookVO();
                    String url2 = ur.attr("src");
                    String isbn13 = url2.substring(url2.lastIndexOf("l"),url2.indexOf(".jpg"));
                    String isbn = isbn13.replaceAll("l", "");

                    s.setBest_bookimageURL(url2);
                    s.setBest_isbn13(isbn);
                    elist.add(s);



                }
                for(BestBookVO bo : elist) {
                    Log.d("URL", bo.getBest_bookimageURL());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }




    public interface ClickListener {
        void onClick(View view, int position);
        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private FragmentHome.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final FragmentHome.ClickListener clickListener) {
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
}



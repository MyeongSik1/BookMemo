package com.example.choimyeongsik.BookBank;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.choimyeongsik.BookBank.DB.BookDatabase;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

public class BookInformationActivity extends AppCompatActivity implements View.OnClickListener {
    TextView text_title;
    TextView text_sale_price;
    TextView text_datatime;
    TextView text_authors;
    ImageView image_thumbnail;
    TextView Genre;
    Button load_button;
    String category;
    String bookurl;
    TextView introduction;
    TextView Toc;
    TextView review;
    TextView inbook;
    ImageView see4;
    ImageView see1;
    ImageView see2;
    ImageView see3;
    TextView exist;


    private   BarChart barChart;
    int[] to2 = new int[8];
    private String[] St = new String[8];
    private String[] Ge = new String[8];
    private int a;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_information);
        getWindow().setStatusBarColor(Color.parseColor("#D8D8D8"));
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_information);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent(); /*데이터 수신*/
        final String name = intent.getExtras().getString("title");
        String sale_price = intent.getExtras().getString("sale_price");
        String datatime = intent.getExtras().getString("datatime");
        final String thumbnail = intent.getExtras().getString("thumbnail");
        final String authors = intent.getExtras().getString("authors");
        final String main_publisher = intent.getExtras().getString("main_publisher");
        final String isbn = intent.getExtras().getString("isbn");
        bookurl = intent.getExtras().getString("url");


        Genre = (TextView) findViewById(R.id.Genre);
        text_authors = (TextView) findViewById(R.id.main_authors);
        text_datatime = (TextView) findViewById(R.id.datatime);
        text_sale_price = (TextView) findViewById(R.id.main_price);
        text_title = (TextView) findViewById(R.id.main_title);
        image_thumbnail = (ImageView) findViewById(R.id.main_image);

        introduction = (TextView) findViewById(R.id.Introduction);
        Toc = (TextView) findViewById(R.id.Toc);
        review = (TextView) findViewById(R.id.review);
        inbook = (TextView) findViewById(R.id.inbook);
        exist = (TextView)findViewById(R.id.exist);
        see1 = (ImageView) findViewById(R.id.see1);
        see2 = (ImageView) findViewById(R.id.see2);
        see3 = (ImageView) findViewById(R.id.see3);
        see4 = (ImageView) findViewById(R.id.see4);
        barChart = (BarChart) findViewById(R.id.barChart);
        introduction.setMaxLines(8);
        Toc.setMaxLines(8);
        review.setMaxLines(8);
        barChart.setHighlightPerTapEnabled(false);
        barChart.getAxisRight().setEnabled(false);
        barChart.setScaleEnabled(false);
        barChart.getLegend().setEnabled(false);
        barChart.setTouchEnabled(true);
        barChart.setDragEnabled(false);
        barChart.setScaleEnabled(false);
        barChart.setScaleXEnabled(false);
        barChart.setScaleYEnabled(false);
        barChart.setPinchZoom(false);
        barChart.setHighlightPerDragEnabled(false);
        barChart.setHighlightPerTapEnabled(false);
        barChart.setDrawGridBackground(false);
        barChart.setGridBackgroundColor(Color.rgb(255, 255, 255));
        barChart.getXAxis().setGridColor(Color.rgb(255, 255, 255));

        load_button = (Button) findViewById(R.id.load);

        if (isbn == null) {
            Genre.setText("분류 안됨");                            // 도서 장르 분류
        } else {
            String isbnurl = "https://www.bookoob.co.kr/" + isbn;
            try {
                String isbn1 = new Background().execute(isbnurl).get();
                if (TextUtils.isEmpty(isbn1)) {    // 반환받은 값이 null일경우
                    Genre.setText("분류 안됨");
                } else {
                    String isbn3 = isbn1.substring(isbn1.indexOf(""), isbn1.indexOf(">"));
                    category = isbn3;
                    Genre.setText(isbn1);
                }

            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }





        String dataTime = datatime.substring(0, 10);
        int to = Integer.parseInt(sale_price);
        DecimalFormat decimalFormat = new DecimalFormat("#,##0");
        final String pr = decimalFormat.format(to);
        String te = "-1";

        if (te.equals(pr)) {
            text_sale_price.setText("정상판매 X");
        } else {
            text_sale_price.setText(pr);
        }
        text_title.setText(name);
        //  text_main_publisher.setText(main_publisher);
        text_datatime.setText(dataTime);
        text_authors.setText(authors);
        Glide.with(getApplicationContext()).load(thumbnail).error(R.mipmap.ic_launcher).override(600, 600).into(image_thumbnail);

        DataTask dataTask = new DataTask();    // 책소개 파싱
        dataTask.execute();
        String Graphurl = "http://data4library.kr/api/usageAnalysisList?authKey=01ac025572c930c78ee563df2ff9829e85be0b408ad7786afa0b1a2e40709aba &isbn13=" + isbn;
        Statistics task1 = new Statistics(); // 독자 데이터 파싱
        task1.execute(Graphurl);



        introduction.setOnClickListener(this);
        Toc.setOnClickListener( this);
        review.setOnClickListener(this);
        inbook.setOnClickListener( this);
        see1.setOnClickListener(this);
        see2.setOnClickListener(this);
        see3.setOnClickListener(this);
        see4.setOnClickListener(this);


        load_button.setOnClickListener(new View.OnClickListener() {          // 책저장( 중복확인)
            @Override
            public void onClick(View view) {
                BookDatabase bookDatabase = new BookDatabase(getApplication());
                SQLiteDatabase db = bookDatabase.getWritableDatabase();

                String sql = "SELECT title FROM "+ "tb_book" + " WHERE title = '" + name + "'";
                Cursor cu = db.rawQuery(sql, null);
                if (cu.getCount() != 1) {
                    db.execSQL("insert into tb_book ( title, pirce, authors, publisher, category, thumbnail, isbn) values (?,?,?,?,?,?,?)", new String[]{ name, pr, authors, main_publisher, category, thumbnail, isbn});
                    Toast toast = Toast.makeText(BookInformationActivity.this, "등록 완료", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    Toast toast = Toast.makeText(BookInformationActivity.this, "이미 등록된 책입니다", Toast.LENGTH_SHORT);
                    toast.show();
                }
                db.close();
                finish();
            }
        });


    }

    @Override
    public void onClick(View v) {            // 텍스트나 아래이미지 눌렀을경우 텍스트 늘리기
        switch (v.getId()) {
            case R.id.see1:
                MoreText(introduction,see1);
                break;
            case R.id.Introduction:
                MoreText(introduction,see1);
                break;
            case R.id.see2:
                MoreText(Toc,see2);
                break;
            case R.id.Toc:
                MoreText(Toc,see2);
                break;
            case R.id.see3:
                MoreText(review,see3);
                break;
            case R.id.review:
                MoreText(review,see3);
                break;
            case R.id.see4:
                MoreText(inbook,see4);
                break;
            case R.id.inbook:
                MoreText(inbook,see4);
                break;
        }
    }


    class DataTask extends AsyncTask<String[], Void, String[]> { // 책 자료 파싱


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String[] s) {
            super.onPostExecute(s);
            Toc.setText(s[1]);
            introduction.setText(s[0]);
            review.setText(s[2]);
            inbook.setText((s[3]));
            inbook.setMaxLines(8);
            introduction.setMaxLines(8);
            Toc.setMaxLines(8);
            review.setMaxLines(8);
        }

        @Override
        protected String[] doInBackground(String[]... strings) {
            int own = 0;
            int inbo = 0;
            int revi = 0;

            try {
                org.jsoup.nodes.Document doc = Jsoup.connect(bookurl).get();
                doc.outputSettings().prettyPrint(false);
                Elements contents3 = doc.select("h3.tit");

                for (int i = 1; i <= contents3.size(); i++) {
                    if (contents3.get(i).ownText().equals("목차")) {
                        own = i;

                    }
                    if(contents3.get(i).ownText().equals("책 속으로")) {
                        inbo = i;

                    }
                    if(contents3.get(i).ownText().equals("출판사서평")) {
                        revi = i;

                    }
                    if(i == contents3.size() -1) {
                        break;
                    }
                }



                Elements contents = doc.select("p.desc").eq(0); // 책소개
                Elements contents4 = doc.select("p.desc").eq(own); // 목차
                Elements contents5 = doc.select("p.desc").eq(inbo); // 책 속으로
                Elements contents6 = doc.select("p.desc").eq(revi); // 출판사 서평
                String text5 = tagdel(contents5);
                String text = tagdel(contents);
                String text2 = tagdel(contents4);
                String text3 = tagdel(contents6);

                String text4[] = {text, text2, text3, text5};
                return text4;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }


    }

    class Statistics extends AsyncTask <String, Void, Document> {       // 독자 분석
        @Override
        protected void onPostExecute(Document document) {
            super.onPostExecute(document);
            ArrayList xVals = new ArrayList();
            ArrayList yVals1 = new ArrayList();
            if ( to2[2] == 0) {      // 독자 데이터가 없을경우
                exist.setVisibility(View.VISIBLE);
            } else {
                barChart.setVisibility(View.VISIBLE);
            }


            for(int i=0; i<to2.length; i++) {
                a += to2[i];
            }
            for (int z = 0; z < to2.length; z++) {
                if (to2[z] != 0) {
                    double d = (double) to2[z] / (double) a * 100 ;   // Percent
                    xVals.add(St[z]+Ge[z]);
                    yVals1.add(new BarEntry(z, Math.round(d)));
                }
            }
            BarDataSet set = new BarDataSet(yVals1, "A");
            BarData data = new BarData(set);
            Description des = barChart.getDescription();
            des.setEnabled(false);


            XAxis xAxis = barChart.getXAxis();
            xAxis.setGranularity(1f);
            xAxis.setGranularityEnabled(true);
            barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xVals));
            barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM); // 텍스트 밑으로 내리기
            barChart.getAxisLeft().setEnabled(false);
            data.setValueTextSize(15);
            xAxis.setTextSize(15);
            data.setValueFormatter(new MyValueFormatter());   //  percent와 소수점 제거
            set.setColors(ContextCompat.getColor(BookInformationActivity.this, R.color.red), // Bar 색상 변경
                    ContextCompat.getColor(BookInformationActivity.this, R.color.yellow),
                    ContextCompat.getColor(BookInformationActivity.this, R.color.green));
            barChart.setData(data);
            barChart.animateY(5000);

        }

        @Override
        protected Document doInBackground(String... zkzk) {

            try {
                URL url = new URL(zkzk[0]);
                DocumentBuilderFactory factory =
                        DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder(); //XML문서 빌더 객체를 생성
                Document document = builder.parse(new InputSource(url.openStream())); //XML문서를 파싱한다.
                document.getDocumentElement().normalize(); // 공백등 제거
                NodeList nodeList = document.getElementsByTagName("loanGrp");
                int length = nodeList.getLength();

                for (int i = 0; i < length; i++) {
                    org.w3c.dom.Element data = (org.w3c.dom.Element) nodeList.item(i);
                    NodeList ageList = data.getElementsByTagName("age");
                    Node text1 = (Node) ageList.item(0).getFirstChild();
                    String strTemp = text1.getNodeValue();
                    NodeList genderList = data.getElementsByTagName("gender");
                    String Str = genderList.item(0).getFirstChild().getNodeValue();
                    NodeList loanCntList = data.getElementsByTagName("loanCnt");
                    String Str2 = loanCntList.item(0).getFirstChild().getNodeValue();

                    Log.d("나이성별권수", "나이 :" + strTemp + "|" + "성별 :" + Str + "권수 :" + "|" + Str2);
                    int to = Integer.parseInt(Str2);
                    to2[i] = to;
                    St[i] = strTemp;
                    Ge[i] = Str;
                }
                for (int i = 0; i < St.length; i++) {
                    if (St[i].equals("청소년(14~19)")) {
                        St[i] = "10대";
                    }
                    if (St[i].equals("초등(8~13)")) {
                        St[i] = "초등";
                    }
                    if (Ge[i].equals("남성")) {
                        Ge[i] = "(남)";
                    }
                        if (Ge[i].equals("여성")) {
                            Ge[i] = "(여)";
                        }
                    }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("home", e.getMessage());
            }

            return null;
        }


    }


    class Background extends AsyncTask<String, Integer, String> {
        ProgressDialog asyncDialog = new ProgressDialog(BookInformationActivity.this);

        @Override
        protected void onPreExecute() {
            asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            asyncDialog.setMessage("책 데이터를 가져오는 중입니다. . ");
            asyncDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... isurl) {   // 장르 구분 파싱
            try {

                org.jsoup.nodes.Document doc = Jsoup.connect(isurl[0]).get();
                Elements contents = doc.select("div#category");
                String text = contents.text();
                String text3 = text.substring(text.indexOf("") , text.indexOf("★ 이분야 새키핑알림 받기"));
                String text2 = text3.substring(text.indexOf("") , text.indexOf(">"));
                if (TextUtils.isEmpty(text2)) {
                    asyncDialog.dismiss();
                    return text2;
                } else {
                    asyncDialog.dismiss();
                    return text3;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

    }


    class MyBarDataSet extends BarDataSet {
        public MyBarDataSet(List<BarEntry> yVals, String label) {
            super(yVals, label);
        }

        @Override
        public int getColor(int index) {
            if(getEntryForIndex(index).getY() < 140)
                return mColors.get(0);
            else if(getEntryForIndex(index).getY() > 145)
                return mColors.get(1);
            else if(getEntryForIndex(index).getY() > 130)
                return mColors.get(2);
            else return mColors.get(3);
        }

    }

    public class MyValueFormatter extends ValueFormatter {

        private DecimalFormat mFormat;

        public MyValueFormatter() {
            mFormat = new DecimalFormat("#");
        }

        @Override
        public String getFormattedValue(float value) {
            return mFormat.format(value) + "%";
        }
    }

    public void MoreText(TextView textView, ImageView imageView) {
        if (textView.getMaxLines() == 8) {
            imageView.setImageResource(R.drawable.baseline_keyboard_arrow_up_black_18dp);
            textView.setEllipsize(null);
            textView.setMaxLines(1000);
        } else {
            imageView.setImageResource(R.drawable.baseline_keyboard_arrow_down_black_18dp);
            textView.setEllipsize(TextUtils.TruncateAt.END);
            textView.setMaxLines(8);
        }

    }

    public String tagdel(Elements contents) {          // 공백을 살려야해서 이런식으로함
        String text = contents.html().replace("<br>", "\n")
                .replace("<span class=\"ellipsis\">...</span><span class=\"hide_desc\">", "")
                .replace("&nbsp;", "")
                .replace("</span>", "")
                .replace("&lt;/p&gt; &lt;p class=\"se-text-paragraph se-text-paragraph-align-left \" id=\"SE-439BBD9D-4A27-4933-A294-A30419FFC0C3\" style=\"line-height: 1.8;\"&gt; &lt;/p&gt; &lt;p class=\"se-text-paragraph se-text-paragraph-align-left \" id=\"SE-F459159B-AA1C-4E9C-A1BC-F6B1F49AF176\" style=\"line-height: 1.8;\"&gt;&lt;/p&gt;", "")
                .replace("&lt;/p&gt;", "")
                .replace("&lt;p&gt;", "")
                .replace("&lt;","")
                .replace("&gt;","")
                .replace("<b>" ,"")
                .replace("</b>", "");
        return text;
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


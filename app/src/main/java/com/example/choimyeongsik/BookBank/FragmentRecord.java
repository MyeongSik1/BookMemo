package com.example.choimyeongsik.BookBank;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daasuu.cat.CountAnimationTextView;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentRecord extends Fragment {
    TextView textView;
    int fiction,Health,Literature,Economy,Linguistics,sociology,Humanities,History,infant,child,
            elementary,elementary2,Adolescent,nature,Travel,Art,essay,comic,Assum,
            examination,Religion,Hobby,Computer;
    PieChart pieChart;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.fragment_record, container, false);
        CountAnimationTextView countAnimationTextView = (CountAnimationTextView)rootview.findViewById(R.id.count_animation_textview);
        pieChart = (PieChart)rootview.findViewById(R.id.piechart);
        pieChart.setUsePercentValues(true);
      /*  pieChart.getDescription().setEnabled(false);
       pieChart.setExtraOffsets(5,10,5,5);
        pieChart.setDragDecelerationFrictionCoef(0.95f);
       pieChart.setDrawHoleEnabled(false);
        pieChart.setHoleColor(Color.WHITE);
       pieChart.setTransparentCircleRadius(61f); */







        Cursor cs = null;
        BookDatabase helper = new BookDatabase(getContext());
        SQLiteDatabase db = helper.getWritableDatabase();
        cs = db.rawQuery("select * from  tb_book", null);

        int sd = 0;
        int count = 0;
        while (cs.moveToNext()) {
            String as = cs.getString(cs.getColumnIndex("pirce"));
            if (as != null) {
                as = as.replaceAll(",", "");
                int a = Integer.parseInt(as);
                sd = sd + a;
            }
            String category = cs.getString(cs.getColumnIndex("category"));
            if (category != null) {
                category = category.replaceAll(" ", "").trim();
                if (category.equals("건강/미용")) {
                    Health += 1;
                }
                if (category.equals("소설")) {
                    fiction += 1;
                }
                if (category.equals("문학(시,에세이 등)")) {
                    Literature += 1;
                }
                if (category.equals("경제/경영")) {
                    Economy += 1;
                }
                if (category.equals("어학(외국어)")) {
                    Linguistics += 1;
                }
                if (category.equals("사회학")) {
                    sociology += 1;
                }
                if (category.equals("인문학")) {
                    Humanities += 1;

                }
                if (category.equals("역사/문화")) {
                    History += 1;

                }
                if (category.equals("유아(0∼3세)")) {
                    infant += 1;
                }
                if (category.equals("아동(4∼6세)")) {
                    child += 1;
                }
                if (category.equals("초등1∼3학년")) {
                    elementary += 1;

                }
                if (category.equals("초등4∼6학년 ")) {
                    elementary2 += 1;
                }
                if (category.equals("청소년")) {
                    Adolescent += 1;
                }
                if (category.equals("자연/과학")) {
                    nature += 1;
                }
                if (category.equals("여행")) {
                    Travel += 1;
                }
                if (category.equals("예술")) {
                    Art += 1;
                }
                if (category.equals("논술/면접")) {
                    essay += 1;
                }
                if (category.equals("만화")) {
                    comic += 1;
                }
                if (category.equals("가정/생활")) {
                    Assum += 1;
                }
                if (category.equals("수험서/자격증")) {
                    examination += 1;
                }
                if (category.equals("종교")) {
                    Religion += 1;
                }
                if (category.equals("취미")) {
                    Hobby += 1;
                }
                if (category.equals("컴퓨터")) {
                    Computer += 1;
                }
            }

            }
        ArrayList <PieEntry> yValues = new ArrayList<PieEntry>();
        if(Health != 0) yValues.add(new PieEntry(Health, "건강/미용"));
        if(fiction != 0) yValues.add(new PieEntry(fiction, "소설"));
        if(Literature != 0) yValues.add(new PieEntry(Literature,"문학"));
        if(Economy != 0)   yValues.add(new PieEntry(Economy,"경제/경영"));
        if(Linguistics != 0)  yValues.add(new PieEntry(Linguistics,"어학"));
        if(sociology != 0)  yValues.add(new PieEntry(sociology,"사회"));
        if(Humanities != 0)    yValues.add(new PieEntry(Humanities,"인문학"));
        if(History != 0)   yValues.add(new PieEntry(History,"역사/문화"));
        if(infant != 0)   yValues.add(new PieEntry(infant ,"유아"));
        if(child != 0)   yValues.add(new PieEntry(child,"아동"));
        if(elementary != 0) yValues.add(new PieEntry(elementary ,"초등1∼3학년"));
        if(elementary2 != 0)  yValues.add(new PieEntry(elementary2 ,"초등4∼6학년"));
        if(Adolescent != 0)   yValues.add(new PieEntry(Adolescent,"청소년"));
        if(nature != 0)  yValues.add(new PieEntry(nature,"자연/과학"));
        if(Travel != 0)   yValues.add(new PieEntry(Travel,"여행"));
        if(Art != 0)   yValues.add(new PieEntry(Art,"예술"));
        if(essay != 0)   yValues.add(new PieEntry(essay ,"논술/면접"));
        if(comic != 0)   yValues.add(new PieEntry(comic,"만화"));
        if(Assum != 0)   yValues.add(new PieEntry(Assum,"가정/생활"));
        if(examination != 0) yValues.add(new PieEntry(examination ,"수험서/자격증"));
        if(Religion != 0)  yValues.add(new PieEntry(Religion,"종교"));
        if(Hobby != 0)  yValues.add(new PieEntry(Hobby,"취미"));
        if(Computer != 0) yValues.add(new PieEntry(Computer,"컴퓨터"));
        Description description = new Description();
        description.setText(""); //라벨
        description.setTextSize(15);
        pieChart.setDescription(description);
        PieDataSet dataSet = new PieDataSet(yValues,"");
        dataSet.setValueFormatter(new PercentFormatter(pieChart));
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelTextSize(15f);
        Legend i = pieChart.getLegend();
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        i.setTextSize(15f);




        dataSet.setColors(ColorTemplate.PASTEL_COLORS);

        PieData data = new PieData((dataSet));

        data.setValueTextSize(13f);
        data.setValueTextColor(Color.YELLOW);

        pieChart.setData(data);








        countAnimationTextView
                .setDecimalFormat(new DecimalFormat("###,###,###"))
                .setAnimationDuration(1000)
                .countAnimation(0, sd);
        return rootview;
    }

    public class MyValueFormatter implements IValueFormatter {

        private DecimalFormat mFormat;

        public MyValueFormatter() {
            mFormat = new DecimalFormat("###,###,##0.0"); // use one decimal
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            // write your logic here
            return mFormat.format(value) + " $";
        }
    }
}




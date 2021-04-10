package com.example.choimyeongsik.BookBank.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
//
public class BookDatabase extends SQLiteOpenHelper {
    private Context mContext;
    public static String tableName = "container";
    public static final int DATABASE_VERSION = 9 ;

    public BookDatabase(Context context){
        super(context,tableName, null, DATABASE_VERSION);
        mContext = context;

    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String bookSQL = "create table tb_book" +
                "(_id integer PRIMARY KEY autoincrement," +
                "title," +
                "pirce," +
                "authors," +
                "publisher," +
                "category," +
                "thumbnail," +
                "isbn," +
                "name," +
                "image," + "BLOB," +
                "contents)";

        sqLiteDatabase.execSQL(bookSQL);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if(newVersion == DATABASE_VERSION) {
            sqLiteDatabase.execSQL("drop table tb_book");
            onCreate(sqLiteDatabase);
        }

    }






}

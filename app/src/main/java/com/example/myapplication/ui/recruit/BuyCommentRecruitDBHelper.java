package com.example.myapplication.ui.recruit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BuyCommentRecruitDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "comments.db";
    private static final int DATABASE_VERSION = 2;  // 데이터베이스 버전 증가
    public static final String TABLE_COMMENTS = "comments";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PROMISE = "promise";
    public static final String COLUMN_ACCOUNT = "account";
    public static final String COLUMN_BANK = "bank";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_PLACE = "place";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_DIVISION = "division";
    public static final String COLUMN_PAYMENT = "payment";

    private static final String DATABASE_CREATE = "create table "
            + TABLE_COMMENTS + "(" + COLUMN_ID
            + " integer primary key autoincrement, "
            + COLUMN_PROMISE + " text not null, "
            + COLUMN_ACCOUNT + " text not null, "
            + COLUMN_BANK + " text not null, "
            + COLUMN_DATE + " text not null, "
            + COLUMN_TIME + " text not null, "
            + COLUMN_PLACE + " text not null, "
            + COLUMN_PRICE + " text not null, "
            + COLUMN_DIVISION + " text not null, "
            + COLUMN_PAYMENT + " text not null);";

    public BuyCommentRecruitDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMENTS);
        onCreate(db);
    }

    public void insertComment(String promise, String account, String bank, String date, String time, String place, String price, String division, String payment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PROMISE, promise);
        values.put(COLUMN_ACCOUNT, account);
        values.put(COLUMN_BANK, bank);
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_TIME, time);
        values.put(COLUMN_PLACE, place);
        values.put(COLUMN_PRICE, price);
        values.put(COLUMN_DIVISION, division);
        values.put(COLUMN_PAYMENT, payment);

        db.insert(TABLE_COMMENTS, null, values);
    }

    public Cursor getAllComments() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_COMMENTS, null, null, null, null, null, null);
    }
}

// RecruitPlusDBHelper.java
package com.example.myapplication.ui.recruit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RecruitPlusDBHelper extends SQLiteOpenHelper {

    // 데이터베이스 정보
    private static final String DATABASE_NAME = "recruitPlus.db";
    private static final int DATABASE_VERSION = 1;

    // 테이블 이름
    private static final String TABLE_NAME = "RecruitPlus";

    // 테이블 컬럼
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_TIME = "time";
    private static final String COLUMN_PLACE = "place";
    private static final String COLUMN_BANK = "bank";
    private static final String COLUMN_ACCOUNT = "account";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_DIVISION = "division";
    private static final String COLUMN_PAYMENT = "payment";
    private static final String COLUMN_PROMISE = "promise"; // 추가

    // 테이블 생성 SQL 문
    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_DATE + " TEXT, " +
                    COLUMN_TIME + " TEXT, " +
                    COLUMN_PLACE + " TEXT, " +
                    COLUMN_BANK + " TEXT, " +
                    COLUMN_ACCOUNT + " TEXT, " +
                    COLUMN_PRICE + " INTEGER, " +  // 정수 형식으로 변경
                    COLUMN_DIVISION + " INTEGER, " +  // 정수 형식으로 변경
                    COLUMN_PAYMENT + " INTEGER, " +  // 정수 형식으로 변경
                    COLUMN_PROMISE + " TEXT);"; // 추가

    public RecruitPlusDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // 데이터 삽입
    public boolean insertData(String date, String time, String place, String bank, String account, int price, int division, int payment, String promise) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_DATE, date);
        contentValues.put(COLUMN_TIME, time);
        contentValues.put(COLUMN_PLACE, place);
        contentValues.put(COLUMN_BANK, bank);
        contentValues.put(COLUMN_ACCOUNT, account);
        contentValues.put(COLUMN_PRICE, price);
        contentValues.put(COLUMN_DIVISION, division);
        contentValues.put(COLUMN_PAYMENT, payment);
        contentValues.put(COLUMN_PROMISE, promise);
        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }

    // 데이터 조회
    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    // 데이터 업데이트
    public boolean updateData(String id, String date, String time, String place, String bank, String account, int price, int division, int payment, String promise) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_DATE, date);
        contentValues.put(COLUMN_TIME, time);
        contentValues.put(COLUMN_PLACE, place);
        contentValues.put(COLUMN_BANK, bank);
        contentValues.put(COLUMN_ACCOUNT, account);
        contentValues.put(COLUMN_PRICE, price);
        contentValues.put(COLUMN_DIVISION, division);
        contentValues.put(COLUMN_PAYMENT, payment);
        contentValues.put(COLUMN_PROMISE, promise);
        int result = db.update(TABLE_NAME, contentValues, COLUMN_ID + " = ?", new String[]{id});
        return result > 0;
    }

    // 데이터 삭제
    public Integer deleteData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{id});
    }
}

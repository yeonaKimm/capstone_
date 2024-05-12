package com.example.myapplication.ui.recruit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class TaxiRecruitDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "TaxiRecruitdb";
    private static final int DATABASE_VERSION = 1;

    public TaxiRecruitDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE taxis (id INTEGER PRIMARY KEY, date TEXT, time TEXT, people INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS taxis");
        onCreate(db);
    }

    // 새로운 게시글을 추가하는 메서드 외부에서 호출할 수 있도록 public으로 지정합니다.
    public void insertTaxi(String date, String time, int people) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("date", date);
        values.put("time", time);
        values.put("people", people);
        db.insert("taxis", null, values);
        db.close();
    }

    // 모든 게시글을 조회하여 리스트로 반환하는 메서드입니다. 외부에서 호출할 수 있도록 public으로 지정합니다.
    public List<String> getAllTaxis() {
        List<String> taxisList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM taxis", null);

        int dateIndex = cursor.getColumnIndex("date");
        int timeIndex = cursor.getColumnIndex("time");
        int peopleIndex = cursor.getColumnIndex("people");

        if (dateIndex != -1 && timeIndex != -1 && peopleIndex != -1  && cursor.moveToFirst()) {
            do {
                String date = cursor.getString(dateIndex);
                String time = cursor.getString(timeIndex);
                int people = cursor.getInt(peopleIndex);
                taxisList.add("\n" + "탑승날짜: " + date  + "\n탑승시간: " + time  + "\n탑승인원: " + people  + "\n");
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return taxisList;
    }
}

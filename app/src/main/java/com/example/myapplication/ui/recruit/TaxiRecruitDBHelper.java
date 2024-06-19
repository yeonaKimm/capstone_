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
    private static final int DATABASE_VERSION = 2;

    public TaxiRecruitDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE taxis (id INTEGER PRIMARY KEY, date TEXT, time TEXT, people INTEGER, start_location TEXT, end_location TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE taxis ADD COLUMN start_location TEXT");
            db.execSQL("ALTER TABLE taxis ADD COLUMN end_location TEXT");
        }
    }

    public void insertTaxi(String date, String time, int people, String startLocation, String endLocation) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("date", date);
        values.put("time", time);
        values.put("people", people);
        values.put("start_location", startLocation);
        values.put("end_location", endLocation);
        db.insert("taxis", null, values);
        db.close();
    }

    public List<TaxiList_Item_Recruit> getAllTaxis() {
        List<TaxiList_Item_Recruit> taxisList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM taxis", null);

        int dateIndex = cursor.getColumnIndex("date");
        int timeIndex = cursor.getColumnIndex("time");
        int peopleIndex = cursor.getColumnIndex("people");
        int startLocationIndex = cursor.getColumnIndex("start_location");
        int endLocationIndex = cursor.getColumnIndex("end_location");

        if (dateIndex != -1 && timeIndex != -1 && peopleIndex != -1 && startLocationIndex != -1 && endLocationIndex != -1 && cursor.moveToFirst()) {
            do {
                String date = cursor.getString(dateIndex);
                String time = cursor.getString(timeIndex);
                int people = cursor.getInt(peopleIndex);
                String startLocation = cursor.getString(startLocationIndex);
                String endLocation = cursor.getString(endLocationIndex);
                taxisList.add(new TaxiList_Item_Recruit(date, time, people, startLocation, endLocation));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return taxisList;
    }
}

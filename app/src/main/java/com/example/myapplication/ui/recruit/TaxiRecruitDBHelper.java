package com.example.myapplication.ui.recruit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TaxiRecruitDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "TaxiRecruitdb";
    private static final int DATABASE_VERSION = 3; // Incremented version number

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
        if (oldVersion < 3) {
            // Handle future upgrades if needed
        }
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle downgrade if needed
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

        // 모집중과 마감된 항목을 분리하여 정렬
        List<TaxiList_Item_Recruit> recruiting = new ArrayList<>();
        List<TaxiList_Item_Recruit> closed = new ArrayList<>();
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());

        for (TaxiList_Item_Recruit item : taxisList) {
            try {
                Date tripTime = dateFormat.parse(item.getDate() + " " + item.getTime());
                if (tripTime != null && tripTime.before(now)) {
                    closed.add(item);
                } else {
                    recruiting.add(item);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        // 정렬: 내림차순
        Collections.sort(recruiting, new Comparator<TaxiList_Item_Recruit>() {
            @Override
            public int compare(TaxiList_Item_Recruit o1, TaxiList_Item_Recruit o2) {
                try {
                    Date date1 = dateFormat.parse(o1.getDate() + " " + o1.getTime());
                    Date date2 = dateFormat.parse(o2.getDate() + " " + o2.getTime());
                    return date2.compareTo(date1);
                } catch (ParseException e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        });

        Collections.sort(closed, new Comparator<TaxiList_Item_Recruit>() {
            @Override
            public int compare(TaxiList_Item_Recruit o1, TaxiList_Item_Recruit o2) {
                try {
                    Date date1 = dateFormat.parse(o1.getDate() + " " + o1.getTime());
                    Date date2 = dateFormat.parse(o2.getDate() + " " + o2.getTime());
                    return date2.compareTo(date1);
                } catch (ParseException e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        });

        // 모집중인 리스트 뒤에 마감된 리스트를 추가
        recruiting.addAll(closed);

        return recruiting;
    }
}

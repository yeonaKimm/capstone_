package com.example.myapplication.ui.recruit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class BuyRecruitDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "BuyRecruitdb";
    private static final int DATABASE_VERSION = 1;

    public BuyRecruitDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE buys (id INTEGER PRIMARY KEY, topic TEXT, price INTEGER, people INTEGER, content TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS buys");
        onCreate(db);
    }

    public void insertBuy(String topic, int price, int people, String content) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("topic", topic);
        values.put("price", price);
        values.put("people", people);
        values.put("content", content);
        db.insert("buys", null, values);
        db.close();
    }

    // 모든 게시글을 조회하여 리스트로 반환하는 메서드입니다. 외부에서 호출할 수 있도록 public으로 지정합니다.
    public List<BuyList_Item_Recruit> getAllBuys() {
        List<BuyList_Item_Recruit> buysList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM buys", null);

        int topicIndex = cursor.getColumnIndex("topic");
        int priceIndex = cursor.getColumnIndex("price");
        int peopleIndex = cursor.getColumnIndex("people");
        int contentIndex = cursor.getColumnIndex("content");

        if (topicIndex != -1 && priceIndex != -1 && peopleIndex != -1 && contentIndex != -1 && cursor.moveToFirst()) {
            do {
                String topic = cursor.getString(topicIndex);
                String content = cursor.getString(contentIndex);
                int price = cursor.getInt(priceIndex);
                int people = cursor.getInt(peopleIndex);
                buysList.add(new BuyList_Item_Recruit(topic, content, price, people));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return buysList;
    }
}

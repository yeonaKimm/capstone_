package com.example.myapplication.ui.recruit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class ShareRecruitDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ShareRecruitdb";
    private static final int DATABASE_VERSION = 1;

    public ShareRecruitDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE shares (id INTEGER PRIMARY KEY, topic TEXT, place TEXT, quantity INTEGER, content TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS shares");
        onCreate(db);
    }

    // 새로운 게시글을 추가하는 메서드 외부에서 호출할 수 있도록 public으로 지정합니다.
    public void insertShare(String topic, String place, int quantity, String content) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("topic", topic);
        values.put("place", place);
        values.put("quantity", quantity);
        values.put("content", content);
        db.insert("shares", null, values);
        db.close();
    }

    // 모든 게시글을 조회하여 리스트로 반환하는 메서드입니다. 외부에서 호출할 수 있도록 public으로 지정합니다.
    public List<String> getAllshares() {
        List<String> sharesList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM shares", null);

        int topicIndex = cursor.getColumnIndex("topic");
        int placeIndex = cursor.getColumnIndex("place");
        int quantityIndex = cursor.getColumnIndex("quantity");
        int contentIndex = cursor.getColumnIndex("content");

        if (topicIndex != -1 && placeIndex != -1 && quantityIndex != -1 && contentIndex != -1 && cursor.moveToFirst()) {
            do {
                String topic = cursor.getString(topicIndex);
                String place = cursor.getString(placeIndex);
                int quantity = cursor.getInt(quantityIndex);
                String content = cursor.getString(contentIndex);
                sharesList.add("\n" + "제목: " + topic  + "\n직거래장소: " + place  + "\n수량: " + quantity + "\n설명: " + content + "\n");
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return sharesList;
    }
}

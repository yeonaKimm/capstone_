package com.example.myapplication.ui.recruit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
        db.execSQL("CREATE TABLE shares (id INTEGER PRIMARY KEY, topic TEXT, place TEXT, quantity INTEGER, content TEXT, imageUri TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS shares");
        onCreate(db);
    }

    // 새로운 게시글을 추가하는 메서드 외부에서 호출할 수 있도록 public으로 지정합니다.
    public void insertShare(String topic, String place, int quantity, String content, String imageUri) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("topic", topic);
        values.put("place", place);
        values.put("quantity", quantity);
        values.put("content", content);
        values.put("imageUri", imageUri);
        // Log the content values to ensure they are set correctly
        Log.d("ShareRecruitDBHelper", "Inserting Share: " + values.toString());

        long result = db.insert("shares", null, values);
        if (result == -1) {
            Log.e("ShareRecruitDBHelper", "Failed to insert row");
        } else {
            Log.d("ShareRecruitDBHelper", "Successfully inserted row with ID: " + result);
        }

        db.close();
    }

    // 모든 게시글을 조회하여 리스트로 반환하는 메서드입니다. 외부에서 호출할 수 있도록 public으로 지정합니다.
    public List<ShareList_Item_Recruit> getAllshares() {
        List<ShareList_Item_Recruit> sharesList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM shares", null);

        int topicIndex = cursor.getColumnIndex("topic");
        int placeIndex = cursor.getColumnIndex("place");
        int quantityIndex = cursor.getColumnIndex("quantity");
        int contentIndex = cursor.getColumnIndex("content");
        int imageUriIndex = cursor.getColumnIndex("imageUri");

        if (topicIndex != -1 && contentIndex != -1 && placeIndex != -1 && quantityIndex != -1 && imageUriIndex != -1 && cursor.moveToFirst()) {
            do {
                String topic = cursor.getString(topicIndex);
                String content = cursor.getString(contentIndex);
                String place = cursor.getString(placeIndex);
                int quantity = cursor.getInt(quantityIndex);
                String imageUri = cursor.getString(imageUriIndex);
                sharesList.add(new ShareList_Item_Recruit(topic, content, place, quantity, imageUri));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return sharesList;
    }
}

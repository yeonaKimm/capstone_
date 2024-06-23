package com.example.myapplication.ui.recruit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class BuyRecruitDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "BuyRecruit.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "buy_recruit";
    private static final String COL_ID = "id";
    private static final String COL_TOPIC = "topic";
    private static final String COL_CONTENT = "content";
    private static final String COL_PRICE = "price";
    private static final String COL_PEOPLE = "people";
    private static final String COL_IMAGE_URI = "image_uri";
    private static final String COL_USER_ID = "user_id";
    private static final String COL_CLOSED = "closed";

    public BuyRecruitDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TOPIC + " TEXT, " +
                COL_CONTENT + " TEXT, " +
                COL_PRICE + " INTEGER, " +
                COL_PEOPLE + " INTEGER, " +
                COL_IMAGE_URI + " TEXT, " +
                COL_USER_ID + " TEXT, " +
                COL_CLOSED + " INTEGER DEFAULT 0)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertBuy(String topic, int price, int people, String content, String imageUri, String userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_TOPIC, topic);
        contentValues.put(COL_PRICE, price);
        contentValues.put(COL_PEOPLE, people);
        contentValues.put(COL_CONTENT, content);
        contentValues.put(COL_IMAGE_URI, imageUri);
        contentValues.put(COL_USER_ID, userId);
        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }

    public List<BuyList_Item_Recruit> getAllBuys() {
        List<BuyList_Item_Recruit> buyList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        if (res.moveToFirst()) {
            do {
                int id = res.getInt(res.getColumnIndexOrThrow(COL_ID));
                String topic = res.getString(res.getColumnIndexOrThrow(COL_TOPIC));
                String content = res.getString(res.getColumnIndexOrThrow(COL_CONTENT));
                int price = res.getInt(res.getColumnIndexOrThrow(COL_PRICE));
                int people = res.getInt(res.getColumnIndexOrThrow(COL_PEOPLE));
                String imageUri = res.getString(res.getColumnIndexOrThrow(COL_IMAGE_URI));
                String userId = res.getString(res.getColumnIndexOrThrow(COL_USER_ID));
                boolean closed = res.getInt(res.getColumnIndexOrThrow(COL_CLOSED)) == 1;
                buyList.add(new BuyList_Item_Recruit(id, topic, content, price, people, imageUri, closed, userId));
            } while (res.moveToNext());
        }
        res.close();
        return buyList;
    }

    public void updateRecruitStatus(int id, boolean isClosed) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_CLOSED, isClosed ? 1 : 0);
        db.update(TABLE_NAME, contentValues, COL_ID + " = ?", new String[]{String.valueOf(id)});
    }
}

package com.example.myapplication.ui.recruit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class BuyRecruitDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "buyrecruit.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "buyrecruits";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TOPIC = "topic";
    private static final String COLUMN_CONTENT = "content";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_PEOPLE = "people";
    private static final String COLUMN_IMAGE_URI = "image_uri";
    private static final String COLUMN_CLOSED = "closed";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_TIMESTAMP = "timestamp";

    public BuyRecruitDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_TOPIC + " TEXT, "
                + COLUMN_CONTENT + " TEXT, "
                + COLUMN_PRICE + " INTEGER, "
                + COLUMN_PEOPLE + " INTEGER, "
                + COLUMN_IMAGE_URI + " TEXT, "
                + COLUMN_CLOSED + " INTEGER, "
                + COLUMN_USER_ID + " TEXT, "
                + COLUMN_TIMESTAMP + " INTEGER)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long insertBuy(String topic, String content, int price, int people, String imageUri, String userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TOPIC, topic);
        values.put(COLUMN_CONTENT, content);
        values.put(COLUMN_PRICE, price);
        values.put(COLUMN_PEOPLE, people);
        values.put(COLUMN_IMAGE_URI, imageUri);
        values.put(COLUMN_USER_ID, userId);
        values.put(COLUMN_TIMESTAMP, System.currentTimeMillis());
        long result = db.insert(TABLE_NAME, null, values);
        db.close();
        return result;
    }

    public List<BuyList_Item_Recruit> getAllBuys() {
        List<BuyList_Item_Recruit> buysList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, COLUMN_TIMESTAMP + " DESC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String topic = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TOPIC));
                String content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT));
                int price = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRICE));
                int people = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PEOPLE));
                String imageUri = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_URI));
                boolean isClosed = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CLOSED)) != 0;
                String userId = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_ID));
                long timestamp = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_TIMESTAMP));

                buysList.add(new BuyList_Item_Recruit(id, topic, content, price, people, imageUri, isClosed, userId, timestamp));
            } while (cursor.moveToNext());

            cursor.close();
        }

        return buysList;
    }

    public void updateRecruitStatus(int id, boolean isClosed) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CLOSED, isClosed ? 1 : 0);
        db.update(TABLE_NAME, values, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }
}

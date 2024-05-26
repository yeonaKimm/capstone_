package com.example.myapplication.ui.Login;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "user.db";
    private static final String TABLE_NAME = "user_table";
    private static final String COL_1 = "ID";
    private static final String COL_2 = "EMAIL";
    private static final String COL_3 = "NAME";
    private static final String COL_4 = "GENDER";
    private static final String COL_5 = "AGE_RANGE";
    private static final String COL_6 = "BIRTHYEAR";
    private static final String COL_7 = "NICKNAME";
    private static final String COL_8 = "PROFILE_PICTURE";
    private static final String COL_9 = "RANK_ID";
    private static final String COL_10 = "LATITUDE";
    private static final String COL_11 = "LONGITUDE";

    private static final String RANK_TABLE_NAME = "rank_table";
    private static final String RANK_COL_1 = "RANK_ID";
    private static final String RANK_COL_2 = "RANK_NAME";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 유저 테이블 생성
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                "ID TEXT PRIMARY KEY, " +
                "EMAIL TEXT NOT NULL, " +
                "NAME TEXT NOT NULL, " +
                "GENDER TEXT NOT NULL, " +
                "AGE_RANGE TEXT NOT NULL, " +
                "BIRTHYEAR TEXT NOT NULL, " +
                "NICKNAME TEXT NOT NULL, " +
                "PROFILE_PICTURE TEXT NOT NULL, " +
                "RANK_ID TEXT NOT NULL, " +
                "LATITUDE REAL, " +
                "LONGITUDE REAL, " +
                "FOREIGN KEY (RANK_ID) REFERENCES " + RANK_TABLE_NAME + "(" + RANK_COL_1 + "))");

        // 회원 등급 테이블 생성 (예시)
        db.execSQL("CREATE TABLE " + RANK_TABLE_NAME + " (" +
                "RANK_ID TEXT PRIMARY KEY, " +
                "RANK_NAME TEXT NOT NULL)");

        // 예시 데이터 삽입
        ContentValues contentValues = new ContentValues();
        contentValues.put(RANK_COL_1, "1");
        contentValues.put(RANK_COL_2, "Basic");
        db.insert(RANK_TABLE_NAME, null, contentValues);
        contentValues.put(RANK_COL_1, "2");
        contentValues.put(RANK_COL_2, "Premium");
        db.insert(RANK_TABLE_NAME, null, contentValues);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + RANK_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertUser(String id, String email, String name, String gender, String ageRange, String birthyear, String nickname, String profilePicture, String rankId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, id);
        contentValues.put(COL_2, email);
        contentValues.put(COL_3, name);
        contentValues.put(COL_4, gender);
        contentValues.put(COL_5, ageRange);
        contentValues.put(COL_6, birthyear);
        contentValues.put(COL_7, nickname);
        contentValues.put(COL_8, profilePicture);
        contentValues.put(COL_9, rankId);
        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }

    public String getUserNickname(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT NICKNAME FROM " + TABLE_NAME + " WHERE ID = ?", new String[]{userId});
        if (res != null && res.moveToFirst()) {
            int colIndex = res.getColumnIndex(COL_7);
            if (colIndex != -1) {
                String nickname = res.getString(colIndex);
                res.close();
                return nickname;
            }
        }
        if (res != null) {
            res.close();
        }
        return null;
    }

    public boolean updateUserNickname(String userId, String nickname) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_7, nickname);
        int result = db.update(TABLE_NAME, contentValues, "ID = ?", new String[]{userId});
        return result > 0;
    }

    public boolean deleteUser(String userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_NAME, "ID = ?", new String[]{userId});
        return result > 0;
    }

    public boolean updateUserLocation(String userId, double latitude, double longitude) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_10, latitude);
        contentValues.put(COL_11, longitude);
        int result = db.update(TABLE_NAME, contentValues, "ID = ?", new String[]{userId});
        return result > 0;
    }
}

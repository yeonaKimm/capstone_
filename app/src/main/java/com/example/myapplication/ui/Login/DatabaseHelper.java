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
    private static final int DATABASE_VERSION = 2; // 데이터베이스 버전을 올립니다.

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                COL_1 + " INTEGER PRIMARY KEY, " +
                COL_2 + " TEXT, " +
                COL_3 + " TEXT, " +
                COL_4 + " TEXT, " +
                COL_5 + " TEXT, " +
                COL_6 + " TEXT, " +
                COL_7 + " TEXT, " +
                COL_8 + " TEXT, " +
                COL_9 + " TEXT, " +
                COL_10 + " REAL, " +
                COL_11 + " REAL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COL_10 + " REAL");
            db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COL_11 + " REAL");
        }
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

    public boolean updateUserNickname(String id, String nickname) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_7, nickname);
        int result = db.update(TABLE_NAME, contentValues, "ID = ?", new String[]{id});
        return result > 0;
    }

    public boolean updateUserLocation(String id, double latitude, double longitude) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_10, latitude);
        contentValues.put(COL_11, longitude);
        int result = db.update(TABLE_NAME, contentValues, "ID = ?", new String[]{id});
        return result > 0;
    }

    public String getUserNickname(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT NICKNAME FROM " + TABLE_NAME + " WHERE ID = ?", new String[]{userId});
        if (res != null && res.moveToFirst()) {
            int colIndex = res.getColumnIndex(COL_7);
            if (colIndex >= 0) {
                String nickname = res.getString(colIndex);
                res.close();
                return nickname;
            }
            res.close();
        }
        return null;
    }

    public boolean isUserExists(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE ID = ?", new String[]{id});
        boolean exists = res.getCount() > 0;
        res.close();
        return exists;
    }

    public void deleteUser(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "ID = ?", new String[]{id});
    }
}

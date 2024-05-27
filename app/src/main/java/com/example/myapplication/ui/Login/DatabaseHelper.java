package com.example.myapplication.ui.Login;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (ID TEXT PRIMARY KEY, EMAIL TEXT, NAME TEXT, GENDER TEXT, AGE_RANGE TEXT, BIRTHYEAR TEXT, NICKNAME TEXT, PROFILE_PICTURE TEXT NOT NULL, RANK_ID TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
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
        contentValues.put(COL_8, profilePicture != null ? profilePicture : "default_profile_picture_url");
        contentValues.put(COL_9, rankId);
        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }

    public String getUserNickname(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT NICKNAME FROM " + TABLE_NAME + " WHERE ID = ?", new String[]{userId});
        if (res != null && res.moveToFirst()) {
            int colIndex = res.getColumnIndex("NICKNAME");
            if (colIndex != -1) {
                String nickname = res.getString(colIndex);
                res.close();
                return nickname;
            } else {
                Log.e("DatabaseHelper", "Column 'NICKNAME' not found in the database.");
            }
        } else {
            Log.e("DatabaseHelper", "Cursor is null or empty.");
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
        contentValues.put("LATITUDE", latitude);
        contentValues.put("LONGITUDE", longitude);
        int result = db.update(TABLE_NAME, contentValues, "ID = ?", new String[]{userId});
        return result > 0;
    }

    public boolean isUserExists(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT 1 FROM " + TABLE_NAME + " WHERE ID = ?", new String[]{userId});
        boolean exists = (res != null && res.getCount() > 0);
        if (res != null) {
            res.close();
        }
        return exists;
    }
}

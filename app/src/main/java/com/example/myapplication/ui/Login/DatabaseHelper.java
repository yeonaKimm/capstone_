package com.example.myapplication.ui.Login;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "User.db";
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
    private static final String COL_12 = "RADIUS";
    private static final String COL_13 = "MAX_DISTANCE";
    private static final String COL_14 = "RATING";
    private static final String COL_15 = "RECENT_REVIEW";
    private static final String COL_16 = "REVIEW_DATE";

    private static final String TAG = "DatabaseHelper";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 2); // 버전을 2로 올립니다.
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL("CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY, EMAIL TEXT, NAME TEXT, GENDER TEXT, AGE_RANGE TEXT, BIRTHYEAR TEXT, NICKNAME TEXT, PROFILE_PICTURE TEXT, RANK_ID INTEGER, LATITUDE REAL, LONGITUDE REAL, RADIUS INTEGER, MAX_DISTANCE REAL, RATING INTEGER, RECENT_REVIEW TEXT, REVIEW_DATE TEXT)");
        } catch (Exception e) {
            Log.e(TAG, "Error creating database", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            if (oldVersion < 2) {
                db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN RATING INTEGER DEFAULT 0");
                db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN RECENT_REVIEW TEXT DEFAULT ''");
                db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN REVIEW_DATE TEXT DEFAULT ''");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error upgrading database", e);
        }
    }

    public boolean insertUser(String id, String email, String name, String gender, String ageRange, String birthyear, String nickname, String profilePicture, String rankId) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COL_1, id);
            contentValues.put(COL_2, email);
            contentValues.put(COL_3, name);
            contentValues.put(COL_4, gender);
            contentValues.put(COL_5, ageRange);
            contentValues.put(COL_6, birthyear);
            contentValues.put(COL_7, nickname);
            contentValues.put(COL_8, profilePicture != null ? profilePicture : "default_picture_url");
            contentValues.put(COL_9, rankId);
            contentValues.put(COL_10, 0);
            contentValues.put(COL_11, 0);
            contentValues.put(COL_12, 0);
            contentValues.put(COL_13, 0);
            contentValues.put(COL_14, 0); // Default value for rating
            contentValues.put(COL_15, ""); // Default value for recent review
            contentValues.put(COL_16, ""); // Default value for review date

            long result = db.insert(TABLE_NAME, null, contentValues);
            Log.d(TAG, "Insert User Result: " + result);
            return result != -1;
        } catch (Exception e) {
            Log.e(TAG, "Error inserting user", e);
            return false;
        } finally {
            db.close();
        }
    }

    public boolean isUserExists(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = null;
        try {
            res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE ID = ?", new String[]{id});
            return res.getCount() > 0;
        } catch (Exception e) {
            Log.e(TAG, "Error checking if user exists", e);
            return false;
        } finally {
            if (res != null) {
                res.close();
            }
            db.close();
        }
    }

    public boolean updateUserLocation(String id, double latitude, double longitude) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COL_10, latitude);
            contentValues.put(COL_11, longitude);
            int result = db.update(TABLE_NAME, contentValues, "ID = ?", new String[]{id});
            return result > 0;
        } catch (Exception e) {
            Log.e(TAG, "Error updating user location", e);
            return false;
        } finally {
            db.close();
        }
    }

    public String getUserNickname(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = null;
        try {
            res = db.rawQuery("SELECT NICKNAME FROM " + TABLE_NAME + " WHERE ID = ?", new String[]{userId});
            if (res != null && res.moveToFirst()) {
                return res.getString(res.getColumnIndexOrThrow(COL_7));
            }
            return null;
        } catch (Exception e) {
            Log.e(TAG, "Error getting user nickname", e);
            return null;
        } finally {
            if (res != null) {
                res.close();
            }
            db.close();
        }
    }

    public String getUserGender(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = null;
        try {
            res = db.rawQuery("SELECT GENDER FROM " + TABLE_NAME + " WHERE ID = ?", new String[]{userId});
            if (res != null && res.moveToFirst()) {
                return res.getString(res.getColumnIndexOrThrow(COL_4));
            }
            return null;
        } catch (Exception e) {
            Log.e(TAG, "Error getting user gender", e);
            return null;
        } finally {
            if (res != null) {
                res.close();
            }
            db.close();
        }
    }

    public String getUserAgeRange(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = null;
        try {
            res = db.rawQuery("SELECT AGE_RANGE FROM " + TABLE_NAME + " WHERE ID = ?", new String[]{userId});
            if (res != null && res.moveToFirst()) {
                return res.getString(res.getColumnIndexOrThrow(COL_5));
            }
            return null;
        } catch (Exception e) {
            Log.e(TAG, "Error getting user age range", e);
            return null;
        } finally {
            if (res != null) {
                res.close();
            }
            db.close();
        }
    }

    public boolean updateUserNickname(String id, String nickname) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COL_7, nickname);
            int result = db.update(TABLE_NAME, contentValues, "ID = ?", new String[]{id});
            return result > 0;
        } catch (Exception e) {
            Log.e(TAG, "Error updating user nickname", e);
            return false;
        } finally {
            db.close();
        }
    }

    public boolean updateUserRadius(String id, int radius, double maxDistance) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COL_12, radius);
            contentValues.put(COL_13, maxDistance);
            Log.d(TAG, "Updating radius for ID: " + id + ", radius: " + radius + ", maxDistance: " + maxDistance);
            int result = db.update(TABLE_NAME, contentValues, "ID = ?", new String[]{id});
            Log.d(TAG, "Update Result: " + result);
            return result > 0;
        } catch (Exception e) {
            Log.e(TAG, "Error updating user radius", e);
            return false;
        } finally {
            db.close();
        }
    }

    public void deleteAllUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.delete(TABLE_NAME, null, null);
            Log.d(TAG, "All user data deleted");
        } catch (Exception e) {
            Log.e(TAG, "Error deleting all users", e);
        } finally {
            db.close();
        }
    }

    public Cursor getUserData(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE ID = ?", new String[]{userId});
    }
}

package com.example.myapplication.ui.recruit;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * RecruitEvaluationDBHelper 클래스는 SQLite 데이터베이스를 관리합니다.
 */
public class RecruitEvaluationDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "RecruitEvaluation.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "Evaluations";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_RATING = "rating";
    private static final String COLUMN_REVIEW = "review";

    public RecruitEvaluationDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_RATING + " INTEGER, " +
                COLUMN_REVIEW + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    /**
     * 데이터베이스에 평가 데이터를 삽입합니다.
     *
     * @param rating 평가 점수
     * @param review 한줄평
     * @return 삽입 성공 여부
     */
    public boolean insertEvaluation(int rating, String review) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_RATING, rating);
        contentValues.put(COLUMN_REVIEW, review);

        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }
}

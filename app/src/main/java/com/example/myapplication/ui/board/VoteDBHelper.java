package com.example.myapplication.ui.board;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class VoteDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "VoteBoarddb";
    private static final int DATABASE_VERSION = 1;

    public VoteDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE votes (id INTEGER PRIMARY KEY, topic TEXT, content TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS votes");
        onCreate(db);
    }

    // 새로운 게시글을 추가하는 메서드 외부에서 호출할 수 있도록 public으로 지정합니다.
    public void insertVote(String topic, String content) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("topic", topic);
        values.put("content", content);
        db.insert("votes", null, values);
        db.close();
    }

    // 모든 게시글을 조회하여 리스트로 반환하는 메서드입니다. 외부에서 호출할 수 있도록 public으로 지정합니다.
    public List<String> getAllVotes() {
        List<String> votesList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM votes", null);

        int topicIndex = cursor.getColumnIndex("topic");
        int contentIndex = cursor.getColumnIndex("content");

        if (topicIndex != -1 && contentIndex != -1 && cursor.moveToFirst()) {
            do {
                String topic = cursor.getString(topicIndex);
                String content = cursor.getString(contentIndex);
                votesList.add("\n" + "제목: " + topic + "\n내용: " + content + "\n");
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return votesList;
    }

}
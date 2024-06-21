package com.example.myapplication.ui.safety;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class SafetyDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Contactdb";
    private static final int DATABASE_VERSION = 1;

    public SafetyDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE contacts (id INTEGER PRIMARY KEY, name TEXT, contact TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(db);
    }

    // 새로운 게시글을 추가하는 메서드 외부에서 호출할 수 있도록 public으로 지정합니다.
    public void insertContact(String name, String contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("contact", contact);
        db.insert("contacts", null, values);
        db.close();
    }

    // 모든 게시글을 조회하여 리스트로 반환하는 메서드입니다. 외부에서 호출할 수 있도록 public으로 지정합니다.
    public List<String> getAllContacts() {
        List<String> contactsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM contacts", null);

        int nameIndex = cursor.getColumnIndex("name");
        int contactIndex = cursor.getColumnIndex("contact");

        if (nameIndex != -1 && contactIndex != -1 && cursor.moveToFirst()) {
            do {
                String name = cursor.getString(nameIndex);
                String contact = cursor.getString(contactIndex);
                contactsList.add("\n"  + name + "\n" + contact + "\n");
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return contactsList;
    }

}

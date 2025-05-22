package com.example.diary.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MyDbHelper extends SQLiteOpenHelper {
    public MyDbHelper(@Nullable Context context) {
        super(context, MyConstants.DB_NAME, null, MyConstants.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(MyConstants.TABLE_STRUCTURE);
        db.execSQL(MyConstants.TABLE_STRUCTURE_MAP);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(MyConstants.DROP_TABLE);
        db.execSQL(MyConstants.DROP_TABLE_MAP);
        onCreate(db);
    }
}

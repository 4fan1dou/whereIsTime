package com.me.whereistime.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by qwtangwenqiang on 2016/6/7.
 */
public class DBUser extends SQLiteOpenHelper {
    public static final String TABLE_NAME_USER = "user";
    public static final String ID = "_id";
    public static final String USER_NAME = "user_name";
    public static final String USER_PASSWORD = "user_password";
    public static final int VERSION = 1;

    public DBUser(Context context) {
        super(context, TABLE_NAME_USER, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createDB = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_USER + " ("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + USER_NAME + " TEXT NOT NULL,"
                + USER_PASSWORD + " TEXT NOT NULL"
                +" )";
        db.execSQL(createDB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

package com.me.whereistime.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by qwtangwenqiang on 2016/6/29.
 */
public class DBSubTask extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "subTaskTable";
    public static final String ID = "_id";
    public static final String TASK_DISC = "task_disc";
    public static final String TASK_LABEL = "task_label";
    public static final String IS_FINISH = "isFinish";
    public static final String father_id = "father_id";
    public static final int VERSION = 1;

    public DBSubTask(Context context) {
        super(context, TABLE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createDB = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TASK_DISC + " TEXT NOT NULL,"
                + TASK_LABEL + " TEXT NOT NULL,"
                + IS_FINISH + " TEXT NOT NULL,"
                + father_id + " INTEGER"
                +" )";
        db.execSQL(createDB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

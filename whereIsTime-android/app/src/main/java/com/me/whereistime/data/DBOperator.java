package com.me.whereistime.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.me.whereistime.configure.Configure;
import com.me.whereistime.entity.SingleTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qwtangwenqiang on 2016/6/26.
 */
public class DBOperator {
    public SQLiteDatabase db;
    private SQLiteDatabase dbReader;
    private SQLiteDatabase dbWriter;
    public DBOperator(SQLiteDatabase dbReader, SQLiteDatabase dbWriter) {
        this.dbReader = dbReader;
        this.dbWriter = dbWriter;
    }

    public List<SingleTask> getAllSingleTask() {
        List<SingleTask> list = new ArrayList<SingleTask>();

        Cursor cursor = dbReader.rawQuery("select * from singleTaskTable", null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            int count = cursor.getCount();

            for (int i = 0; i < count; i++) {
                cursor.moveToPosition(i);

                String user = cursor.getString(cursor.getColumnIndex(DBSingleTask.USER));

                if (!user.equals(Configure.currentUser))
                    continue;

                SingleTask singleTask = new SingleTask();

                singleTask.setTaskId(cursor.getInt(cursor.getColumnIndex(DBSingleTask.ID)));

                singleTask.setTaskDisc(cursor.getString(cursor.getColumnIndex(DBSingleTask.TASK_DISC)));
                singleTask.setTaskLable(cursor.getString(cursor.getColumnIndex(DBSingleTask.TASK_LABEL)));
                singleTask.setIsFinish(cursor.getString(cursor.getColumnIndex(DBSingleTask.IS_FINISH)));
                list.add(singleTask);

                Log.i("任务", singleTask.getTaskDisc()+" " +singleTask.getTaskLable());
            }
        }
        return list;
    }
}

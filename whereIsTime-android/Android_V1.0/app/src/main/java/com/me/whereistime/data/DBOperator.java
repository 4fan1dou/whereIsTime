package com.me.whereistime.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.RelativeLayout;

import com.me.whereistime.activity.TaskActivity;
import com.me.whereistime.configure.Configure;
import com.me.whereistime.entity.MultipleTask;
import com.me.whereistime.entity.SingleTask;
import com.me.whereistime.entity.SubTask;

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

                Log.i("所有单任务", singleTask.getTaskDisc()+" " +singleTask.getTaskLable());
            }
        }
        return list;
    }

    public List<MultipleTask> getAllMultipleTask() {
        List<MultipleTask> list = new ArrayList<>();

        Cursor cursor = dbReader.rawQuery("select * from multipleTaskTable", null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            int count = cursor.getCount();

            for (int i = 0; i < count; i++) {
                cursor.moveToPosition(i);

                String user = cursor.getString(cursor.getColumnIndex(DBMultipleTask.USER));

                if (!user.equals(Configure.currentUser))
                    continue;

                MultipleTask multipleTask = new MultipleTask();

                multipleTask.setTaskId(cursor.getInt(cursor.getColumnIndex(DBMultipleTask.ID)));
                multipleTask.setTaskDisc(cursor.getString(cursor.getColumnIndex(DBMultipleTask.TASK_DISC)));
                multipleTask.setTaskLable(cursor.getString(cursor.getColumnIndex(DBMultipleTask.TASK_LABEL)));

                multipleTask.setTaskList(getSubTaskList(multipleTask.getTaskId()));

                list.add(multipleTask);

                Log.i("多任务", multipleTask.getTaskDisc()+" " +multipleTask.getTaskLable());
            }
        }
        return list;
    }

    public List<SubTask> getSubTaskList(int fartherId) {
        List<SubTask> list = new ArrayList<SubTask>();
        DBSubTask dbSubTask = new DBSubTask(TaskActivity.instance);
        SQLiteDatabase dbread = dbSubTask.getReadableDatabase();

        Cursor cursor = dbread.rawQuery("select * from subTaskTable", null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            int count = cursor.getCount();

            for (int i = 0; i < count; i++) {
                cursor.moveToPosition(i);

                int fId = cursor.getInt(cursor.getColumnIndex(DBSubTask.father_id));

                if (fId != fartherId)
                    continue;

                SubTask subTask = new SubTask();
                subTask.setFartherId(fartherId);
                subTask.setTaskId(cursor.getInt(cursor.getColumnIndex(DBSubTask.ID)));
                subTask.setTaskDisc(cursor.getString(cursor.getColumnIndex(DBSubTask.TASK_DISC)));
                subTask.setTaskLable(cursor.getString(cursor.getColumnIndex(DBSubTask.TASK_LABEL)));
                subTask.setIsFinish(cursor.getString(cursor.getColumnIndex(DBSubTask.IS_FINISH)));

                list.add(subTask);

            }
        }
        return list;
    }
}

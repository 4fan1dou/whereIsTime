package com.me.whereistime.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.me.whereistime.R;
import com.me.whereistime.adapter.MultipleTaskAdapter;
import com.me.whereistime.adapter.SingleTaskAdapter;
import com.me.whereistime.configure.Configure;
import com.me.whereistime.data.DBMultipleTask;
import com.me.whereistime.data.DBOperator;
import com.me.whereistime.data.DBSingleTask;
import com.me.whereistime.data.DBSubTask;
import com.me.whereistime.entity.MultipleTask;
import com.me.whereistime.entity.SingleTask;
import com.me.whereistime.entity.SubTask;

import java.util.ArrayList;
import java.util.List;

public class TaskActivity extends Activity {
    public static TaskActivity instance;
    private ViewPager mTabPager;
    private ImageView mTab1, mTab2;
    private LayoutInflater inflater;

    //单个任务界面
    private ImageView ib_single_add;
    private ListView lv_single_list;

    //任务集界面
    private ImageView ib_multiple_add;
    private ListView lv_multiple_list;

    //请求码
    public static final int SINGLE_REQUEST = 1;
    public static final int MUL_REQUEST = 2;
    public static final int SUB_REQUEST = 3;

    //适配器
    private SingleTaskAdapter singleTaskAdapter;
    private MultipleTaskAdapter multipleTaskAdapter;

    //数据库操作
    private DBSingleTask dbSingleTask;
    private SQLiteDatabase dbSingleWriter;
    private SQLiteDatabase dbSingleReader;

    private DBMultipleTask dbMultipleTask;
    private SQLiteDatabase dbMulWriter;
    private SQLiteDatabase dbMulReader;

    private DBSubTask dbSubTask;
    private SQLiteDatabase dbSubWriter;
    private SQLiteDatabase dbSubReader;

    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        instance = this;
        initView();
    }


    @Override
    protected void onResume() {
        super.onResume();
        updateSingleTask();
        updateMultipleTask();
    }

    private void initView() {
        //init database
        dbSingleTask = new DBSingleTask(TaskActivity.this);
        dbSingleWriter = dbSingleTask.getWritableDatabase();
        dbSingleReader = dbSingleTask.getReadableDatabase();

        dbMultipleTask = new DBMultipleTask(TaskActivity.this);
        dbMulWriter = dbMultipleTask.getWritableDatabase();
        dbMulReader = dbMultipleTask.getReadableDatabase();

        dbSubTask = new DBSubTask(TaskActivity.this);
        dbSubWriter = dbSubTask.getWritableDatabase();
        dbSubReader = dbSubTask.getReadableDatabase();

        //init view
        mTabPager = (ViewPager) findViewById(R.id.tabpager);
        mTab1 = (ImageView) findViewById(R.id.img_daban);
        mTab2 = (ImageView) findViewById(R.id.img_daibanji);

        mTab1.setOnClickListener(new MyOnClickListener(0));
        mTab2.setOnClickListener(new MyOnClickListener(1));

        //将要分页显示的View装入数组中
        LayoutInflater mLi = LayoutInflater.from(this);
        View view1 = mLi.inflate(R.layout.single_task, null);
        View view2 = mLi.inflate(R.layout.multiple_task, null);

        //初始化两个界面
        initSingleView(view1);
        initMultipleView(view2);

        //每个页面的view数据
        final ArrayList<View> views = new ArrayList<View>();
        views.add(view1);
        views.add(view2);

        //填充ViewPager的数据适配器
        PagerAdapter mPagerAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return views.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                ((ViewPager) container).removeView(views.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ((ViewPager) container).addView(views.get(position));
                return views.get(position);
            }
        };
        mTabPager.setAdapter(mPagerAdapter);
    }

    private void initMultipleView(View view) {
        ib_multiple_add = (ImageView) view.findViewById(R.id.ib_add);
        lv_multiple_list = (ListView) view.findViewById(R.id.lv_list);
        ib_multiple_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TaskActivity.this, AddTaskActivity.class);
                startActivityForResult(intent, MUL_REQUEST);
            }
        });
        
        updateMultipleTask();

    }

    private void updateMultipleTask() {
        List<MultipleTask> lists = getMultipleTaskInfo();
        Log.i("当前多任务", lists.toString());
        multipleTaskAdapter = new MultipleTaskAdapter(TaskActivity.this, lists, instance);
        lv_multiple_list.setAdapter(multipleTaskAdapter);
    }

    private List<MultipleTask> getMultipleTaskInfo() {
        DBOperator dbOperate = new DBOperator(dbMulReader, dbMulWriter);
        List<MultipleTask> lists = dbOperate.getAllMultipleTask();
        return lists;
    }

    private void initSingleView(View view) {
        ib_single_add  = (ImageView) view.findViewById(R.id.ib_add);
        lv_single_list = (ListView) view.findViewById(R.id.lv_list);
        ib_single_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TaskActivity.this, AddTaskActivity.class);
                startActivityForResult(intent, SINGLE_REQUEST);
            }
        });

        updateSingleTask();
    }

    public void deleteSingleTask(int position) {
        SingleTask singleTask = (SingleTask) singleTaskAdapter.getItem(position);
        dbSingleWriter.delete(DBSingleTask.TABLE_NAME, "_id=?",
                new String[]{String.valueOf(singleTask.getTaskId())});
        updateSingleTask();
    }

    public void deleteSubTask(int subTaskId) {
        dbSubWriter.delete(DBSubTask.TABLE_NAME, "_id=?",
                new String[]{String.valueOf(subTaskId)});
        updateMultipleTask();
    }

    public void updateSingleTask() {
        List<SingleTask> lists = getSingleTaskInfo();
        singleTaskAdapter = new SingleTaskAdapter(TaskActivity.this, lists, instance);
        lv_single_list.setAdapter(singleTaskAdapter);
    }

    private List<SingleTask> getSingleTaskInfo() {
        DBOperator dbOperate = new DBOperator(dbSingleReader, dbSingleWriter);
        List<SingleTask> lists = dbOperate.getAllSingleTask();
        return lists;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TaskActivity.SINGLE_REQUEST && resultCode == RESULT_OK) {
            Log.i("添加返回：", "单个任务");
            String task_disc = data.getStringExtra("task_disc");
            String task_label = data.getStringExtra("task_label");
            Log.i("单个任务", task_disc+"  "+task_label);
            //将任务写入数据库
            storeSingleTask(task_disc, task_label);
        } else if (requestCode == TaskActivity.MUL_REQUEST && resultCode == RESULT_OK) {
            Log.i("添加返回：", "多个任务");
            String task_disc = data.getStringExtra("task_disc");
            String task_label = data.getStringExtra("task_label");
            Log.i("任务集合", task_disc + "  " + task_label);
            storeMultipleTask(task_disc, task_label);
        } else if (requestCode == TaskActivity.SUB_REQUEST && resultCode == RESULT_OK) {
            String task_disc = data.getStringExtra("task_disc");
            String task_label = data.getStringExtra("task_label");
            int fartherId = data.getIntExtra("fartherId", -1);
            storeSubTask(task_disc, task_label, fartherId);
        }
        else {
            Log.i("nothing:", "do nothing");
        }
    }

    private void storeSubTask(String task_disc, String task_label, int fartherId) {
        ContentValues cv = new ContentValues();
        cv.put(DBSubTask.TASK_DISC, task_disc);
        cv.put(DBSubTask.TASK_LABEL, task_label);
        cv.put(DBSubTask.father_id, fartherId);
        cv.put(DBSubTask.IS_FINISH, false);
        dbSubWriter.insert(DBSubTask.TABLE_NAME, null, cv);
        updateMultipleTask();
    }

    private void storeMultipleTask(String task_disc, String task_label) {
        //dbMulWriter = dbMultipleTask.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DBMultipleTask.TASK_DISC, task_disc);
        cv.put(DBMultipleTask.TASK_LABEL, task_label);
        cv.put(DBMultipleTask.USER, Configure.currentUser);
        dbMulWriter.insert(DBMultipleTask.TABLE_NAME, null, cv);
        //dbMulWriter.close();

        Log.i("cv", cv.toString());
        updateMultipleTask();
        List<MultipleTask> lists = getMultipleTaskInfo();
        for (int i = 0; i < lists.size(); i++) {
            Log.i("lists:", lists.get(i).getTaskDisc());
        }

    }

    private void storeSingleTask(String task_disc, String task_label) {
        ContentValues cv = new ContentValues();
        cv.put(DBSingleTask.TASK_DISC, task_disc);
        cv.put(DBSingleTask.TASK_LABEL, task_label);
        cv.put(DBSingleTask.IS_FINISH, "false");
        cv.put(DBSingleTask.USER, Configure.currentUser);
        dbSingleWriter.insert(DBSingleTask.TABLE_NAME, null, cv);
        updateSingleTask();
    }

    public void addSubTask(int fartherId) {
        Intent intent = new Intent(TaskActivity.this, AddTaskActivity.class);
        intent.putExtra("fartherId", fartherId);
        startActivityForResult(intent, SUB_REQUEST);
    }

    public void deleteMultipleTask(int taskId) {
        dbMulWriter.delete(DBMultipleTask.TABLE_NAME, "_id=?",
                new String[]{String.valueOf(taskId)});
        dbSubWriter.delete(DBSubTask.TABLE_NAME, "father_id=?",
                new String[]{String.valueOf(taskId)});
        updateMultipleTask();
    }

    /**
     * 头标点击监听
     */
    public class MyOnClickListener implements View.OnClickListener {
        private int index = 0;

        public MyOnClickListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View v) {
            mTabPager.setCurrentItem(index);
        }
    }
}

package com.me.whereistime.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.MultiTapKeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.me.whereistime.R;
import com.me.whereistime.adapter.SingleTaskAdapter;
import com.me.whereistime.configure.Configure;
import com.me.whereistime.data.DBOperator;
import com.me.whereistime.data.DBSingleTask;
import com.me.whereistime.entity.SingleTask;

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
    //请求码
    public static final int REQUSET = 1;
    //适配器
    private SingleTaskAdapter singleTaskAdapter;

    //数据库操作
    private DBSingleTask dbSingleTask;
    private SQLiteDatabase dbSingleWriter;
    private SQLiteDatabase dbSingleReader;
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
        updateTask();
    }

    private void initView() {
        dbSingleTask = new DBSingleTask(TaskActivity.this);
        dbSingleWriter = dbSingleTask.getWritableDatabase();
        dbSingleReader = dbSingleTask.getReadableDatabase();

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
    }

    private void initSingleView(View view) {
        ib_single_add  = (ImageView) view.findViewById(R.id.ib_add);
        lv_single_list = (ListView) view.findViewById(R.id.lv_list);
        ib_single_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TaskActivity.this, AddTaskActivity.class);
                startActivityForResult(intent, REQUSET);
            }
        });

        updateTask();
    }

    public void deleteSingleTask(int position) {
        SingleTask singleTask = (SingleTask) singleTaskAdapter.getItem(position);
        dbSingleWriter.delete(DBSingleTask.TABLE_NAME, "_id=?",
                new String[]{String.valueOf(singleTask.getTaskId())});
        updateTask();
    }

    public void updateTask() {
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
        if (requestCode == TaskActivity.REQUSET && resultCode == RESULT_OK) {
            String task_disc = data.getStringExtra("task_disc");
            String task_label = data.getStringExtra("task_label");
            Log.i("任务", task_disc+"  "+task_label);
            //将任务写入数据库
            storeSingleTaks(task_disc, task_label);
        }
    }

    private void storeSingleTaks(String task_disc, String task_label) {
        ContentValues cv = new ContentValues();
        cv.put(DBSingleTask.TASK_DISC, task_disc);
        cv.put(DBSingleTask.TASK_LABEL, task_label);
        cv.put(DBSingleTask.IS_FINISH, "false");
        cv.put(DBSingleTask.USER, Configure.currentUser);
        dbSingleWriter.insert(DBSingleTask.TABLE_NAME, null, cv);
        updateTask();
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

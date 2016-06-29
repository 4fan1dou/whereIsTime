package com.me.whereistime.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.me.whereistime.R;
import com.me.whereistime.configure.Configure;
import com.me.whereistime.data.DBSingleTask;
import com.me.whereistime.data.DBSubTask;
import com.me.whereistime.entity.SingleTask;
import com.me.whereistime.view.progressbar.CircleProgressBar;

import java.security.Policy;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    /**
     * 圆形进度条
     */
    private CircleProgressBar myProgress;
    private ImageButton ib_setting;
    private ImageButton ib_statistic;
    private ImageButton ib_begin;
    private ImageButton ib_shutdown;
    public int progressRate;

    private LinearLayout ll_time_show;
    public TextView tv_minute;
    public TextView tv_second;
    private TextView tv_task_disc;

    private static int all_time_seconds;
    private static final int MSG_WHAT_TIME_BEGIN = 1;
    private static final int MSG_WHAT_TIME_UP = 2;
    private Timer timer = new Timer();
    private TimerTask timerTask = null;
    private Boolean isRunning;

    //震动提醒
    private Vibrator vibrator;

    //当前任务
    private String task_disc;
    private int task_id;
    private int taskType; //判断单个任务或者子任务  1为子任务，-1为单个任务

    //数据库管理
    private SQLiteDatabase dbWriter;
    private DBSingleTask dbSingleTask;

    private SQLiteDatabase dbSubWriter;
    private DBSubTask dbSubTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        task_disc = getIntent().getStringExtra("task_disc");
        task_id = getIntent().getIntExtra("task_id", -1);
        taskType = getIntent().getIntExtra("type", -1);
        //震动提醒
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        initView();
    }

    private void initView() {
        isRunning = false;
        myProgress = (CircleProgressBar) findViewById(R.id.myProgress);
        ib_setting = (ImageButton) findViewById(R.id.ib_setting);
        ib_statistic = (ImageButton) findViewById(R.id.ib_statistic);
        ib_shutdown = (ImageButton) findViewById(R.id.ib_shutdown);
        ib_begin = (ImageButton) findViewById(R.id.ib_begin);
        ib_setting.setOnClickListener(settingListener);
        ib_begin.setOnClickListener(beginListener);
        ib_shutdown.setOnClickListener(shutdownListener);


        ll_time_show = (LinearLayout) findViewById(R.id.time_show);
        tv_minute = (TextView) ll_time_show.findViewById(R.id.tv_minute);
        tv_second = (TextView) ll_time_show.findViewById(R.id.tv_second);
        tv_minute.setText("01");
        tv_second.setText("00");

        tv_task_disc = (TextView) findViewById(R.id.tv_task_disc);
        tv_task_disc.setText(task_disc);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isRunning == false) {
            initTime();
            myProgress.setMaxProgress(Integer.parseInt(tv_minute.getText().toString()) * 60
                    + Integer.parseInt(tv_second.getText().toString()));
        }
    }

    private void initTime() {
        myProgress.setProgress(0);
        SharedPreferences sp = getApplicationContext()
                .getSharedPreferences(SettingActivity.class.getName(), Context.MODE_PRIVATE);
        int db_work_time = sp.getInt(Configure.DATA_WORK_TIME, 30);
        Log.d("worktime:", String.valueOf(db_work_time));
        if (db_work_time < 10)
            tv_minute.setText("0"+db_work_time);
        else
            tv_minute.setText(db_work_time+"");
        tv_second.setText("00");
    }

    View.OnClickListener settingListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, SettingActivity.class);
            startActivity(intent);
        }
    };

    View.OnClickListener beginListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ib_shutdown.setVisibility(View.VISIBLE);
            ib_begin.setVisibility(View.INVISIBLE);
            startTime();
        }
    };

    View.OnClickListener shutdownListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("选择")
                    .setMessage("已经完成 OR 放弃")
                    .setPositiveButton("已经完成", hasFinishedTask)
                    .setNegativeButton("放弃任务", giveupTask)
                    .show();
        }
    };

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_WHAT_TIME_BEGIN) {
                int minute = (all_time_seconds/60)%60;
                int second = all_time_seconds%60;
                tv_minute.setText(minute+"");
                tv_second.setText(second+"");
                if (minute < 10) {
                    tv_minute.setText("0"+String.valueOf(minute));
                }
                if (second < 10) {
                    tv_second.setText("0"+String.valueOf(second));
                }
                myProgress.setProgress(progressRate);
            } else if (msg.what == MSG_WHAT_TIME_UP) {
                beginVibrator();
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Time is up")
                        .setMessage("It's time to rest!")
                        .setPositiveButton("Sure", beginToRestListener)
                        .setCancelable(false)
                        .show();
            }
        }
    };

    private void beginVibrator() {
        long [] pattern = {100,400,100,400}; // 停止 开启 停止 开启
        vibrator.vibrate(pattern,-1); //重复两次上面的pattern 如果只想震动一次，index设为-1
    }
    private void stopVibrator() {
        vibrator.cancel();
    }

   Dialog.OnClickListener beginToRestListener = new Dialog.OnClickListener() {

       @Override
       public void onClick(DialogInterface dialog, int which) {
           stopVibrator();
           storeCurTask();
           Intent intent = new Intent();
           intent.setClass(MainActivity.this, RestActivity.class);
           startActivity(intent);
           ib_begin.setVisibility(View.VISIBLE);
           ib_shutdown.setVisibility(View.INVISIBLE);
           //MainActivity.this.finish();
       }
   };

    private void storeCurTask() {
        if (task_id == -1)
            return;

        if (taskType == 1) {
            dbSubTask = new DBSubTask(MainActivity.this);
            dbSubWriter = dbSubTask.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(DBSubTask.IS_FINISH, "true");
            dbSubWriter.update(DBSubTask.TABLE_NAME, cv, "_id=?",
                    new String[]{String.valueOf(task_id)});
        } else {
            dbSingleTask = new DBSingleTask(MainActivity.this);
            dbWriter = dbSingleTask.getWritableDatabase();

            ContentValues cv = new ContentValues();
            cv.put(DBSingleTask.IS_FINISH, "true");
            dbWriter.update(DBSingleTask.TABLE_NAME, cv, "_id=?",
                    new String[]{String.valueOf(task_id)});
            dbWriter.close();
        }
    }

    Dialog.OnClickListener hasFinishedTask = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            stopTime();
            beginToRestListener.onClick(dialog, which);
        }
    };

    Dialog.OnClickListener giveupTask = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            Toast.makeText(MainActivity.this, "放弃了当前番茄", Toast.LENGTH_SHORT).show();
            ib_begin.setVisibility(View.VISIBLE);
            ib_shutdown.setVisibility(View.INVISIBLE);
            stopTime();
            initTime();
        }
    };

    //开始计时操作
    private void startTime() {
        if(isRunning == true)
            return;
        all_time_seconds = 0; //初始总秒数
        progressRate = 0;
        int minute = 0, second = 0;
        if (!TextUtils.isEmpty(tv_minute.getText())) {
            minute = Integer.parseInt(tv_minute.getText().toString());
        }
        if (!TextUtils.isEmpty(tv_second.getText())) {
            second = Integer.parseInt(tv_second.getText().toString());
        }
        all_time_seconds = minute*60+second;
        if (timerTask == null) {
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    isRunning = true;
                    //每执行一次，减少一秒
                    all_time_seconds--;
                    //circle bar 中用增加来显示进度
                    progressRate++;
                    //用handler来刷新界面
                    handler.sendEmptyMessage(MSG_WHAT_TIME_BEGIN);
                    if (all_time_seconds == 0) {
                        handler.sendEmptyMessage(MSG_WHAT_TIME_UP);
                        stopTime();
                    }
                }
            };
            //启动timerTask，延迟1000ms，每1000ms执行一次
            timer.schedule(timerTask, 1000, 1000);
        }
    }

    private void stopTime() {
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
            isRunning = false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopVibrator();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopVibrator();
    }
}

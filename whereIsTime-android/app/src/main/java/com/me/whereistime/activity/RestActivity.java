package com.me.whereistime.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.me.whereistime.R;
import com.me.whereistime.configure.Configure;
import com.me.whereistime.view.progressbar.CircleProgressBar;

import java.util.Timer;
import java.util.TimerTask;

public class RestActivity extends AppCompatActivity {

    /**
     * 圆形进度条
     */
    private CircleProgressBar myProgress;
    private ImageButton ib_rest_stop;
    public int progressRate;

    private LinearLayout ll_time_show;
    public TextView tv_minute;
    public TextView tv_second;

    private static int all_time_seconds;
    private static final int MSG_WHAT_TIME_BEGIN = 1;
    private static final int MSG_WHAT_TIME_UP = 2;
    private Timer timer = new Timer();
    private TimerTask timerTask = null;
    private Boolean isRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest);

        initView();
    }

    private void initView() {
        isRunning = false;
        myProgress = (CircleProgressBar) findViewById(R.id.myProgress);

        ib_rest_stop = (ImageButton) findViewById(R.id.ib_rest_stop);
        ib_rest_stop.setOnClickListener(restStopListener);

        ll_time_show = (LinearLayout) findViewById(R.id.time_show);
        tv_minute = (TextView) ll_time_show.findViewById(R.id.tv_minute);
        tv_second = (TextView) ll_time_show.findViewById(R.id.tv_second);
        tv_minute.setText("01");
        tv_second.setText("00");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isRunning == false) {
            initTime();
            myProgress.setMaxProgress(Integer.parseInt(tv_minute.getText().toString()) * 60
                    + Integer.parseInt(tv_second.getText().toString()));
        }
        startTime();
    }

    private void initTime() {
        SharedPreferences sp = getApplicationContext()
                .getSharedPreferences(SettingActivity.class.getName(), Context.MODE_PRIVATE);
        int db_work_time = sp.getInt(Configure.DATA_SHORT_REST_TIME, 10);
        if (db_work_time < 10)
            tv_minute.setText("0"+db_work_time);
        else
            tv_minute.setText(db_work_time+"");
        tv_second.setText("00");
    }

    View.OnClickListener restStopListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            stopTime();
//            Intent intent = new Intent();
//            intent.setClass(RestActivity.this, MainActivity.class);
//            startActivity(intent);
            RestActivity.this.finish();
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
                Toast.makeText(RestActivity.this, "休息结束", Toast.LENGTH_SHORT).show();
                restStopListener.onClick(ib_rest_stop);
            }
        }
    };

    //开始计时操作
    private void startTime() {
        all_time_seconds = 0; //初始总秒数
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
}

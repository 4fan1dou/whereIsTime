package com.me.whereistime.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.me.whereistime.R;
import com.me.whereistime.configure.Configure;

/**
 * Created by qwtangwenqiang on 2016/5/19.
 */
public class SettingActivity extends Activity{
    private ImageButton ib_go_main;

    private RelativeLayout rv_set_work;
    private RelativeLayout rv_sb_work_time;
    private SeekBar sb_work_time;
    private TextView tv_work_minute;

    private RelativeLayout rv_set_short_rest;
    private RelativeLayout rv_sb_short_rest_time;
    private SeekBar sb_short_rest_time;
    private TextView tv_short_rest_minute;

    private RelativeLayout rv_set_long_rest;
    private RelativeLayout rv_sb_long_rest_time;
    private SeekBar sb_long_rest_time;
    private TextView tv_long_rest_minute;

    private RelativeLayout rv_set_long_rest_space;
    private RelativeLayout rv_sb_space_count;
    private SeekBar sb_space_count;
    private TextView tv_long_rest_space_count;

    private int db_work_time;
    private int db_short_rest_time;
    private int db_long_rest_time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);

        initDB();
        initView();
    }

    private void initDB() {
        SharedPreferences sp = getApplicationContext()
                .getSharedPreferences(SettingActivity.class.getName(), Context.MODE_PRIVATE);
        db_work_time = sp.getInt(Configure.DATA_WORK_TIME, 30);
        db_short_rest_time = sp.getInt(Configure.DATA_SHORT_REST_TIME, 10);
        db_long_rest_time = sp.getInt(Configure.DATA_LONG_REST_TIME, 20);
    }

    private void initView() {
        ib_go_main = (ImageButton) findViewById(R.id.ib_go_main);
        ib_go_main.setOnClickListener(goMainListener);

        rv_set_work = (RelativeLayout) findViewById(R.id.rv_set_work);
        rv_set_work.setOnClickListener(showWorkListener);
        rv_sb_work_time = (RelativeLayout) findViewById(R.id.rv_sb_work_time);
        sb_work_time = (SeekBar) findViewById(R.id.sb_work_time);
        sb_work_time.setProgress(db_work_time);
        sb_work_time.setOnSeekBarChangeListener(workSeekBarListener);
        tv_work_minute = (TextView) findViewById(R.id.tv_work_minute);
        tv_work_minute.setText(sb_work_time.getProgress()+"");

        rv_set_short_rest = (RelativeLayout) findViewById(R.id.rv_set_short_rest);
        rv_set_short_rest.setOnClickListener(showShortRestListener);
        rv_sb_short_rest_time = (RelativeLayout) findViewById(R.id.rv_sb_short_rest_time);
        sb_short_rest_time = (SeekBar) findViewById(R.id.sb_short_rest_time);
        sb_short_rest_time.setProgress(db_short_rest_time);
        sb_short_rest_time.setOnSeekBarChangeListener(shortRestSeekBarListener);
        tv_short_rest_minute = (TextView) findViewById(R.id.tv_short_rest_minute);
        tv_short_rest_minute.setText(sb_short_rest_time.getProgress()+"");

        rv_set_long_rest = (RelativeLayout) findViewById(R.id.rv_set_long_rest);
        rv_set_long_rest.setOnClickListener(showLongRestListener);
        rv_sb_long_rest_time = (RelativeLayout) findViewById(R.id.rv_sb_long_rest_time);
        sb_long_rest_time = (SeekBar) findViewById(R.id.sb_long_rest_time);
        sb_long_rest_time.setProgress(db_long_rest_time);
        sb_long_rest_time.setOnSeekBarChangeListener(longRestSeekBarListener);
        tv_long_rest_minute = (TextView) findViewById(R.id.tv_long_rest_minute);
        tv_long_rest_minute.setText(sb_long_rest_time.getProgress()+"");

        rv_set_long_rest_space = (RelativeLayout) findViewById(R.id.rv_set_long_rest_space);
        rv_set_long_rest_space.setOnClickListener(showCountListener);
        rv_sb_space_count = (RelativeLayout) findViewById(R.id.rv_sb_space_count);
        sb_space_count = (SeekBar) findViewById(R.id.sb_space_count);
        sb_space_count.setOnSeekBarChangeListener(countSeekBarListener);
        tv_long_rest_space_count = (TextView) findViewById(R.id.tv_long_rest_space_count);
        tv_long_rest_space_count.setText(sb_space_count.getProgress()+"");
    }

    View.OnClickListener showCountListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (rv_sb_space_count.getVisibility() == View.GONE) {
                rv_sb_space_count.setVisibility(View.VISIBLE);
            } else {
                rv_sb_space_count.setVisibility(View.GONE);
            }
        }
    };

    View.OnClickListener showLongRestListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (rv_sb_long_rest_time.getVisibility() == View.GONE) {
                rv_sb_long_rest_time.setVisibility(View.VISIBLE);
            } else {
                rv_sb_long_rest_time.setVisibility(View.GONE);
            }
        }
    };

    View.OnClickListener showShortRestListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (rv_sb_short_rest_time.getVisibility() == View.GONE) {
                rv_sb_short_rest_time.setVisibility(View.VISIBLE);
            } else {
                rv_sb_short_rest_time.setVisibility(View.GONE);
            }
        }
    };

    View.OnClickListener goMainListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            saveSetDB();
            Intent intent = new Intent();
            intent.setClass(SettingActivity.this, MainActivity.class);
            startActivity(intent);
            SettingActivity.this.finish();
        }
    };

    private void saveSetDB() {
        SharedPreferences sp = getApplicationContext()
                .getSharedPreferences(SettingActivity.class.getName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(Configure.DATA_WORK_TIME, sb_work_time.getProgress());
        editor.putInt(Configure.DATA_SHORT_REST_TIME, sb_short_rest_time.getProgress());
        editor.putInt(Configure.DATA_LONG_REST_TIME, sb_long_rest_time.getProgress());

        editor.commit();
    }

    @Override
    protected void onDestroy() {
        saveSetDB();
        System.out.println("on destroy");
        super.onDestroy();
    }

    View.OnClickListener showWorkListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (rv_sb_work_time.getVisibility() == View.GONE) {
                rv_sb_work_time.setVisibility(View.VISIBLE);
            } else {
                rv_sb_work_time.setVisibility(View.GONE);
            }
        }
    };

    SeekBar.OnSeekBarChangeListener workSeekBarListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            int curProgress = seekBar.getProgress();
            tv_work_minute.setText(curProgress+"");
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            int curProgress = seekBar.getProgress();
            if (curProgress == 0) {
                Toast.makeText(getBaseContext(), "时间不能设置为0", Toast.LENGTH_SHORT).show();
                seekBar.setProgress(10);
            }
        }
    };

    SeekBar.OnSeekBarChangeListener shortRestSeekBarListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            int curProgress = seekBar.getProgress();
            tv_short_rest_minute.setText(curProgress+"");
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            int curProgress = seekBar.getProgress();
            if (curProgress == 0) {
                Toast.makeText(getBaseContext(), "时间不能设置为0", Toast.LENGTH_SHORT).show();
                seekBar.setProgress(10);
            }
        }
    };

    SeekBar.OnSeekBarChangeListener longRestSeekBarListener = new SeekBar.OnSeekBarChangeListener(){
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            int curProgress = seekBar.getProgress();
            tv_long_rest_minute.setText(curProgress+"");
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            int curProgress = seekBar.getProgress();
            if (curProgress == 0) {
                Toast.makeText(getBaseContext(), "时间不能设置为0", Toast.LENGTH_SHORT).show();
                seekBar.setProgress(10);
            }
        }
    };

    SeekBar.OnSeekBarChangeListener countSeekBarListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            int curProgress = seekBar.getProgress();
            tv_long_rest_space_count.setText(curProgress + "");
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            int curProgress = seekBar.getProgress();
            if (curProgress == 0) {
                Toast.makeText(getBaseContext(), "个数不能设置为0", Toast.LENGTH_SHORT).show();
                seekBar.setProgress(2);
            }
        }
    };
}

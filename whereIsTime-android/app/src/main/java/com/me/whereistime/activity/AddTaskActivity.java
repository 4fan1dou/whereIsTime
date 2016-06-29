package com.me.whereistime.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.me.whereistime.R;

public class AddTaskActivity extends AppCompatActivity {
    private EditText et_task_disc;
    private EditText et_task_label;
    private Button btn_sure;

    private int fartherId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        fartherId = getIntent().getIntExtra("fartherId", -1);
        initView();
    }

    private void initView() {
        et_task_disc = (EditText) findViewById(R.id.et_task_disc);
        et_task_label = (EditText) findViewById(R.id.et_task_label);
        btn_sure = (Button) findViewById(R.id.btn_sure);

        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(et_task_disc.getText())) {
                    Toast.makeText(AddTaskActivity.this, "请输入任务名称", Toast.LENGTH_SHORT).show();
                    return;
                }
                String label;
                if (TextUtils.isEmpty(et_task_label.getText())) {
                    label = "null";
                } else {
                    label = et_task_label.getText().toString();
                }
                Intent intent = new Intent();
                intent.putExtra("task_disc", et_task_disc.getText().toString());
                intent.putExtra("task_label", label);
                intent.putExtra("fartherId", fartherId);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}

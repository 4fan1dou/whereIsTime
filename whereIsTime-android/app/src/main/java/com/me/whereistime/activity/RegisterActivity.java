package com.me.whereistime.activity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.me.whereistime.R;
import com.me.whereistime.data.DBUser;

public class RegisterActivity extends AppCompatActivity {
    private Button login_reback_btn;
    private EditText login_user_edit;
    private EditText login_passwd_edit;
    private Button login_register_btn;
    private DBUser dataUser;
    private SQLiteDatabase dbWriter;
    private SQLiteDatabase dbReader;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();
    }

    private void initView() {
        login_reback_btn = (Button) findViewById(R.id.login_reback_btn);
        login_user_edit = (EditText) findViewById(R.id.login_user_edit);
        login_passwd_edit = (EditText) findViewById(R.id.login_passwd_edit);
        login_register_btn = (Button) findViewById(R.id.login_register_btn);

        dataUser = new DBUser(this);
        dbReader = dataUser.getReadableDatabase();
        dbWriter = dataUser.getWritableDatabase();

        login_reback_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterActivity.this.finish();
            }
        });

        login_register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = login_user_edit.getText().toString();
                String userPass = login_passwd_edit.getText().toString();
                if ("".equals(userName)) {
                    Toast.makeText(RegisterActivity.this, "请输入账号", Toast.LENGTH_SHORT).show();
                } else if ("".equals(userPass)) {
                    Toast.makeText(RegisterActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                } else {
                    registerUser(userName, userPass);
                }
            }
        });
    }

    private void registerUser(String userName, String userPass) {
        boolean isExit = changeIfUserExit(userName);
        Log.i("存在：", isExit + "");
        if (isExit == true) {
            Toast.makeText(RegisterActivity.this, "账号已经存在", Toast.LENGTH_SHORT).show();
        } else {
            ContentValues cv = new ContentValues();
            cv.put(DBUser.USER_NAME, userName);
            cv.put(DBUser.USER_PASSWORD, userPass);
            dbWriter.insert(DBUser.TABLE_NAME_USER, null, cv);
            Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
            RegisterActivity.this.finish();
        }
    }

    private boolean changeIfUserExit(String userName) {
        cursor = dbReader.query(DBUser.TABLE_NAME_USER, null,null,null,
                null,null,null,null);

        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            if (cursor.getString(cursor.getColumnIndex(DBUser.USER_NAME)).toString()
                    .equals(userName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbReader.close();
        dbWriter.close();
    }
}

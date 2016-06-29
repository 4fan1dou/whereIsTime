package com.me.whereistime.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.me.whereistime.R;
import com.me.whereistime.activity.MainActivity;
import com.me.whereistime.activity.TaskActivity;
import com.me.whereistime.data.DBSingleTask;
import com.me.whereistime.entity.SingleTask;

import java.util.List;

/**
 * Created by qwtangwenqiang on 2016/6/26.
 */
public class SingleTaskAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context mContext;
    private List<SingleTask> lists;
    private TaskActivity instance;

    public SingleTaskAdapter(Context context, List<SingleTask> lists, TaskActivity instance) {
        this.mContext = context;
        this.lists = lists;
        this.inflater = LayoutInflater.from(mContext);
        this.instance = instance;
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        //如果缓存convertView为空，则需要创建View
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.single_task_item, null);
            viewHolder.tv_task_disc = (TextView) convertView.findViewById(R.id.tv_task_disc);
            viewHolder.btn_begin = (Button) convertView.findViewById(R.id.btn_begin);
            //将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tv_task_disc.setText(lists.get(position).getTaskDisc());
        if (lists.get(position).getIsFinish().equals("true")) {
            viewHolder.btn_begin.setText("完成");
        } else {
            viewHolder.btn_begin.setText("开始");
        }

        final String task_disc = viewHolder.tv_task_disc.getText().toString();
        final int task_id = lists.get(position).getTaskId();
        viewHolder.btn_begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MainActivity.class);
                intent.putExtra("task_disc", task_disc);
                intent.putExtra("task_id", task_id);
                mContext.startActivity(intent);
            }
        });

        final int cur_position = position;

        viewHolder.tv_task_disc.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new AlertDialog.Builder(mContext)
                        .setTitle("操作选项").
                        setItems(new CharSequence[]{"删除"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        instance.deleteSingleTask(cur_position);
                                        break;
                                }
                            }
                        }).show();
                return false;
            }
        });

        return convertView;
    }

    static class ViewHolder {
        public TextView tv_task_disc;
        public Button btn_begin;
    }
}

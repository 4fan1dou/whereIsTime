package com.me.whereistime.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.Image;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.me.whereistime.R;
import com.me.whereistime.activity.TaskActivity;
import com.me.whereistime.common.Utility;
import com.me.whereistime.entity.MultipleTask;
import com.me.whereistime.entity.SingleTask;
import com.me.whereistime.entity.SubTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qwtangwenqiang on 2016/6/29.
 */
public class MultipleTaskAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context mContext;
    private List<MultipleTask> lists;
    private TaskActivity instance;
    public List<Boolean> listBool;

    public MultipleTaskAdapter(Context context, List<MultipleTask> lists, TaskActivity instance) {
        this.mContext = context;
        this.lists = lists;
        this.inflater = LayoutInflater.from(mContext);
        this.instance = instance;
        listBool = new ArrayList<>();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        //如果缓存convertView为空，则需要创建View
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.multiple_task_item, null);
            viewHolder.tv_task_disc = (TextView) convertView.findViewById(R.id.tv_task_disc);
            viewHolder.ib_goSub = (ImageButton) convertView.findViewById(R.id.ib_goSub);
            viewHolder.ib_add = (ImageButton) convertView.findViewById(R.id.ib_add);
            viewHolder.lv_subtask_list = (ListView) convertView.findViewById(R.id.lv_subtask_list);
            //将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final int fartherId = lists.get(position).getTaskId();
        viewHolder.ib_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                instance.addSubTask(fartherId);
            }
        });

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
                                        instance.deleteMultipleTask(fartherId);
                                        break;
                                }
                            }
                        }).show();
                return false;
            }
        });

        List<SubTask> subList = lists.get(position).getTaskList();
        SubTaskAdapter subTaskAdapter = new SubTaskAdapter(mContext, subList, instance);
        viewHolder.lv_subtask_list.setAdapter(subTaskAdapter);
        Utility.setListViewHeightBasedOnChildren(viewHolder.lv_subtask_list);

        listBool.add(false);
        viewHolder.tv_task_disc.setText(lists.get(position).getTaskDisc());
        final ViewHolder finalViewHolder = viewHolder;
        viewHolder.ib_goSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext,"click it", Toast.LENGTH_SHORT).show();
                if (listBool.get(position) == false) {
                    ((ImageButton)v).setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.muljiantou_select));
                    finalViewHolder.lv_subtask_list.setVisibility(View.VISIBLE);
                    listBool.set(position, true);
                } else {
                    ((ImageButton)v).setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.muljiantou_default));
                    finalViewHolder.lv_subtask_list.setVisibility(View.GONE);
                    listBool.set(position, false);
                }
            }
        });

        return convertView;
    }

    static class ViewHolder {
        public TextView tv_task_disc;
        public ImageButton ib_goSub;
        public ImageButton ib_add;
        public ListView lv_subtask_list;
    }

    View.OnClickListener ibListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
}

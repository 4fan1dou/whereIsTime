package com.me.whereistime.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qwtangwenqiang on 2016/6/29.
 */
public class MultipleTask {
    private int taskId;
    private String taskDisc;
    private String taskLable;

    private List<SubTask> taskList;

    public MultipleTask() {
        taskList = new ArrayList<>();
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getTaskDisc() {
        return taskDisc;
    }

    public void setTaskDisc(String taskDisc) {
        this.taskDisc = taskDisc;
    }

    public String getTaskLable() {
        return taskLable;
    }

    public void setTaskLable(String taskLable) {
        this.taskLable = taskLable;
    }

    public List<SubTask> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<SubTask> taskList) {
        this.taskList = taskList;
    }
}

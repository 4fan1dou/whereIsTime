package com.me.whereistime.entity;

/**
 * Created by qwtangwenqiang on 2016/6/26.
 */
public class SingleTask {
    private int taskId;
    private String taskDisc;
    private String taskLable;
    private String isFinish;

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

    public String getIsFinish() {
        return isFinish;
    }

    public void setIsFinish(String isFinish) {
        this.isFinish = isFinish;
    }
}

package com.ljq.asynctask.model;


/**
 * @author linjunqiang
 * @Date 2021/5/3
 */
public class Task {
    private String taskId;
    private String taskName;
    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Task(taskId=" + this.getTaskId() + ", taskName=" + this.getTaskName() + ", data=" + this.getData() + ")";
    }

    public String getTaskId() {
        return this.taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return this.taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }


}

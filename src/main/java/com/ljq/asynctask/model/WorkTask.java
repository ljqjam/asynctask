package com.ljq.asynctask.model;

import com.ljq.asynctask.handler.IHandler;

/**
 * @author linjunqiang
 * @Date 2021/5/3
 */
public class WorkTask {

    private IHandler handler;
    private Task task;

    public WorkTask(IHandler handler, Task task) {
        this.handler = handler;
        this.task = task;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public IHandler getHandler() {
        return handler;
    }

    public void setHandler(IHandler handler) {
        this.handler = handler;
    }
}

package com.ljq.asynctask.handler;

import com.ljq.asynctask.model.Result;
import com.ljq.asynctask.model.Task;

/**
 * @author linjunqiang
 * @Date 2021/5/4
 */
public interface IHandler {
    String getJobName();

    Result execute(Task task) throws Exception;
}

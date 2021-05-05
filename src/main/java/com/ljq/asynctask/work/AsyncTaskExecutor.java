package com.ljq.asynctask.work;

import com.ljq.asynctask.constant.CusException;
import com.ljq.asynctask.handler.IHandler;
import com.ljq.asynctask.model.Result;
import com.ljq.asynctask.model.Task;
import com.ljq.asynctask.model.WorkTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author linjunqiang
 * @Date 2021/5/5
 */
public class AsyncTaskExecutor {

    private static final Logger log = LoggerFactory.getLogger(AsyncTaskExecutor.class);

    public Result run(WorkTask workTask) {

        Result result;
        try {
            result = workTask.getHandler().execute(workTask.getTask());
            if (!result.getErrno().equals(0)) {
                throw new CusException(result.getErrmsg());
            }
        } catch (Exception exception) {
            log.error("async task error {} : {}", workTask.getTask(), exception.getMessage());
            result = new Result(exception.getMessage());
        }
        return result;
    }

    /**
     * TODO
     * @param task 任务
     */
    public void kill(Task task) {

    }

}

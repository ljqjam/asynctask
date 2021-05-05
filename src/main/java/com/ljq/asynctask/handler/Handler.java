package com.ljq.asynctask.handler;

import com.ljq.asynctask.model.Result;
import com.ljq.asynctask.model.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * @author linjunqiang
 * @Date 2021/5/4
 */
public class Handler implements IHandler {
    private static final Logger log = LoggerFactory.getLogger(Handler.class);
    private final String jobName;
    private final Object target;
    private final Method method;

    public Handler(String jobName, Object target, Method method) {
        this.jobName = jobName;
        this.target = target;
        this.method = method;
    }

    @Override
    public String getJobName() {
        return this.jobName;
    }

    @Override
    public Result execute(Task task) throws Exception {
        log.debug("{} {}", this.target.toString(), this.method.toString());
        return (Result) this.method.invoke(this.target, task);
    }

    @Override
    public String toString() {
        return "JobHandler(jobName=" + this.getJobName() + ", target=" + this.target + ", method=" + this.method + ")";
    }
}

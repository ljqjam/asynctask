package com.ljq.asynctask.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ljq.asynctask.collector.TaskCollector;
import com.ljq.asynctask.constant.CommonConstant;
import com.ljq.asynctask.constant.CusException;
import com.ljq.asynctask.handler.IHandler;
import com.ljq.asynctask.model.Result;
import com.ljq.asynctask.model.Task;
import com.ljq.asynctask.model.WorkTask;
import com.ljq.asynctask.work.TaskCoordinator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * @author linjunqiang
 * @Date 2021/5/4
 * 自定义一个MyServlet处理异步任务
 */
public class MyServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(MyServlet.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private Task getTaskInfo(HttpServletRequest req) throws IOException, CusException {
        Task task = (Task)OBJECT_MAPPER.readValue(req.getInputStream(), Task.class);
        String[] arrStr = StringUtils.split(req.getRequestURI(), CommonConstant.URI . concat("/"));

        //URI的第三级作为注解的值（任务名称）
        if (arrStr != null && arrStr.length == CommonConstant.URI_SPLIT_LEN && !StringUtils.isEmpty(arrStr[1])) {
            task.setTaskName(arrStr[1]);
        } else {
            throw new CusException("任务请求uri配置错误");
        }
        return task;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        log.info("get one task uri:{}", req.getRequestURI());
        Result result = null;

        try {
            Task task = this.getTaskInfo(req);

            log.debug("servlet request run begin {}", task.getTaskId());
            result = this.doWork(task);
            log.debug("servlet request run end {}", task.getTaskId());
        } catch (Exception exception) {
            result = new Result(exception.getMessage());
        }

        this.setResponse(resp, result);
    }

    private void setResponse(HttpServletResponse resp, Result result) throws IOException {
        if (!result.getErrno().equals(0)) {
            log.error("asyncTask servlet request failed {}", result);
            resp.setStatus(500);
        }
        resp.setHeader("Content-type", "application/json;charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(OBJECT_MAPPER.writeValueAsString(result));
    }

    private Result doWork(Task task) throws CusException {
        if (StringUtils.isEmpty(task.getTaskName())) {
            throw new CusException("找不到taskName, taskId:" + task.getTaskId());
        } else {
            IHandler handle = TaskCollector.getHandle(task.getTaskName());
            if (handle == null) {
                throw new CusException("找不到任务配置, " + task.getTaskName() + ",taskId:" + task.getTaskId());
            } else {
                WorkTask workTask = new WorkTask(handle, task);
                return TaskCoordinator.add(workTask);
            }
        }
    }

}

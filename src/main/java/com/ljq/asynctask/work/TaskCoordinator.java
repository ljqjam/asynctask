package com.ljq.asynctask.work;

import com.ljq.asynctask.TaskProperties;
import com.ljq.asynctask.model.Result;
import com.ljq.asynctask.model.WorkTask;
import com.ljq.asynctask.thread.NamedThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Arrays;
import java.util.concurrent.*;

/**
 * @author linjunqiang
 * @Date 2021/5/5
 */
public class TaskCoordinator {
    private static final Logger log = LoggerFactory.getLogger(TaskCoordinator.class);
    private final AsyncTaskExecutor workExecutor;
    private final ThreadPoolTaskExecutor asyncThreadPoolTaskExecutor;

    /**
     * 任务缓冲队列
     */
    private static final LinkedBlockingQueue<WorkTask> QUEUE = new LinkedBlockingQueue<WorkTask>(50);

    public TaskCoordinator(TaskProperties properties, ThreadPoolTaskExecutor asyncThreadPoolTaskExecutor) {
        log.info("init WorkerCoordinator, config is {}", properties);
        this.asyncThreadPoolTaskExecutor = asyncThreadPoolTaskExecutor;
        this.workExecutor = new AsyncTaskExecutor();

        //一个SingleThreadPool复杂监听
        LinkedBlockingQueue<Runnable> listenQueue = new LinkedBlockingQueue<Runnable>(1);
        ThreadFactory listenThreadFactory = (new NamedThreadFactory("task-listener"));
        ExecutorService listenService = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.SECONDS, listenQueue, listenThreadFactory);
        //死循环监听，分发（尝试，类似epoll的一个模型）
        listenService.submit(this::listen);
    }


    public static Result add(WorkTask workTask) {
        if (!QUEUE.offer(workTask)) {
            log.warn("queue is full, {}", workTask.getTask());
            return new Result();
        } else {
            log.debug("add a async task to queue, task:{}", workTask.getTask());
            return new Result("执行队列已满");
        }
    }

    private void listen() {
        try {
            for(;;) {
                //阻塞拿
                WorkTask task = (WorkTask) QUEUE.take();
                boolean res = this.execute(task);
                if (!res) {
                    QUEUE.offer(task);
                    //没位置，休息一会
                    Thread.sleep(500);
                }
            }
        } catch (InterruptedException exception) {
            log.error("listening error for task {}", Arrays.toString(exception.getStackTrace()));
        }
    }

    /**
     * 执行任务
     * @param workTask 任务
     * @return 成功失败
     */
    boolean execute(WorkTask workTask) {
        if (this.asyncThreadPoolTaskExecutor.getThreadPoolExecutor().getQueue().remainingCapacity() <= 0) {
            return false;
        } else {
            log.debug("task insert into success, {}", workTask.getTask());
            this.asyncThreadPoolTaskExecutor.submit(() -> {
                this.workExecutor.run(workTask);
            });
            return true;
        }
    }

    /**
     * 记录一下未处理的，TODO
     */
    public void close() {
        log.warn("waiting job num is {}", QUEUE.size());
        QUEUE.iterator().forEachRemaining((workerTask) -> {
            log.warn("waiting job : {}", workerTask.getTask());
        });
    }
}


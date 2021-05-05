package com.ljq.asynctask.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author linjunqiang
 * @Date 2021/5/4
 */
public class NamedThreadFactory implements ThreadFactory {

    /** 命名前缀 */
    private final String prefix;
    /** 线程组 */
    private final ThreadGroup group;
    private final AtomicInteger threadNumber = new AtomicInteger(1);

    /** 无法捕获的异常统一处理 */
    private final Thread.UncaughtExceptionHandler handler;

    private final Boolean daemon;
    private final Integer priority;

    /**
     * 构造
     *
     * @param prefix 线程名前缀
     */
    public NamedThreadFactory(String prefix) {
        this(prefix, null);
    }

    /**
     * 构造
     *
     * @param prefix 线程名前缀
     * @param threadGroup 线程组，可以为null
     */
    public NamedThreadFactory(String prefix, ThreadGroup threadGroup) {
        this(prefix, threadGroup, null, null, null);
    }

    /**
     * 构造
     * @param prefix 线程名前缀
     * @param threadGroup 线程组，可以为null
     * @param handler 未捕获异常处理
     * @param daemon 是否守护线程
     * @param priority 优先级
     */
    public NamedThreadFactory(String prefix, ThreadGroup threadGroup, Thread.UncaughtExceptionHandler handler, Boolean daemon, Integer priority) {
        this.prefix = prefix;
        this.group = threadGroup;
        this.handler = handler;
        this.daemon = daemon;
        this.priority = priority;
    }

    @Override
    public Thread newThread(Runnable r) {
        final Thread t = new Thread(this.group, r, prefix + threadNumber.getAndIncrement());

        if (daemon != null) {
            t.setDaemon(daemon);
        }
        if (priority != null) {
            t.setPriority(priority);
        }

        //异常处理
        if(null != this.handler) {
            t.setUncaughtExceptionHandler(handler);
        }
        return t;
    }

}
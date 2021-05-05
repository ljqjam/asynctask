package com.ljq.asynctask;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author linjunqiang
 * @Date 2021/5/4
 * TODO 配置不要写死
 */
@Configuration
public class TaskProperties {

    private int workerQueueSize = 5;
    private int workerThreadCount = 5;
    private boolean waitOnShutdown = true;
    private int keepAliveSeconds = 5;


    @Override
    public String toString() {
        return "TaskProperties(workerQueueSize=" + this.getWorkerQueueSize() + ", workerThreadCount=" + this.getWorkerThreadCount() + ", waitOnShutdown=" + this.isWaitOnShutdown() + ", keepAliveSeconds=" + this.getKeepAliveSeconds() + ")";
    }

    public void setWorkerQueueSize(int workerQueueSize) {
        this.workerQueueSize = workerQueueSize;
    }

    public int getWorkerQueueSize() {
        return this.workerQueueSize;
    }

    public void setWorkerThreadCount(int workerThreadCount) {
        this.workerThreadCount = workerThreadCount;
    }

    public int getWorkerThreadCount() {
        return this.workerThreadCount;
    }

    public boolean isWaitOnShutdown() {
        return this.waitOnShutdown;
    }

    public void setWaitOnShutdown(boolean waitOnShutdown) {
        this.waitOnShutdown = waitOnShutdown;
    }

    public int getKeepAliveSeconds() {
        return this.keepAliveSeconds;
    }

    public void setKeepAliveSeconds(int keepAliveSeconds) {
        this.keepAliveSeconds = keepAliveSeconds;
    }
}


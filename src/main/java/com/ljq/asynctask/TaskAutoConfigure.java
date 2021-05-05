package com.ljq.asynctask;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;

import com.ljq.asynctask.collector.TaskCollector;
import com.ljq.asynctask.servlet.MyServletRegistrar;
import com.ljq.asynctask.thread.NamedThreadFactory;
import com.ljq.asynctask.work.TaskCoordinator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author linjunqiang
 * @Date 2021/5/3
 */
@Configuration
public class TaskAutoConfigure {
    private static final Logger log = LoggerFactory.getLogger(TaskAutoConfigure.class);


    @Bean
    public TaskCollector taskCollector() {
        return new TaskCollector();
    }

    @Bean
    @ConditionalOnBean({TaskCollector.class})
    public MyServletRegistrar myServlet() {
        return new MyServletRegistrar();
    }

    @Bean
    public TaskProperties taskProperties() {
        return new TaskProperties();
    }

    @Bean
    public ThreadPoolTaskExecutor asyncThreadPoolTaskExecutor(TaskProperties taskProperties) {
        ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();
        pool.setCorePoolSize(taskProperties.getWorkerThreadCount());
        pool.setMaxPoolSize(taskProperties.getWorkerThreadCount());
        pool.setKeepAliveSeconds(taskProperties.getKeepAliveSeconds());
        pool.setWaitForTasksToCompleteOnShutdown(taskProperties.isWaitOnShutdown());
        pool.setQueueCapacity(taskProperties.getWorkerQueueSize());

        ThreadFactory workerThreadFactory = (new NamedThreadFactory("asynctask-worker"));
        pool.setThreadFactory(workerThreadFactory);
        pool.setRejectedExecutionHandler(new CallerRunsPolicy());
        return pool;
    }

    @Bean(
            destroyMethod = "close"
    )
    public TaskCoordinator taskCoordinator(TaskProperties taskProperties, ThreadPoolTaskExecutor taskExecutor) {
        return new TaskCoordinator(taskProperties, taskExecutor);
    }
}


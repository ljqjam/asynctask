package com.ljq.asynctask.collector;

import com.ljq.asynctask.annotation.AsyncEnable;
import com.ljq.asynctask.annotation.AsyncTask;
import com.ljq.asynctask.constant.CusException;
import com.ljq.asynctask.handler.Handler;
import com.ljq.asynctask.handler.IHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author linjunqiang
 * @Date 2021/5/4
 * 实现ApplicationContextAware注入上下文，SmartInitializingSingleton，在所有bean初始化完成后调用
 */
public class TaskCollector implements ApplicationContextAware, SmartInitializingSingleton {

    private static final Logger log = LoggerFactory.getLogger(TaskCollector.class);
    private ApplicationContext applicationContext;
    private static final ConcurrentMap<String, IHandler> TASK_MAP = new ConcurrentHashMap<String, IHandler>();


    @Override
    public void afterSingletonsInstantiated() {
        this.initJobHandler(this.applicationContext);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


    private void initJobHandler(ApplicationContext applicationContext) {
        if (applicationContext != null) {
            String[] beanNames = applicationContext.getBeanNamesForAnnotation(AsyncEnable.class);

            //遍历所有有AsyncEnable注解的bean
            for (String beanName : beanNames) {
                Object bean = applicationContext.getBean(beanName);
                Map<Method, AsyncTask> methods = null;

                try {
                    //拿加了AsyncTask注解的函数
                    methods = MethodIntrospector.selectMethods(bean.getClass(), (MethodIntrospector.MetadataLookup<AsyncTask>) method -> (AsyncTask) AnnotatedElementUtils.findMergedAnnotation(method, AsyncTask.class));
                } catch (Exception e) {
                    log.error("Async task resolve error for {} {}", beanName, e.getMessage());
                }

                if (!CollectionUtils.isEmpty(methods)) {
                    for (Map.Entry<Method, AsyncTask> entry : methods.entrySet()) {
                        AsyncTask asyncTask = entry.getValue();
                        if (asyncTask != null) {
                            Method method = entry.getKey();
                            method.setAccessible(true);
                            String name = asyncTask.taskName();
                            //TODO 这里可以验证一下函数的入参出参
                            this.registerHandler(name, new Handler(name, bean, method));
                        }
                    }
                }
            }

        }
    }

    public static IHandler getHandle(String name) {
        return (IHandler)TASK_MAP.get(name);
    }

    public void registerHandler(String name, IHandler handler) {
        log.info("register handler {}, {}", name, handler);
        TASK_MAP.put(name, handler);
    }

}

package com.sm.nadaman.common.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ThreadPoolUtil {

    private static ThreadPoolUtil mInstance;
    private ThreadPoolUtil(){

    }
    public static ThreadPoolUtil getInstance(){
        if (mInstance==null){
            synchronized (ThreadPoolUtil.class){
                if (mInstance==null){
                    mInstance=new ThreadPoolUtil();
                }
            }
        }
        return mInstance;
    }
    /**
     * 定长 线程池
     * @param mRunnable
     */
    public void fixedThread(Runnable mRunnable){
        ExecutorService executorService=Executors.newFixedThreadPool(4);
        executorService.execute(mRunnable);
    }

    /**
     * 延迟加载 的线程池
     * @param mRunnable
     */
    public void  scheduleThread(Runnable mRunnable){
        ScheduledExecutorService scheduledExecutorService=Executors.newScheduledThreadPool(2);
        scheduledExecutorService.schedule(mRunnable,2,TimeUnit.SECONDS);
    }
}

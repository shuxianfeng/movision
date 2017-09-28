package com.movision.fsearch.service.impl;


import com.movision.fsearch.service.IJobService;
import com.movision.utils.G;
import com.movision.utils.L;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Timer;
import java.util.TimerTask;

@Service
public class JobService implements IJobService {
    //调度控制器
    private Timer timer = null;

    public interface RepeatJob {
        void run(boolean firstTime);
    }

    @PostConstruct
    public void init() throws Exception {
        timer = new Timer();
    }

    @PreDestroy
    public void destroy() throws Exception {
        timer.cancel();
        timer = null;
    }

    /**
     * 计划定时任务
     *
     * @param task
     * @param periodKey
     * @param runAtOnce 是否立即执行
     */
    public void scheduleRepeatJob(final RepeatJob task, final String periodKey,
                                  boolean runAtOnce) {
        if (runAtOnce) {
            task.run(true);
        }
        doScheduleRepeatJob(task, periodKey);
    }

    /**
     * 执行重复任务（递归实现）
     *
     * @param task
     * @param periodKey
     */
    private void doScheduleRepeatJob(final RepeatJob task,
                                     final String periodKey) {

        if (timer == null) {
            L.warn("JobService - timer is null");
            return;
        }
        //延时时间，默认1分钟后开始执行
        long period = (long) (G.getConfig().getDouble(periodKey, 1d) * 60 * 1000);

        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                TaskManager.addTask(new Runnable() {

                    @Override
                    public void run() {
                        task.run(false);
                        doScheduleRepeatJob(task, periodKey);
                    }

                });
            }

        }, period);
    }

}

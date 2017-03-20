package com.movision.fsearch.service.impl;


import com.movision.utils.TaskEngine;

public class TaskManager {
    private static final TaskEngine ENGINE = new TaskEngine(2);

    public static void addTask(Runnable runnable) {
        ENGINE.addTask(runnable);
    }
}

package com.movision.fsearch.service;


import com.movision.fsearch.service.impl.JobService;

public interface IJobService {

    void scheduleRepeatJob(final JobService.RepeatJob task, final String periodKey,
                           boolean runAtOnce);
}

package com.zhuhuibao.service;

import com.zhuhuibao.service.impl.JobService.RepeatJob;

public interface IJobService {

	void scheduleRepeatJob(final RepeatJob task, final String periodKey,
			boolean runAtOnce);
}

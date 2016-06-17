package com.zhuhuibao.fsearch.service;

import com.zhuhuibao.fsearch.service.impl.JobService.RepeatJob;

public interface IJobService {

	void scheduleRepeatJob(final RepeatJob task, final String periodKey,
			boolean runAtOnce);
}

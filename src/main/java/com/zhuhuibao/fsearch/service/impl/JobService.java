package com.zhuhuibao.fsearch.service.impl;

import com.zhuhuibao.fsearch.service.IJobService;
import com.zhuhuibao.utils.G;
import com.zhuhuibao.utils.L;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Timer;
import java.util.TimerTask;

@Service
public class JobService implements IJobService {
	private Timer timer = null;

	public static interface RepeatJob {
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

	public void scheduleRepeatJob(final RepeatJob task, final String periodKey,
			boolean runAtOnce) {
		if (runAtOnce) {
			task.run(true);
		}
		doScheduleRepeatJob(task, periodKey);
	}

	private void doScheduleRepeatJob(final RepeatJob task,
			final String periodKey) {

		if (timer == null) {
			L.warn("JobService - timer is null");
			return;
		}

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

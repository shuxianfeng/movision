package com.movision.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

public class TaskEngine {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(TaskEngine.class);

	/**
	 * A queue of tasks to be executed.
	 */
	private volatile LinkedList<Runnable> taskList = new LinkedList<Runnable>();

	/**
	 * An array of worker threads.
	 */
	private Thread[] workers = null;

	/**
	 * A Timer to perform periodic tasks.
	 */
	private Timer taskTimer = null;

	private Object lock = new Object();

	// TODO thread pool
	// corePoolSize, maximumPoolSize, keepAliveTime
	public TaskEngine(int workerNum) {
		taskTimer = new Timer(true);
		workers = new Thread[workerNum];
		for (int i = 0; i < workers.length; i++) {
			TaskEngineWorker worker = new TaskEngineWorker();
			workers[i] = new Thread(worker);
			workers[i].setDaemon(true);
			workers[i].start();
		}
	}

	/**
	 * Adds a task to the task queue with normal priority. The task will be
	 * executed immediately provided there is a free worker thread to execute
	 * it. Otherwise, it will execute as soon as a worker thread becomes
	 * available.
	 */
	public void addTask(Runnable r) {
		addTask(r, Thread.NORM_PRIORITY);
	}

	/**
	 * @param priority
	 */
	public void addTask(Runnable task, int priority) {
		synchronized (lock) {
			taskList.addFirst(task);
			// Notify the worker threads. The notifyAll() methods notifies all
			// threads waiting on this object.
			lock.notifyAll();
		}
	}

	public int getTaskSize() {
		return taskList.size();
	}

	public boolean allTasksDone() {
		synchronized (lock) {
			return taskList.size() == 0;
		}
	}

	/**
	 * Schedules a task to periodically run. This is useful for tasks such as
	 * updating search indexes, deleting old data at periodic intervals, etc.
	 * 
	 * @param task
	 *            task to be scheduled.
	 * @param delay
	 *            delay in milliseconds before task is to be executed.
	 * @param period
	 *            time in milliseconds between successive task executions.
	 * @return a TimerTask object which can be used to track executions of the
	 *         task and to cancel subsequent executions.
	 */
	public TimerTask scheduleTask(Runnable task, long delay, long period) {
		TimerTask timerTask = new ScheduledTask(task);
		taskTimer.schedule(timerTask, delay, period);
		return timerTask;
	}

	public TimerTask scheduleTask(Runnable task, long delay) {
		TimerTask timerTask = new ScheduledTask(task);
		taskTimer.schedule(timerTask, delay);
		return timerTask;
	}

	/**
	 * Return the next task in the queue. If no task is available, this method
	 * will block until a task is added to the queue.
	 * 
	 * @return a <code>Updater</code> object
	 */
	private Runnable nextTask() {
		synchronized (lock) {
			// Block until we have another object in the queue to execute.
			while (taskList.isEmpty()) {
				try {
					lock.wait();
				} catch (InterruptedException ie) {
				}
			}
			return (Runnable) taskList.removeLast();
		}
	}

	/**
	 * A worker thread class which executes <code>Updater</code> objects.
	 */
	private class TaskEngineWorker implements Runnable {

		private boolean done = false;

		/**
		 * Get tasks from the task engine. The call to get another task will
		 * block until there is an available task to execute.
		 */
		public void run() {
			while (!done) {
				try {
					nextTask().run();
				} catch (Throwable t) {
					LOGGER.error(null, t);
				}
			}
		}
	}

	/**
	 * A subclass of TimerClass that passes along a Runnable to the task engine
	 * when the scheduled task is run.
	 */
	private class ScheduledTask extends TimerTask {

		private Runnable task;

		public ScheduledTask(Runnable task) {
			this(task, Thread.NORM_PRIORITY);
		}

		/**
		 * @param priority
		 */
		public ScheduledTask(Runnable task, int priority) {
			this.task = task;
		}

		public void run() {
			// Put the task into the queue to be run as soon as possible by a
			// worker.
			addTask(task);
		}
	}
}

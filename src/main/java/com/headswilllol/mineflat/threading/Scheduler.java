package com.headswilllol.mineflat.threading;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Scheduler {

	private static HashMap<Integer, Task> tasks = new HashMap<Integer, Task>();

	private static List<Integer> tasksToRun = new ArrayList<Integer>();
	private static List<Integer> asyncTasksToRun = new ArrayList<Integer>();
	private static HashMap<Integer, Thread> asyncRunningTasks = new HashMap<Integer, Thread>();

	public static Task runTask(Runnable runnable){
		return runTaskLater(runnable, 0);
	}

	public static Task runTaskLater(Runnable runnable, int delay){
		Task t = new Task(runnable, delay);
		tasks.put(t.getTaskId(), t);
		tasksToRun.add(t.getTaskId());
		return t;
	}

	public static Task runAsyncTask(Runnable runnable){
		return runTaskLater(runnable, 0);
	}

	public static Task runAsyncTaskLater(Runnable runnable, int delay){
		Task t = new Task(runnable, delay);
		tasks.put(t.getTaskId(), t);
		asyncTasksToRun.add(t.getTaskId());
		return t;
	}

	public static void checkTasks(){
		List<Integer> tRemove = new ArrayList<Integer>();
		for (Integer i : tasksToRun) {
			if (System.currentTimeMillis() - tasks.get(i).getTimeScheduled() >= tasks.get(i).getDelay()) {
				tRemove.add(i);
				tasks.get(i).getRunnable().run();
				tasks.remove(i);
			}
		}
		tasksToRun.removeAll(tRemove);
		List<Integer> atRemove = new ArrayList<Integer>();
		for (Integer i : asyncTasksToRun) {
			if (System.currentTimeMillis() - tasks.get(i).getTimeScheduled() >= tasks.get(i).getDelay()) {
				atRemove.add(i);
				Thread t = new Thread(tasks.get(i).getRunnable());
				t.start();
				asyncRunningTasks.put(i, t);
			}
		}
		asyncTasksToRun.removeAll(atRemove);
		List<Integer> artRemove = new ArrayList<Integer>();
		for (Integer i : asyncRunningTasks.keySet()){
			if (!asyncRunningTasks.get(i).isAlive()){
				artRemove.add(i);
				tasks.remove(i);
			}
		}
		for (Integer i : artRemove){
			asyncRunningTasks.remove(i);
		}
	}

}

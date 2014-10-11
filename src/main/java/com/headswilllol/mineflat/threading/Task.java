package com.headswilllol.mineflat.threading;

public class Task {

	private static int nextId = 0;

	private int id;
	private Runnable runnable;
	private long timeScheduled;
	private int delay;

	Task(Runnable runnable, int delay){
		this.runnable = runnable;
		this.delay = delay;
		this.timeScheduled = System.currentTimeMillis();
		this.id = nextId;
		nextId += 1;
	}

	Task(Runnable runnable){
		this(runnable, 0);
	}

	public int getTaskId(){
		return id;
	}

	public Runnable getRunnable(){
		return runnable;
	}

	public long getTimeScheduled(){
		return timeScheduled;
	}

	public int getDelay(){
		return delay;
	}

}

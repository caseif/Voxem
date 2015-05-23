/*
 * Voxem
 * Copyright (c) 2014-2015, Maxim Roncacé <caseif@caseif.net>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package net.caseif.voxem.threading;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Scheduler {

    private static HashMap<Integer, Task> tasks = new HashMap<>();

    private static List<Integer> tasksToRun = new ArrayList<>();
    private static List<Integer> asyncTasksToRun = new ArrayList<>();
    private static HashMap<Integer, Thread> asyncRunningTasks = new HashMap<>();

    private static final Object ASYNC_LOCK = new Object();

    public static Task runTask(Runnable runnable) {
        return runTaskLater(runnable, 0);
    }

    public static Task runTaskLater(Runnable runnable, int delay) {
        Task t = new Task(runnable, delay);
        tasks.put(t.getTaskId(), t);
        tasksToRun.add(t.getTaskId());
        return t;
    }

    public static Task runAsyncTask(Runnable runnable) {
        return runAsyncTaskLater(runnable, 0);
    }

    public static Task runAsyncTaskLater(Runnable runnable, int delay) {
        Task t = new Task(runnable, delay);
        tasks.put(t.getTaskId(), t);
        synchronized (ASYNC_LOCK) {
            asyncTasksToRun.add(t.getTaskId());
        }
        return t;
    }

    public static void checkTasks() {
        List<Integer> tRemove = new ArrayList<>();
        for (Integer i : tasksToRun) {
            if (System.currentTimeMillis() - tasks.get(i).getTimeScheduled() >= tasks.get(i).getDelay()) {
                tRemove.add(i);
                tasks.get(i).getRunnable().run();
                tasks.remove(i);
            }
        }
        tasksToRun.removeAll(tRemove);
        List<Integer> atRemove = new ArrayList<>();
        synchronized (ASYNC_LOCK) {
            for (Integer i : asyncTasksToRun) {
                if (System.currentTimeMillis() - tasks.get(i).getTimeScheduled() >= tasks.get(i).getDelay()) {
                    atRemove.add(i);
                    Thread t = new Thread(tasks.get(i).getRunnable());
                    t.start();
                    asyncRunningTasks.put(i, t);
                }
            }
            asyncTasksToRun.removeAll(atRemove);
            List<Integer> artRemove = new ArrayList<>();
            for (Integer i : asyncRunningTasks.keySet()) {
                if (!asyncRunningTasks.get(i).isAlive()) {
                    artRemove.add(i);
                    tasks.remove(i);
                }
            }
            for (Integer i : artRemove) {
                asyncRunningTasks.remove(i);
            }
        }
    }

}

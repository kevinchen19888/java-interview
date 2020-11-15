package com.kevin.code.concurrent;

import java.util.stream.IntStream;

/**
 * ava程序中对线程所设置的优先级只是给操作系统一个建议，操作系统不一定会采纳。
 * 而真正的调用顺序，是由操作系统的线程调度算法决定的
 *
 * @author kevin
 */
public class ThreadPriorityDemo {
    public static void main(String[] args) {
        //testThreadPriority();
        //threadGroupPriority();
        testDaemonThread();
    }

    private static void testThreadPriority() {
        IntStream.range(1, 10).forEach(i -> {
            Thread thread = new Thread(new T1(i));
            thread.setPriority(i);
            thread.start();
        });
    }

    /**
     * 测试线程和线程组的优先级不一致的时候的运行策略:
     * 如果某个线程优先级大于线程所在线程组的最大优先级，那么该线程的优先级将会失效，取而代之的是线程组的最大优先级
     */
    public static void threadGroupPriority() {
        ThreadGroup threadGroup = new ThreadGroup("t1");
        threadGroup.setMaxPriority(9);

        Thread thread = new Thread(threadGroup, "thread");
        thread.setPriority(5);
        System.out.println("我是线程组的优先级" + threadGroup.getMaxPriority());
        System.out.println("我是线程的优先级" + thread.getPriority());
    }

    /**
     */
    public static void testDaemonThread() {
        // 复制一个线程数组到一个线程组
        DaemonThread daemonThread = new DaemonThread();
        daemonThread.run();

    }

    private static class DaemonThread implements Runnable {
        @Override
        public void run() {
            //Thread.currentThread().setDaemon(true);
            System.out.println(Thread.currentThread().getName());
            System.out.printf("当前线程是否是守护线程:%s,优先级:%s", Thread.currentThread().isDaemon(), Thread.currentThread().getPriority());
        }
    }


    public static class T1 extends Thread {
        private int threadSuffix;

        public T1(int threadSuffix) {
            this.threadSuffix = threadSuffix;
        }

        @Override
        public void run() {
            super.run();
            Thread.currentThread().setName("T1-thread-" + threadSuffix);
            System.out.println(String.format("当前执行的线程是：%s，优先级：%d",
                    Thread.currentThread().getName(),
                    Thread.currentThread().getPriority()));
        }
    }
}

package com.kevin.code.concurrent;

/**
 * ThreadLocal用法实例
 * 每个线程都保存了一份 ThreadLocal的变量副本
 * 应用场景:
 * ThreadLocal 适用于每个线程需要自己独立的实例且该实例需要在方多个法中被使用，也即变量在线程间隔离而在方法或类间共享的场景
 *
 * @author kevin
 */
public class ThreadLocalDemo {
    private static final ThreadLocal<Long> longLocal = ThreadLocal.withInitial(() -> Thread.currentThread().getId());
    private static final ThreadLocal<String> stringLocal = ThreadLocal.withInitial(() -> Thread.currentThread().getName());

    public void set() {
        longLocal.set(Thread.currentThread().getId());
        stringLocal.set(Thread.currentThread().getName());
    }

    public long getLong() {
        return longLocal.get();
    }

    public String getString() {
        return stringLocal.get();
    }

    public static void main(String[] args) throws InterruptedException {
        final ThreadLocalDemo test = new ThreadLocalDemo();
        // 设置main线程
        test.set();

        System.out.println("main:" + test.getLong());
        System.out.println("main:" + test.getString());
        System.out.println("=============");

        // 设置thread1
        Thread thread1 = new Thread(() -> {
            test.set();
            System.out.println("thread1:" + test.getLong());
            System.out.println("thread1:" + test.getString());
            System.out.println("=============");
        });
        thread1.start();
        thread1.join();

        System.out.println("main:" + test.getLong());
        System.out.println("main:" + test.getString());
    }
    /*
     * output:
     * 1,main线程的id&name;
     * 2,thread1线程的id&name
     * 3,main线程的id&name;
     * */

}

package com.kevin.code.javabase.base;

import lombok.SneakyThrows;

public class LockDemo implements Runnable {

    public synchronized void get() {
        System.out.println("get method:" + Thread.currentThread().getId());
        set();
    }

    public synchronized void set() {
        System.out.println("set method:" + Thread.currentThread().getId());
    }

    @SneakyThrows
    @Override
    public void run() {
        get();
        Thread.sleep(1000);
    }

    @SneakyThrows
    public static void main(String[] args) {
        LockDemo ss = new LockDemo();
        Thread thread = new Thread(ss);
        thread.start();
        //new Thread(ss).start();
        //new Thread(ss).start();
        thread.join();
        System.out.println("main exec ok!");
    }



}

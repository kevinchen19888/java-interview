package com.kevin.code.javabase.base;

/**
 * @author kevin
 */
public class SynchronizedDemo {

    public void method() {
        synchronized (this) {
            System.out.println("synchronized code block");
        }
    }

    public synchronized void method2() {
        System.out.println("synchronized method");
    }
}

package com.kevin.code.javabase.base;

import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.Scanner;

/**
 * @author kevin
 */
public final class FinalKeyDemo implements Serializable {
    final int age = 0;

    public final void finalTest() {
        final String name = "kevin";
        System.out.println(name);
    }


    public static int f(int value) {
        try {
            return value * value;
        } finally {
            if (value == 2) {
                System.out.println("finally exec");
                return 0;
            }
        }
    }

    @SneakyThrows
    public static void main(String[] args) {
        //System.out.println(f(2));;
        //scannerTest();
        //streamReaderTest();

        //int hash = 10;
        //int len = 8;
        //System.out.println(hash % len == (hash & (len - 1)));
        // 􁞴􀝐 Java 􁕚􁑕􁓕􁉘 MXBean
        //dumpThreadInfo();

        //threadStartMethod();
    }

    /**
     * 调⽤ start ⽅法⽅可启动线程并使线程进⼊就绪状态，⽽ run ⽅法只是 thread 的⼀个普通
     * ⽅法调⽤，还是在主线程⾥执⾏
     */
    private static void threadStartMethod() {
        Thread1 thread1 = new Thread1();
        thread1.run(); // 直接调用 run 方法本质上是main线程执行
        //thread1.start();
        System.out.println("main 执行完成");
    }

    private static class Thread1 extends Thread {

        @Override
        public void run() {
            System.out.println("当前线程为:" + Thread.currentThread().getName());
        }
    }

    private static void dumpThreadInfo() {
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        ThreadInfo[] threadInfos = threadMXBean.dumpAllThreads(false,
                false);
        for (ThreadInfo threadInfo : threadInfos) {
            System.out.println("[" + threadInfo.getThreadId() + "] " +
                    threadInfo.getThreadName());
        }
    }

    public static void scannerTest() {
        Scanner scanner = null;
        try {
            scanner = new Scanner(System.in);
            System.out.println("please input:");
            String input = scanner.nextLine();
            System.out.println("your input is:" + input);
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
    }

    public static void streamReaderTest() throws IOException {
        BufferedReader input = new BufferedReader(new
                InputStreamReader(System.in));
        System.out.println("please input:");
        String s = input.readLine();
        System.out.println("your input is:" + s);
        input.close();
    }


}

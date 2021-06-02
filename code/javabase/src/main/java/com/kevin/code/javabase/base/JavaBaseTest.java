package com.kevin.code.javabase.base;

import org.junit.Test;

/**
 * @author kevin
 */
public class JavaBaseTest {

    @Test
    public void autoIncrementTest() {
        int i = 1;
        i = i++;
        int j = i++;
        int k = i + ++i * i++;
        System.out.println("i="+i);
        System.out.println("j="+j);
        System.out.println("k="+k);
    }

    public static void main(String[] args) {
        Singleton.SINGLETON.hello();
    }


    enum Singleton {
        SINGLETON;

        public void hello() {
            System.out.println("hello world!");
        }

    }
}


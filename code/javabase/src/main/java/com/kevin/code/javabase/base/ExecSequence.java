package com.kevin.code.javabase.base;

import lombok.Data;

/**
 * 具有继承关系的父子类中,静态代码块&构造代码块&构造方法执行顺序demo
 *
 * @author kevin
 */

public class ExecSequence {
    public static void main(String[] args) {
        ExecSequence execSequence = new SubExecSequence();
        //ExecSequence execSequence = new ExecSequence();
        SubExecSequence subExecSequence = new SubExecSequence();
    }

    static {
        System.out.println("ExecSequence静态代码块run...");
    }

    {
        System.out.println("ExecSequence构造代码块run...");
    }

    public ExecSequence() {
        System.out.println("ExecSequence构造方法run...");
    }

}

@Data
class SubExecSequence extends ExecSequence {
    static {
        System.out.println("SubExecSequence静态代码块run...");
    }

    {
        System.out.println("SubExecSequence构造代码块run...");
    }

    public SubExecSequence() {
        System.out.println("SubExecSequence构造方法执行");
    }
}



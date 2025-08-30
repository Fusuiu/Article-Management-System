package com.itheima;

import org.junit.jupiter.api.Test;

public class ThreadLocalTest {
    @Test
    public  void testThreadLocalSetAndGet(){
        //提供一个ThreadLocal对象
        ThreadLocal tl =new ThreadLocal();
        //开启两个线程
        new Thread(()->{
            //在线程中存储局部变量
            tl.set("萧炎");
            //输出当前线程的名字和线程中的局部变量
            System.out.println(Thread.currentThread().getName()+tl.get());
            System.out.println(Thread.currentThread().getName()+tl.get());
            System.out.println(Thread.currentThread().getName()+tl.get());
        },"蓝色").start();
        new Thread(()->{
            //在线程中存储局部变量
            tl.set("药尘");
            //输出当前线程的名字和线程中的局部变量
            System.out.println(Thread.currentThread().getName()+tl.get());
            System.out.println(Thread.currentThread().getName()+tl.get());
            System.out.println(Thread.currentThread().getName()+tl.get());
        },"绿色").start();

    }
}

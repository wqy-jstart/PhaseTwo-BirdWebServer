package com.webserver.test;

/**
 * ★单例模式
 * java23种设计模式之一
 *
 * 使用该模式定义的类全局仅可创建一个实例
 */
public class SingletonDemo {
    public static void main(String[] args) {
        Singleton s1 = Singleton.getInstance();
        System.out.println(s1);//com.webserver.test.Singleton@232204a1
        Singleton s2 = Singleton.getInstance();
        System.out.println(s2);//com.webserver.test.Singleton@232204a1
    }
}

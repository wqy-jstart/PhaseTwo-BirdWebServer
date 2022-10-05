package com.webserver.test;

/**
 *  ★单例模式(java23种设计模式之一)
 *  1:私有化构造器(防止外界通过new来实例化对象)
 *  2:提供一个静态方法用于将当前类实例返回给外界
 *  3:提供一个静态的私有的当前类型实例的属性并初始化(确保只有一个实例)
 */
public class Singleton {
    //私有化并静态的创建一个对象(确保唯一性)
    //使得该引用Singleton在调用getInstance()方法后只返回一种对象
    private static Singleton instance = new Singleton();

    private Singleton(){}//私有化构造器
    //getInstance()该静态方法负责返回一个对象(正常情况调多少返回多少不同对象,但是若为单例模式,只能有一个对象)
    public static Singleton getInstance(){
        return instance;
    }
}

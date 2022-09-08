package com.webserver.test;

import java.util.Arrays;

/**
 * 字符串拆分split(String regex,int limit)方法重载的演示
 */
public class SplitDemo {
    public static void main(String[] args) {
        String line = "1=2=3=4=5=6=======";
        String[] array = line.split("=");
        System.out.println(Arrays.toString(array));
        //[1, 2, 3, 4, 5, 6]

        //limit=2 表达仅拆分为两项
        array = line.split("=",2);
        System.out.println(Arrays.toString(array));
        //[1, 2=3=4=5=6=======]

        //limit=3  表达仅拆分为三项
        array = line.split("=",3);
        System.out.println(Arrays.toString(array));
        //[1, 2, 3=4=5=6=======]

        //当可拆分项不足limit时,仅保留所有可拆分项
        array = line.split("=",100);
        System.out.println(Arrays.toString(array));
        //[1, 2, 3, 4, 5, 6, , , , , , , ]

        //当limit为0时,作用与split(String regex)一致
        array = line.split("=",0);
        System.out.println(Arrays.toString(array));
        //[1, 2, 3, 4, 5, 6]

        //limit为负数时为应拆尽拆并且全保留
        array = line.split("=",-1);
        System.out.println(Arrays.toString(array));
        //[1, 2, 3, 4, 5, 6, , , , , , , ]
    }
}

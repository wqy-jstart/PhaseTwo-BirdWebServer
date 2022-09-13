package com.webserver.test;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * 利用URLDecoder解码器将16进制按照UTF-8转换为汉字
 */
public class URLDecoderDemo {
    public static void main(String[] args) {
        String str = "武";
        //一个中文占据三个字节
        byte[] data = str.getBytes(StandardCharsets.UTF_8);
        //11100110 10101101 10100110-------对应二进制
        System.out.println(Arrays.toString(data));//[-26, -83, -90]

        str = "%E6%AD%A6%E6%B8%85%E6%BA%90";
        try {
            str = URLDecoder.decode(str,"UTF-8");//URLDecoder解码器
            System.out.println(str);//武清源
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}

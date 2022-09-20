package com.webserver.test;

import javax.activation.MimetypesFileTypeMap;

/**
 * java有现成的API可以解析不同的文件所对应的Content(内容)-Type(类型)
 * new MimetypesFileTypeMap()实例调用getContentType()方法传入文件内容类型返回字符串
 */
public class ContentTypeDemo {
    public static void main(String[] args) {
        String contentType
        =new MimetypesFileTypeMap().getContentType("123.png");
        System.out.println(contentType);//image/png
    }
}

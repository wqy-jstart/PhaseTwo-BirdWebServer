package com.webserver.core;
/**
 * 项目的主类，用于启动WebServer(网络服务器)
 * 该项目主要功能是实现Tomcat这个WebServer的核心功能。了解HTTP协议以及浏览器与服务端
 * 之间的处理细节。
 * 了解SpringMVC核心类的设计与实现
 */

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 服务端
 */
public class BirdBootApplication {
    private ServerSocket serverSocket;
    public BirdBootApplication(){
        try {
            System.out.println("正在启动服务器...");
            serverSocket = new ServerSocket(8088);//服务端定义一个端口号
            System.out.println("服务端启动完毕!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start(){
        try {
            System.out.println("等待客户端连接...");
            //服务端借助Socket引用类型调用accept()方法等待客户端连接
            Socket socket = serverSocket.accept();
            System.out.println("一个客户端连接了!");
            //实例化ClientHandler处理该客户端交互
            ClientHandler handler = new ClientHandler(socket);
            Thread t = new Thread(handler);//创建线程传入handler
            t.start();//★启动该线程,等待CPU分配时间片之后启动线程run()方法
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        BirdBootApplication application = new BirdBootApplication();
        application.start();//利用该对象引用打点调用start()方法
    }
}

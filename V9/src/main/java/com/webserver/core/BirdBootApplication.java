package com.webserver.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 项目的主类，用于启动WebServer(网络服务器)
 * 该项目主要功能是实现Tomcat这个WebServer的核心功能。了解HTTP协议以及浏览器与服务端
 * 之间的处理细节。
 * 了解SpringMVC核心类的设计与实现
 */
/**
 * 服务端
 */
public class BirdBootApplication {
    private ServerSocket serverSocket;
    public BirdBootApplication(){
        try{
            System.out.println("正在启动服务器...");
            serverSocket = new ServerSocket(8088);
            System.out.println("服务端启动完毕！");
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void start(){
        try {
            System.out.println("等待客户端连接...");
            Socket socket = serverSocket.accept();
            System.out.println("一个客户端连接了!");
            //启动一个线程处理该客户端交互
            ClientHandler handler = new ClientHandler(socket);
            Thread t = new Thread(handler);
            t.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        BirdBootApplication application = new BirdBootApplication();
        application.start();
    }
}

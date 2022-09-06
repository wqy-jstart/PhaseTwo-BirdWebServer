package com.webserver.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * 该线程任务负责处理与某个客户端的一次HTTP交互
 * 由于HTTP协议要求客户端和服务端采取一问一答的原则，因此这里处理与客户端的一次交互规划
 * 为三个步骤:
 * 1:解析请求(将客户端发送过来的请求内容读取到)
 * 2:处理请求(根据请求内容进行对应的处理)
 * 3:发送响应(将处理结果回馈给浏览器)
 * 断开连接
 */
public class ClientHandler implements Runnable{
    //Socket封装了TCP协议的通讯细节,使用它就可以和远端计算机建立TCP连接,并基于
    //两条流(一个输出一个输入流)与远端计算机交互数据
    private Socket socket;
    public ClientHandler(Socket socket){
        this.socket=socket;
    }

    @Override
    public void run() {
        try {
            InputStream in = socket.getInputStream();//通过socket获取输入流
            int d;//接收read()方法返回读取1个字节二进制低八位内容(一次读一个)
            while ((d = in.read()) != -1){//当为-1时说明字节读取到末尾
                System.out.print((char)d);//
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

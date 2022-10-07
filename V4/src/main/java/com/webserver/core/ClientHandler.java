package com.webserver.core;

import com.webserver.http.HttpServletRequest;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * 该线程任务负责处理与某个客户端的一次HTTP交互
 * 由于HTTP协议要求客户端和服务端采取一问一答的原则，因此这里处理与客户端的一次交互规划
 * 为三个步骤:
 * 1:解析请求(将客户端发送过来的请求内容读取到)----请求行,消息头,消息正文
 * 2:处理请求(根据请求内容进行对应的处理)
 * 3:发送响应(将处理结果回馈给浏览器)
 * 断开连接
 */
public class ClientHandler implements Runnable {
    //Socket封装了TCP协议的通讯细节,使用它就可以和远端计算机建立TCP连接,并基于
    //两条流(输出和输入)与远端计算机交互数据
    private Socket socket;
    public ClientHandler(Socket socket){
        this.socket=socket;
    }

    @Override
    public void run() {
        try {
            //将socket传入实例化对象中,此处需自行处理异常
            HttpServletRequest request = new HttpServletRequest(socket);//实例化request
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            //按照HTTP协议要求,处理最后要断开连接
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

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
            InputStream in = socket.getInputStream();//通过socket获取输入流
            StringBuilder builder = new StringBuilder();//利用StringBuilder可以实现对读取的字符进行追加操作
            char pre='a',cur='a';//pre表示上次读取的字符,cur表示本次读取的字符
            int d;//接收read()方法返回读取1个字节二进制低八位内容(一次读一个)
            while ((d = in.read()) !=-1){//当为-1时说明流读取到了末尾
                cur = (char) d;//赋给本次读取的字符
                if (pre==13&cur==10){
                    break;//跳出循环
                }
                builder.append(cur);//追加本次读取到的字符
                pre = cur;//进入下一个循环前将本次读取的字符记作上次读取的字符
            }
            String line = builder.toString().trim();//这里StringBuilder重写了toString()方法,调用并去除空格赋给line
            System.out.println("请求行内容: "+line);
            //请求行相关信息
            String method;//请求方式
            String uri;//抽象路径
            String protocol;//协议版本
            //将请求行内容按照空格来拆分,并返回一个字符串数组
            String[] data = line.split("\\s");
            method = data[0];
            uri = data[1];
            protocol = data[2];
            System.out.println("method:"+method);
            System.out.println("uri:"+uri);
            System.out.println("protocol:"+protocol);
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

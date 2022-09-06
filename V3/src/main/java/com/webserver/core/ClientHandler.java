package com.webserver.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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
    private Socket socket;
    public ClientHandler(Socket socket){
        this.socket=socket;
    }

    @Override
    public void run() {
        try {
           String line = readline();//接收返回的请求行内容
            System.out.println("请求行内容："+line);
            //请求的相关信息
            String method;//请求方式
            String uri;//抽象路径
            String protocol;//协议版本

            String[] data = line.split("\\s");//按空格拆分
            System.out.println("拆分后为："+ Arrays.toString(data));//分了3个元素
            method = data[0];//获取元素1,赋值给请求方式
            uri = data[1];//获取元素2,赋值给抽象路径
            protocol = data[2];//获取元素3,赋值给协议版本
            //打桩输出
            System.out.println("method:"+method);
            System.out.println("uri:"+uri);
            System.out.println("protocol:"+protocol);

            //读取消息头
            Map<String,String> headers = new HashMap<>();
            while (true) {
                line = readline();//循环调用readline()方法读一行
                if (line.isEmpty()){//如果读取到的是空字符串,说明单独读取到了CRLF
                    break;//读取到最后为空时结束
                }
                System.out.println("消息头："+line);
                //将消息头按照":"拆分为名字和值并作为key,value存入到headers中
                data = line.split(":\\s");
                //System.out.println(Arrays.toString(data));//输出拆分后的消息头
                headers.put(data[0],data[1]);//将截取的两部分分为key和value
            }
            System.out.println("headers:"+headers);//Map输出的格式为(Key=Value)

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            //按照HTTP协议要求，处理最后要断开连接
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //该方法实现调用一次读一行读到CRLF(回车换行)时一行结束
    public String readline() throws IOException { //重用的代码要抛出去
        //调用同一个socket对象若干次getInputStream()方法返回的始终是同一条输入流
        InputStream in = socket.getInputStream();//通过socket获取输入流
        StringBuilder builder = new StringBuilder();
        char pre='a',cur='a';//pre表示上次读取的字符，cur表示本次读取的字符
        int d;//接收read()方法返回读取1个字节二进制低八位内容(一次读一个)
        while ((d = in.read()) != -1){//当为-1时说明字节读取到末尾
            cur = (char)d;//本次读到的字符
            if (pre==13&&cur==10){//若上一个读取的是回车符并且本次读取的是换行符
                break;//跳出循环,相当于以行为单位读取
            }
            builder.append(cur);//拼接本次读取到的字符
            pre = cur;//进入下一次循环前将本次读取的字符记作上次读取的字符
        }
        return builder.toString().trim();//该返回值类型为String型,变量为line。
    }
}

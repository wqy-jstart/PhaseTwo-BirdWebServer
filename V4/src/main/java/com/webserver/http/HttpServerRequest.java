package com.webserver.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 请求对象,该类的每一个实例用于标识HTTP协议规定的请求内容
 * 一个请求由三部分构成:
 * 1:请求行
 * 2:
 */
public class HttpServerRequest {
    private Socket socket;
    public HttpServerRequest(Socket socket) throws IOException {
        this.socket=socket;
        String line = readline();//接收返回的请求行内容,若接收失败就不用解析并给浏览器发信号
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
            line = readline();
            if (line.isEmpty()){//如果读取到的是空字符串,说明单独读取到了CRLF
                break;
            }
            System.out.println("消息头："+line);
            //将消息头按照":"拆分为名字和值并作为key,value存入到headers中
            data = line.split(":\\s");
            //System.out.println(Arrays.toString(data));//输出拆分后的消息头
            headers.put(data[0],data[1]);
        }
        System.out.println("headers:"+headers);//Map输出的格式为(Key=Value)
    }
    private String readline() throws IOException { //重用的代码要抛出去
        //调用同一个socket对象若干次getInputStream()方法返回的始终是同一条输入流
        InputStream in = socket.getInputStream();//通过socket获取输入流
        StringBuilder builder = new StringBuilder();
        char pre='a',cur='a';//pre表示上次读取的字符，cur表示本次读取的字符
        int d;//接收read()方法返回读取第几个字节内容(一次读一个字符)
        while ((d = in.read()) != -1){//当为-1时说明字节读取到末尾
            cur = (char)d;//本次读到的字符
            if (pre==13&&cur==10){//若上一个读取的是回车符并且本次读取的是换行符
                break;//跳出循环
            }
            builder.append(cur);//拼接本次读取到的字符
            pre = cur;//进入下一次循环前将本次读取的字符记作上次读取的字符
        }
        return builder.toString().trim();//该返回值类型为String型,变量为line。
    }
}

package com.webserver.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * 请求对象,该类的每一个实例用于标识HTTP协议规定的请求内容
 * 一个请求由三部分构成:
 * 1:请求行
 * 2:消息头
 * 3:消息正文
 */
public class HttpServletRequest {//面向对象思想,做到实例化该类即可完成请求业务
    private Socket socket;
    //请求行相关信息
    private String method;//请求方式
    private String uri;//抽象路径(该抽象路径是用户输入包含与请求行中的)
    private String protocol;//协议版本
    //消息头的相关信息
    private Map<String,String> headers = new HashMap<>();//创建一个Map散列表,用来存储消息头和内容

    public HttpServletRequest(Socket socket) throws IOException, EmptyRequestException {
        this.socket=socket;
        /*在构造方法中调用解析请求相关方法*/
        //1:解析请求行
        parseRequestLine();
        //2:解析消息头
        parseHeaders();
        //3:解析消息正文
        parseContent();
    }

    //1:解析请求行
    private void parseRequestLine() throws IOException, EmptyRequestException {
        String line = readline();//调用该方法完成读取请求行内容
        if (line.isEmpty()){//如果请求的信息为空
            //抛出空请求异常
            throw new EmptyRequestException();
        }
        System.out.println("请求行内容: "+line);
        //将请求行内容按照空格来拆分,并返回一个字符串数组
        String[] data = line.split("\\s");
        method = data[0];//获取元素1,赋值给请求方式
        uri = data[1];//获取元素2,赋值给抽象路径
        protocol = data[2];//获取元素3,赋值给协议版本
        //打桩输出
        System.out.println("method:"+method);
        System.out.println("uri:"+uri);
        System.out.println("protocol:"+protocol);
    }

    //2:解析消息头
    private void parseHeaders() throws IOException {
        //读取消息头
        while (true){
            String line = readline();//循环调用readline()方法读一行
            //因为请求到最后浏览器会单独发送一个回车加换行,故如果读取到的是空字符串,说明单独读取到了CRLF
            if (line.isEmpty()){
                break;//读取到最后为空时说明读取结束,跳出循环
            }
            System.out.println("消息头: "+line);
            //将消息头按照":"拆分名字和值并作为key,value存入到headers键值对中
            String[] data = line.split(":\\s");
            //System.out.println(Arrays.toString(data));//输出拆分后的消息头
            headers.put(data[0],data[1]);//将截取的两部分分为key和value
        }
        System.out.println("headers: "+headers);//Map输出的格式为{key=value,key=value......}
    }

    //3:解析消息正文
    private void parseContent(){

    }

    //该方法实现调用一次读一行读到CRLF(回车换行)时一行结束
    public String readline() throws IOException { //该部分代码需要重用,故异常需调用者处理
        //调用同一个socket对象若干次getInputStream()方法返回的始终是同一条输入流
        InputStream in = socket.getInputStream();//通过socket获取输入流
        StringBuilder builder = new StringBuilder();//利用StringBuilder可以实现对读取的字符进行追加操作
        char pre='a',cur='a';//pre表示上次读取的字符,cur表示本次读取的字符
        int d;//接收read()方法返回读取1个字节二进制低八位内容(一次读一个)
        while ((d = in.read()) !=-1){//当为-1时说明流读取到了末尾
            cur = (char) d;//赋给本次读取的字符
            if (pre==13&cur==10){//若上一个读取的是回车符并且本次读取的是换行符
                break;//跳出循环
            }
            builder.append(cur);//追加本次读取到的字符
            pre = cur;//进入下一个循环前将本次读取的字符记作上次读取的字符
        }
        return builder.toString().trim();//这里StringBuilder重写了toString()方法,调用并去除空格赋给line
    }

    public String getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }

    public String getProtocol() {
        return protocol;
    }

    /**
     * 根据消息头名字来获取对应的值
     * @param name
     * @return
     */
    public String getHeader(String name) {//根据传进来的消息头
        return headers.get(name);//利用Map中的get方法来获取对应key的value
    }
}

package com.webserver.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 请求对象,该类的每一个实例用于标识HTTP协议规定的请求内容
 * 一个请求由三部分构成:
 * 1:请求行
 * 2:消息头
 * 3:消息正文
 */
public class HttpServletRequest {
    private Socket socket;
    //请求行的相关信息
    private String method;//请求方式
    private String uri;//抽象路径
    private String protocol;//协议版本

    private String requestURL;//uri中的请求部分,即:"?"左侧内容
    private String queryString;//uri中的参数部分,即:"?"右侧内容
    private Map<String,String> parameters = new HashMap<>();//定义一个散列表存放键值对(每一组参数)

    //消息头的相关信息
    private Map<String,String> headers = new HashMap<>();//创建一个Map散列表

    public HttpServletRequest(Socket socket) throws IOException, EmptyRequestException {
        this.socket = socket;
        //1:解析请求行
        parseRequestLine();
        //2:解析消息头
        parseHeaders();
        //3:解析消息正文
        parseContent();
    }
    //1：解析请求行
    private void parseRequestLine () throws IOException, EmptyRequestException {
        String line = readline();//接收返回的请求行内容,若接收失败就不用解析并给浏览器发信号
        if (line.isEmpty()){//若请求行为空字符串
            //抛出空请求异常
            throw new EmptyRequestException();
        }
        System.out.println("请求行内容：" + line);
        String[] data = line.split("\\s");//按空格拆分
        System.out.println("拆分后为：" + Arrays.toString(data));//分了3个元素
        method = data[0];//获取元素1,赋值给请求方式
        uri = data[1];//获取元素2,赋值给抽象路径
        protocol = data[2];//获取元素3,赋值给协议版本
        //进一步解析uri
        parseURL();//调用解析uri的方法
        //打桩输出
        System.out.println("method:" + method);
        System.out.println("uri:" + uri);
        System.out.println("protocol:" + protocol);

    }
    //进一步解析uri
    private void parseURL(){
         /*
            uri有两种情况:
            1:不含有参数的
              例如: /index.html
              直接将uri的值赋值给requestURI即可.
            2:含有参数的
              例如:/regUser?username=fancq&password=&nickname=chuanqi&age=22
              将uri中"?"左侧的请求部分赋值给requestURI
              将uri中"?"右侧的参数部分赋值给queryString
              将参数部分首先按照"&"拆分出每一组参数，再将每一组参数按照"="拆分为参数名与参数值
              并将参数名作为key，参数值作为value存入到parameters中。
         */
        String[] data = uri.split("\\?");//转成普通的问号
        requestURL = data[0];
        if (data.length>1){ //数组长度>1说明"?"后面有参数
            queryString = data[1];
            //对参数部分进行转码
            try {
                queryString = URLDecoder.decode(queryString,"UTF-8");
            } catch (UnsupportedEncodingException e) { //异常为不支持的字符集
                e.printStackTrace();
            }
            data = queryString.split("&");
            for (String para : data) {
                String[] arr = para.split("=", 2);//此方法重载要求必须拆分成两项
                parameters.put(arr[0], arr[1]);//put到Map散列表里(key,value)
//或        if (arr.length>1) {
//三目运算      parameters.put(arr[0], arr.length > 1 ? arr[1] : "");
//          }
            }
        }
        System.out.println("requestURI:"+requestURL);
        System.out.println("queryString:"+queryString);
        System.out.println("parameters:"+parameters);
    }

    //2：解析消息头
    private void parseHeaders () throws IOException {
        //读取消息头
        while (true) {
            String line = readline();
            if (line.isEmpty()) {//如果读取到的是空字符串,说明单独读取到了CRLF
                break;
            }
            System.out.println("消息头：" + line);
            //将消息头按照":"和空格,拆分为名字和值并作为key,value存入到headers中
            String[] data = line.split(":\\s");
            //System.out.println(Arrays.toString(data));//输出拆分后的消息头
            headers.put(data[0], data[1]);
        }
        System.out.println("headers:" + headers);//Map输出的格式为(Key=Value)
    }

    //3：解析消息正文
    private void parseContent(){

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
     * 根据消息头名字获取对应的值
     * @param name
     * @return
     */
    public String getHeader(String name) {
        return headers.get(name);
    }

    public String getRequestURL() {
        return requestURL;
    }

    public String getQueryString() {
        return queryString;
    }

    public String getParameter(String name) {
        return parameters.get(name);
    }
}

package com.webserver.http;

import javax.activation.MimetypesFileTypeMap;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 响应对象
 * 该类的每一个实例用于表示HTTP协议规定的响应。
 * 一个响应由三部分构成:
 * 1:状态行
 * 2:响应头
 * 3:响应正文
 */
public class HttpServletResponse {
    private static MimetypesFileTypeMap mime = new MimetypesFileTypeMap();

    private Socket socket;

//1:  状态行相关信息：
    private int statusCode = 200;//状态代码
    private String statusReason = "OK";//状态描述

//2:  响应头相关信息：
    //创建一个Map散列表，它存储的内容是键值对(key-value)映射。
    private Map<String,String> headers = new HashMap<>();

//3:  响应正文相关信息：
    private File contentFile;//响应正文对应的实体文件


    public HttpServletResponse(Socket socket){
        this.socket=socket;
    }

    /**
     * 该方法会将当前响应对象内容以标准的HTTP响应格式通过socket获取的输出流发送给
     * 对应的客户端。
     */
    public void response() throws IOException {
        //发送状态行
        sendStatusLine();
        //发送响应头
        sendHeaders();
        //发送响应正文
        sendContent();
    }
    //发送状态行
    private void sendStatusLine() throws IOException {
        //HTTP/1.1 200 OK(CRLF)
        println("HTTP/1.1"+" "+statusCode+" "+statusReason);
    }

    //发送响应头
    private void sendHeaders() throws IOException {
         /*
            headers
            key                 value
            Content-Type        text/html
            Content-Length      245
            Server              BirdWebServer
            ...                 ...
         */
        //遍历headers散列表来发送每一个响应头
        Set<Map.Entry<String,String>> entrySet = headers.entrySet();
        for (Map.Entry<String,String> e : entrySet){
            String key = e.getKey();
            String value = e.getValue();
            println(key+": "+value);
        }
        //单独发送回车+换行表达响应头发送完毕
        println("");//传入空字符串后不走getBytes直接输出CRLF
    }

    //发送响应正文(二进制)
    private void sendContent() throws IOException {
        //将index.html文件所有数据发送
        OutputStream out = socket.getOutputStream();//通过socket获取输出流
        try(
            FileInputStream fis = new FileInputStream(contentFile)//获取文件输入流
           ) {
            byte[] data = new byte[1024 * 10];//块读
            int len;//表示一次读取的量
            while ((len = fis.read(data)) != -1) {
                out.write(data, 0, len);
            }
        }
//            else {
//                OutputStream out = socket.getOutputStream();//通过socket获取文件输出流
//                File file1 = new File(staticDir,"root");
//                File file2 = new File(file1,"404.html");
//                println("HTTP/1.1 404 NotFound");
//                println("Content-Type: text/html");
//                println("Content-Length: " + file2.length());
//                println("");
//                FileInputStream fis = new FileInputStream(file2);
//                byte[] data = new byte[1024 * 10];//块读
//                int len;//表示一次读取的量
//                while ((len = fis.read(data)) != -1) {
//                    out.write(data, 0, len);
//                }
//            }
    }


    private void println(String line) throws IOException {//被重用的操作
        OutputStream out = socket.getOutputStream();//通过socket获取输出流
        out.write(line.getBytes(StandardCharsets.ISO_8859_1));
        out.write(13);//回车符
        out.write(10);//换行符
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusReason() {
        return statusReason;
    }

    public void setStatusReason(String statusReason) {
        this.statusReason = statusReason;
    }

    public File getContentFile() {
        return contentFile;
    }

    public void setContentFile(File contentFile) {
        this.contentFile = contentFile;
        addHeader("Content-Type",mime.getContentType(contentFile));
        addHeader("Content-Length",contentFile.length()+"");
    }

    /**
     * 添加一个响应头
     * @param name 响应头名字
     * @param value 响应头的值
     */
    public void addHeader(String name,String value){
        headers.put(name,value);
    }
}

package com.webserver.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * 响应对象
 * 该类的每一个实例用于表示HTTP协议规定的响应。
 * 一个响应由三部分构成:状态行，响应头，响应正文。
 */
public class HttpServletResponse {
    private Socket socket;

    //状态行相关信息
    private int statusCode = 200;//状态代码
    private String statusReason = "OK";//状态描述

    //响应正文相关信息
    private File contentFile;//设置用户请求的文件(两种情况,请求成功和失败的文件)

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
        sendContent();//服务器要回复的HTML页面
    }

    //发送状态行
    private void sendStatusLine() throws IOException {
        println("HTTP/1.1"+" "+statusCode+" "+statusReason);//将状态行利用以上判断得到的变量来表示(注意空格)
    }

    //发送响应头
    private void sendHeaders() throws IOException {
        println("Content-Type: text/html");

        println("Content-Length: "+contentFile.length());//发送正文长度

        //单独发送回车+换行表达响应头发送完毕
        println("");//调用方法传入空字符串,执行方法中后两个写入方法,即回车+换行
    }

    //发送响应正文
    private void sendContent() throws IOException {
        OutputStream out = socket.getOutputStream();//通过socket获取文件输出流向浏览器页面写内容(服务器写出->浏览器)
        try(
                FileInputStream fis = new FileInputStream(contentFile)//创建输入流读取需要响应的HTML文件(服务器读取->HTML文件)
        ){
            byte[] data = new byte[1024*10];//块读
            int len;//表示一次读取的量
            while ((len = fis.read(data)) != -1){
                out.write(data,0,len);//将读取的HTML文件写入浏览器页面
            }
        }
    }

    /**
     * 该方法用来利用socket获取的输出流向服务器中写出状态行,响应头,正文长度
     * @param line
     * @throws IOException
     */
    private void println(String line) throws IOException {
        OutputStream out = socket.getOutputStream();//通过socket获取输出流,向客户端发送内容
        //因为要发送给浏览器,故ISO_8859_1该字符集是浏览器规定的欧洲字符集,其包含数字,字母和符号,不含中文
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

    //因为用户请求的结果有两种,成功或失败,故提供set方法让外界根据用户请求的结果来设置不同的文件
    public void setContentFile(File contentFile) {
        this.contentFile = contentFile;
    }
}

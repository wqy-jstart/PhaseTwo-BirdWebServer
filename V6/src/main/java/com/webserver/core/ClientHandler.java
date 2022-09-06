package com.webserver.core;

import com.webserver.http.HttpServerRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

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
    private static File rootDir;
    private static File staticDir;
    static {
        try {
            //rootDir表示类加载路径:target/classes目录
            rootDir = new File(
                    ClientHandler.class.getClassLoader()
                            .getResource(".").toURI()
            );
            //定位static目录(static目录下存放的是所有静态资源)
            staticDir = new File(rootDir,"static");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
    public ClientHandler(Socket socket){
        this.socket=socket;
    }

    @Override
    public void run() {
        try {
//            1:解析请求(将客户端发送过来的请求内容读取到)
            HttpServerRequest request = new HttpServerRequest(socket);//实例化request

//            2:处理请求(根据请求内容进行对应的处理)
            String path = request.getUri();//将获得的抽象路径赋给path

//            3:发送响应(将处理结果回馈给浏览器)
            //定位static目录下的HTML文件
            File file = new File(staticDir,path);//path为项目中的HTML文件
            System.out.println("文件是否存在："+file.exists());
            if (file.exists()) {
            /*
                测试:给浏览器发送一个响应，包含static目录下的index.html
                HTTP/1.1 200 OK(CRLF)
                Content-Type: text/html(CRLF)
                Content-Length: 2546(CRLF)(CRLF)
                1011101010101010101......
             */
                OutputStream out = socket.getOutputStream();//通过socket获取文件输出流
                //发送状态行
                //HTTP/1.1 200 OK(CRLF)
                println("HTTP/1.1 200 OK");

                //发送响应头
                //Content-Type: text/html(CRLF)
                println("Content-Type: text/html");

                //Content-Length: 2546(CRLF)
                println("Content-Length: " + file.length());

                //单独发送回车+换行表达响应头发送完毕
                println("");//空字符串

                //发送响应正文
                //将index.html文件所有数据发送
                FileInputStream fis = new FileInputStream(file);
                byte[] data = new byte[1024 * 10];//块读
                int len;//表示一次读取的量
                while ((len = fis.read(data)) != -1) {
                    out.write(data, 0, len);//
                }
            }else {
                OutputStream out = socket.getOutputStream();//通过socket获取文件输出流
                File file1 = new File(staticDir,"root");
                File file2 = new File(file1,"404.html");
                println("HTTP/1.1 404 NotFound");
                println("Content-Type: text/html");
                println("Content-Length: " + file2.length());
                println("");
                FileInputStream fis = new FileInputStream(file2);
                byte[] data = new byte[1024 * 10];//块读
                int len;//表示一次读取的量
                while ((len = fis.read(data)) != -1) {
                    out.write(data, 0, len);//
                }
            }
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

    private void println(String line) throws IOException {//被重用的操作
        OutputStream out = socket.getOutputStream();//通过socket获取输出流
        out.write(line.getBytes(StandardCharsets.ISO_8859_1));
        out.write(13);//回车符
        out.write(10);//换行符
    }

    public static void main(String[] args) throws URISyntaxException {
        File rootDir = new File( //寻找类加载路径
                ClientHandler.class.getClassLoader()
                        .getResource(".").toURI()
        );
        System.out.println(rootDir);//输出类加载路径
        //定位static目录(static目录下存放的是所有静态资源)
        File staticDir = new File(rootDir,"static");
        System.out.println(staticDir);//输出static目录的全部路径
        //定位static目录下的index.html
        File file = new File(staticDir,"index.html");
        System.out.println("文件是否存在："+file.exists());//true
    }
}

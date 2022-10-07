package com.webserver.core;

import com.webserver.http.HttpServletRequest;

import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

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
    private static File rootDir;//声明类加载路径
    private static File staticDir;//声明类加载路径下的static静态资源目录
    static {
        try {
            //定位rootDir表示类加载路径:target/classes目录
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
//            1.解析请求(将客户端发送过来的请求内容读取到)
            //将socket传入实例化对象中,此处需自行处理异常
            HttpServletRequest request = new HttpServletRequest(socket);//实例化request

//            2.处理请求(根据请求内容进行对应的处理)
            String path = request.getUri();//利用get方法获取request中的抽象路径并赋给path(该抽象路径是用户输入包含与请求行中的)

//            3.发送响应(将处理结果回馈给浏览器)
            //定位static目录下的HTML文件
            File file = new File(staticDir,path);//path为用户输入项目对应的HTML文件
            System.out.println("文件是否存在: "+file.exists());
            /*
                测试:给浏览器发送一个响应，包含static目录下的index.html
                HTTP/1.1 200 OK(CRLF)
                Content-Type: text/html(CRLF)
                Content-Length: 2546(CRLF)(CRLF)
                1011101010101010101......
             */
            OutputStream out = socket.getOutputStream();//通过socket获取文件输出流向服务器写出内容
            //发送状态行
            println("HTTP/1.1 200 OK");

            //发送响应头
            println("Content-Type: text/html");

            println("Content-Length: "+file.length());//发送正文长度

            //单独发送回车+换行表达响应头发送完毕
            println("");//调用方法传入空字符串,执行方法中后两个写入方法,即回车+换行

            //发送响应正文(将HTML文件所有数据发送)
            FileInputStream fis = new FileInputStream(file);//创建输入流读取服务器发送过来的响应
            byte[] data = new byte[1024*10];//块读
            int len;//表示一次读取的量
            while ((len = fis.read(data)) != -1){
                out.write(data,0,len);
            }

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

    /**
     * 该方法用来利用socket获取的输出流向浏览器中写入状态行,响应头,正文长度
     * @param line
     * @throws IOException
     */
    private void println(String line) throws IOException {
        OutputStream out = socket.getOutputStream();//通过socket获取输出流,向浏览器中写内容
        //因为要发送给浏览器,故ISO_8859_1该字符集是浏览器规定的欧洲字符集,其包含数字,字母和符号,不含中文
        out.write(line.getBytes(StandardCharsets.ISO_8859_1));
        out.write(13);//回车符
        out.write(10);//换行符
    }

    /**
     * 验证定位类加载路径
     * @param args
     * @throws URISyntaxException
     */
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

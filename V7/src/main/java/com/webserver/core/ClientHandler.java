package com.webserver.core;

import com.webserver.http.HttpServletRequest;
import com.webserver.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.URISyntaxException;

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
            HttpServletRequest request = new HttpServletRequest(socket);//实例化request
            HttpServletResponse response = new HttpServletResponse(socket);
//            2:处理请求(根据请求内容进行对应的处理)
            String path = request.getUri();//将获得的抽象路径赋给path
            //定位static目录下的HTML文件
            File file = new File(staticDir,path);//path为项目中的HTML文件
            //根据用户提供的抽象路径去static目录下定位到一个文件
            if(file.isFile()) {//file.isFile()表示存在并且是一个文件
                System.out.println("该文件存在！");
               response.setContentFile(file);
            }else {
                System.out.println("文件不存在！");
                response.setStatusCode(404);
                response.setStatusReason("NotFound");
                file = new File(staticDir, "root/404.html");
                response.setContentFile(file);
            }

//           3:发送响应(将处理结果回馈给浏览器)
            response.response();

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

package com.webserver.core;

import com.webserver.http.HttpServletRequest;
import com.webserver.http.HttpServletResponse;

import java.io.File;
import java.net.URISyntaxException;

/**
 * 这个类是SpringMVC框架与tomcat容器整合的一个关键类,接管了处理请求的工作.
 * 这样当tomcat将请求对象和响应对象创建完毕后在处理请求的环节通过调用这个类来完成,从而
 * 将处理请求交给了SpringMVC框架
 * 并在处理后发送响应给浏览器
 */
public class DispatcherServlet {
    private static DispatcherServlet instance = new DispatcherServlet();//私有创建一个静态的对象(单例模式)
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
            staticDir = new File(rootDir, "static");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private DispatcherServlet() {
    }

    //getInstance()该方法正常用来返回一个对象名
    public static DispatcherServlet getInstance() {
        return instance;
    }

    //★该方法用来请求抽象路径和判断文件是否属于该项目下,并作出不同的响应页面
    public void service(HttpServletRequest request, HttpServletResponse response) {//传入两个参数用来根据不同请求结果来设置响应内容
        String path = request.getUri();//利用get方法获取request中的抽象路径并赋给path(该抽象路径是用户输入包含与请求行中的)
        //定位static目录下的HTML文件
        File file = new File(staticDir, path);//path为用户输入项目对应的HTML文件
        //根据用户提供的抽象路径去static目录下定位到一个文件
        if (file.isFile()) {//file.isFile()表示存在并且是一个文件
            System.out.println("该文件存在!");
            response.setContentFile(file);//设置用户成功请求的文件

            response.addHeader("Server","BirdWebServer");
        } else {
            System.out.println("文件不存在!");
            response.setStatusCode(404);
            response.setStatusReason("NotFound");
            file = new File(staticDir, "root/404.html");//并将响应的内容改为404.html,使得服务器响应404页面
            response.setContentFile(file);//将请求的文件设置为404.html
        }
    }
}

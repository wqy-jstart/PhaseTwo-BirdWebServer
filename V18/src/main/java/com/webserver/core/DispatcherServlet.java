package com.webserver.core;

import com.webserver.annotations.Controller;
import com.webserver.annotations.RequestMapping;
import com.webserver.http.HttpServletRequest;
import com.webserver.http.HttpServletResponse;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;

/**
 * 这个类时SpringMVC框架与tomcat容器整合的一个关键类,接管了处理请求的工作.
 * 这样当tomcat将请求对象和响应对象创建完毕后在处理请求的环节通过调用这个类来完成,从而
 * 将处理请求交给了SpringMVC框架
 * 并在处理后发送响应给浏览器
 */
public class DispatcherServlet {
    private static DispatcherServlet instance = new DispatcherServlet();
    private static File rootDir;//声明类加载路径
    private static File staticDir;//声明类加载路径下的static静态资源目录

    static {
        try {
            //rootDir表示类加载路径:target/classes目录
            rootDir = new File(
                    DispatcherServlet.class.getClassLoader()
                            .getResource(".").toURI()
            );
            //定位rootDir父目录下的static目录(static目录下存放的是所有静态资源)
            staticDir = new File(rootDir, "static");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private DispatcherServlet() {
    }

    public static DispatcherServlet getInstance() {
        return instance;//只返回一种该对象
    }

    //★该方法用来请求抽象路径和判断文件是否属于该项目下,并作出不同的响应页面
    public void service(HttpServletRequest request, HttpServletResponse response) {
        String path = request.getRequestURI();//将获得的抽象路径赋给path
        //是否为处理业务
        try {
            Method method = HandlerMapping.getMethod(path);//传入抽象路径获取方法
            if (method!=null){ //如果方法不为null,调用方法
                //★通过方法对象获取其所属的类的类对象
                Class cls = method.getDeclaringClass();
                Object controller = cls.newInstance();
                method.invoke(controller,request,response);//调用有参方法并传递类对象和两个参数
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //定位static目录下的HTML文件
        File file = new File(staticDir, path);//path为项目中的HTML文件
        //根据用户提供的抽象路径去static目录下定位到一个文件
        if (file.isFile()) {//file.isFile()表示存在并且是一个文件
            System.out.println("是一个普通文件！");
            response.setContentFile(file);//响应抽象路径下项目存在的HTML文件
            response.addHeader("Server", "BirdWebServer");
        } else {
            System.out.println("该文件不存在！");
            //修改状态
            response.setStatusCode(404);
            response.setStatusReason("NotFound");
            file = new File(staticDir, "root/404.html");
            response.setContentFile(file);//响应项目中指定的404(HTML页面)
        }
    }
}

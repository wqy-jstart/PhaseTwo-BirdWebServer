package com.webserver.core;

import com.webserver.http.HttpServletRequest;
import com.webserver.http.HttpServletResponse;

import java.io.File;
import java.lang.reflect.Method;
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
        return instance;//只返回一种该对象
    }

    //★该方法用来请求抽象路径和判断文件是否属于该项目下,并作出不同的响应页面
    public void service(HttpServletRequest request, HttpServletResponse response) {//传入两个实例用来根据不同请求结果来设置响应内容
        //利用getRequestURI()方法获取request中的抽象路径(有参无参不影响该路径)并赋给path(该抽象路径是用户输入包含于请求行中的)
        //例如:请求行内容: GET /regUser?..... HTTP/1.1
        String path = request.getRequestURI();
        //首先判断是否为请求某个特定的业务
        try {
            Method method = HandlerMapping.getMethod(path);//调用HandleMapping实例中的getMethod()方法,传入抽象路径path
            if (method != null) {//判断该方法是否为空NULL
                //★通过该方法获取其所属类的类对象
                Class cls = method.getDeclaringClass();
                Object controller = cls.newInstance();//实例化该类
                //通过invoke()方法来调用方法(参数1:该方法的所属的类的引用 参数2:方法参数列表1 参数3:方法参数列表2)
                method.invoke(controller, request, response);
                return;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        //定位static目录下的HTML文件
        File file = new File(staticDir, path);//path为用户输入的抽象路径,即项目对应的HTML文件
        //根据用户提供的抽象路径去static目录下定位到一个文件
        if (file.isFile()) {//file.isFile()表示存在并且是一个文件
            System.out.println("该文件存在!");
            response.setContentFile(file);//设置用户成功请求的文件
            response.addHeader("Server", "BirdWebServer");
        } else {
            System.out.println("文件不存在!");
            response.setStatusCode(404);
            response.setStatusReason("NotFound");
            file = new File(staticDir, "root/404.html");//并将响应的内容改为404.html,使得服务器响应404页面
            response.setContentFile(file);//将请求的文件设置为404.html

        }
    }
}

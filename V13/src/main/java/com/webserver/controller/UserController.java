package com.webserver.controller;

import com.webserver.entity.User;
import com.webserver.http.HttpServletRequest;
import com.webserver.http.HttpServletResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URISyntaxException;

//处理用户注册
public class UserController {
    private static File userDir;//用来表示存放所有用户信息的目录

    static {
        userDir = new File("./users");
        if (!userDir.exists()) { //判断如果目录不存在
            userDir.mkdirs();//重新多级创建
        }
    }

    private static File rooDir;//声明类加载路径
    private static File staticDir;//声明类加载路径下的static静态资源目录

    //放入静态块中加载,全局只有一份
    static {
        try {
            //rootDir表示类加载路径:target/classes目录
            rooDir = new File(
                    UserController.class.getClassLoader()
                            .getResource(".").toURI()
            );
            //定位rootDir父目录下的static目录(static目录下存放的是所有静态资源)
            staticDir = new File(rooDir, "static");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void reg(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("开始处理注册!!!");
        //★通过request调用获取含参uri中?用户输入的键值对信息的key值来获取信息内容value(字符串类型)
        String username = request.getParameters("username");
        String password = request.getParameters("password");
        String nickname = request.getParameters("nickname");
        String ageStr = request.getParameters("age");
        //至此拿到用户输入的内容,再进行必要验证(判断用户输入的内容是否为空)
        if (username == null || username.isEmpty() ||
                password == null || password.isEmpty() ||
                nickname == null || nickname.isEmpty() ||
                ageStr == null || ageStr.isEmpty() ||
                !ageStr.matches("[0-9]+")//调用matches()方法用正则表达式去匹配年龄,返回boolean值
        ) {
            //当信息输入有误时提示该页面
            File file = new File(staticDir,"/reg_info_error.html");
            response.setContentFile(file);//调用HttpServletResponse类中设置正文文件的方法,设置并响应该输入有误的HTML文件
            return;
        }
        //信息输入正确时输出
        System.out.println("姓名："+username+",密码"+password+",昵称"+nickname+",年龄"+ageStr);

        int age = Integer.parseInt(ageStr);//利用int的包装类调用parseInt()方法来将字符串转换为int型
        //实例化用户User对象
        User user = new User(username,password,nickname,age);//传入用户的参数
        //参数1：userDir表示父目录,参数2：userDir目录下的子项
        File file = new File(userDir,username+".obj");//利用获取的用户名拼成后缀.obj文件
        //判断若文件存在则说明该用户已经注册过了
        if (file.exists()){
            File file1 = new File(staticDir,"/have_user.html");//前往static目录下寻找该HTML文件
            response.setContentFile(file1);//将该文件设置为响应正文文件,发给浏览器并正确做出响应
            return;
        }
        try( //将对象序列化到文件中
                FileOutputStream fos = new FileOutputStream(file);//创建低级文件输出流
                ObjectOutputStream oos = new ObjectOutputStream(fos)//获取处理对象流,将对象写出到文件里
        ){
            oos.writeObject(user);//把对象序列化之后写入文件中(前提User类需继承可序列化接口)
            //至此对象即将写入文件,可以响应注册成功页面
            File file1 = new File(staticDir,"/reg_success.html");
            response.setContentFile(file1);//将成功文件设置为正文响应文件,做出正确的响应
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

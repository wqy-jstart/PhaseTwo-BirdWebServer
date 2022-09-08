package com.webserver.Controller;

import com.webserver.entity.User;
import com.webserver.http.HttpServletRequest;
import com.webserver.http.HttpServletResponse;

import java.io.*;

public class UserController {
    private static File userDir;//用来表示存放所有用户信息的目录
    static {
        userDir = new File("./users");
        if (!userDir.exists()){//判断如果不存在
            userDir.mkdirs();//重新创建
        }
    }
    //处理注册
    public void reg(HttpServletRequest request, HttpServletResponse response){
        System.out.println("开始处理注册！！！");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String nickname = request.getParameter("nickname");
        String ageStr = request.getParameter("age");
        //必要验证
        if (username==null||username.isEmpty()||
            password==null||password.isEmpty()||
            nickname==null||nickname.isEmpty()||
            ageStr==null||ageStr.isEmpty()||
            !ageStr.matches("[0-9]+")
        ){
            //信息输入有误提示页面
            response.sendRedirect("/reg_info_error.html");
            return;
        }
        System.out.println("姓名："+username+",密码"+password+",昵称"+nickname+",年龄"+ageStr);

        int age = Integer.parseInt(ageStr);
        //实例化对象
        User user = new User(username,password,nickname,age);
        //参数1：userDir表示父目录,参数2：userDir目录下的子项
        File file = new File(userDir,username+".obj");//将输入的对象拼成文件
        //文件存在则说明该用户已经注册过了
        if (file.exists()){
            response.sendRedirect("/have_user.html");//响应页面到该地址
            return;
        }
        try(//只有实现了AutoCloseable接口的类才可以在这里定义,所有的流都实现了
            //把流放在小括号内就不用手动关流了
            //序列化对象,将其输出至文件保存★对象需要实现Serializable接口:对象->字节->文件
                FileOutputStream fos = new FileOutputStream(file);//利用文件输出流连接该文件
                ObjectOutputStream oos = new ObjectOutputStream(fos)//对象输出流连接低级文件流
            ) {
            oos.writeObject(user);//把对象序列化之后写入文件中
            //利用相应对象要求浏览器访问注册成功页面
            response.sendRedirect("/reg_success.html");//响应页面至该地址
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //处理登录
    public void login(HttpServletRequest request,HttpServletResponse response){
        System.out.println("开始处理登录！！！");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        System.out.println("用户名为："+username+",密码为："+password);
        //必要的验证工作
        if (username==null||username.isEmpty()||
            password==null||password.isEmpty()){
            response.sendRedirect("/login_info_error.html");
            return;
        }
        //若没有错误
        File file = new File(userDir,username+".obj");
        if (file.exists()){
            try( //反序列化对象
                    FileInputStream fis = new FileInputStream(file);
                    ObjectInputStream ois = new ObjectInputStream(fis);
            ) {
                User user = (User) ois.readObject();//读取回来的是用户登录的对应信息
                //比较登录的密码和该注册用户的密码是否一致
                if (user.getPassword().equals(password)){
                    //登录成功
                    response.sendRedirect("/login_success.html");
                    return;//登录成功后返回
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();//处理异常
            }
        }
        //登录失败
        response.sendRedirect("/login_fail.html");//若登录失败,则响应该页面
    }
}

package com.webserver.Controller;

import com.webserver.entity.User;
import com.webserver.http.HttpServletRequest;
import com.webserver.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class UserController {
    private static File userDir;//用来表示存放所有用户信息的目录
    static {
        userDir = new File("./users");
        if (!userDir.exists()){//判断如果不存在
            userDir.mkdirs();//重新创建
        }
    }

    public void reg(HttpServletRequest request, HttpServletResponse response){
        System.out.println("开始处理注册！！！");
        String username = request.getParameters("username");
        String password = request.getParameters("password");
        String nickname = request.getParameters("nickname");
        String ageStr = request.getParameters("age");
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
        try(
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
}

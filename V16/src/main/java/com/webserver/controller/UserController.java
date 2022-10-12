package com.webserver.controller;

import com.webserver.entity.User;
import com.webserver.http.HttpServletRequest;
import com.webserver.http.HttpServletResponse;

import java.io.*;

public class UserController {
    private static File userDir;//用来表示存放所有用户信息的目录

    static {
        userDir = new File("./users");
        if (!userDir.exists()) { //判断如果目录不存在
            userDir.mkdirs();//重新多级创建
        }
    }

    /**
     * 处理用户注册
     * @param request 请求
     * @param response 响应
     */
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
//            File file = new File(staticDir,"/reg_info_error.html");
//            response.setContentFile(file);//调用HttpServletResponse类中设置正文文件的方法,设置并响应该输入有误的HTML文件
            response.sendRedirect("/reg_info_error.html");//直接重定向到指定页面
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
            response.sendRedirect("/have_user.html");//直接重定向到用户存在的页面
            return;
        }
        try( //将对象序列化到文件中
                FileOutputStream fos = new FileOutputStream(file);//创建低级文件输出流
                ObjectOutputStream oos = new ObjectOutputStream(fos)//获取处理对象流,将对象写出到文件里
        ){
            oos.writeObject(user);//把对象序列化之后写入文件中(前提User类需继承可序列化接口)
            //至此对象即将写入文件,可以响应注册成功页面
           response.sendRedirect("/reg_success.html");//直接重定向到成功页面
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理登录
     * @param request 请求
     * @param response 响应
     */
    public void login(HttpServletRequest request,HttpServletResponse response){
        System.out.println("开始处理登录!");
        //通过request的getParameters()方法获取表单中用户登录的信息
        String username = request.getParameters("username");
        String password = request.getParameters("password");
        System.out.println("用户名为："+username+",密码为："+password);
        //必要的验证工作(判断输入的内容是否为空和null)
        if (username==null||username.isEmpty()||
                password==null||password.isEmpty()){
            response.sendRedirect("/login_info_error.html");//如果是,响应输入格式有误
            return;
        }
        //若没有错误
        File file = new File(userDir,username+".obj");//定位.obj文件
        if (file.exists()){
            try(
                    FileInputStream fis = new FileInputStream(file);
                    ObjectInputStream ois = new ObjectInputStream(fis)
            ) {
                //反序列化指定用户名的.obj文件,用User接收,返回的User对象中为注册的信息
                User user = (User) ois.readObject();
                //若注册的密码和登录的密码一致
                if (user.getPassword().equals(password)) {
                    //登录成功
                    response.sendRedirect("/login_success.html");
                    return;//登录成功后返回
                }
            }catch (IOException | ClassNotFoundException e){
                e.printStackTrace();//处理异常
            }
        }
        //若文件不存在
        response.sendRedirect("/login_fail.html");//用户不存在,响应该错误页面
    }
}

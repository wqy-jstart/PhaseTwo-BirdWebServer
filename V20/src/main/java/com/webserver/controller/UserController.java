package com.webserver.controller;

import com.webserver.annotations.Controller;
import com.webserver.annotations.RequestMapping;
import com.webserver.http.HttpServletRequest;
import com.webserver.http.HttpServletResponse;
import com.webserver.util.DBUtil;

import java.io.*;
import java.sql.*;

@Controller
public class UserController {
    private static File userDir;//用来表示存放所有用户信息的目录
    static {
        userDir = new File("./users");
        if (!userDir.exists()){//判断如果不存在
            userDir.mkdirs();//重新创建
        }
    }
    //处理注册
    @RequestMapping("/regUser")
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
            //信息输入有误提示错误页面
            response.sendRedirect("/reg_info_error.html");
            return;
        }
        System.out.println("姓名："+username+",密码"+password+",昵称"+nickname+",年龄"+ageStr);

        int age = Integer.parseInt(ageStr);
       //2 将用户信息插入到数据库的userinfo表中
        try(
                Connection connection = DBUtil.getConnection()
        ) {
            Statement statement = connection.createStatement();
            //查1代表如果WHERE条件成立就能显示1,反之不显示,可以用来判断WHERE条件是否成立
            String sql = "SELECT 1 FROM userinfo WHERE username='"+username+"'";
            ResultSet rs = statement.executeQuery(sql);
            //如果WHERE条件成立说明此次用户名和上一次相同,可查到数字1,next()方法为true
            while (rs.next()){ //判断结果集是否有一条记录
                response.sendRedirect("/have_user.html");
                return;
            }
            /*
               INSERT INTO userinfo (username,password,nickname,age)
               VALUES('xx','xx','xx',2)
             */
            sql = "INSERT INTO userinfo (username,password,nickname,age) " +
                         "VALUES('"+username+"','"+password+"','"+nickname+"',"+age+")";
            System.out.println(sql);
            int num = statement.executeUpdate(sql);
            if (num>0){
                //利用响应对象要求浏览器访问注册成功页面
                response.sendRedirect("/reg_success.html");//响应页面至该地址
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //处理登录
    @RequestMapping("/loginUser")
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
        try(
                Connection connection = DBUtil.getConnection()
        ) {
            String sql = "SELECT id,username,password,nickname,age " +
                         "FROM userinfo " +
                         "WHERE username=? " +
                         "AND password=?";
            PreparedStatement pr = connection.prepareStatement(sql);
            pr.setString(1,username);
            pr.setString(2,password);
            ResultSet rs = pr.executeQuery();
            if (rs.next()) {
                response.sendRedirect("/login_success.html");
            } else {
                //登录失败
                response.sendRedirect("/login_fail.html");//若登录失败,则响应该页面
            }
        }catch (SQLException throwables){
            throwables.printStackTrace();
        }
    }
}

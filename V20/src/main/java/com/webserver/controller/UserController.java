package com.webserver.controller;

import com.webserver.annotations.Controller;
import com.webserver.annotations.RequestMapping;
import com.webserver.entity.User;
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
        if (!userDir.exists()) { //判断如果目录不存在
            userDir.mkdirs();//重新多级创建
        }
    }

    /**
     * 处理用户注册
     *
     * @param request  请求
     * @param response 响应
     */
    @RequestMapping("/regUser")
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
        System.out.println("姓名：" + username + ",密码" + password + ",昵称" + nickname + ",年龄" + ageStr);

        int age = Integer.parseInt(ageStr);//利用int的包装类调用parseInt()方法来将字符串转换为int型
        // 将用户注册的信息插入到数据库的userinfo表中
        try (
                Connection connection = DBUtil.getConnection()
        ) {
            Statement statement = connection.createStatement();
            //查1代表如果WHERE条件成立就能显示1,反之不显示,可以用来判断WHERE条件是否成立
            String sql = "SELECT 1 FROM userinfo WHERE username='" + username + "'";
            ResultSet rs = statement.executeQuery(sql);//返回查询的结果集
            //如果WHERE条件成立说明此次用户名和上一次相同,可查到数字1,next()方法为true
            while (rs.next()) { //★判断结果集是否有一条记录(判断是否用户名存在)
                response.sendRedirect("/have_user.html");
                return;
            }
            //插入到userinfo表中
            /*
               INSERT INTO userinfo (username,password,nickname,age)
               VALUES('xx','xx','xx',2)
             */
            sql = "INSERT INTO userinfo(username,password,nickname,age) " +
                    "VALUES('" + username + "','" + password + "','" + nickname + "'," + age + ")";
            System.out.println(sql);
            int num = statement.executeUpdate(sql);
            if (num > 0) {
                //利用响应对象要求浏览器访问注册成功页面
                response.sendRedirect("/reg_success.html");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * 处理登录
     *
     * @param request  请求
     * @param response 响应
     */
    @RequestMapping("/loginUser")
    public void login(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("开始处理登录!");
        //通过request的getParameters()方法获取表单中用户登录的信息
        String username = request.getParameters("username");
        String password = request.getParameters("password");
        System.out.println("用户名为：" + username + ",密码为：" + password);
        //必要的验证工作(判断输入的内容是否为空和null)
        if (username == null || username.isEmpty() ||
                password == null || password.isEmpty()) {
            response.sendRedirect("/login_info_error.html");//如果是,响应输入格式有误
            return;
        }
        //若没有错误
        try (
                Connection connection = DBUtil.getConnection()
        ) {//利用JDBC查询数据库
            String sql = "SELECT id,username,password,nickname,age " +
                    "FROM userinfo " +
                    "WHERE username=? " +
                    "AND password=?";
            PreparedStatement pr = connection.prepareStatement(sql);//使用预编译处理sql语句
            pr.setString(1, username);
            pr.setString(2, password);
            ResultSet rs = pr.executeQuery();
            if (rs.next()) {//若查询到一条数据
                response.sendRedirect("/login_success.html");
            } else {//否则没有查到数据说明没有该用户,登录失败
                //登录失败
                response.sendRedirect("/login_fail.html");//若登录失败,则响应该页面
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}

package com.webserver.controller;

import com.webserver.annotations.Controller;
import com.webserver.annotations.RequestMapping;
import com.webserver.http.HttpServletRequest;
import com.webserver.http.HttpServletResponse;
import com.webserver.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Controller
public class ArticleController {
    @RequestMapping("/writeArticle")
    public void write(HttpServletRequest request, HttpServletResponse response){
        System.out.println("开始处理发表文章功能！！！！");
        //获取表单信息
        String title = request.getParameter("title");
        String author = request.getParameter("author");
        String content = request.getParameter("content");
        /*
            有一个验证:如果作者无效，要提示一个页面，该作者不存在!

            1:先根据该文章的作者名去userinfo表中找到该用户，并得到该用户的id(主键值)
            2:将文章信息插入到article表中，u_id字段插入该用户的主键值
         */
        //必要的验证工作
        if (author==null || author.isEmpty() ||
            content==null || content.isEmpty()){
            response.sendRedirect("/article_info_error.html");
            return;
        }
        try(
                Connection connection = DBUtil.getConnection()
        ){
            String sql = "SELECT username,id " +
                         "FROM userinfo " +
                         "WHERE username=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1,author);//依据作者来查找userinfo表,并反馈id
            ResultSet rs = ps.executeQuery();//查找,反馈结果
            if (rs.next()) {
                //获取用户id
                int id = rs.getInt("id");
                String sql1 = "INSERT INTO article(title,content,u_id) VALUES (?,?,?)";
                PreparedStatement ps1 = connection.prepareStatement(sql1);
                ps1.setString(1,title);
                ps1.setString(2,content);//将用户输入的内容插入到article表中
                ps1.setInt(3,id);
                int num = ps1.executeUpdate();//插入,影响内容
                if (num>0){//影响了表的条数,发表成功！
                    response.sendRedirect("/write_article_success.html");
                }else {//没有影响表的条数,发表失败！
                    response.sendRedirect("/write_article_fail.html");
                }
            }else {//没有此作者
                response.sendRedirect("/have_not_user.html");
            }
        }catch (SQLException throwables){
            throwables.printStackTrace();
        }
    }
}

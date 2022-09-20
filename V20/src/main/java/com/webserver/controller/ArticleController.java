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
        String title = request.getParameter("title");
        String author = request.getParameter("author");
        String content = request.getParameter("content");
        //必要的验证工作
        if (author==null || author.isEmpty()){
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
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                String sql1 = "INSERT INTO article(title,content,u_id) VALUES (?,?,?)";
                PreparedStatement ps1 = connection.prepareStatement(sql1);
                ps1.setString(1,title);
                ps1.setString(2,content);//将用户输入的内容插入到article表中
                ps1.setInt(3,id);
                ps1.executeUpdate();
            }
        }catch (SQLException throwables){
            throwables.printStackTrace();
        }
    }
}

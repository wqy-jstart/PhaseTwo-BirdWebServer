package com.webserver.controller;

import com.webserver.annotations.Controller;
import com.webserver.annotations.RequestMapping;
import com.webserver.http.HttpServletRequest;
import com.webserver.http.HttpServletResponse;
import com.webserver.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Controller
public class ArticleController {
    @RequestMapping("/writeArticle")
    public void write(HttpServletRequest request, HttpServletResponse response){
        System.out.println("开始处理发表文章功能!!!!");
        String title = request.getParameters("title");
        String author = request.getParameters("author");
        String content = request.getParameters("content");
        System.out.println("标题："+title+",作者："+author+",正文："+content);
        try (
            Connection connection = DBUtil.getConnection();
        ){
            String sql = "INSERT INTO article(id,title,content) VALUES(NULL ,?,?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1,title);
            ps.setString(2,content);
            int i = ps.executeUpdate();
            if (i > 0){
                response.sendRedirect("/write_article_success.html");
            }else {
                response.sendRedirect("/write_article_fail.html");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}

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
    /**
     * 处理发表文章的业务
     * @param request request
     * @param response response
     */
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
            String sql = "SELECT id,username,password,nickname,age from userinfo where username=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1,author);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){ // 如果查到数据
                String sql1 = "INSERT INTO article(id,title,content,u_id) VALUES(NULL ,?,?,?)";
                PreparedStatement ps1 = connection.prepareStatement(sql1);
                ps1.setString(1,title);
                ps1.setString(2,content);
                ps1.setInt(3,rs.getInt("id"));
                int i = ps1.executeUpdate();
                if (i > 0){
                    response.sendRedirect("/write_article_success.html");
                }else {
                    response.sendRedirect("/write_article_fail.html");
                }
            }else {
                response.sendRedirect("/have_not_user.html");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}

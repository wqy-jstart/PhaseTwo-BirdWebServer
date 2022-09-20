package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
/**
 * 执行DML语句(数据操作语言)
 */
public class JDBCDemo2 {
    public static void main(String[] args) throws ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        try(
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/empdb?characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&rewriteBatchedStatements=true","root","root");
        ) {
           Statement statement = conn.createStatement();
              /*
                 向class表中插入数据：1年级1班
               */
           String sql = "INSERT INTO class(name) VALUES('1年级1班')";
           /*
              int executeUpdate(String sql)
              通常用来专门执行DML(INSERT,UPDATE,DELETE)语句,返回的int值表示"影响"了表中多少条记录
            */
           int num = statement.executeUpdate(sql);
           if (num>0){ //说明至少影响了表中的1条记录
               System.out.println("插入成功！");
           }else {
               System.out.println("插入失败！");
           }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}

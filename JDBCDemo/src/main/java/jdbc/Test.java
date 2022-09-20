package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 利用JDBC创建两张表student和class
 */
public class Test {
    public static void main(String[] args) throws ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        try (
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/empdb?characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&rewriteBatchedStatements=true","root","root")
        ){
            Statement statement = conn.createStatement();
            String sql1 = "CREATE TABLE student1(" +
                    "    id INT primary key auto_increment," +
                    "    name VARCHAR(10)," +
                    "    age INT(3)," +
                    "    class_id INT" +
                    ")";
//            String sql2 = "CREATE TABLE class(" +
//                    "    id INT primary key auto_increment," +
//                    "    name VARCHAR(30) NOT NULL" +
//                    ")";
            statement.execute(sql1);
//            statement.execute(sql2);
            System.out.println("执行完毕！");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}

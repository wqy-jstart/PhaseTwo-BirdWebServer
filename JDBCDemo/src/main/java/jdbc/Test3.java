package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 每个年级四个班,一共六个年级
 */
public class Test3 {
    public static void main(String[] args) throws ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        try(
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/empdb?characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&rewriteBatchedStatements=true","root","root");
        ) {
            Statement statement = conn.createStatement();
            for (int i = 1; i <= 6; i++) {
                for (int j = 1; j <= 4; j++) {
                    String sql = "INSERT INTO class (name) VALUES ('"+i+"年级"+j+"班')";
                    int num = statement.executeUpdate(sql);
                    if (num>0){
                        System.out.println("插入完毕！");
                    }else {
                        System.out.println("插入失败！");
                    }
                }
            }
//            for (int y = 1,c = 0;y<7;y=++c/4+1){
//                String sql = "INSERT INTO class (name) VALUES ('"+y+"年级"+(c%4+1)+"班')";
//                System.out.println(sql);
//            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}

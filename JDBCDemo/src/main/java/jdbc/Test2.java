package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 将1年级一班,改为"学前班"
 */
public class Test2 {
    public static void main(String[] args) throws ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        try (
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/empdb?characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&rewriteBatchedStatements=true","root","root");
        ){
            Statement statement = conn.createStatement();
            String sql = "UPDATE class SET name='学前班' WHERE id=1";
            int num = statement.executeUpdate(sql);//用此方法返回影响数据的条数
            if (num>0){
                System.out.println("修改成功！");
            }else {
                System.out.println("修改失败！");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}

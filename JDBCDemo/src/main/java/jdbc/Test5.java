package jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 查询3年级2班的所有学生
 */
public class Test5 {
    public static void main(String[] args) {
        try (
                Connection connection = DBUtil.getConnection()
        ){
            Statement statement = connection.createStatement();
            String sql = "SELECT s.name sn,s.age,c.name cn\n" +
                         "FROM student s\n" +
                         "JOIN class c\n" +
                         "ON s.class_id=c.id\n" +
                         "WHERE c.name='3年级2班'";
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()){//因为查询的是多条,用while循环获取并输出
                String sname = rs.getString("sn");
                int sage = rs.getInt("age");
                String cname = rs.getString("cn");
                System.out.println(sname+","+sage+","+cname);
            }
            rs.close();//当结果集遍历完毕后将其关闭
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}

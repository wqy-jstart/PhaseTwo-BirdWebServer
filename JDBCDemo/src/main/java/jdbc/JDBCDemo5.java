package jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 插入张三字段
 */
public class JDBCDemo5 {
    public static void main(String[] args) {
        try(
                Connection connection = DBUtil.getConnection()
        ){
            String sql = "INSERT INTO student1(name,age,class_id) VALUES(?,?,?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1,"张三");
            ps.setInt(2,5);
            ps.setInt(3,0);
            int num = ps.executeUpdate();
            if (num>0){
                System.out.println("插入成功！");
            }else {
                System.out.println("插入失败！");
            }
        }catch (SQLException throwables){
            throwables.printStackTrace();
        }
    }
}

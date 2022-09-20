package jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;
/**
 * 将emp表中的只用名字的员工删除。名字可以由用户输入
 *
 * 例如:
 * 灭霸
 * 将灭霸从emp表中删除
 */
public class Test7 {
    public static void main(String[] args) {
        try(
                Connection connection = DBUtil.getConnection()
        ){
            Scanner scanner = new Scanner(System.in);
            System.out.println("请选择要开除的员工：");
            String name = scanner.nextLine();
            String sql = "DELETE FROM emp WHERE name=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1,name);
            int num = ps.executeUpdate();
            if (num>0){
                System.out.println("删除成功！");
            }else {
                System.out.println("删除失败!");
            }
        }catch (SQLException throwables){
            throwables.printStackTrace();
        }
    }
}

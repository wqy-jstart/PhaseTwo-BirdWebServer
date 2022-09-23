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
            ps.setString(1,name);//将用户输入的内容当做过滤条件执行SQL语句做删除操作
            int num = ps.executeUpdate();//返回影响数据的条数(增加或减少)
            if (num>0){//若至少影响一条,则删除成功!
                System.out.println("删除成功！");
            }else {//否则删除失败!
                System.out.println("删除失败!");
            }
        }catch (SQLException throwables){
            throwables.printStackTrace();
        }
    }
}

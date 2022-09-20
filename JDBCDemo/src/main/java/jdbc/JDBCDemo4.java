package jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * 预编译SQL语句
 */
public class JDBCDemo4 {
    public static void main(String[] args) {
        try (
                Connection connection = DBUtil.getConnection()
        ) {
            /*
                预编译SQL语句是将在SQL中会变化的值(原来拼接SQL语句的部分)先以"?"进行占位
                查看孙悟空的信息(原)
                SELECT id,name,salary,dept_id
                FROM emp
                WHERE name='孙悟空'
             */
            Scanner scanner = new Scanner(System.in);
            System.out.println("请输入要查询的员工名称？");
            while (true){ //while(true){}重复查询
                String name = scanner.nextLine();
                if ("exit".equals(name)){
                    break;
                }
            /*
                拼接SQL语句会存在两个明显问题:
                1:代码复杂度高，容易出现错误，且可读性差。
                2:存在着SQL注入攻击
             */
            /*String sql = "SELECT id,name,salary,dept_id " +
                         "FROM emp " +
                         "WHERE name='"+name+"'";*/
            String sql = "SELECT id,name,sal,dept_id " +
                    "FROM emp " +
                    "WHERE name=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            //name字段时VARCHAR，因此这里设置该?对应的值应当选取字符串类型
            ps.setString(1, name);//第1个"?"要设置为字符串的值'孙悟空'
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int id = rs.getInt(1);
                String n = rs.getString(2);
                int salary = rs.getInt(3);
                int deptId = rs.getInt(4);
                System.out.println(id + "," + n + "," + salary + "," + deptId);
            }
        }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}

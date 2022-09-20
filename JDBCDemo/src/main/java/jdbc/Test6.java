package jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * 查看某部门名称下工资高于某值的所有员工信息
 * 用户可以输入部门名称和工资
 *
 * 例如:
 * 神仙 2000
 * 查看部门名称为'神仙'工资>2000的
 */
public class Test6 {
    public static void main(String[] args) {
        try(
                Connection connection = DBUtil.getConnection()//获取数据库连接
        ){
            Scanner scanner = new Scanner(System.in);//创建扫描仪
            System.out.println("请输入部门名称：");
            String name = scanner.nextLine();
            System.out.println("请输入要高于的工资：");
            int salary = scanner.nextInt();
            String sql = "SELECT e.name,e.sal,d.name,e.dept_id " +
                        "FROM emp e " +
                        "JOIN dept d " +
                        "ON e.dept_id = d.id " +
                        "WHERE d.name=? AND e.sal>?";
            PreparedStatement pr = connection.prepareStatement(sql);
            pr.setString(1,name);
            pr.setInt(2,salary);
            ResultSet rs = pr.executeQuery();
            while (rs.next()){
                String ename = rs.getString(1);
                int sal = rs.getInt(2);
                String dname = rs.getString(3);
                int dept = rs.getInt(4);
                System.out.println(ename+","+sal+","+dname+","+dept);
            }
            rs.close();
        }catch (SQLException throwables){
            throwables.printStackTrace();
        }
    }
}

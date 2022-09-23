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
            PreparedStatement pr = connection.prepareStatement(sql);//使用预编译来处理执行SQL语句
            pr.setString(1,name);//将第一个?设置为用户输入的名字
            pr.setInt(2,salary);//将第二个?设置为用户输入的最低工资
            ResultSet rs = pr.executeQuery();//将以上两个输入内容作为过滤条件执行SQL语句返回查询到的结果集
            while (rs.next()){//若查询到就会返回true
                //获取需要查询的结果集并输出
                String ename = rs.getString(1);
                int sal = rs.getInt(2);
                String dname = rs.getString(3);
                int dept = rs.getInt(4);
                System.out.println(ename+","+sal+","+dname+","+dept);
            }
            rs.close();//关闭结果集
        }catch (SQLException throwables){
            throwables.printStackTrace();
        }
    }
}

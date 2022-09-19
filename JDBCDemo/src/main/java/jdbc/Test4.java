package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

/**
 * 向Student表中插入1000条记录
 * 班级随机(1-25)插入class_id 名字随机(NameCreator生成) 年龄7-12岁之间
 */
public class Test4 {
    public static void main(String[] args) throws ClassNotFoundException {
        try(
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/empdb?characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&rewriteBatchedStatements=true","root","root");
        ) {
            Statement statement = conn.createStatement();
            Random random = new Random();
            int a;
            int age;
            for (int i = 0; i < 1000; i++) {
                a=random.nextInt(25)+1;
                age=random.nextInt(7)+6;//年龄
                String name = NameCreator.createName();
                String sql = "INSERT INTO student (name,age,class_id) VALUES ('"+name+"',"+age+","+a+");";
                System.out.println(sql);
                int c = age==6?0:(age-7)*4+random.nextInt(4)+2;//+2是因为1年级的ID从2开始
                System.out.println("年龄"+age+"年级ID:"+c);
//                int num = statement.executeUpdate(sql);
//                if (num>0){
//                    System.out.println("插入成功！");
//                }else {
//                    System.out.println("插入失败！");
//                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}

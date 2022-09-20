package jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/**
 * 指定DQL语句(数据查询语言)
 */
public class JDBCDemo3 {
    public static void main(String[] args) {
        try(
                Connection connection = DBUtil.getConnection()
        ) {
            Statement statement = connection.createStatement();
            //查看6岁的学生都有谁？
            String sql = "SELECT id,name,age,class_id " +
                         "FROM student " +
                         "WHERE age=6";
            /*
               Statement中有专门用来执行查询语句DQL的方法：ResultSet executeQuery(String sql)
               该方法会返回一个ResultSet对象,这个对象封装了查询出来的结果集
             */
            ResultSet rs = statement.executeQuery(sql);
            /*
              结果集的遍历类似集合的迭代器
              boolean next()
              该方法是结果集核心方法之一,由于让结果集游标向下一条记录,返回值表示是否有下一条。
              注：游标默认是在结果集第一条记录上
             */
            while (rs.next()){
                //根据字段位置获取
                //获取该条记录第一个字段的值(因为该字段时int型,所以用getInt())
                int id = rs.getInt(1);
                String name = rs.getString(2);
                int age = rs.getInt(3);
                int classId = rs.getInt(4);
//                //根据字段名
//                int id = rs.getInt("id");
//                String name = rs.getString("name");
//                int age = rs.getInt("age");
//                int classId = rs.getInt("class_id");
                System.out.println(id+","+name+","+age+","+classId);
            }
            //如果结果集数据需要保存,应当在当前java程序中用集合等形式保存结果集使用
            rs.close();//当结果集遍历完毕后将其关闭
        } catch (SQLException e) {
        }
    }
}

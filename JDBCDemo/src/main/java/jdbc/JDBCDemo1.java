package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * JDBC java database connectivity java数据库连接
 * JDBC是有SUM提供的一套API，是使用java连接数据的一套标准API接口，各个数据库提供上都实现了这一条接口提供了
 * 连接其提供的数据库产品的实现类(以jar包形式，又称为连接该数据库的驱动包)。
 */
public class JDBCDemo1 {
    public static void main(String[] args) throws ClassNotFoundException {
        /*
           使用JDBC连接数据库的步骤
            1:加载驱动 Class.forName()
            2:建立连接 DriverManager建立连接
            3:获取执行对象
         */
        //第一步：
        //不同数据库的包名,类名不一样,但是对于同一个数据库无论哪个版本写法都一样、
        Class.forName("com.mysql.cj.jdbc.Driver");
        //第二步：
        /*
          DriverManager的getConnection用来连接数据库,该方法需要传入三个参数
          参数1：数据库的URL地址(每种数据库有各自的固定格式)
          参数2：用户名
          参数3：密码
          该方法返回一个java.sql.Connection的实例(Connection是一个接口,是JDBC核心接口之一,表)
         */
        try(
            //jdbc:不同数据库的URL格式...  /数据库名？参数(可写可不写)...字符集&是否加密...   时区
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/empdb?characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&rewriteBatchedStatements=true","root","root")
        ) {
            System.out.println("与MySQL建立连接！");
        /*第三步
          通过连接对象Connection获取执行SQL的执行对象Statement
        */
            Statement statement = conn.createStatement();
        /*
           新建一张表：userinfo
           表字段：id,username,password,age,salary
         */
            String sal = "CREATE TABLE userinfo(" +
                    "    id INT primary key auto_increment," +
                    "    username VARCHAR(30) NOT NULL ," +
                    "    password VARCHAR(30)," +
                    "    age INT(3)," +
                    "    salary DOUBLE(7,2)" +
                    ")";
        /*
           execute(String sql)
           该方法可以用来执行任意类型的SQL语句,但是由于DML,DQL有专门的的SQL
           因此该方法常用于执行DDL语句(CREATE,DROP,ALTER)
           注意：在sql查询时数据库的URL地址要使用该数据库empdb后再去查询
         */
            statement.execute(sal);//到sql控制台可以查到该表的创建
            System.out.println("执行完毕！");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}

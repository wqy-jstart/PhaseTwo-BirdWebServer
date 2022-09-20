package jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Random;

/**
 * Statement每次执行SQL语句时，都会将该SQL语句发送给数据库，而数据库接收到SQL语句后会解析SQL语句并生成
 * 执行计划(该操作是一个耗时的操作)。然后在执行该计划。
 * 当SQL语义相同，但是数据不同时，如果我们执行这些SQL，那么每次数据库接收SQL都要生成执行计划。
 *
 * PreparedStatement会在创建时先将预编译SQL语句发送给数据库来生成执行计划(仅1次)，并且"?"内容会在生成的
 * 执行计划中当作"参数".
 * 在多次执行时，每次仅需要将"?"对应的数据发送给数据库，来重用预编译SQL对应的执行计划，这样效率会高很多。
 *
 *
 * Statement执行SQL语句时，数据是需要拼接SQL来完成，这存在SQL注入攻击，但是PreparedStatement会先将
 * 预编译SQL发送给数据库生成执行计划，那么所有数据都会被当作参数。因此就算传入的是注入攻击的内容，它也仅会
 * 当这部分内容为参数值，语义已经不会发生改变了(因为执行计划已经生成。)
 *
 * 拼接SQL注入攻击内容后，语义发生了改变，因此数据库接收到该SQL是就错误的执行了内容
 * SELECT * FROM userinfo WHERE username='xxx' AND password='1' OR '1'='1'
 *
 * 预编译SQL先行发送给数据，生成执行计划后，数据库就理解了操作，并等待你发送过来用户名和密码的值了
 * SELECT * FROM userinfo WHERE username=? AND password=?
 * 当我们发送SQL注入攻击内容时
 * 参数1(第一个?的内容):xxx,  参数2(第二个?的内容):1' OR '1'='1
 * 此时数据库会理解为你要查询的人的密码是"1' OR '1'='1",并不会将其当作SQL语句的一部分了。
 */
public class JDBCDemo6 {
    public static void main(String[] args) {
        try (
                Connection conn = DBUtil.getConnection()
        ) {
            //￥插入1000条数据用Statement
//            Statement statement = conn.createStatement();
//            Random random = new Random();
//            long start = System.currentTimeMillis();
//            for(int i=0;i<1000;i++) {
//                int age = random.nextInt(7) + 6;//年龄6-12岁
//                int c = age == 6 ? 1 : (age - 7) * 4 + random.nextInt(4) + 2;//+2是因为1年级的ID从2开始
//                String name = NameCreator.createName();
//                String sql = "INSERT INTO student1 (name,age,class_id) VALUES ('" + name + "'," + age + "," + c + ")";
//                statement.executeUpdate(sql);
//            }
//            long end = System.currentTimeMillis();
//            System.out.println("插入完毕，耗时:"+(end-start)+"ms");//2098ms

            //￥插入1000条数据用PreparedStatement
            String sql = "INSERT INTO student1 (name,age,class_id) VALUES (?,?,?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            Random random = new Random();
            long start = System.currentTimeMillis();
            for(int i=0;i<1000;i++) {
                int age = random.nextInt(7) + 6;//年龄6-12岁
                int c = age == 6 ? 1 : (age - 7) * 4 + random.nextInt(4) + 2;//+2是因为1年级的ID从2开始
                String name = NameCreator.createName();
                statement.setString(1,name);
                statement.setInt(2,age);
                statement.setInt(3,c);
                statement.executeUpdate();//调用执行方法
            }
            long end = System.currentTimeMillis();
            System.out.println("插入完毕，耗时:"+(end-start)+"ms");//2098ms
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

将用户的注册的信息存入数据库中
1.在com.webserver下新建一个util包,添加DBUtil类
2.在pom.xml配置文件中导入依赖
<!-- 连接MySQL数据库的依赖 -->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>8.0.15</version>
        </dependency>

        <!-- 数据库连接池 -->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid</artifactId>
            <version>1.1.21</version>
        </dependency>
3.在DBUtil类中封装与数据库建立连接的静态方法,以及在静态块中初始化
数据库用户名,数据库密码,数据库路径,初始连接数,最大连接数,当类加载时被调用
4.在UserController类中,修改注册和登录部分利用JDBC与数据库建立连接,将数据存放到数据库userinfo表中
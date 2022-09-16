# 数据库
# 存储数据的仓库,就称为数据库.
# 我们创建的一个目录users,里面存放了很多的obj文件,每一个文件保存了一组信息.users就是一个
# 数据库.只是我们管理起来要么手动,要么写一些代码进行维护.
# 低效，不通用.
#
# 数据库管理系统DBMS: DataBaseManagementSystem
# 常见的数据库管理系统:
# MySQL
# ORACLE
# SQLServer
# DB2

# 主要的学习目标:
# SQL：Structured Query Language
# 学习SQL语句。
# SQL语句是我们操作数据库的语言，通过执行SQL可以完成对数据的相关操作。

# 连接数据库的形式:
# 1:控制台(命令行)的客户端
# 2:第三方图形界面的软件
# 3:IDEA这样的集成开发环境中也带有连接数据库的操作
# 4:使用JDBC连接数据库
#
# SQL基础:
#(1).查看有多少个数据库(固定)
SHOW DATABASES ;

#(2).创建数据库
# CREATE DATABASE 数据库名;
#创建数据库:mydb
CREATE DATABASE mydb;

#(3).给指定数据库设置字符集
# CREATE DATABASE 数据库名 CHARSET=UTF8/GBK;
CREATE DATABASE mydb CHARSET =UTF8;
CREATE DATABASE mydb CHARSET =GBK;#重复库名会报错
CREATE DATABASE db1 CHARSET =GBK;
CREATE DATABASE db2 CHARSET =UTF8;

#(4).查看具体的数据库信息
# SHOW CREATE DATABASE 数据库名;
SHOW CREATE DATABASE db2;
SHOW CREATE DATABASE db1;

#(5).删除数据库
# DROP DATABASE 数据库名;
DROP DATABASE db1;
DROP DATABASE db2;

#(6).想保存数据,就要选取需要保存数据的数据库,然后才能在该数据库中建立表等操作.
# USE 数据库名;
#使用mydb这个数据库
USE mydb;

#[1]练习：
#创建mydb1和mydb2数据库,字符集分别为utf8和gbk
CREATE DATABASE mydb1 CHARSET =UTF8;
CREATE DATABASE mydb2 CHARSET =GBK;
#查询所有数据库检查是否创建成功
SHOW DATABASES ;
#检查两个数据库的字符集是否正确
SHOW CREATE DATABASE mydb1;
SHOW CREATE DATABASE mydb2;
#先使用mydb1再使用mydb2
USE mydb1;
USE mydb2;
#删除两个数据库
DROP DATABASE mydb1;
DROP DATABASE mydb2;

# 在mysql中,我们可以为不同的项目创建不同的数据库,而每个数据库中都可以创建若干张表,每张表
# 用来保存一组数据,比如我们为保存用户信息可以创建userinfo表,保存文章信息可以创建article表等

# SQL语句分类：
# DDL,DML,DQL,TCL,DCL

# 表相关的操作
# DDL语句:数据定义语言,用来操作数据库对象的.
# 数据库对象:表,视图,索引都属于数据库对象.

#(7).创建表(表中所有字段的默认值为null)
# CREATE TABLE 表名(-------★不给长度默认11位
#     列名1 类型[(长度)] [DEFAULT 默认值] [约束条件],
#     列名2 类型...
# )[CHARSET=utf8/gbk]

# 创建userinfo表
CREATE TABLE userinfo(
                         id INT,
                         username VARCHAR(32),
                         password VARCHAR(32),
                         nickname VARCHAR(32),
                         age INT(3)
);
#数字的长度表示位数,VARCHAR的长度表示最多占用的字节数

#(8).查看当前数据库创建的所有表
SHOW TABLES ;

#(9).查看创建的某一张表的详细信息
# SHOW CREATE TABLE 表名;
SHOW CREATE TABLE userinfo;

#(10).查看表结构,如果没有表则报错
# DESC 表名;
DESC userinfo;

#(11).修改表
#修改表名
# RENAME TABLE 原表名 TO 新表名;
RENAME TABLE userinfo TO user;

#(12).删除表
# DROP TABLE 表名;
DROP TABLE user;

#[2].练习：
#创建数据库mydb3 字符集为gbk
CREATE DATABASE mydb3 CHARSET =GBK;
#使用该数据库
USE mydb3;
#创建t_hero英雄表,有名字和年龄字段 默认字符集
CREATE TABLE t_hero(
                       id INT,
                       name VARCHAR(32),
                       age INT(3)
);
#修改表名为hero
RENAME TABLE t_hero TO hero;
#查看hero表的详细信息
SHOW CREATE TABLE hero;
#查看表结构
DESC hero;
#删除表hero
DROP TABLE hero;
#删除数据库mydb3
DROP DATABASE mydb3;
#查看当前数据库创建的所有表和数据库
SHOW TABLES ;
SHOW DATABASES ;

#先创建user表
CREATE TABLE user(
                     id INT(3),
                     name VARCHAR(32),
                     age INT(3)
);
#修改表结构：ALTER TABLE
#(13)添加列
#ALTER TABLE 表名 ADD 列名 类型[长度]
ALTER TABLE user ADD gender VARCHAR(10);
#查看表的结构
DESC user;

CREATE TABLE hero(
                     username VARCHAR(32),
                     age INT(3)
);

ALTER TABLE hero ADD id VARCHAR(10) FIRST ;#加入FIRST后为第一列
#查询hero表的结构
DESC hero;

#(14).在表中插入一个字段
# ALTER TABLE 表名 ADD 要添加的列名 类型[长度] AFTER 字段名(在其之后);
ALTER TABLE hero ADD gender VARCHAR(32) AFTER username;

#(15).删除表中现有的列
# ALTER TABLE 表名 DROP 列名;
ALTER TABLE hero DROP gender;
#查询表的结构
DESC hero;

#(16).修改表中现有的列(表名,字段名,类型,长度)----CHANGE(改变)
# ALTER TABLE 表名 CHARSET 原字段名 新字段名 新类型;
#将age的类型从INT改成VARCHAR
ALTER TABLE hero CHANGE age age VARCHAR(10);
DESC hero;
#将age的长度改成100
ALTER TABLE hero CHANGE age age VARCHAR(100);
DESC hero;
#将age改为gender,长度改为10
ALTER TABLE hero CHANGE age gender VARCHAR(10);
DESC hero;

#[3].练习：
#创建数据库mydb4并设置字符集为UTF8
CREATE DATABASE mydb4 CHARSET=UTF8;
#查看具体的数据库mydb4的信息
SHOW CREATE DATABASE mydb4;
#使用数据库mydb4
USE mydb4;
#创建teacher表,有名字字段
CREATE TABLE teacher(
    name VARCHAR(32)
);#[CHARSET = utf8/gbk]可指定字符集
#在teacher表中添加id字段,类型为INT,放在第一位
ALTER TABLE teacher ADD id INT(3) FIRST ;
#在teacher表中添加一个age字段,类型为INT
ALTER TABLE teacher ADD age INT(3);
#在teacher表中添加一个字段salary,类型为INT,放在name后面-----AFTER(之后)
ALTER TABLE teacher ADD salary INT(3) AFTER name;
#查看表teacher的结构
DESC teacher;
#删除teacher表中的列age,内容都会删除
ALTER TABLE teacher DROP age;
#修改teacher表的名字为t
RENAME TABLE teacher To t;
#查看表t的结构
DESC t;
#删除表t
DROP TABLE t;
#查看当前数据库创建的所有表
SHOW TABLES ;
#删除数据库mydb4
DROP DATABASE mydb4;
#查看有多少个数据库(固定)
SHOW DATABASES ;

#总结
# DDL语言,数据定义语言,操作数据库对象
# CREARE,ALTER,DROP
# 创建表:CREATE TABLE
# 修改表: ALTER TABLE
# 删除表: DROP TABLE

#DML语言:数据操作语言,是对表中的数据进行操作的语言,包含:增,删,改操作
CREATE TABLE person(
                       name VARCHAR(32),
                       age INT(3)
);
SHOW TABLES ;

#(17).★插入数据(数据库中''中加字符串)-----INSERT(插入)
# INSERT INTO 表名 [(字段1,,字段2...)] VALUES (字段1的值,字段2的值...);
INSERT INTO person(name,age) VALUES ('武清源',20);
INSERT INTO person(name,age) VALUES ('张三',22);
#也可颠倒字段,但后面的值一定要对应
INSERT INTO person(age,name) VALUES (22,'王五');

#未指定的列插入的都是列的默认值,当创建表时没有为列声明特定的默认值时,列默认值都为null.
INSERT INTO person(name) VALUES ('李四');

#字段名可以忽略不写，此时为全列插入，即:VALUES需要指定每一列的值，且顺序，个数，类型必须与表中的字段一致
INSERT INTO person VALUES('传奇',22);

#(18).查看person表中的所有数据
#SELECT * FROM 表名
SELECT * FROM person;
#查看表结构
DESC person;
#查看当前数据库创建的所有表
SHOW TABLES ;

#(19).修改表数据操作:----UPDATE语句
# UPDATE 表名 SET 字段名1=新值1[,字段2=新值2,...][WHERE 过滤条件]

#通常修改语句要添加WHERE子句,用于添加过滤条件来定位要修改的记录,不添加WHERE子句则是全表所有记录都修改。
#下面的操作会将person表中每条记录的age字段值都改为55！！！
UPDATE person SET age=55;

#将李四的年龄改为23岁
UPDATE person SET age=23 WHERE name='李四';

#WHERE中常用的条件:=,>,>=,<,<=,<>(所有数据库都支持的不等于,！=不是所有数据库都支持)
#将年龄大于50岁的人的年龄改为25
UPDATE person SET age=25 WHERE age>50;

#修改字段时,可以将计算表达式的结果进行修改
#将每个人的年龄涨一岁------这种情况不需要加WHERE条件
UPDATE person SET age=age+1;

#将年龄为24岁的人改名为 李老四,年龄为55(逗号隔开)
UPDATE person SET name='李老四',age=55
WHERE age=24;

SELECT *FROM person;

#(20).删除数据:DELETE语句
# DELETE FROM 表明 [WHERE 过滤条件]
# 注意！！！ 不添加WHERE条件则是全表删除！！！---系统会提示

# 删除名字为张三的记录
DELETE FROM person WHERE name='张三';

UPDATE person SET age=20 WHERE name='张三';

#删除年龄大于25的人
DELETE FROM person WHERE age>25;

#清空表操作
DELETE FROM person;

#[4].练习：
#创建数据库day1db 字符集utf8并使用
CREATE DATABASE day1db CHARSET=UTF8;
#使用数据库day1db
USE day1db;
#创建t_hero表,有name字段 字符集为utf8
CREATE TABLE t_hero(
    name VARCHAR(32)
)CHARSET=UTF8;
#修改表名为hero-------RENAME
RENAME TABLE t_hero TO hero;
#最后面添加价格字段money
ALTER TABLE hero ADD money INT(3);
#最前面添加id字段
ALTER TABLE hero ADD id INT(3) FIRST;
#在name后面添加age字段
ALTER TABLE hero ADD age INT(3) AFTER name;
#表中添加以下数据: 1,李白,50,6888 2.赵云,30,13888 3.刘备,25,6888
#合并写(建议)
INSERT INTO hero(id,name,age,money) VALUES (1,'李白',50,6888),(2,'赵云',30,13888),(3,'刘备',25,6888);
#分开写(不建议)
INSERT INTO hero(id,name,age,money) VALUES (1,'李白',50,6888);
INSERT INTO hero(id,name,age,money) VALUES (2,'赵云',30,13888);
INSERT INTO hero(id,name,age,money) VALUES (3,'刘备',25,6888);
#查看hero表中所有数据
SELECT * FROM hero;
#查询价格为6888的英雄名
SELECT name FROM hero WHERE money=6888;
#修改刘备的年龄为52岁
UPDATE hero SET age=52 WHERE name='刘备';
#修改年龄小于等于50岁的价格为5000
UPDATE hero SET money=5000 WHERE age<=50;
#删除价格为5000的信息
DELETE FROM hero WHERE money=5000;
#删除表hero
DELETE FROM hero;
#删除数据库day1db
DROP DATABASE day1db;
#查看有多少的数据库
SHOW DATABASES;

#   ★数据类型
#整数:INT(m)和BIGINT(m).m表示的是长度 例如:m=5 存数字18 实际存储:00018
#浮点数:DOUBLE(m,n)。m表示数字长度(整体数字的长度，包含小数)，n表示小数位 DOUBLE(5,3) 99.999
# INSERT INTO XXX VALUES(12.9984) 实际插入数据时当精度超过可保存范围时，会进行四舍五入

#   ★字符类型
#CHAR(n):定长字符串。每条记录实际占用的字节空间是定长的，不足的部分补充空字符来满足长度要求
#        优点:查询速度快  缺点:浪费磁盘空间
# VARCHAR(n):(变长字符串)。最多存n指定的字节数对应的字符，实际保存是用多少占多少。(推荐)
#         优点:节省磁盘空间 缺点:查询速度慢
# TEXT(n):可变长字符串，最大65535
#
#   ★日期时间类型
# DATE:保存年月日
# TIME:保存时分秒
# DATETIME:保存年月日十分秒
# TIMESTAMP:时间戳，记录UTC时间，从1970-01-01 00:00:00到表示的时间之间经过的毫秒
USE empdb;
SELECT * FROM emp;

#(21).引入数据类型
CREATE TABLE userinfo(
                         id INT,#整型
                         name VARCHAR(30),#变长字符
                         birth DATETIME,#存年月日时分秒
                         salary DOUBLE(7,2)#浮点型
);

#字段名可以忽略不写，此时为全列插入，即:VALUES需要指定每一列的值，且顺序，个数，类型必须与表中的字段一致
INSERT INTO userinfo VALUES (1,'张三','1992-08-02 11:22:05',5000.59);

#插入李四,DATETIME类型在不指定时分秒,默认为00:00:00
INSERT INTO userinfo VALUES (2,'李四','1999-05-16',6500.23);

# #DATETIME中年月日不能省略否则报错或者默认DATETIME默认为null(MySQL版本不同引起)
# INSERT INTO userinfo VALUES (3,'王五','06:15:55',1900.788);

#插入DOUBLE类型数字时,如果小数部分超过范围会进行四舍五入
INSERT INTO userinfo VALUES (3,'王五','1987-04-13 06:15:55',1900.788);
INSERT INTO userinfo VALUES (4,'赵六','1966-03-24',2000);
INSERT INTO userinfo VALUES (5,'钱七','1860-01-22',40000);

# #整数超过范围会报错或者补最大值(MySQL版本不同)----不建议这样写
# INSERT INTO userinfo VALUES (6,'老八','1976-02-18',140000);

#查询表中的所有数据
SELECT * FROM userinfo;

#(22).约束
# 约束是为表中某个字段添加特定的限制条件，只有符合条件的记录才可以保存
#
# 主键约束:该字段非空且唯一，用该字段的值唯一表示一条记录
# 非空约束:该字段的值不允许为空
# 外键约束:实际开发中几乎不使用外键约束

CREATE TABLE student(
                        id INT AUTO_INCREMENT PRIMARY KEY,#primary(主要的) key主键
                        name VARCHAR(30) NOT NULL,#NOT NULL非空约束
                        age INT(3),
                        gender CHAR(1) #长度为1,超出按第一个
);
# 查看student表结构
DESC student;

#由于name为NOT NULL，因此名字必须指定值，不指定会报错
# INSERT INTO student VALUES (NULL,NULL,55,'m');#名字不能为空

#★id本身的主键约束要求非空且唯一，但是由于有自增，所以当不指定时使用系统生成的自增值(有自增时就不要主动赋值)
INSERT INTO student VALUES (NULL,'刘桑',55,'m');

#★主键有自增时,可以忽略主键字段的插入
INSERT INTO student(name,age,gender) VALUES ('克晶',18,'f');

#有非空约束的字段插入数据是不可以忽略(因为忽略的字段默认插入NULL值)
# INSERT INTO student(age,gender) VALUES (18,'f');

#允许为NULL的字段可以通过显式的指定NULL作为值插入
INSERT INTO student(name,age,gender) VALUES ('传奇',22,NULL);
#也可以不指定字段来为该字段隐式的插入NULL值
INSERT INTO student(name,age) VALUES ('国斌',22);

#查询student表中所有的数据
SELECT * FROM student;

CREATE TABLE teacher(
                        id INT PRIMARY KEY,
                        name VARCHAR(30) NOT NULL,
                        age INT(3),
                        gender CHAR(1)
);
# 插入一行数据
INSERT INTO teacher VALUES (1,'张三',22,'F');

# 报错，主键字段不允许插入重复的值Duplicate entry '1' for key 'PRIMARY'
INSERT INTO teacher VALUES (1,'李四',23,'M');

# 报错,主键字段不能为NULL值Column 'id' cannot be null
INSERT INTO teacher VALUES (NULL,'李四',23,'M');

# (23).NULL作为条件是,要使用IS NULL 或 IS NOT NULL
#查询不到任何数据-----在SQL中值不能与NULL(空)画等号,不支持
SELECT * FROM student WHERE gender=NULL;

# 查看性别为null的学生信息
SELECT * FROM student WHERE gender IS NULL;

# 查看性别不为null的学生信息
SELECT * FROM student WHERE gender IS NOT NULL;

#查询表中所有的数据
SELECT * FROM teacher;
SELECT * FROM student;


# (24).DQL语句 数据查询语句
# 基本语法：
# SELECT 字段名1[,字段名2...或 * ] FROM 表名;

USE empdb;
# 查看emp表中每条记录的所有字段值
SELECT * FROM emp;
# 查看每个员工的名字,职位,入职时间
SELECT name,job,hiredate FROM emp;

# WHERE子句,用来添加过滤条件,此时可以仅将满足条件的记录查询出来
# 比较运算符：=,>,>=,<,<=,<>(不等于)----!=操作不是所有的数据库都支持
# 查看工资大于1000的员工名字,职位,工资
#  SELECT(选择)
#  FROM(来自于)
#  WHERE(哪里)
SELECT name,job,sal
FROM emp
WHERE sal>1000;

#查看职位除'人事'之外的所有员工的名字,工资和职位
SELECT name,sal,job
FROM emp
WHERE job <>'人事';

#查看部门编号为2的员工名字,工资,职位,部门编号(dept_id)
SELECT name,job,sal,dept_id
FROM emp
WHERE dept_id=2;

# (25).AND,OR来连接多个条件---★AND优先级高于OR,可以通过()来提高OR的优先级
# AND:都为真时才为真  &&
# OR:都为假时才为假  ||
#查看2号部门工资高于1000的员工的名字,工资,职位,部门编号？---两个条件AND
SELECT name,sal,job,dept_id
FROM emp
WHERE dept_id=2 AND sal>1000;

#查看职位是人事或销售的所有员工的名字,工资,职位,部门编号?---两个条件OR
SELECT name,sal,job,dept_id
FROM emp
WHERE job='人事' OR job='销售';

#查看人事部的所有员工和工资高于1000的销售部员工的名字,工资,职位？
SELECT name,sal,job
FROM emp
WHERE job='人事' OR (sal>1000 AND job='销售');

#查看人事部和销售部工资高于1000的员工的名字,工资,职位？
SELECT name,sal,job
FROM emp
# AND优先级高于OR,因此可以通过()来提高OR的优先级
WHERE (job='人事' OR job='销售') AND sal>1000;

# (26).IN(列表) 值在列表中(等于列表中的其中之一)----★获取同一字段的子集时用IN
#      NOT IN(不在列表) 值不在列表中
# 查看职位是人事和销售的所有员工的名字,工资,职位,部门编号？
SELECT name,sal,job,dept_id
FROM emp
WHERE job IN('人事','销售');

#查看人事和销售以外的所有员工的名字,工资,职位,部门编号？
SELECT name,sal,job,dept_id
FROM emp
WHERE job<>'人事' AND job<>'销售';
#不在列表中
SELECT name,sal,job,dept_id
FROM emp
WHERE job NOT IN ('人事','销售');

#查看工资在2000到3000之间员工的名字,工资,职位？
SELECT name,sal,job
FROM emp
WHERE sal>=2000 AND sal<=3000;

# (27).用法：BETWEEN min AND max
#使用between(介于...之间)来实现区间需求
SELECT name,sal,job
FROM emp
WHERE sal BETWEEN 2000 AND 3000;

# (28).DISTINCT用于去除指定列重复值的行----distinct(不同的)
#查看公司有多少种职位？---单列去重
SELECT DISTINCT job FROM emp;

#多列去重时,就是去除指定这些列的值的组合有重复的行
#去除职位与部门编号重复的行
SELECT DISTINCT job,dept_id FROM emp;

#查看emp表中的所有数据
SELECT * FROM emp;

#[5].练习：
#1.查询2号部门工资高于1000的员工信息
SELECT *
FROM emp
WHERE dept_id=2 AND sal>1000;
#2.查询3号部门或工资等于5000的员工信息
SELECT *
FROM emp
WHERE dept_id=3 OR sal=5000;
#3.查询工资在1000到2000之间的员工姓名和工资
SELECT name,sal
FROM emp
WHERE sal BETWEEN 1000 AND 2000;
#4.查询工资不等于3000和5000的员工信息
SELECT *
FROM emp
WHERE sal NOT IN (3000,5000);
#5.查询1号部门有哪几种不同的工作
#可以先查看1号部门的所有职位
SELECT job
FROM emp
WHERE dept_id=1;
#然后查询不同的工作
SELECT DISTINCT job
FROM emp
WHERE dept_id=1;


# (29).模糊查询:LIKE
#LIKE中两个通配符:_和%
#_(下划线):表示任意的一个字符
# %:表示任意个字符(0-多个)

#查看姓孙的员工信息？(名字第一个字符是孙的员工)
SELECT *
FROM emp
WHERE name LIKE '孙%';

#查看名字里含"悟"的员工？
SELECT name,sal,job
FROM emp
WHERE name LIKE '%悟%';

#查看名字第三个字是'精'的员工
SELECT name,sal,job
FROM emp
WHERE name LIKE '__精';

#查看第二个字是'骨'的员工？
SELECT name,sal,job
FROM emp
WHERE job LIKE '_骨%';

# 总结
# %X%：字符串中包含'X'
# %X：字符串以X结尾
# X%：字符串以X开头
# _X%：字符串第二个字符是X
# %X_：倒数第二个字符是X
# X%Y：字符串以X开头以Y结尾
# X_Y：字符串只有三个字,第一个是X,第三个是Y

#[6].练习：
#查询名字姓"猪"的员工姓名
SELECT name
FROM emp
WHERE name LIKE '猪%';
#查询名字中包含"僧"的员工信息
SELECT *
FROM emp
WHERE name LIKE '%僧%';
#查询名字中以"精"结尾的员工姓名
SELECT name
FROM emp
WHERE name LIKE '%精';
#查询工作中包含"销售"并且工资大于1500的员工信息
SELECT *
FROM emp
WHERE job LIKE '%销售%' AND sal>1500;
#查询工作中第二个字是"售"的员工姓名和工作
SELECT name,job
FROM emp
WHERE job LIKE '_售%';
#查询1号和2号部门中工作以"市"开头的员工信息
SELECT *
FROM emp
WHERE dept_id IN (1,2) AND job LIKE '市%';
#查看表emp的所有数据
SELECT * FROM emp;

# (30).排序-----ORDER(order顺序)
# ORDER BY子句,根据指定的字段排序查询结果集,该子句只能放在查询语句的最后一个子句上
# 查看公司所有员工的姓名和工资,且按工资从低到高排序
SELECT name,sal
From emp
ORDER BY sal;

# 查看公司所有员工的姓名和入职时间,且按入职时间从早到晚排序
SELECT name,hiredate
FROM emp
ORDER BY hiredate;

# 按照升序排序时可以使用关键字ASC,但是通常不需要写,因为默认就是升序(从小到大)
SELECT name,hiredate
FROM emp
ORDER BY hiredate ASC;

#按照工资从大到小排序(降序),降序使用关键字DESC
SELECT name,sal
FROM emp
ORDER BY sal DESC;

#查看每个部门的工资排名--可以逗号隔开多字段排序
SELECT name,dept_id,sal
FROM emp
ORDER BY dept_id,sal;#部门升序,工资升序排列

SELECT name,dept_id,sal
FROM emp
ORDER BY dept_id,sal DESC;#部门升序,工资降序排列

SELECT name,dept_id,sal
FROM emp
ORDER BY dept_id DESC,sal DESC;#部门降序,工资降序排列

#[7].练习：
#查询表emp中的所有数据
SELECT * FROM emp;
#1.查询有领导的员工信息,按照入职日期(hiredate)升序排列
SELECT *
FROM emp
WHERE manager IS NOT NUll
ORDER BY hiredate;
#2.查询1号部门中名字中包含八的员工信息
SELECT *
FROM emp
WHERE dept_id=1 AND name LIKE '%八%';
#3.查询2号和3号部门中工资低于1500的员工信息
SELECT *
FROM emp
WHERE dept_id IN (2,3) AND sal<1500;
#或者用OR
SELECT *
FROM emp
WHERE (dept_id=2 OR dept_id=3) AND sal<1500;
#4.查询人事和程序员中工资高于2500的员工姓名,工资,工作
SELECT name,job,sal
FROM emp
WHERE job IN ('人事','程序员') AND sal>2500;
#5.查询不是CEO的员工中工资高于2000的员工姓名,工资和工作,并且按照工资降序排列
SELECT name,job,sal
FROM emp
WHERE job<>'CEO' AND sal>2000
ORDER BY sal DESC;

# (31).分页查询----LIMIT(limit 限制)
# 将满足查询条件的数据分段分批的查询出来。这可以减少不必要的系统开销。
# 分页查询在SQL92标准没有定义，这意味着不同的数据库，分页查询方式完全不一样。
# 在ORDER BY中使用LIMIT来完成的
# LIMIT "跳过的"记录数,请求的记录数(每页显示的记录数)
# LIMIT (页数-1)*每页显示的记录数,每页显示的记录数
# 不跟在ORDER BY后面也行,但建议跟,因为排序后再分页比较清晰
#查询表emp中的所有数据
SELECT * FROM emp;

#按照工资降序后,每页显示3条,查看第2页
SELECT name,sal
FROM emp
ORDER BY sal DESC #按照降序排列
LIMIT 3,3; #每页显示3条,查看第二页,跳过3条

#按照工资降序后,每页显示3条,查看第3页
SELECT name,sal
FROM emp
ORDER BY sal DESC #按照降序排列
LIMIT 6,3; #每页显示3条,查看第三页,跳过6条

#按照工资降序后,每页显示4条,查看第3页
SELECT name,sal
FROM emp
ORDER BY sal DESC #按照降序排列
LIMIT 8,4; #每页显示4条但末尾只有3条,查看第三页,跳过8条
#=====================================================================
# (32).查询的字段可以使用表达式
# 查看每个员工的年薪是多少？
SELECT name,sal,sal*12
FROM emp;

# (33).查询时也可以用函数的结果作为字段
# 孙悟空的职位是销售  name+"的职位是"+job(java中)
SELECT CONCAT(name,'的职位是',job) FROM emp;

#查看每个员工的工资,奖金,工资+奖金
# ★数字与NULL进行运算,结果为NULL
SELECT name,sal,comm,sal+comm
FROM emp;

# (34).NVL函数 用来替换NULL值
# NVL(ar1,arg2) 当arg1不为null时则函数返回arg1的值,如果arg1为null则返回arg2的值
SELECT name,sal,NVL(comm,0) FROM emp;#第一个参数为NULL返回第二个参数值

# (35).字段定义别名
#我们可以为字段定义别名,也可以给表定义别名。
# 1.SELECT 字段1 别名1,字段2 别名2 FROM 表名;
SELECT name ename,sal salary FROM emp;
SELECT name,sal*12 salary FROM emp;

#★支持的语法
# 字段名 别名
SELECT name,sal*12 salary FROM emp;
# 字段名 as 别名
SELECT name as ename,sal*12 salary FROM emp;
# 字段名 as '别名'
SELECT name as 'ename',sal*12 'salary' FROM emp;
# 字段名 as "别名"
SELECT name as "ename",sal*12 "salary" FROM emp;
#查询表emp中的所有数据
SELECT * FROM emp;

#[8].练习：
#1.查询员工表中3号部门工资高于1500的员工信息
SELECT *
FROM emp
WHERE dept_id=3 AND sal>1500;
#2.查询2号部门员工或者没有领导的员工信息
SELECT *
FROM emp
WHERE dept_id=2 OR manager IS NULL;
#3.查询有领导的员工姓名,工资按照工资降序排列
SELECT name,sal
FROM emp
WHERE manager IS NOT NULL
ORDER BY sal DESC;
#4.查询2号和3号部门的员工姓名和入职日期hiredate按照入职日期降序排列
SELECT name,hiredate
FROM emp
WHERE dept_id IN (2,3)
ORDER BY hiredate DESC;
#5.查询名字中包含僧和包含精的员工姓名
SELECT name
FROM emp
WHERE name LIKE '%僧%' OR name LIKE '%精%';
#6.查询工资高于2000的工作有哪几种？
SELECT DISTINCT job
FROM emp
WHERE sal>2000;
#7.查询工资升序第4页的2条数据
SELECT name,sal
FROM emp
ORDER BY sal ASC
LIMIT 6,2;

# (36).聚合函数(也称为多行函数),用来将多条记录统计为一条记录---忽略NULL值
# MIN():求最大值
# MAX():求最小值
# COUNT():统计记录数
# AVG():求平均值
# SUM():求和
# COUNT():统计某一字段的数量
#查看emp表中所有数据
SELECT * FROM emp;

#查看所有的员工数量添加别名(员工数量)
SELECT COUNT(*) 员工数量
FROM emp;

#查看公司最低收入的员工工资是多少？
SELECT name,MIN(sal) FROM emp;

#查看工资的最低,最高,平均,求和 并各自加上别名
SELECT MIN(sal) 最低工资,MAX(sal) 最高工资,AVG(sal) 平均工资,SUM(sal) 工资总和 FROM emp;

#聚合函数忽略NULL值,在AVG中比较明显可以看出这一点,一下仅对4个奖金的人取了平均值,并非11个人
SELECT MIN(comm) 最低奖金,MAX(comm) 最高奖金,AVG(comm) 平均奖金,SUM(comm) 奖金总和 FROM emp;

#[9].练习：
#1.查询销售的平均工资
SELECT job,AVG(sal)
FROM emp
WHERE job='销售';
#2.查询程序员的最高工资
SELECT job,MAX(sal)
FROM emp
WHERE job='程序员';
#3.查询名字包含精的员工数量
SELECT COUNT(name)
FROM emp
WHERE name LIKE '%精%';
#4.查看和销售相关的工作一个月工资总和
SELECT SUM(sal)
FROM emp
WHERE job LIKE '%销售%';
#5.查询2号部门的最高工资和最低工资
SELECT MAX(sal) 最高工资,MIN(sal) 最低工资
FROM emp
WHERE dept_id=2;

# (37).GROUP BY 分组
#GROUP BY 也是统计服务的,所以是搭配在聚合函数上使用的。

#查看每个部门的平均工资是多少？ORDER BY(排序)--ASC升序,DESC降序
SELECT dept_id,sal
FROM emp
ORDER BY dept_id;

#SELECT字句中不聚合函数中的其他字段必须出现在GROUP BY字句中
SELECT dept_id,AVG(sal)
FROM emp
GROUP BY dept_id;

#每种职位的最高工资是多少？
SELECT job,MAX(sal)
FROM emp
GROUP BY job;

#[10].练习：
#1.查看每个部门的最高工资
SELECT dept_id ,MAX(sal)
FROM emp
GROUP BY dept_id;
#2.查看每个部门工资高于2000的人数
SELECT dept_id,COUNT(*)
FROM emp
WHERE sal>2000
GROUP BY dept_id;
#3.查询每种工作的最低工资
SELECT job,MIN(sal)
FROM emp
GROUP BY job;
#4.查看1号部门和2号部门的人数
SELECT dept_id,COUNT(*)
FROM emp
WHERE dept_id IN (1,2) #过滤只看1号和2号部门
GROUP BY dept_id;
#5.查询平均工资最高的部门和平均工资
SELECT AVG(sal) avg,dept_id #查看平均工资和部门
FROM emp
GROUP BY dept_id #按照部门分组
ORDER BY AVG(sal) DESC #平均工资降序排列
LIMIT 0,1; #取第一条
# 可以为函数或表达式字段取别名，然后利用别名排序。
SELECT AVG(sal) avg,dept_id #查看平均工资和部门
FROM emp
GROUP BY dept_id #按照部门分组
ORDER BY avg  DESC
LIMIT 0,1;

#[11].练习：
# 查看平均工资大于2000的部门
SELECT dept_id,AVG(sal) avg
FROM emp
GROUP BY dept_id #按照部门分组
HAVING avg>2000; #HAVING在统计结果之后进行过滤,可过滤函数

# 查看最低工资大于1000的部门的平均工资
SELECT dept_id,AVG(sal) avg
FROM emp
GROUP BY dept_id #按照部门分组
HAVING MIN(sal)>1000;


























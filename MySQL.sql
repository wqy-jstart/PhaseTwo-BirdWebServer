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
# CREATE TABLE 表名(
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

#(18).查看person表中的所有数据
#SELECT * FROM 表名
SELECT * FROM person;
#查看表结构
DESC person;
# 练习
SELECT * FROM emp;
# 1. 查询工资大于等于3000的员工姓名和工资
SELECT name,sal
FROM emp
WHERE sal>3000;
# 2. 查询1号部门的员工姓名和工作
SELECT name,job
FROM emp
WHERE dept_id=1;
# 3. 查询不是程序员的员工姓名和工作(两种写法)
#第一种
SELECT name,job
FROM emp
WHERE job<>'程序员';
#第二种
SELECT name,job
FROM emp
WHERE job!='程序员';
# 4. 查询奖金等于300的员工姓名,工资和工作
SELECT name,sal,job
FROM emp
WHERE comm=300;
# 5. 查询1号部门工资大于2000的员工信息
SELECT *
FROM emp
WHERE dept_id=1 AND sal>2000;
# 6. 查询3号部门或工资等于5000的员工信息
SELECT *
FROM emp
WHERE dept_id=3 OR sal=3000;
# 7. 查询出CEO和项目经理的名字
SELECT name
FROM emp
WHERE job IN ('CEO','项目经理');
# 8. 查询工资为3000,1500和5000的员工信息
SELECT *
FROM emp
WHERE sal IN (3000,1500,5000);
# 9. 查询工资不等于3000,1500和5000的员工信息
SELECT *
FROM emp
WHERE sal NOT IN (3000,1500,5000);
# 10. 查询工资在1000到2000之间的员工信息
#第一种
SELECT *
FROM emp
WHERE sal BETWEEN 1000 AND 2000;
#第二种
SELECT *
FROM emp
WHERE sal>1000 AND sal<2000;
# 11. 查询工资在1000到2000以外的员工信息
#第一种
SELECT *
FROM emp
WHERE sal NOT BETWEEN 1000 AND 2000;
#第二种
SELECT *
FROM emp
WHERE sal<1000 OR sal>2000;
# 12. 查询有领导的员工姓名和领导id
SELECT name,dept_id
FROM emp
WHERE manager IS NOT NULL;
# 13. 查询没有领导的员工姓名和领导id
SELECT name,id
FROM emp
WHERE manager IS NULL;
# 14. 查询员工表中出现了哪几种不同的工作
SELECT DISTINCT job
FROM emp;
# 15. 查询员工表中出现了那几个部门的id
SELECT DISTINCT dept_id
FROM emp;
# 16. 查询姓孙的员工姓名
SELECT name
FROM emp
WHERE name LIKE '孙%';
# 17. 查询名字最后一个字是精的员工信息
SELECT *
FROM emp
WHERE name LIKE '%精';
# 18. 查询工作中包含销售的员工信息
SELECT *
FROM emp
WHERE job LIKE '%销售%';
# 19. 查询工作中第二个字是售的员工信息
SELECT *
FROM emp
WHERE job LIKE '_售%';
# 20. 查询名字中包含僧的员工并且工资高于2000的员工信息
SELECT *
FROM emp
WHERE name LIKE '%僧%' AND sal>2000;
# 21. 查询1号和2号部门中工作以市开头的员工信息
SELECT *
FROM emp
WHERE dept_id IN (1,2) AND job LIKE '市%';
# 22. 查询所有员工的姓名和工资 按照工资升序排序
SELECT name,sal
FROM emp
ORDER BY sal ASC;
# 23. 查询所有员工的姓名和工资 按照工资降序排序
SELECT name,sal
FROM emp
ORDER BY sal DESC;
# 24. 查询所有员工姓名 工资和部门id 按照部门id降序排序,如果部门id一致则按照工资升序排序
SELECT name,sal,dept_id
FROM emp
ORDER BY dept_id DESC,sal;
# 25. 查询员工表中3号部门工资高于1500的员工信息
SELECT *
FROM emp
WHERE dept_id=3 AND sal>1500;
# 26. 查询2号部门员工或者没有领导的员工信息
SELECT *
FROM emp
WHERE dept_id=2 OR manager IS NULL;
# 27. 查询有领导的员工姓名,工资按照工资降序排序
SELECT name,sal
FROM emp
WHERE manager IS NOT NULL
ORDER BY sal DESC;
# 28. 查询2号和3号部门的员工姓名和入职日期hiredate 按照入职日期降序排序
SELECT name,hiredate
FROM emp
WHERE dept_id IN (2,3)
ORDER BY hiredate DESC;
# 29. 查询名字中包含僧和包含精的员工姓名
SELECT name
FROM emp
WHERE name LIKE '%僧%' OR name LIKE '%精%';
# 30. 查询工资高于2000的工作有哪几种?
SELECT DISTINCT job
FROM emp
WHERE sal>2000;
# 31. 查询工资最高的前三个员工
SELECT name,sal
FROM emp
ORDER BY sal DESC
    LIMIT 0,3;
# 32. 查询员工表按照id排序, 第2页的5条数据
SELECT *
FROM emp
ORDER BY id
    LIMIT 5,5;
# 33. 查询员工表按照id排序, 第3页的4条数据
SELECT *
FROM emp
ORDER BY id
    LIMIT 8,4;
# 34. 查询3号部门工资最低的员工姓名和工资
SELECT name,sal,dept_id
FROM emp
WHERE dept_id=3
ORDER BY sal
    LIMIT 0,1;
# 35. 查询工作不是人事的员工中工资降序第二页的3条数据
SELECT *
FROM emp
WHERE job NOT IN ('人事','员工')
ORDER BY sal DESC
    LIMIT 3,3;
# 36. 查询没有领导的员工和3号部门的员工,工资降序取前三条
SELECT *
FROM emp
WHERE manager IS NULL OR dept_id=3
ORDER BY sal DESC
    LIMIT 0,3;
# 37. 查询2号部门的最高工资
SELECT MAX(sal)
FROM emp
WHERE dept_id=2;
# 40. 查询有领导的员工中工资在1000到2000之间的人数
SELECT COUNT(*)
FROM emp
WHERE manager IS NOT NULL AND sal BETWEEN 1000 AND 2000;
# 41. 查询3号部门的工资总和
SELECT SUM(sal)
FROM emp
WHERE dept_id=3;
# 42. 查询程序员和销售的总人数
SELECT COUNT(*)
FROM emp
WHERE job IN ('程序员','销售');
# 43. 查询1号部门有领导的员工的平均工资
SELECT AVG(sal)
FROM emp
WHERE dept_id=1 AND manager IS NOT NULL;
# 44. 查询1号部门的最低工资和最高工资
SELECT MIN(sal),MAX(sal)
FROM emp
WHERE dept_id=1;
# 45. 查询和销售相关的工作人数
SELECT COUNT(*)
FROM emp
WHERE job LIKE '%销售%';
# 46. 查询工资不是1500和3000的员工人数
SELECT COUNT(*)
FROM emp
WHERE sal NOT IN (1500,3000);
# 47. 查询1号部门出现了哪几种工作
SELECT DISTINCT job
FROM emp
WHERE dept_id=1;
# 48. 查询名字包含精的员工数量
SELECT COUNT(*)
FROM emp
WHERE name LIKE '%精%';
# 49. 查询和销售相关的工作一个月工资总和
SELECT SUM(sal)
FROM emp
WHERE job LIKE '%销售%';
# 50. 查询2号部门的最高工资和最低工资起别名
SELECT MAX(sal) max,MIN(sal) min
FROM emp
WHERE dept_id=2;
# 51.查询每个部门的平均工资
SELECT dept_id,AVG(sal)
FROM emp
GROUP BY dept_id;
# 52. 查询每种工作的平均工资
SELECT job,AVG(sal)
FROM emp
GROUP BY job;
# 53. 查询每个部门的最高工资
SELECT dept_id,MAX(sal)
FROM emp
GROUP BY dept_id;
# 54. 查询每种工作的最低工资
SELECT job,MIN(sal)
FROM emp
GROUP BY job;
# 55. 查询每个部门工资高于2000的人数
SELECT dept_id,COUNT(*)
FROM emp
WHERE sal>2000
GROUP BY dept_id;
# 56. 查询每个部门有领导的员工人数
SELECT dept_id,COUNT(*)
FROM emp
WHERE manager IS NOT NULL
GROUP BY dept_id;
# 57. 查询1号部门每种工作的最低工资
SELECT job,MIN(sal)
FROM emp
WHERE dept_id=1
GROUP BY job;
# 58. 查询平均工资最高的部门id和平均工资
SELECT dept_id,AVG(sal)
FROM emp
GROUP BY dept_id
HAVING AVG(sal)=(SELECT AVG(sal) avg
FROM emp
GROUP BY dept_id
ORDER BY avg DESC
    LIMIT 0,1);
# 59. 查询每个部门的平均工资,要求平均工资大于2000
SELECT dept_id,AVG(sal) avg
FROM emp
GROUP BY dept_id
HAVING avg>2000;
# 60. 查询每种工作的人数,只查询人数大于1的
SELECT job,COUNT(*) num
FROM emp
GROUP BY job
HAVING num>1;
# 61. 查询每个部门的工资总和,只查询有领导的员工, 并且要求工资总和大于5400
SELECT dept_id,SUM(sal) sum
FROM emp
WHERE manager IS NOT NULL
GROUP BY dept_id
HAVING sum>5400;
# 62. 查询每个部门的平均工资, 只查询工资在1000到3000之间的,并且过滤掉平均工资低于2000的
SELECT dept_id,AVG(sal) avg
FROM emp
WHERE sal BETWEEN 1000 AND 3000
GROUP BY dept_id
HAVING avg>2000;


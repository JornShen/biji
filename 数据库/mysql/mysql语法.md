
mysql 语法


名称 一般用 `。。。` 引起来，可以避免冲突

```sql

CREATE TABLE `d_student`(
  `id` BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
  `name` VARCHAR(20) NOT NULL,
  `age` INT  NOT NULL,
  `sex` VARCHAR(2),
  `class_id` BIGINT(20),
  FOREIGN KEY (class_id) REFERENCES d_class(id) on DELETE CASCADE on UPDATE  CASCADE # 级联删除，级联更新
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

```

AUTO_INCREMENT 自动增长

ENGINE 数据引擎， 级联操作必须要Innodb

DEFAULT CHARSET 字符编码

---

#### 索引无效的情况：

1. 查询条件是LIKE ‘abc%’，MySQL将使用索引；如果查询条件是LIKE ‘%abc’，MySQL将不使用索引。

2. WHERE子句的查询条件里使用了函数(WHERE DAY(column) = …)，MySQL也将无法使用索引。

3. 对索引列运算，运算包括（+、-、\*、/、！、<>、%、like'%\_'（%放在前面）、or、in、exist等），导致索引失效。（对索引进行运算）.

4. 对于多列索引，不是使用的第一部分，则不会使用索引，不满足最左匹配的时候。


---

group by 的用法:

The GROUP BY statement is often used with aggregate(合计) functions (COUNT, MAX, MIN, SUM, AVG) to group the result-set by one or more columns.

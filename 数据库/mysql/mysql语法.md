 
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








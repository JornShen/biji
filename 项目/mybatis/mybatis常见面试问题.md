####   mybatis 常见面试问题

[](https://my.oschina.net/zudajun/blog/747682)

1. \#{}和${}的区别是什么？

${} 是 Properties 文件中的 **变量占位符**，它可以用于 **标签属性值和sql内部**，属于 **静态文本替换**，比如 ${driver} 会被静态替换为 com.mysql.jdbc.Driver。#{}是 **sql的参数占位符**，Mybatis会将sql中的#{}替换为?号，在sql执行前会使用**PreparedStatement的参数设置方法**，按序给sql的?号占位符设置参数值，比如ps.setInt(0, parameterValue)，#{item.name}的取值方式为使用反射从参数对象中获取item对象的name属性值，相当于param.getItem().getName()。

拓展：PreparedStatement 为什么可以防止sql注入?

答： 其实是因为 **SQL语句在程序运行前已经进行了预编译**，在程序运行时第一次操作数据库之前，SQL语句已经被数据库分析，编译和优化，**对应的执行计划也会缓存下来并允许数据库已参数化的形式进行查询**，当运行时动态地把参数传给PreprareStatement时，即使参数里有敏感字符如 or '1=1', 数据库也会作为一个参数一个字段的属性值来处理而**不会作为一个SQL指令**。下次执行相同的sql语句时，数据库端不会再进行预编译了，而直接用数据库的缓冲区，提高数据访问的效率。

##### mybatis

poem.xml

```xml

<dependency>
  <groupId>org.mybatis</groupId>
  <artiftactId>mybatis</artifactId>
  <version>x.x.x</version>
</dependency>

```
每个基于 MyBatis 的应用都是以一个 **SqlSessionFactory** 的实例为中心的。SqlSessionFactory 的实例可以通过 SqlSessionFactoryBuilder 获得。而 SqlSessionFactoryBuilder 则可以从 XML 配置文件或一个预先定制的 Configuration 的实例构建出 SqlSessionFactory 的实例。

既然有了 SqlSessionFactory ，顾名思义，我们就可以从中获得 SqlSession 的实例了。SqlSession 完全包含了面向数据库执行 SQL 命令所需的所有方法。你可以通过 SqlSession 实例来直接执行已映射的 SQL

```java
	SqlSession session = sqlSessionFactory.openSession();
	try {
	  BlogMapper mapper = session.getMapper(BlogMapper.class);
	  Blog blog = mapper.selectBlog(101);
	} finally {
	  session.close();
	}
```


数据库如何创建

如何映射

基本语法

xml 和 注解 的 实现方法

自动生成代码

每个线程都应该有它自己的 SqlSession 实例。SqlSession 的实例不是线程安全的，因此是不能被共享的，所以它的**最佳的作用域是请求或方法作用域**。绝对不能将 SqlSession 实例的引用放在一个**类的静态域**，甚至一个**类的实例变量**也不行。也绝不能将 SqlSession 实例的引用放在任何类型的管理作用域中，比如 Servlet 架构中的 HttpSession。如果你现在正在使用一种 Web 框架，要考虑 SqlSession 放在一个和 HTTP 请求对象相似的作用域中。换句话说，每次收到的 HTTP 请求，就可以打开一个 SqlSession，返回一个响应，就关闭它。这个关闭操作是很重要的，你应该把这个关闭操作放到 finally 块中以确保每次都能执行关闭。

映射器是创建用来绑定映射语句的接口。映射器接口的实例是从 SqlSession 中获得的。因此从技术层面讲，映射器实例的最大作用域是和 SqlSession 相同的，因为它们都是从 SqlSession 里被请求的。尽管如此，映射器实例的最佳作用域是方法作用域

#### spring 

Spring则立志于全方面的简化Java开发。对此，她主要采取了4个关键策略：

1，基于POJO的轻量级和最小侵入性编程；

2，通过依赖注入和面向接口松耦合；

3，基于切面和惯性进行声明式编程；

4，通过切面和模板减少样板式代码；

而他主要是通过：面向Bean、依赖注入以及面向切面这三种方式来达成的。


BeanFactory 支持两个对象模型。

    1，单例：模型提供了具有特定名称的对象的共享实例，可以在查询时对其进行检索。Singleton 是默认的也是最常用的对象模型。对于无状态服务对象很理想。

    2，原型：模型确保每次检索都会创建单独的对象。在每个用户都需要自己的对象时，原型模型最适合。

Aop的核心构造是方面，它将那些影响多个类的行为封装到**可重用的模块中**。

Spring Aop编写的应用程序代码是松散耦合的。

这些组件被分别整合在核心容器（Core Container）、Aop（Aspect Oriented Programming）和设备支持（Instrmentation）、数据访问及集成（Data Access/Integeration）、Web、报文发送（Messaging）、Test，6个模块集合中。


1.核心容器：由spring-beans、spring-core、spring-context和spring-expression（Spring Expression Language, SpEL） 4个模块组成。

spring-beans和spring-core模块是Spring框架的核心模块，包含了控制反转（Inversion of Control, IoC）和依赖注入（Dependency Injection, DI）。

BeanFactory 接口是Spring框架中的核心接口，它是工厂模式的具体实现。BeanFactory 使用控制反转对应用程序的配置和依赖性规范与实际的应用程序代码进行了分离。但 BeanFactory 容器实例化后并**不会自动实例化 Bean，只有当 Bean 被使用时 BeanFactory 容器才会对该 Bean 进行实例化与依赖关系的装配。**

spring-contest模块构架于核心模块之上，他扩展了BeanFactory，为她添加了Bean生命周期控制、框架事件体系以及资源加载透明化等功能。此外该模块还提供了许多企业级支持，如邮件访问、远程访问、任务调度等，**ApplicationContext**是该模块的核心接口，她是 BeanFactory 的超类，与 BeanFactory 不同，ApplicationContext 容器实例化后会**自动对所有的单实例 Bean 进行实例化与依赖关系的装配，使之处于待用状态。** 

spring-expression模块是统一表达式语言（unified EL）的扩展模块，可以查询、管理运行中的对象，同时也方便的可以调用对象方法、操作数组、集合等。它的语法类似于传统EL，但提供了额外的功能，最出色的要数函数调用和简单字符串的模板函数。这种语言的特性是基于 Spring 产品的需求而设计，他可以非常方便地同Spring IoC进行交互。

2.Aop和设备支持：由spring-aop、spring-aspects和spring-instrumentation 3个模块组成。

spring-aop是Spring的另一个核心模块，是Aop主要的实现模块。在Spring中，他是以JVM的**动态代理技术**为基础，然后设计出了一系列的Aop横切实现，比如前置通知、返回通知、异常通知(advice 增强)等，同时，Pointcut接口来匹配切入点，可以使用现有的切入点来设计横切面，也可以扩展相关方法根据需求进行切入。

spring-aspects模块集成自AspectJ框架，主要是为Spring Aop提供多种Aop实现方法。

spring-instrumentation模块是基于JAVA SE中的"java.lang.instrument"进行设计的，应该算是Aop的一个支援模块，主要作用是在JVM启用时，生成一个代理类，程序员通过代理类在运行时修改类的字节，从而改变一个类的功能，实现Aop的功能。

3.数据访问及集成：由spring-jdbc、spring-tx、spring-orm、spring-jms和spring-oxm 5个模块组成。

spring-jdbc模块是Spring 提供的JDBC抽象框架的主要实现模块，用于简化Spring JDBC。主要是提供JDBC模板方式、关系数据库对象化方式、SimpleJdbc方式、事务管理来简化JDBC编程，主要实现类是**JdbcTemplate、SimpleJdbcTemplate以及NamedParameterJdbcTemplate。**
（spring 自带数据源，还可以使用c3p0, dbcp, druid）

spring-tx模块是Spring JDBC事务控制实现模块。使用Spring框架，它对事务做了很好的封装，通过它的Aop配置，可以灵活的配置在任何一层；但是在很多的需求和应用，直接使用JDBC事务控制还是有其优势的。其实，事务是以业务逻辑为基础的；一个完整的业务应该对应业务层里的一个方法；如果业务操作失败，则整个事务回滚；所以，事务控制是绝对应该放在业务层的；但是，持久层的设计则应该遵循一个很重要的原则：保证操作的原子性，即持久层里的每个方法都应该是不可以分割的。所以，在使用Spring JDBC事务控制时，应该注意其特殊性。

spring-orm模块是ORM框架支持模块，主要集成 Hibernate, Java Persistence API (JPA) 和 Java Data Objects (JDO) 用于资源管理、数据访问对象(DAO)的实现和事务策略。

spring-jms模块（Java Messaging Service）能够发送和接受信息，自Spring Framework 4.1以后，他还提供了对spring-messaging模块的支撑。

spring-mybatis

spring-oxm模块主要提供一个抽象层以支撑OXM（OXM是Object-to-XML-Mapping的缩写，它是一个O/M-mapper，将java对象映射成XML数据，或者将XML数据映射成java对象），例如：JAXB, Castor, XMLBeans, JiBX 和 XStream。

4.Web：由spring-web、spring-webmvc、spring-websocket和spring-webmvc-portlet 4个模块组成。

spring-web模块为Spring提供了最基础Web支持，主要建立于核心容器之上，通过Servlet或者Listeners来初始化IoC容器，也包含一些与Web相关的支持。

spring-webmvc模块众所周知是一个的Web-Servlet模块，实现了Spring MVC（model-view-controller）的Web应用。

spring-webmvc-portlet模块是知名的Web-Portlets模块（Portlets在Web门户上管理和显示的可插拔的用户界面组件。Portlet产生可以聚合到门户页面中的标记语言代码的片段，如HTML，XML等），主要是为SpringMVC提供Portlets组件支持。

5.报文发送：即spring-messaging模块

主要职责是为Spring 框架集成一些基础的报文传送应用。

spring-test模块主要为测试提供支持的，毕竟在不需要发布（程序）到你的应用服务器或者连接到其他企业设施的情况下能够执行一些集成测试或者其他测试对于任何企业都是非常重要的。

Spring体系的核心是**IoC和Aop模块**。对于kernel而言，进程调度器就是其关键部位，kernel通过“进程”这个概念来抽象物理的计算资源，同时通过调度算法的设计来实现对计算资源的高效使用。而对于Spring来说，也是一样的，一方面通过IoC容器来进行POJO对象管理，以及对他们进行松耦合处理，同时也让信息资源可以用最简单的Java 语言来抽象和描述;另一方面，可以通过Aop来增强服务的功能。

基本来说，Spring体系中已经涵盖了Java EE中经常用到的许多服务，比如事务处理、Web MVC、JDBC、ORM、远程调用，这些服务的价值是不可忽视的，就像kernel如果没有实现许多驱动，那Linux对用户而言也是没有任何价值的。 

IoC 的实现包 **spring-beans 和 AOP 的实现包 spring-aop ** 是整个框架的基础，而 **spring-core**则是整个框架的核心，基础的功能都在这三个包里。

在此基础之上，spring-context 提供**上下文环境**，为各个模块提供粘合作用。在 spring-context 基础之上提供了 spring-tx 和 spring-orm包，而web部分的功能，都是要依赖spring-web来实现的。
	












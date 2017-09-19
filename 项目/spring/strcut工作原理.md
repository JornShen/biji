
什么是DAO层?

DAO: DAO层一般有接口和该接口的实现类, 接口用于规范实现类, 实现类一般用于用于操作数据库, 一般操作修改，添加，删除数据库操作的步骤很相似，就写了一个公共类DAO类 ，修改，添加，删除数据库操作时 直接调用公共类DAO类。操作持久层的一个类。

Service层业务层

javaMVC 架构中最外层是view也就是页面，control是一些控制后台和页面访问的类，model其实是dao层，但大部分人，会再增加一层service层来提供更为方便的应用


1. 客户端初始化一个指向Servlet容器（例如Tomcat）的请求
2. 这个请求经过一系列的**过滤器（Filter）** （这些过滤器中有一个叫做ActionContextCleanUp的可选过滤器，这个过滤器对于Struts2和其他框架的集成很有帮助，例如：SiteMesh Plugin）
3. 接着FilterDispatcher（现已过时）被调用，FilterDispatcher询问ActionMapper来决定这个请是否需要调用某个Action
4. 如果ActionMapper决定需要调用某个Action，FilterDispatcher把请求的处理交给ActionProxy
5. ActionProxy通过Configuration Manager询问框架的配置文件，找到需要调用的Action类
6. ActionProxy创建一个ActionInvocation的实例。
7. ActionInvocation实例使用命名模式来调用，在调用Action的过程前后，涉及到相关拦截器（Intercepter）的调用
8. 一旦Action执行完毕，ActionInvocation负责根据struts.xml中的配置找到对应的返回结果。返回结果通常是（但不总是，也可 能是另外的一个Action链）一个需要被表示的JSP或者FreeMarker的模版。在表示的过程中可以使用Struts2 框架中继承的标签。在这个过程中需要涉及到ActionMapper

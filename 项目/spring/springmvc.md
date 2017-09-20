### Spring MVC 
 
客户通过浏览器访问不同的页面，进而完成不同业务逻辑并且进行完成**数据交互**，这种软件结构就是B/S结构

如果出现了高并发加海量数据的情况，最常用的手段是使用集群加数据库读写分离的方法来解决，当然，使用反向代理和 CDN 提升网站性能也是必不可少的环节.

这样的架构其实还是可以继续进行优化的，比如使用非关系型数据库、进行业务拆分以及使用分布式服务等等。**这个世界没有哪个网站从诞生开始就是大型网站，所以一味地追求网站架构是一种得不偿失的行为。**但随着业务的增长，推动网站架构的演进还是非常有必要的，因为这样你才能随心所欲的去应对各种情况。

性能、可用性、伸缩性、扩展性以及安全性是网站架构最核心的几个要素，基本这些问题解决了，网站架构的大部分困难也就克服了。

此外，这个世界没有哪两家公司的业务情况是完全相同的，所以在架构演化的道路上**绝对不能盲从。**不要受大公司以及从大公司挖过来的技术高手的太大影响，从而偏离了原先的架构演化路线。大公司的经验和成功模式固然很重要，但盲目借鉴，可能会偏离符合实际的架构演化道路。也不能为了技术而技术，一味追求最新技术，只会让架构之路越走越难。最后有时候，问题并不是出在它的系统架构，而是业务架构出了问题，所以，**合理地调整业务需求*往往比修改架构更为有效。

在网站架构演化的过程中，追求技术是为了扩展业务边际，架构设计与业务一定是**相辅相成的。**


Model 负责数据访问，较现代的 Framework 都会建议使用独立的数据对象（DTO、POCO、POJO等等）来代替弱类型的集合对象。 
Model  数据访问的代码会使用 Data Access 的代码或是 ORM-based Framework，也可以进一步使用 Repository Pattern 与 Unit of Works Pattern 来切割数据源的相依性；
Controller 负责处理消息，较高级的 Framework 会有一个默认的实现来作为 Controller 的基础，例如 Spring 的 DispatcherServlet 或是 ASP.NET MVC 的 Controller 等，在职者分离原则上，每个 Controller 负责的部分不同，因此会将各个 Controller 切割成不同的文件以利维护；
View 负责显示数据，这个部分多为前端应用，而 Controller 会有一个机制将处理的结果 (可能是 Model, 集合或是状态等) 交给 View，然后由 View 来决定怎么显示。

其中，Model 部分是它的**核心功能**，但在使用上还是颇为讲究的。

在 MVC 设计模式中， Model 响应用户请求并返回响应数据，View 负责格式化数据并把它们呈现给用户，业务逻辑和表示层分离，**同一个 Model 可以被不同的 View 重用**，所以大大提高了代码的可重用性。

Controller 是自包含（self-contained）指高独立内聚的对象，与 Model 和 View 保持相对独立，所以可以方便的改变应用程序的数据层和业务规则。例如，把数据库从 MySQL 移植到 Oracle，或者把RDBMS 数据源改变成 LDAP 数据源，只需改变 Model 即可。

Spring 将它的 Web MVC 框架作为总体方案架构的一部分，因此它被设计为松散耦合，并且无缝地和 Spring 中间层丰富的 IoC 和 AOP 管理功能融为了一体，这也是 Spring MVC 的特点。

pring MVC 框架是一个基于请求驱动的 Web 框架。

![](../pic/m1.jpg)

![](../pic/m2.jpg)

1、首先用户发送请求——DispatcherServlet，前端控制器收到请求后自己不进行处理，而是委托给其他的解析器进行处理，作为统一访问点，进行全局的流程控制；

2、DispatcherServlet——HandlerMapping，HandlerMapping 将会把请求映射为 HandlerExecutionChain 对象（包含一个 Handler 处理器（页面控制器）对象、多个 HandlerInterceptor 拦截器）对象，通过这种策略模式，很容易添加新的映射策略；

3、DispatcherServlet——HandlerAdapter，HandlerAdapter 将会把处理器包装为适配器，从而支持多种类型的处理器，即适配器设计模式的应用，从而很容易支持很多类型的处理器；

4、HandlerAdapter——处理器功能处理方法的调用，HandlerAdapter 将会根据适配的结果调整真正的处理器的功能处理方法，完成功能处理；并返回一个 ModelAndView 对象（包含 模型数据、逻辑视图名）；

5、ModelAndView 的逻辑视图名——ViewResoler，ViewResoler 将吧逻辑视图名解析为具体的 View，通过这种策略模式，很容易更换其他视图技术；

6、View——渲染，View 会根据传进来的 Model 模型数据进行渲染，此处的 Model 实际是一个 Map 数据结构，因此很容易支持其他视图技术；

7、返回控制权给 DispatcherServlet，由 DispatcherServlet 返回响应给用户，到此一个流程结束。


![springmvc架构](../pic/m3.jpg)

### Servlet 说明

servlet 是一个 Java 编写的程序，此程序是基于开篇介绍的 **HTTP 协议**的，在服务器端运行的（如：Tomcat），是按照 servlet 规范编写的一个 Java 类。主要是**处理客户端的请求并将其结果发送到客户端**。servlet 的生命周期是由 servlet 容器来控制的，它可以分为 3 个阶段：
初始化阶段、运行阶段和销毁阶段。

jsp 本质上也是一个servlet

初始化阶段：

-> servlet 容器加载 servlet 类，把 servlet 类的 .class 文件中的数据读到内存中。

--> servlet 容器创建一个 **servletConfig** 对象。servletConfig 对象包含了 servletConfig 的**初始化配置信息。**

---> servletConfig 容器创建一个 **servlet 对象**。

----> servletConfig 容器调用 servlet 对象的 ini 方法进行初始化。

运行阶段：

当 servlet 容器接收到一个请求时，servlet 容器会针对这个请求创建 servletRequest 和 ServletResponse 对象，然后调用 service 方法。并把这两个参数传递给 servlet 方法。servlet 方法通过 servletRequest 对象获得请求的信息。并处理该请求。再通过 servletResponse 对象生成这个请求的响应结果。然后销毁 servletRequest 和 servletResponse 对象。我们不管这个请求时 post 提交的还是 get 提交的，最终这个请求都会由 servlet 方法来处理。


销毁阶段：

当 Web 应用被终止时，servlet 容器会**先**调用 servlet 对象的 destroy 方法，然后再销毁 servlet 对象，同时也会销毁与 servlet 对象相关联的 servletConfig 对象。我们可以在 destroy 方法的实现中，释放 servlet 所占用的资源，如关闭数据库连接，关闭文件输入输出流等。

servlet 被设计成请求驱动，servlet 的请求可能包含多个数据项，当 Web 容器接收到某个 servlet 请求时，servlet 把请求封装成一个 HttpServletRequest 对象，然后把对象传给 servlet 的对应服务方法。

HTTP 的请求方式包括 delete、get、options、post、put 和 trace，在 HttpServlet 类中分别提供了相应的服务方法，它们是 **doDelete（），doGet（）、doOptions（）、doPost（）、doPut（）和 doTrace（）**。

![](../pic/m4.jpg)


##### DispatcherServlet 的初始化

Spring MVC 是基于 Servlet 功能实现的，通过实现 Servlet 接口的 **DispatcherServlet** 来封装其核心功能实现，通过将**请求分派给处理程序*，同时带有可配置的处理程序映射、视图解析、本地语言、主题解析以及上载文件支持。

servlet 初始化阶段会调用其 init 方法，所以我们首先要查看在 DispatcherServlet 中是否重写了 init 方法。我们在其父类 HttpServletBean 中找到了该方法。

DipatcherServlet 的初始化过程主要是通过将当前的 servlet 类型实例转换为 BeanWrapper 类型实例，以便使用 Spring 中提供的注入功能进行对应属性的注入。这些属性如 contextAttribute、contextClass、nameSpace、contextConfigLocation 等，都可以在 web.xml 文件中以初始化参数的方式配置在 servlet 的声明中。DispatcherServlet 继承自 FrameworkServlet，FrameworkServlet 类上包含对应的同名属性，Spring 会保证这些参数被注入到对应的值中。

####### 属性注入

1.封装及验证初始化参数   ServletConfigPropertyValues 除了封装属性外还有对属性验证的功能。

封装属性主要是对初始化的参数进行封装，也就是 servlet 中配置的 <init-param> 中配置的封装。当然，用户可以通过对 requiredProperties 参数的初始化来强制验证某些属性的必然性，这样，在属性封装的过程中，一旦检测到 requiredProperties 中的属性没有指定初始值，就会抛出异常。

2. 将当前 servlet 实例转化成 BeanWrapper 实例

PropertyAccessorFactory.forBeanPropertyAccess 是Spring 中提供的工具方法，主要用于将指定实例转化为 Spring 中可以处理的 BeanWrapper 类型的实例。

3.注册相对于 Resource 的属性编辑器

属性编辑器，我们在上文中已经介绍并且分析过其原理，这里使用属性编辑器的目的是在对当前实例（DispatcherServlet）属性注入过程中一旦遇到 Resource 类型的属性就会使用 ResourceEditor 去解析。

4.属性注入

BeanWrapper 为 Spring 中的方法，支持 Spring 的自动注入。其实我们最常用的属性注入无非是 contextAttribute、contextClass、nameSpace、contextConfigLocation 等属性。

5.servletBean 的初始化

在 ContextLoaderListener 加载的时候已经创建了 WebApplicationContext 实例，而在这个函数中最重要的就是对这个实例进行进一步的补充初始化。

	
在 Web 中包含 SpringWeb 的核心逻辑的 DispatcherServlet 只可以被声明为一次，在 Spring 中已经存在验证，所以这就确保了如果 this.webApplicationContext != null，则可以直接判定 this.webApplicationContext 已经通过构造函数初始化。

onRefresh 是 FrameworkServlet 类中提供的模板方法，在其子类 DispatcherServlet 中进行了重写，主要用于刷新 Spring 在 Web 功能实现中所必须使用的组件。下面我们会介绍它们的初始化过程以及使用场景，而至于具体的使用细节会在稍后的章节中再做详细介绍。

### HandlerMapping 的初始化 

以 HandlerMapping 为例来说明这个 initHandlerMappings() 过程。这里的 Mapping 关系的作用是，**为 HTTP 请求找到相应的 Controller 控制器**，从而利用这些控制器 Controller 去完成设计好的数据处理工作。**HandlerMappings** 完成对 MVC 中 **Controller** 的定义和配置，只不过在 Web 这个特定的应用环境中，这些控制器是与具体的 HTTP 请求相对应的。

DispatcherServlet 中 HandlerMappings 初始化过程的具体实现。

在 HandlerMapping 初始化的过程中，把在 Bean 配置文件中配置好的 handlerMapping 从 Ioc 容器中取得。

handlerMapping 变量就已经获取了在 BeanDefinition 中配置好的映射关系。其他的初始化过程和 handlerMapping 比较类似，都是自己从 Ioc 容器中读入配置，所以这里的 **MVC 初始化过程是建立在 Ioc 容器已经初始化完成的基础上的。**


#### MVC 处理 HTTP 分发请求

在初始化完成时，在上下文环境中已定义的所有 HandlerMapping 都已经被加载了，这些加载的 handlerMapping 被放在一个 List 中并被排序，存储着 HTTP 请求对应的映射数据。这个 List 中的每一个元素都对应着一个具体 handlerMapping 的配置，一般每一个 handlerMapping 可以**持有一系列从 URL 请求到 Controller 的映射**，而Spring MVC 提供了一系列的 HandlerMapping 实现。


通过这些在 HandlerMapping 中定义的映射关系，即这些 URL 请求和控制器的对应关系，使 Spring MVC 应用可以根据 HTTP 请求确定一个对应的 **Controller**。具体来说，这些映射关系是通过接口类 HandlerMapping 来封装的，在 HandlerMapping 接口中定义了一个 getHandler 方法，通过这个方法，可以获得与 HTTP 请求对应的 HandlerExecutionChain，而这个 HandlerExecutionChain，封装了具体的 Controller 对象：


HandlerExecutionChain 看起来比较简洁，它持有一个 Interceptor 链和一个 handler 对象，这个 handler 对象实际上就是 HTTP 请求对应的 Controller，在持有这个 handler 对象的同时，还在 HandlerExecutionChain 中设置了一个 **拦截器链**，通过这个拦截器链中的拦截器，可以为 **handler 对象提供功能的增强。**

要完成这些工作，需要对拦截器链和 handler 都进行配置，这些配置都是在 HandlerExecutionChain 的初始化函数中完成的。为了维护这个拦截器链和 handler，handlerExecutionChain 还提供了一系列与拦截器链维护相关的一些操作，比如可以为拦截器链增加拦截器的 addInterceptor 方法等。

####  HandlerMapping 完成请求和映射处理

在各种准备工作完成后，就是使用 HandlerMapping 来完成请求的映射处理了，而具体执行过程是在 AbstactHandlerMapping 中的 ** getHandler **来完成的。

在取得 handler 的具体过程在 getHandlerInternal 方法中实现。这个方法接受 HTTP 请求作为参数，它的实现在 AbstractUrlHandlerMapping 中，这个实现过程包括从 HTTP 请求中得到 URL ，并根据 URL 到 urlMapping 中获取 handler。

#####  Spring MVC 对 HTTP 请求的分发处理

封装参数

DispatcherServlet，毫无疑问，它是 Spring MVC 中非常重要的一个类，他不仅仅建立了自己的持有 Ioc 容器，还肩负请求分布处理的任务。而 HTTP 的请求发布处理是在 **doService** 方法中完成的。

对于请求的实际处理是由 ** doDispatch()** 来完成的，这个方法很长，但过程简单明了。这个 doDispatch 方法是 DispatcherServlet 完成 ** Dispatcher ** 的主要方法，包括** 准备 ModelAndView **，调用 getHandler 来响应 HTTP 请求，然后通过执行 Handler 的处理来得到返回的 ModelAndView 结果，最后把这个 ModelAndView 对象交给相应的视图对象去呈现。这里是 MVC 模式的核心地区： 

![](../pic/m5.jpg)

得到 handler 对象，然后调用 handler 对象中的 HTTP 进行动作的响应。而具体的业务逻辑则被封装在 handler 中，由这些逻辑对 HTTP 请求进行相应的处理，从而生成所需要的数据，并把这些数据封装到 ModelAndView 对象中。最后，通过 handler 的 handlerRequest 方法触发完成，然后交给 MVC 的 View 部分处理。

其中的 M 大致对应 ModelAndView 的生成，而 C 大致对应 DispatcherServlet 和与用户业务逻辑相关的 handler 实现。在 Spring MVC 框架中，DispatcherServlet 起到了非常核心的作用，是整个 MVC 框架的调度枢纽。对应视图呈现功能，它的调用入口同样在 DispatcherServlet 中的 doDispatch 方法中实现。

它的调用入口是 DispatcherServlet 中的 render 方法。

Spring MVC 对 常用的视图提供的支持。从这个体系中我们可以看出，Spring MVC 对常用视图的支持，比如 JSP/JSTL 视图、FreeMaker 视图等等。View 的设计其实是非常简单的，只需要实现 Render 接口。


使用 JSP 的页面作为 Web UI，是使用 Java 设计 Web 应用比较常见的选择之一，如果在 JSP 中使用 Jstl（JSP Standard Tag Library）来丰富 JSP 的功能，在 Spring MVC 中就需要使用 JstlView 来作为 View 对象，从而对数据进行视图呈现。而 JstlView 没有实现 render 的方法，而使用的 render 方法是它的基类 AbstractView 中实现的。

对于整个 Spring MVC 框架的运行过程，首先，在 Web 环境中建立 Spring Ioc 容器的 Web 容器中的配置和初始化，当然，因为 Web 容器的特殊性，所以在配置方面，需要对 Web 环境相对应的一些特殊处理，比如 Servlet 和 ServletContext 的使用等。

Spring MVC 的整体实现也比较好理解，而其本质其实是**对 Servlet 的封装**，而整个 Spring MVC 的运行是以 DispatcherServlet 为中心进行控制的。 


1. 需要**建立 Controller 控制器和 HTTP 请求之间的映射关系**，即根据请求得到对应的 Controller。而这个工作机制是由 handlerMapping 中封装的 HandlerExecutionChain 来完成的，而对 Controller 控制器和 HTTP 请求的映射关系是在 Bean 中定义的，并在 Ioc 容器的初始化中，载入 handlerMap 中使用。

2. Controller 对象和 HTTP 请求之间的映射关系建立好了以后，MVC 框架中，DispatcherServlet 会根据具体的 URL 请求信息，在 HandlerMapping 中进行查询，从而得到对应的 HandlerExecutionChain，然后根据动作的响应生成需要的 ModelAndView。

3. 得到这个 ModelAndView 以后，DispatcherServlet 会把获得的模型数据交给特定的视图对象，从而完成视图的呈现，而这个具体过程是由 render 方法来完成的。





















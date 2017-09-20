
### AOP 原理

因为AOP可以说是对OOP的补充和完善，而这一切的理念都是从模块化开始的。

衡量OOP成功与否的标准就是它在多大程度上避免了代码的重复。

OOP很适合你定义**从上到下的关系**，但不适合定义水平的关系

AOP的思路来看，系统是被分解成方面（Aspect）或者关注点（Concern），而不是一个个对象。追根溯源，与OOP一样，AOP只不过是一种全新的模块化机制而已，他的主要作用是用来**描述分散在对象、类或函数中的横切关注点**，从关注点中分离出横切关注点则是 AOP的核心概念。

通过分离关注点让解决特定领域问题的代码从业务逻辑中独立出来，业务逻辑的代码中就不再含有针对特定领域问题代码的调用，业务逻辑同特定领域问题的关系则通过切面来封装、维护，这样原本分散在整个应用程序中的代码就可以很好的进行管理了


在编译期修改源代码、运行期字节码加载前修改字节码或字节码加载后动态创建代理类的字节码，这是AOP的具体实现方法，而他是由三个重要步骤来完成的，先是生成代理对象，然后是拦截器的作用，最后是编织的具体实现。

如果以实现方法来分的话，则主要有两类：静态AOP（包括静态织入）和动态AOP（包含动态代理、动态字节码生成、自定义类加载器、字节码转换器）。

![](../pic/a1.jpg)

语言和开发环境, “基础”可以视为待增强对象或者说目标对象；“切面”通常包含对于基础的增强应用；“配置”可以看成是一种编织，通过在AOP体系中提供这个配置环境，可以把基础和切面结合起来，从而完成切面对目标对象的编织实现。

第二层次为语言和开发环境提供支持的，在这个层次中可以看到AOP框架的高层实现，主要包括配置和编织实现两部分内容。例如配置逻辑和编织逻辑实现本身，以及对这些实现进行抽象的一些高层API封装。这些实现和API封装，为前面提到的语言和开发环境的实现提供了有力的支持。

最底层是编织的具体实现模块，各种技术都可以作为编织逻辑的具体实现方法，比如反射、程序预处理、拦截器框架、类装载器框架、元数据处理等等。

应用场景： 

AOP在 权限（Authentication）、缓存（Cache）、内容传递（Context passing）、错误处理（Error handling）、懒加载（Lazy loading）、调试（Debug）、日志（Log）、跟踪优化和校准（tracing、profiling and monitoring）、性能优化（Performance optimization）、持久化（Persistence）、资源池（Resource pooling）、同步（Synchronization）、事务（Transactions）等方面都有用处，可以说是可使用范围及广。下面我们就以一张思维导图来串联，以此来铺开与AOP相关的所有的知识点

![](../pic/a2.jpg)

![](../pic/a3.jpg)

相关术语

目标对象（Target）：包含连接点的对象。也被用来引用增强化或代理化对象。

代理（Proxy）：AOP 框架创建的对象，包含增强。

连接点（Joinpoint）：程序执行过程中明确的点，如**方法的调用或特定的异常被抛出**。 (核心)

切点（Pointcut）：指定一个**通知将被引发**的一系列连接点。AOP框架必须允许开发者指定切入点（代理对象的执行方法）：例如，使用正则表达式。引发连接点, 描述连接点

增强（Advice）：在特定的**连接点**AOP框架执行的动作。各种类型的增强包括“around”、“before”、“throws”增强等等。增强类型将在下面讨论。许多 AOP 框架都是以拦截器做增强模型，维护一个“围绕”连接点的拦截器链。

切面（Advisor）：一个关注点的模块化，这个关注点实现可能另外横切多个对象。事物管理是J2EE应用中横切关注点中一个很好的例子。切面一般是用 Advisor 或者 拦截器实现。

织入（Weaving）：组装方面创建通知化对象。这可以在编译时完成（例如：使用AspectJ编译器），也可以在运行时完成。Spring 和其他一些纯 Java AOP 框架，使用运行时织入。

引入（Introduction）：添加方法或字段到增强化类。类 级别， 或叫做引介增强

AOP增强类型（也叫 通知类型）包括：

1、Before Advice（前置增强）：在一个连接点**之前**执行的增强，但这个增强**不能阻止**流程继续执行到连接点（除非它抛出一个异常）。

2、After Advice（后置增强，全称是 After returning advice 正常返回增强 ）：在连接点正常**完成后**执行的增强，例如，如果一个正常返回，没有抛出异常。**如果抛出异常则不会执行。**

3、Around Advice（环绕增强）：包围一个连接点的增强，如方法调用，是**最强大的增强**。在方法调用前后完成自定义的行为。**它们负责选择继续执行连接点或直接返回它们自己的返回值或抛出异常来执行。**

4、Throws Advice（抛出增强，全称是 After throwing advice 异常返回增强，也叫 Finally returning advice 最终返回增强）：是最常用的增强类型。大部分是基于拦截器框架如Nanning或者JBoss4提供的Around增强。作用是，不管，是否正常执行，都会返回增强中的内容。

5、Introduction Advice（引入增强）：一种非常特殊的增强。它将新的成员变量、成员方法引入到目标类中。它仅能作用于类层次，而不是方法层次，所以他不能作用于任何切入点。

Java社区中是最完整AOP框架还是Aspectj

Spring AOP的核心技术是JDK的动态代理技术。Spring AOP是以动态代理技术为基础，设计出了一系列AOP的横切实现，比如：前置增强、返回增强、异常增强等等。

1，配置 ProxyFactoryBean 显示设置 advisors、advice、target 等。
2，配置 AutoProxyCreator 这种方式下，还是如以前一样定义 bean，但是从容器中获得的其实是代理对象。
3，配置 <aop:config>。
4，配置 <aop:aspectj-autoproxy>，使用 AspectJ 的**注解**来表示之前以及切入点。
而编程方式是直接通过 ProxyFactory 设置 target 对象，再通过 getProxy 方法来获取代理对象。 


为了让AOP起作用，需要参照前面的 AOP 实现原理来完成一系列过程，需要为目标对象建立代理对象，这个代理对象可以通过使用JDK的Proxy来完成，也可以通过第三方的类生成器CGLIB来完成。然后，还需要启动代理对象的拦截器来完成各种横切面的织入，这一系列的织入设计时通过一系列 Adapter 来实现的。通过一系列 Adapter 的设计，就可以把AOP的横切面设计和Proxy模式有机地结合起来，从而实现在 AOP 中定义各种织入方式

#### 实现思路

上文中提到了 AOP 创建代理等等的具体操作都是在 AnnotationAwareAspectAutoProxyCreator 类中来成的

AnnotationAwareAspectAutoProxyCreator 实现代理也是通过 BeanPostProcessor 接口来完成的。
AnnotationAwareAspectAutoProxyCreator 的 postProcessAfterInitialization 具体实现是在其父类 AbstractAutoProxyCreator 中完成的。

而创建代理的核心逻辑部分是在 AbstractAutoProxyCreator 类中完成的，而创建代理前的准备工作主要分为两步：（1）获取增强方法或者增强器。（2）根据获取的增强进行代理。

Advice（也翻作 通知）定义了连接点做什么，为切面增强提供了织入的接口。在 Spring AOP 中，它主要描述 Spring AOP 围绕方法调用而注入的**切面行为**。

![](../pic/a4.jpg)

在 Spring AOP 的实现中，使用了这个统一接口，并通过这个接口，为 AOP 切面增强的注入功能做了更多的细化和扩展，比如前面提到的具体通知类型，如BeforeAdvice、AfterAdvice、ThrowsAdvice等。作为 Spring AOP 定义的接口类，具体的切面增强可以通过这些接口集成到 AOP 框架中去发挥作用。


Pointcut（关注点，也称 切点）用于决定 Advice 增强作用于哪个连接点，也就是说通过 Pointcut 来定义需要增强的方法集合，而这些集合的选取可以通过一定的规则来完成。

通过 Pointcut 接口的基本定义我们可以看到，需要返回一个 MethodMatcher。对于 Point 的匹配判断功能，具体是由这个返回的 MethodMatcher 来完成的，也就是说，有这个 MethodMatcher 来判断是否需要对当前方法调用进行增强或者配置应用。

Advisor（通知器）用一个对象将对目标方法的切面增强设计（Advice）和关注点的设计（Pointcut）结合起来。

通过 Advisor，可以定义应该使用哪个增强并且在哪个关注点使用它，也就是通过 Advisor，把 Advice 和 Pointcut 结合起来，这个结合为使用 IOC 容器配置 AOP应用，提供了便利。

对于指定 bean 的增强方法的获取，一般包含获取所有增强以及寻找所有增强中适合于 bean 的增强并应用这两个步骤。

### Spring AOP 架构

** 先是生成代理对象，然后是拦截器的作用，最后是编织的具体实现。这是AOP实现的三个步骤 **


当我们建立增强实例时，我们必须先使用 ProxyFactory 类加入我们需要织入该类的所有增强，然后为该类创建代理。一般而言，AOP实现代理的方法有三种，而 Spring 内部用到了其中的两种方法：动态代理和静态代理，而动态代理又分为JDK动态代理和CGLIB代理。

而前面提到的 ProxyFactory 类控制着 Spring AOP 中的织入和创建代理的过程。在真正创建代理之前，我们必须指定被增强对象或者目标对象。我们是通过 setTarget() 方法来完成这个步骤的。而 ProxyFactory 内部将生成代理的过程全部转给 DefaultAopProxyFactory 对象来完成，然后根据设置转给其他的类来完成。

![](../pic/a5.jpg)

在 Spring AOP 中，一个 Advisor（通知者，也翻作 通知器或者增强器）就是一个切面，他的主要作用是整合切面增强设计（Advice）和切入点设计（Pointcut）
Advisor有两个子接口：IntroductionAdvisor 和 PointcutAdvisor，基本所有切入点控制的 Advisor 都是由 PointcutAdvisor 实现的。

只要配置文件中出现“aspectj-autoproxy”的注解时就会使用解析器对 AspectJAutoProxyBeanDefinitionParser 进行解析。

所有解析器，都是由 BeanDefinitionParser 接口的统一实现，入口都是从 parse函数开始的。

Spring AOP 内部使用 JDK 动态代理或者 CGLIB 来为目标对象创建代理。
如果被代理的目标对象实现了**至少一个接口**，则会使用 JDK 动态代理。所有该目标类型实现的接口都将被代理。
若该目标对象**没有实现任何接口**，则创建一个 CGLIB 代理。
一般情况下，使用 CGLIB 需要考虑增强（advise）Final 方法不能被复写以及需要指定 CGLIB 包的位置，尽管 CGLIB 效率更高，但还是推荐使用 JDK 动态代理。

AOP 配置中的 prioxy-target-class 和 expose-proxy 属性在代理生成中起到了至关重要的。prioxy-target-class 主要负责上面两种代理的实现，而 expose-proxy 则负责自我调用切面中的增强。

### 获取指定增强

获取指定增强方法的两个步骤：（1）获取所有的增强，（2）寻找所有增强中适用于 bean 的增强并应用
这两个步骤是由 findCandidateAdvisors 和 findAdvisorsThatCanApply 来完成的。

获取增强

AnnotationAwareAspectAutoProxyCreator 
类中的aspectJAdvisorsBuilder.buildAspectJAdvisors()来实现的。

首先，获取所有在 beanFacotry 中注册的 Bean 都会被提取出来。然后遍历所有 beanName，并找出声明 AspectJ 注释的类，进一步处理。最后，将将结果加入缓存

获取通知器

是由 AspectJAdviosrFactory 接口的 getAdvisors 方法来完成的。

根据切点信息生成增强。所有的增强都由 Advisor 的实现类 InstantiationModelAwarePointcutAdvisorImpl 统一封装的。
 
对于所有增强来讲，并不一定都适用于当前的 Bean，还要挑选出适合的增强，也就是满足我们配置的通配符的增强器。具体实现在 findAdvisorsThatCanApply 中，而且 findAdvisorsThatCanApply 也比上面来的简单的多。

findAdvisorsThatCanApply 函数的主要功能是寻找所有增强器中适合于当前 class 的增强器。引介增强与普通的增强是处理不一样的，所以分开处理。


#####  引介增强

引介增强是一种比较特殊的增强类型，它不是在目标方法周围织入增强，
而是为目标类**创建新的方法和属性**，所以引介增强的连接点是**类级别的**，而非方法级别的

作用：引介增强为目标类添加新的属性和方法,这些新属性或方法是可以根据我们业务逻辑需求而动态变化的

AOP是水平扩展,  把扩展(增强) 织入目标对象。

[](http://blog.csdn.net/qwe6112071/article/details/50962613)

#### 创建代理

代理的定义其实非常简单，就是改变原来目标对象方法调用的运行轨迹。
这种改变，首先会对这些方法进行拦截，从而为这些方法提供工作空间，
随后在进行回调，从而完成 AOP 切面实现的一整个逻辑。

**创建代理**是 Spring AOP 功能实现最核心的地方，一般而言 Spring AOP 动态生成代理有两种方法：JDK 和 CGLIB。

JDK 动态代理：JdkDynamicAopProxy implements AopProxy, InvocationHandler

```java
public Object getProxy(ClassLoader classLoader) {
        if(logger.isDebugEnabled()) {
            logger.debug("Creating JDK dynamic proxy: target source is " + this.advised.getTargetSource());
        }

        Class[] proxiedInterfaces = AopProxyUtils.completeProxiedInterfaces(this.advised, true);
        this.findDefinedEqualsAndHashCodeMethods(proxiedInterfaces);
        return Proxy.newProxyInstance(classLoader, proxiedInterfaces, this);
}
```

[动态代理的实现](http://www.cnblogs.com/flyoung2008/archive/2013/08/11/3251148.html)


封装 Advisor 并加入到 ProxyFactory 中以及创建代理是最为繁琐的两个过程，可以通过 ProxyFactory 提供 addAdvisor 方法直接将通知器置如代理创建工厂中，但是将拦截器封装为通知器还是需要一定逻辑的。

AOP 内动态生成代理有两种方法：JDK 和 CGLIB。

一般情况下，如果目标对象实现了接口，默认情况下会采用 JDK 动态代理实现 AOP。
如果目标对象实现了接口，可以强制使用 CGLIB 实现 AOP。
如果目标对象没有实现接口，必须采用 CGLIB，Spring 会自动在 JDK动态代理和 CGLIB 之间转化。
而且，JDK 动态代理只能对实现了**接口的类**生成代理，而不能针对类。CGLIB是**针对类**实现代理的，但主要是**对指定的类生成一个子类，覆盖其中的方法，是继承实现**，所以该类或方法最好不要声明成 final。

AopProxy 才是生成代理的主要位置。而前面看到的 ProxyFactory 在 AopProxy 代理对象和IOC容器配置之间仅仅起一个桥梁作用。

### JDK 代理

在对于 JDK 代理的使用中，JDK 动态代理的实现类 JdkDynamicAopProxy，而 JdkDynamicAopProxy 类最为核心的是 InvocationHandler 接口。而在 JdkDynamicAopProxy 类的方法里较为重要的有三个：**构造函数、invoke 方法和 getProxy 方法**。

invoke 方法是其核心逻辑实现的地方。其主要的工作就是**创建一个拦截器链**，然后使用 ReflectiveMethodInvocation 类对链进行封装，最后通过 **proceed** 方法对拦截器进行逐个调用，而 proceed 方法负责实现方法前调用以及后置调用的逻辑处理，然后将工作委托给各个增强器，在增强器内部实现具体逻辑。

CGLIB 是一个强大的高性能的代码生成包。Spring AOP 中完成 CGLIB 代理是托付给 CglibAopProxy 类来实现的，而也动态代理相似 getProxy 方法是这个类的主要入口。

源码分析总结：

首先通过配置 ProxyFactory 以及拦截器，然后以便 AopProxy 顺利的产生代理对象；配置的方式是由 ProxyFactoryBean 类来完成的，编程的方式是通过 ProxyFactory 来实现的。这两种 AOP 使用方式只是表面方式不一样，内在实现都是一样的。这里值得一提的是为了让 AopProxy 更好的工作，Spring 围绕他设计了许多接口和类，比如：专门生产工厂的 AopFroxyFactory  接口，专门用于生产对象的 DefaultAopFroxyFactory

最终 AopProxy 代理对象的产生，会交给 JdkDynamicAopProxy 和 CglibAopProxy 这两个工厂来完成。

在完成 AopProxy 代理对象后，我们就可以对 AOP 切面逻辑进行实现了，首先会对这些方法进行拦截，从而为这些方法提供工作空间，随后在进行回调，从而完成 AOP 切面实现的一整个逻辑。而这里的拦截 JdkDynamicAopProxy  主要是通过内部的 invoke 方法来实现，而 CGLIB 是通过 getCallbacks 方法来完成的。他们为 AOP 切面的实现提供了舞台。

最后，通过 AopProxy 接口中 getProxy 方法进行回调，然后在根据 Advice 增强来完成具体逻辑。




















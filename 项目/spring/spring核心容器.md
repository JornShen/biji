#### spring 核心容器

IoC也被称作依赖注入(DI)。它是一个**__处理对象依赖项__**的过程，也就是将他们一起工作的其他的对象，只有通过构造参数、工厂方法参数或者（属性注入）通过构造参数实例化或通过工厂方法返回对象后再设置属性。当创建bean后，IoC容器再将这些**__依赖项__**注入进去。这个过程基本上是反转的，因此得名控制反转（IoC）。 (**解决对象创建和依赖关系**)

IoC容器利用Java的POJO类和配置元数据来生成 完全配置和可执行 的系统或应用程序。而Bean在Spring中就是POJO，也可以认为Bean就是对象。

接口分析：

![](pic/a6.jpg)

1. 从接口BeanFactory到HierarchicalBeanFactory，再到ConfigurableBeanFactory,这是一条主要的BeanFactory设计路径。在这条接口设计路径中，**BeanFactory**，是一条主要的BeanFactory设计路径。在这条接口设计路径中，BeanFactory接口定义了基本的Ioc容器的规范。在这个接口定义中，包括了getBean()这样的Ioc容器的基本方法（通过这个方法可以从容器中取得Bean）。而HierarchicalBeanFactory接口在继承了BeanFactory的基本接口后，**增加了getParentBeanFactory()的接口功能**，使BeanFactory具备了双亲Ioc容器的管理功能。在接下来的ConfigurableBeanFactory接口中，主要定义了一些对BeanFactory的配置功能，比如通过setParentBeanFactory()设置双亲Ioc容器，通过addBeanPostProcessor()配置Bean后置处理器，等等。通过这些接口设计的叠加，定义了BeanFactory就是最简单的Ioc容器的基本功能

2. 第二条接口设计主线是，以 **ApplicationContext作为核心** 的接口设计，这里涉及的主要接口设计有，从BeanFactory到ListableBeanFactory，再到ApplicationContext，再到我们常用的WebApplicationContext或者ConfigurableApplicationContext接口。我们常用的应用基本都是org.framework.context 包里的WebApplicationContext或者ConfigurableApplicationContext实现。在这个接口体现中，ListableBeanFactory和HierarchicalBeanFactory两个接口，连接BeanFactory接口定义和ApplicationContext应用的接口定义。在ListableBeanFactory接口中，**细化了许多BeanFactory的接口功能**，比如定义了getBeanDefinitionNames()接口方法；对于ApplicationContext接口，它通过继承MessageSource、ResourceLoader、ApplicationEventPublisher接口，在BeanFactory简单Ioc容器的基础上添加了许多对高级容器的特性支持

##### BeanFactory 和 FactoryBean 的区别

[参考博客](http://chenzehe.iteye.com/blog/1481476)

FactoryBean和BeanFactory 是在Spring中使用最为频繁的类，它们在拼写上很相似。BeanFactory 是Factory，也就是Ioc **容器或对象工厂**， 规定了所遵守的最底层和最基本的编程规范，是核心接口；FactoryBean是一个Bean。在Spring中，所有的Bean都是由BeanFactory（也就是Ioc容器）来进行管理的。但对FactoryBean而言，这个Bean不是简单的Bean，而是一个**能产生或者修饰对象生成的工厂Bean**，它的实现与设计模式中的**工厂模式和修饰器模式**类似， 用户可以通过实现该接口 **定制实例化 Bean 的逻辑**(目的是为了简化bean的实例化)。当配置文件中<bean> 的 class 属性配置的实现类是 FactoryBean 时，通过 getBean() 方法返回的不是 FactoryBean 本身，而是 FactoryBean#getObject() 方法所返回的对象，相当于 FactoryBean#getObject() 代理了 getBean() 方法。

当调用 BeanFactory#getBean("car") 时， Spring 通过反射机制发现 CarFactoryBean 实现了 FactoryBean 的接口，这时 Spring 容器就调用接口方法 CarFactoryBean#getObject() 方法返回。如果希望获取 CarFactoryBean 的实例，则需要在使用 getBean(beanName) 方法时在 beanName 前显示的加上 "&" 前缀：如 getBean("&car");(& 会获取到 FactoryBean 的对象)

类的关系图：

空心三角加实线代表继承、
空心三角加虚线代表实现、
实线箭头加虚线代表依赖、
实心菱形加实线代表组合。
这里用下划线代表接口，没有下划线的代表类。

#### bean 的加载过程

Resource(资源的抽象) -> Bean的定义 -> BeanDefinition

在Java中，将**不同来源的资源抽象成URL**，通过注册不同的handler（URLStreamHandler）来处理不同来源间的资源读取逻辑。而URL中却没有提供一些基本方法来实现自己的抽象结构。因而**Spring对其内部资源，使用了自己的抽象结构：Resource接口来封装**。而ClassPathResource实现类即是对Resource的实现。

#### Resource接口体系

![](pic/a7.jpg)

Resource接口抽象了所有Spring内部使用到的底层资源：File、URL、Classpath等。同时，对于来源不同的资源文件，Resource也有不同实现：文件(FileSystemResource)、Classpath资源(ClassPathResource)、URL资源(UrlResource)、InputStream资源(InputStreamResource)、Byte数组(ByteArrayResource)等等。

资源的原始接口为Resource，它继承自InputStreamResource，实现了其getInputStream方法，这样所有的资源就是通过该方法来获取输入流的。对于资源的加载，也实现了统一，定义了一个资源加载顶级接口ResourceLoader，它默认的加载就是DefaultResourceLoader。

Resource接口抽象了所有Spring内部使用到的底层资源：File、URL、Classpath等。同时，对于来源不同的资源文件，Resource也有不同实现：文件(FileSystemResource)、Classpath资源(ClassPathResource)、URL资源(URLResource)、InputStream资源(InputStreamResource)、Byte数组(ByteArrayResource)等等。

DefaultListableBeanFactory 所起到的是忽略给定接口自动装配功能。简单来说，一般 bean 中的功能 A 如果没有初始化，那么 Spring会自动初始化A，这是Spring的一个特性。但当某些特殊情况时，B不会初始化，比如：B已经实现了 BeanNameAware接口。可以说，就是通过其他方式来解析依赖，类似于 BeanFactory 的 BeanFactoryAware。

##### BeanDefinition 的定义

BeanDefinition载入过程其实就是把定义的BeanDefinition在IoC容器中转化为一个Spring内部表示的数据结构的过程。IoC容器对Bean的管理和依赖注入的实现，都是通过对其持有的BeanDefinition进行各种相关的操作来完成的。这些BeanDefinition数据在IoC容器中通过一个**concurrentHashMap**来维护。

bean在ioc内部的表示是 BeanDefinition. 载入方法：loadBeanDefinitions.


Spring首先获取Document的根元素（一般为<beans/>），然后取得根元素所有的子节点并循环解析这些子节点；如果子节点在Spring默认的命名空间内，则按照Spring Bean定义规则来解析，否则按照自定义的节点解析。在按照Spring Bean定义规则进行解析的parseDefaultElement方法中，完成了对<import/>、<alias/>、<bean/>、<beans/>等元素的解析。

<bean>元素解析已经完了，而<bean>元素属性及其子元素的解析顺序为：1，解析<bean>元素的属性。2，解析<description>子元素。3，解析<meta>子元素。4，解析<lookup-method/>子元素。5，解析<replaced-method>子元素。6，解析<constructor-arg>子元素。7，解析<property>子元素。8，解析<qualifier>子元素。解析过程中像<meta>、<qualifier>等子元素都很少使用，而下面就直接解析最常用的子元素<property>子元素。


bean的解析:
BeanDefinitionParserDelegate.java
DefaultBeanDefinitionDocumentReader.java

##### BeanDefinition的注册

BeanDefinition信息已经在IoC容器内部建立起了自己的数据结构，但这些数据还不能供IoC容器直接使用，需要在IoC容器中对这些BeanDefinition数据进行注册。不同的解析元素解析方式都不同但最后的注册的方式是一样的

重点：BeanDefinitionReaderUtils.registerBeanDefinition 方法

DefaultListableBeanFactory.registerBeanDefinition

BeanDefinition的注册:
this.beanDefinitionMap.put(beanName, beanDefinition);


#####  ApplicationContext

ApplicationContext是spring中较高级的容器。和BeanFactory类似，它可以加载配置文件中定义的bean，将所有的bean集中在一起，当有请求的时候分配bean。 另外，它增加了企业所需要的功能，比如，**从属性文件从解析文本信息和将事件传递给所指定的监听器**。这个容器在org.springframework.

context.ApplicationContext接口中定义。**ApplicationContext包含BeanFactory所有的功能，一般情况下，相对于BeanFactory，ApplicationContext会被推荐使用**。但BeanFactory仍然可以在轻量级应用中使用，比如移动设备或者基于applet的应用程序。

####### ApplicationContext接口关系 (ApplicationContext的扩展)

1. **支持不同的信息源**。扩展了MessageSource接口，这个接口为ApplicationContext提供了很多信息源的扩展功能，比如：国际化的实现为多语言版本的应用提供服务。

2. **访问资源**。这一特性主要体现在ResourcePatternResolver接口上，对Resource和ResourceLoader的支持，这样我们可以从不同地方得到Bean定义资源。

这种抽象使用户程序可以灵活地定义Bean定义信息，尤其是从不同的IO途径得到Bean定义信息。这在接口上看不出来，不过一般来说，具体ApplicationContext都是

继承了DefaultResourceLoader的子类。因为DefaultResourceLoader是AbstractApplicationContext的基类，关于Resource后面会有更详细的介绍。

3. **支持应用事件**。继承了接口ApplicationEventPublisher，为应用环境引入了事件机制，这些事件和Bean的生命周期的结合为Bean的管理提供了便利。

4. **附件服务**。EnvironmentCapable里的服务让基本的Ioc功能更加丰富。

5.ListableBeanFactory和HierarchicalBeanFactory是继承的主要容器。

##### 最常被使用的ApplicationContext接口实现类

1. FileSystemXmlApplicationContext：该容器从XML文件中加载已被定义的bean。在这里，你需要提供给构造器XML文件的完整路径。

2. ClassPathXmlApplicationContext：该容器从XML文件中加载已被定义的bean。在这里，你不需要提供XML文件的完整路径，只需正确配置CLASSPATH 环境变量即可，因为，容器会从CLASSPATH中搜索bean配置文件。

3. WebXmlApplicationContext：该容器会在一个 web 应用程序的范围内加载在XML文件中

获取bean来源， 解析信息成BeanDifination, 注册Bean到concurrentMap中。

ApplicationContext的实现类种类太多，但它们的 **核心都是将对象工厂功能委托给BeanFactory的实现类DefaultListableBeanFactory**，我们就以常用的实现类之一FileSystemXmlApplicationContext来解释。

源码主要了解实现逻辑，原理，看清里面的架构，想一想为什么要这样写。

Spring Ioc容器作为一个产品，其真正的价值体现在一系列产品特征上，而这些特征都是以依赖反转模式作为核心，他们为控制反转提供了很多便利，从而实现了完整的Ioc容器。

#### spring 依赖注入的实现

spring Ioc

BeanFactory.doGetBean，是 **依赖注入的实际入口**，他定义了Bean的定义模式，单例模式（Singleton）和原型模式（Prototype），而依赖注入触发的前提是BeanDefinition数据已经建立好的前提下。其实对于Ioc容器的使用，Spring提供了许多的参数配置，每一个参数配置实际上代表了一个Ioc实现特性，而这些特性的实现很多都需要在依赖注入的过程中或者对Bean进行生命周期管理的过程中完成。Spring Ioc容器作为一个产品，其真正的价值体现在一系列产品特征上，而这些特征都是以依赖反转模式作为核心，他们为控制反转提供了很多便利，从而实现了完整的Ioc容器。

getBean是依赖注入的起点，之后就会调用createBean，createBean可以生成需要的Bean以及对Bean进行初始化，但对createBean进行跟踪，发现他在AbstractBeanFactory中仅仅是声明，而具体实现是在AbstractAutowireCapableBeanFactory类里。

如果要仔细理解这个过程，我们必须从前面提到的populateBean方法入手.

在对doCreateBean的追踪中我们发现Bean的创建方法createBeanInstance与BeanDefinition的载入与解析方法**populateBean**方法是最为重要的。因为控制反转原理的实现就是在这两个方法中实现的。

在createBeanInstance中生成了Bean所包含的Java对象，对象的生成有很多种不同的方法：工厂方法+反射，容器的autowire特性等等，这些生成方法都是由相关BeanDefinition来指定的。

如果把Ioc容器比喻成一个人的话，Bean对象们就构成了他的骨架，依赖注入就是他的血肉，各种组件和支持则汇成了他的筋脉和皮肤，而各种特性则是他的灵魂。各种特性真正的使Spring Ioc有别于其他Ioc框架，也成就了应用开发的丰富多彩，Spring Ioc 作为一个产品，可以说，他的各种特性才是它真正的价值所在。


#### bean 的生命周期

Bean的完整生命周期经历了各种方法调用，这些方法可以划分为以下几类：

1、Bean自身的方法：这个包括了Bean本身调用的方法和通过配置文件中<bean>的init-method和destroy-method指定的方法

2、**Bean级生命周期**接口方法：这个包括了BeanNameAware、BeanFactoryAware、InitializingBean和DiposableBean这些接口的方法

3、**容器级生命周期**接口方法：这个包括了InstantiationAwareBeanPostProcessor 和 BeanPostProcessor 这两个接口实现，一般称它们的实现类为“后处理器”。

4、工厂后处理器接口方法：这个包括了AspectJWeavingEnabler, ConfigurationClassPostProcessor, CustomAutowireConfigurer等等非常有用的工厂后处理器接口的方法。工厂后处理器也是容器级的。在应用上下文装配配置文件之后立即调用。


Spring Ioc容器的核心是BeanFactory和BeanDefinition。分别对应对象工厂和依赖配置的概念。虽然我们通常使用的是ApplicationContext的实现类，但ApplicationContext只是封装和扩展了BeanFactory的功能。XML的配置形式只是Spring依赖注入的一种常用形式而已，而AnnotationConfigApplicationContext配合Annotation注解和泛型，早已经提供了更简易的配置方式，AnnotationConfigApplicationContext和AnnotationConfigWebApplicationContext则是实现无XML配置的核心接口，但无论你使用任何配置，最后都会映射到BeanDefinition。

这里特别要注意的还是BeanDefinition， Bean在XML文件里面的展现形式是<bean id="...">...</bean>，当这个节点被加载到内存中，就被抽象为BeanDefinition了，在XML Bean节点中的那些关键字，在BeanDefinition中都有相对应的成员变量。如何把一个XML节点转换成BeanDefinition，这个工作自然是由BeanDefinitionReader来完成的。Spring通过定义BeanDefinition来管理基于Spring的应用中的各种对象以及它们之间的相互依赖关系。BeanDefinition抽象了我们对Bean的定义，是让容器起作用的主要数据类型。我们知道在计算机世界里，所有的功能都是建立在通过数据对现实进行抽象的基础上的。

Ioc容器是用BeanDefinition来管理对象依赖关系的，对Ioc容器而言，BeanDefinition就是对控制反转模式中管理的对象依赖关系的数据抽象，也是容器实现控制反转的核心数据结构，有了他们容器才能发挥作用。

最后，其实IoC从原理上说是非常简单的，就是把xml文件解析出来，然后放到内存的map里，最后在内置容器里管理bean。但是看IoC的源码，却发现其非常庞大，看着非常吃力。这是因为spring加入了很多特性和为扩展性预留很多的接口，这些特性和扩展，造就了它无与伦比的功能以及未来无限的可能性，可以说正是他们将技术的美学以最简单的方法呈现在了人们面前，当然这也导致了他的复杂性。

Spring框架主要可以分为3个核心内容：

1、容器

2、控制反转（IoC ，Inversion of Control）

3、面向切面编程（AOP ，Aspect-Oriented Programming）

容器消除依赖。避免大量的new操作。

控制反转：反转资源的获取方向。容器主动地将资源推送给需要资源的类（或称为bean）XXXXService
而XXXService需要做的只是用一种合适的方式接受资源。在类中定义一个setter方法，让容器将匹配的资源注入
即在容器中把依赖的资源注入。

跨越好几个模块的功能和需求被称为**横切关注点**，典型的有日志、验证、事务管理


BeanFactory和FactoryBean的区别

BeanFactory： 以Factory结尾，表示它是一个工厂类，是用于管理Bean的一个工厂。

FactoryBean：以Bean结尾，表示它是一个Bean，不同于普通Bean的是：它是实现了FactoryBean<T>接口的Bean，根据该Bean的Id从BeanFactory中获取的实际上是FactoryBean的getObject()返回的对象，而不是FactoryBean本身， 如果要获取FactoryBean对象，可以在id前面加一个&符号来获取。

spring bean的生命周期

1：Bean的建立：

容器寻找Bean的定义信息并将其实例化。

2：属性注入：

使用依赖注入，Spring按照Bean定义信息配置Bean所有属性

3：BeanNameAware的setBeanName()：

如果Bean类有实现org.springframework.beans.BeanNameAware接口，工厂调用Bean的setBeanName()方法传递Bean的ID。

4：BeanFactoryAware的setBeanFactory()：

如果Bean类有实现org.springframework.beans.factory.BeanFactoryAware接口，工厂调用setBeanFactory()方法传入工厂自身。

5：BeanPostProcessors的ProcessBeforeInitialization()

如果有org.springframework.beans.factory.config.BeanPostProcessors和Bean关联，那么其postProcessBeforeInitialization()方法将被将被调用。

6：initializingBean的afterPropertiesSet()：

如果Bean类已实现org.springframework.beans.factory.InitializingBean接口，则执行他的afterProPertiesSet()方法

7：Bean定义文件中定义init-method：

可以在Bean定义文件中使用"init-method"属性设定方法名称例如：

如果有以上设置的话，则执行到这个阶段，就会执行initBean()方法

8：BeanPostProcessors的ProcessaAfterInitialization()

如果有任何的BeanPostProcessors实例与Bean实例关联，则执行BeanPostProcessors实例的ProcessaAfterInitialization()方法

此时，Bean已经可以被应用系统使用，并且将保留在BeanFactory中知道它不在被使用。有两种方法可以将其从BeanFactory中删除掉

1：DisposableBean的destroy()

在容器关闭时，如果Bean类有实现org.springframework.beans.factory.DisposableBean接口，则执行他的destroy()方法

2：Bean定义文件中定义destroy-method

在容器关闭时，可以在Bean定义文件中使用"destroy-method"属性设定方法名称


idea 先创建一个maven空项目，后在创建一个空的maven项目，后创建一个maven的webapp项目，webapp项目
放到后面再创建，否则会有问题


#### 使用方法

使用方法：

在本地服务的基础上，只需做简单配置，即可完成 **远程化调用**：

将上面的local.xml配置拆分成两份，将服务定义部分放在服务提供方remote-provider.xml，将服务引用部分放在服务消费方remote-consumer.xml。
并在提供方增加暴露服务配置<dubbo:service>，在消费方增加引用服务配置<dubbo:reference>。


```xml

remote-prvider.xml
<bean id=“xxxService” class=“com.xxx.XxxServiceImpl” /> <!-- 和本地服务一样实现远程服务 -->

<dubbo:service interface=“com.xxx.XxxService” ref=“xxxService” /> <!-- 增加暴露远程服务配置 -->

remote-consumer.xml

<dubbo:reference id=“xxxService” interface=“com.xxx.XxxService” /> <!-- 增加引用远程服务配置 -->

<bean id=“xxxAction” class=“com.xxx.XxxAction”> <!-- 和本地服务一样使用远程服务 -->
    <property name=“xxxService” ref=“xxxService” />
</bean>

```

```xml
<dubbo:application/> 应用配置，用于配置当前应用信息，不管该应用是提供者还是消费者。

<dubbo:registry/> 注册中心配置，用于配置连接注册中心相关信息。

<dubbo:protocol/> 协议配置，用于配置提供服务的协议信息，协议由提供方指定，消费方被动接受。

<dubbo:service/>服务配置，用于暴露一个服务，定义服务的元信息，一个服务可以用多个协议暴露，一个服务也可以注册到多个注册中心。

<dubbo:reference/> 引用配置，用于**创建一个远程服务代理**，一个引用可以指向多个注册中心。

<dubbo:module/> 模块配置，用于配置当前模块信息，可选。

<dubbo:monitor/> 监控中心配置，用于配置连接监控中心相关信息，可选。

<dubbo:provider/> 提供方的缺省值，当ProtocolConfig和ServiceConfig某属性没有配置时，采用此缺省值，可选。

<dubbo:consumer/> 消费方缺省配置，当ReferenceConfig某属性没有配置时，采
用此缺省值，可选。

<dubbo:method/> 方法配置，用于ServiceConfig和ReferenceConfig指定方法级的配置信息。

<dubbo:argument/> 用于指定方法参数配置。
```

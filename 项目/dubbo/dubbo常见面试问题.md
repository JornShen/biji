1. 测试和生产公用一套zookeeper，怎么保证消费不冲突？

使用白名单，dubbo提供了Filter扩展，可以通过自定义Filter来实现这个功能。
给dubbo添加白名单。

```java
public class AuthorityFilter implements Filter {  
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorityFilter.class);  

    private IpWhiteList ipWhiteList;  

    //dubbo通过setter方式自动注入  
    public void setIpWhiteList(IpWhiteList ipWhiteList) {  
        this.ipWhiteList = ipWhiteList;  
    }  

    @Override  
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {  
        if (!ipWhiteList.isEnabled()) {  
            LOGGER.debug("白名单禁用");  
            return invoker.invoke(invocation);  
        }  

        String clientIp = RpcContext.getContext().getRemoteHost();  
        LOGGER.debug("访问ip为{}", clientIp);  
        List<String> allowedIps = ipWhiteList.getAllowedIps();  
        if (allowedIps.contains(clientIp)) {  
            return invoker.invoke(invocation);  
        } else {  
            return new RpcResult();  
        }  
    }  
}  
```

配置

在resources目录下添加纯文本文件META-INF/dubbo/com.alibaba.dubbo.rpc.Filter，内容如下：

xxxFilter=com.xxx.AuthorityFilter  

修改dubbo的provider配置文件，在dubbo:provider中添加配置的filter，如下：

<dubbo:provider filter="xxxFilter" />

2. Dubbo中zookeeper做注册中心，如果注册中心集群都挂掉，发布者和订阅者之间还能通信么？

可以的，启动dubbo时，消费者会从zk拉取注册的生产者的地址接口等数据，缓存在本地。每次调用时，依然按照本地存储的地址进行调用.但是更新就无法同步获取更新。

（缓存在本地，每次调用按照本地的缓存地址进行调用，通讯地址在本地有缓存）

注册中心对等集群，任意一台宕掉后，将自动切换另一台。

3. dubbo 通信协议

dubbo: 默许得，单纯长衔接、NIO异步通讯。合适几据量小，伴发大得场景，消耗者几量大于参考者得处境。**传输个大文件、视频, 而且为不能用了**。很多普遍得平常类型用这种就够了。

RMI:JDK规范得长途调用协议，闭塞式短衔接&JDK规范的序列化方法。输出输入的数据包巨细同化，消耗者或者参考者几量不好少量，为相对恰当得，能够传文件。

hessian：Hessian选取**HTTP**通讯，因Servlet对外参考办事，在Dubbo中运用Jetty干办事器。**非常合适传输页面、文件**。

Http： 这种没什么棒陈诉得，关键为合适还有给APP、网页得Ajax参考办事。我在类型中到没这么用过，平常那种处境均为干脆用SpringMVC打成war包，经过Keepalived+Nginx+Tomcat采用高可用或者负载都衡。

WebService：老牌得几据交流措施了，SOAP协议，CXF框架完成

thrift：Facebook得RPC框架，Dubbo加了少许头消息，不鼓励null值。

缓存:Dubbo鼓励缓存几据库Memcached、Redis。

服务的提供者怎么完成无效踢出?

由Zookeeper注册核心是例，有单个heartbeat_monitor心脏跳动监察线程，查验参考者可否生存，假如反省不到心脏跳动，就把参考者从参考者列表中移除，反之参加。

可否能够直连服务的提供者？

能够，在dubbo:reference经过设置url干脆指引某个参考者，多地点用逗号分离。


4.  介绍一下dubbo的原理 或 简单介绍一下 dubbo

(dubbo是什么？)是一个分布式服务架构，提供服务的远程调用。

zookeeper的作用

dubbo的注册中心，用来给消费者和生产者订阅和发布服务的。

每次都要去请求zookeeper吗？

不是，dubbo请求后会有缓存。

dubbo服务缓存在哪里

在服务器的内存

如果调用服务的地址发生变化，服务调用者怎么知道

zookeeper会通知订阅者

结果缓存：区别于上面的地址缓存。

结果缓存，主要是用于加速热门数据的访问速度，Dubbo提供声明式缓存，以减少用户加缓存的工作量。缓存的数据具有幂等性，也就是相同的参数具有相同的返回结果。

缓存策略有三种：
1、LruCache，lru基于最近最少使用原则删除多余缓存，保持最热的数据被缓存。
2、ThreadLocalCache，threadlocal当前线程缓存，比如一个页面渲染，用到很多portal，每个portal都要去查用户信息，通过线程缓存，可以减少这种多余访问。
3、JCache，jcache与JSR107集成，可以桥接各种缓存实现。
除此之外，我们还可以自定义扩展缓存。

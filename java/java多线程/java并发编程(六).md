### java 并发编程 (六)

##### 线程池的使用

在任务与执行的策略之间的隐形耦合

![](pic/b1.png)
![](pic/b2.png)

饥饿死锁

![](pic/b3.png)

![](pic/b4.png)

运行时间较长的任务

![](pic/b5.png)

##### 设置线程池的大小

![](pic/b6.png)

![](pic/b7.png)

##### 配置ThreadPoolExecutor

![](pic/b8.png)

线程的创建和销毁

![](pic/b9.png)

管理队列任务

![](pic/b10.png)

![](pic/b11.png)

![](pic/b12.png)

饱和策略

![](pic/b13.png)

![](pic/b14.png)

![](pic/b15.png)

![](pic/b16.png)

线程工厂

![](pic/b17.png)

![](pic/b18.png)

![](pic/b19.png)

![](pic/b20.png)

![](pic/b21.png)

在调用构造函数后再定制ThreadPoolExecutor

![](pic/b22.png)

![](pic/b23.png)

扩展ThreadPoolExecutor

![](pic/b24.png)

![](pic/b25.png)

递归算法的并行性

![](pic/b26.png)

![](pic/b28.png)

![](pic/b27.png)

![](pic/b29.png)

![](pic/b30.png)


#### 避免活跃性危险（死锁，饥饿，活锁，糟糕的相应）


##### 死锁

![](pic/c1.png)

![](pic/c2.png)

![](pic/c4.png)

动态锁顺序死锁

![](pic/c5.png)
![](pic/c6.png)

在协作对象之间发生的死锁

![](pic/c7.png)

开放调用

![](pic/c8.png)

![](pic/c9.png)

![](pic/c10.png)

资源死锁

死锁的避免与诊断

![](pic/c11.png)


支持定时的锁

![](pic/c12.png)

通过线程转储信息来分析死锁

![](pic/c13.png)
![](pic/c14.png)

其他活跃性的危险

饥饿：

![](pic/c15.png)

![](pic/c16.png)

糟糕的响应性

活锁

![](pic/c17.png)

![](pic/c18.png)

### 性能与可伸缩性

![](pic/d1.png)

对性能的思考

![](pic/d2.png)

![](pic/d3.png)

性能与可伸缩性

![](pic/d4.png)

![](pic/d5.png)

![](pic/d6.png)

评估各种性能权衡因素

![](pic/d7.png)

![](pic/d8.png)

![](pic/d9.png)

![](pic/d10.png)

##### Amdahl 定律
![](pic/d11.png)

![](pic/d12.png)

Amdahl 定律的应用

![](pic/d14.png)

##### 线程引入的开销

并行带来的性能提升必须超过并发带来的开销

上下文切换

![](pic/d15.png)

内存同步

![](pic/d16.png)

![](pic/d17.png)

![](pic/d18.png)

阻塞

![](pic/d19.png)

##### 减少锁的竞争

减少锁的竞争会提高性能和可伸缩性

![](pic/d20.png)

![](pic/d21.png)

缩小锁的请求范围（“快进快出”）

![](pic/d22.png)

![](pic/d23.png)

减小锁的粒度

降低线程请求锁的频率

![](pic/d24.png)

![](pic/d25.png)

锁分段

![](pic/d26.png)

避免热点区域

使不同的线程在不同的数据上操作

![](pic/d27.png)

![](pic/d28.png)

一些替代独占锁的方法

![](pic/d29.png)

![](pic/d30.png)

检测cpu的利用率

![](pic/d31.png)

![](pic/d33.png)

对对象池说不

![](pic/d32.png)

比较Map的性能

![](pic/d34.png)

![](pic/d35.png)

减少上下文的切换

![](pic/d36.png)

![](pic/d37.png)


#### 并发程序的测试

![](pic/e1.png)

![](pic/e2.png)

正确性测试

![](pic/e3.png)

![](pic/e4.png)

对阻塞操作的测试

![](pic/e5.png)

![](pic/e6.png)

资源管理的测试

![](pic/e8.png)

使用回调

![](pic/e10.png)

性能测试

![](pic/e11.png)

![](pic/e12.png)

多种算法的比较

![](pic/e13.png)

![](pic/e14.png)

响应性衡量

![](pic/e15.png)

避免性能测试的陷阱

垃圾回收

![](pic/e16.png)

动态编译

![](pic/e17.png)

![](pic/e18.png)

![](pic/e19.png)

对代码的路径的不真实采样

![](pic/e20.png)

不真实的竞争

![](pic/e21.png)

无用代码的消除

![](pic/e22.png)

![](pic/e23.png)

其他的测试方法

![](pic/e24.png)

代码审查

![](pic/e25.png)

静态分析工具

FindBugs

![](pic/e26.png)

![](pic/e27.png)

面向方向的测试技术

![](pic/e28.png)

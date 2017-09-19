#### java 并发编程（三）

##### 对象的组合

将现有的安全组件组合成为规模更大的组件或程序。

设计线程安全的类

![](pic/46.png)

![](pic/47.png)

收集同步需求

![](pic/48.png)

![](pic/49.png)

依赖状态的操作

![](pic/50.png)

状态所有权

![](pic/51.png)

![](pic/52.png)

实例封闭

![](pic/53.png)

![](pic/55.png)

![](pic/54.png)

![](pic/56.png)

java 监视器模式 （java内置锁）

![](pic/57.png)

线程安全性委托

![](pic/58.png)

委托模型的例子

![](pic/59.png)

![](pic/60.png)

![](pic/61.png)

Collections.unmodifiableMap 保证了一个Map不会被修改

独立的状态变量

![](pic/62.png)

当委托失效

仅当一个变量参与到包含其他状态变量的不变性时，才可以声明为volatile

![](pic/63.png)

在现有的线程安全的类中添加功能

![](pic/64.png)

![](pic/65.png)

客户端加锁

![](pic/66.png)


![](pic/67.png)
注意，加的锁要相同

![](pic/68.png)

组合

![](pic/69.png)

将同步策略文档化

![](pic/70.png)

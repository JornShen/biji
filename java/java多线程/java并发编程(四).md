#### java 并发编程（四）

####### 基础构建模块

并发构建模块

同步类

![](pic/71.png)

同步类的问题

![](pic/72.png)

客户端加锁，降低了伸缩性，和并发性

迭代器

![](pic/73.png)


隐藏迭代器

![](pic/74.png)

所有间接的迭代操作都可能抛出ConcurrentModificationException异常

并发容器

![](pic/75.png)

![](pic/76.png)

ConcurrentHashMap

![](pic/77.png)

![](pic/78.png)

CopyOnwriteArrayList

![](pic/79.png)

![](pic/80.png)

阻塞队列的生产者-消费者模式

![](pic/82.png)

![](pic/81.png)

![](pic/83.png)
![](pic/84.png)

![](pic/85.png)

串行线程封闭

![](pic/86.png)
![](pic/87.png)

双端队列和工作密取

Deque 和 BlockingDeque

实现：ArrayDeque 和 LinkedBlockingDeque

![](pic/88.png)

阻塞方法和中断方法

![](pic/89.png)

![](pic/90.png)

同步工作类

![](pic/91.png)

**闭锁**

![](pic/92.png)

![](pic/93.png)

FutureTask 也可以用做闭锁

![](pic/94.png)

![](pic/95.png)

信号量

![](pic/96.png)

![](pic/97.png)

栅栏

![](pic/98.png)

![](pic/99.png)

构建高效且可伸缩的结果缓存

![](pic/100.png)

![](pic/101.png)

if 处依然有可能出现问题。非原子“先检查后执行”

最终实现:
![](pic/102.png)

小结：

![](pic/103.png)

![](pic/104.png)

![](pic/105.png)


### 显示锁

![](pic/f1.png)

###### Lock 与 ReentrantLock

![](pic/f2.png)

![](pic/f3.png)

lock 的用法:
![](pic/f4.png)

###### 轮询锁和定时锁

trylock， 避免死锁的发生

![](pic/f5.png)

![](pic/f6.png)

![](pic/f7.png)

可中断的锁获取操作

![](pic/f8.png)

![](pic/f9.png)

###### 非块结构的加锁

![](pic/f10.png)

###### 性能考虑因素

![](pic/f11.png)

###### 公平性

![](pic/f12.png)
![](pic/f13.png)

非公平性锁性能高的原因：

![](pic/f14.png)

![](pic/f15.png)

######  在synchronized和ReentrantLock 之间进行选择

![](pic/f16.png)

ReentrantLock 使用场景
![](pic/f17.png)

![](pic/f18.png)

###### 读写锁

![](pic/f19.png)

![](pic/f20.png)

![](pic/f21.png)

![](pic/f22.png)


![](pic/f23.png)
![](pic/f24.png)

![](pic/f25.png)

#### 构架自定义的同步工具

![](pic/g1.png)

###### 状态依赖性的管理

轮询解决状态依赖问题

![](pic/g2.png)
![](pic/g3.png)

![](pic/g4.png)

![](pic/g5.png)

外部自己进行处理

![](pic/g6.png)

![](pic/g7.png)

![](pic/g8.png)

![](pic/g9.png)

![](pic/g10.png)

![](pic/g11.png)

条件队列（条件变量）

![](pic/g12.png)

![](pic/g13.png)
![](pic/g14.png)

######  使用条件队列

条件谓词

![](pic/g15.png)

![](pic/g16.png)

过早唤醒

![](pic/g17.png)

丢失的信号

![](pic/g18.png)

通知

![](pic/g19.png)

![](pic/g20.png)

![](pic/g21.png)

阀门类

![](pic/g22.png)

子类的安全性问题

![](pic/g23.png)

封装条件队列

人口协议和出口协议

![](pic/g24.png)

显示的Condition 对象

![](pic/g25.png)

![](pic/g26.png)

await, signal, signalAll

![](pic/g27.png)

![](pic/g28.png)

synchronizer 剖析

![](pic/g29.png)

![](pic/g30.png)

![](pic/g31.png)

![](pic/g32.png)

AbstractQueueSynchronizer

![](pic/g33.png)

![](pic/g34.png)

![](pic/g35.png)

![](pic/g36.png)

![](pic/g38.png)

一个简单的闭锁

![](pic/g39.png)

![](pic/g40.png)

![](pic/g41.png)

java.util.concurrent 同步类中达到AQS

ReentrantLock

![](pic/g42.png)
![](pic/g43.png)

![](pic/g44.png)

Semaphore 和 CountDownLatch

![](pic/g45.png)

![](pic/g46.png)

FutureTask

![](pic/g47.png)

ReentrantReadWriteLock

![](pic/g48.png)

![](pic/g49.png)

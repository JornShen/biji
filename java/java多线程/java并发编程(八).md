### 原子变量和非阻塞同步机制

![](pic/h1.png)

锁的劣势

![](pic/h2.png)

![](pic/h3.png)

![](pic/h4.png)

![](pic/h6.png)

硬件对并发的支持

![](pic/h5.png)

![](pic/h7.png)

比较并交换

CAS （compare and swap）

![](pic/h8.png)

![](pic/h9.png)

非阻塞的计数器

![](pic/h10.png)

![](pic/h11.png)

JVM 对CAS的支持

![](pic/h12.png)

原子变量类

![](pic/h13.png)

![](pic/h14.png)

原子变量是一种更好的 volatile

![](pic/h15.png)

![](pic/h16.png)

![](pic/h17.png)

![](pic/h18.png)

![](pic/h19.png)

非阻塞算法

![](pic/h20.png)

非阻塞的栈

![](pic/h21.png)
![](pic/h22.png)
![](pic/h23.png)

![](pic/h24.png)

非阻塞的链表

![](pic/h25.png)

![](pic/h26.png)
![](pic/h27.png)

原子的域更新器

![](pic/h28.png)

ABA问题

![](pic/h29.png)

![](pic/h30.png)


### java 内存模型

![](pic/i1.png)

###### 平台的内存模型

![](pic/i2.png)

![](pic/i3.png)

###### 重排序

![](pic/i4.png)

![](pic/i5.png)

![](pic/i6.png)

借助同步

![](pic/i7.png)

![](pic/i8.png)
![](pic/i9.png)

发布

![](pic/i11.png)

![](pic/i12.png)

安全的发布对象

![](pic/i13.png)

安全初始化模式

![](pic/i14.png)
![](pic/i15.png)
单例模式

双重检查加锁

![](pic/i16.png)

![](pic/i17.png)

初始化过程的安全性

![](pic/i18.png)

![](pic/i19.png)

![](pic/i20.png)

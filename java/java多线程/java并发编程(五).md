### 结构化并发应用程序

###### 六 任务执行

![](pic/106.png)


显示为任务创建线程

无限创建线程的不足

![](pic/107.png)
![](pic/108.png)

Executor 框架

任务是一组逻辑单元，而线程是使任务异步执行的机制。

![](pic/109.png)


![](pic/110.png)
![](pic/111.png)

提交和执行解耦


![](pic/112.png)

执行策略

![](pic/113.png)

**线程池**

![](pic/114.png)
![](pic/115.png)


![](pic/116.png)

Executor的生命周期

JVM 只有在所有（非守护）线程结束全部终止后才会退出。

![](pic/117.png)

![](pic/118.png)

延迟任务和周期任务

![](pic/119.png)

![](pic/120.png)

![](pic/121.png)

找出可利用的并行性

![](pic/122.png)

![](pic/123.png)

![](pic/a1.png)

在异构任务并行化中存在的局限

![](pic/a2.png)

![](pic/a3.png)

CompletionService

![](pic/a4.png)

使用Future将图片下载和html文本的渲染分离开

![](pic/a6.png)


![](pic/a8.png)
![](pic/a7.png)

为任务设定时限

invokeAll

![](pic/a9.png)

![](pic/a10.png)

![](pic/a11.png)

#### （六）取消与关闭

![](pic/a12.png)

任务取消

![](pic/a13.png)


中断

![](pic/a14.png)

![](pic/a15.png)

![](pic/a16.png)
![](pic/a17.png)

中断策略

![](pic/a18.png)

响应中断

![](pic/a19.png)

![](pic/a20.png)

计时运行

![](pic/a21.png)
![](pic/a22.png)

通过Future来实现取消

![](pic/a23.png)

![](pic/a24.png)

处理不可中断的阻塞

![](pic/a25.png)

![](pic/a26.png)

采用newTaskFor来封装非标准的取消

![](pic/a27.png)

![](pic/a28.png)

![](pic/a29.png)

停止基于线程的服务

![](pic/a30.png)

日志服务

![](pic/a31.png)

![](pic/a33.png)

生产者 queue，消费者是 writer


![](pic/a32.png)
![](pic/a34.png)

![](pic/a35.png)

关闭ExecutorService

![](pic/a36.png)

![](pic/a37.png)

![](pic/a38.png)

毒丸对象

![](pic/a39.png)


![](pic/a40.png)
![](pic/a41.png)
![](pic/a42.png)

毒丸对象的使用注意点
![](pic/a43.png)

shutdownNow的局限性

![](pic/a44.png)

![](pic/a45.png)
![](pic/a46.png)

![](pic/a47.png)
![](pic/a48.png)

处理非正常的线程终止

![](pic/a49.png)

![](pic/a50.png)

![](pic/a51.png)

未捕获异常的处理

![](pic/a52.png)

![](pic/a53.png)

![](pic/a54.png)

JVM关闭

![](pic/a55.png)

关闭钩子

![](pic/a56.png)

![](pic/a57.png)

守护线程
![](pic/a58.png)

终结器
![](pic/a59.png)

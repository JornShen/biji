
#### 数据库

比较重要的段落，相关截图

![](redispic/a1.png)

![](redispic/a2.png)

![](redispic/a3.png)

![](redispic/a4.png)

源码（4.0.1）的src/server.h 文件中可以看到相关的数据结构

![](redispic/a5.png)

![](redispic/a6.png)
![](redispic/a7.png)

![](redispic/a8.png)

过期时间是一个unix的时间戳

![](redispic/a9.png)

![](redispic/a10.png)

![](redispic/a11.png)

![](redispic/a12.png)
![](redispic/a13.png)

![](redispic/a14.png)
![](redispic/a15.png)

![](redispic/a16.png)

![](redispic/a17.png)

![](redispic/a18.png)

### RDB 持久化

![](redispic/b1.png)

![](redispic/b2.png)

![](redispic/b3.png)

![](redispic/b4.png)

![](redispic/b5.png)

![](redispic/b6.png)

![](redispic/b7.png)

RDB 文件结构

![](redispic/b8.png)

![](redispic/b9.png)

![](redispic/b10.png)

![](redispic/b11.png)

![](redispic/b12.png)

![](redispic/b13.png)

![](redispic/b14.png)

![](redispic/b15.png)

value的编码

String
![](redispic/b16.png)

![](redispic/b17.png)

![](redispic/b18.png)

list
![](redispic/b19.png)

set

![](redispic/b20.png)


hash

![](redispic/b21.png)

zset
![](redispic/b22.png)

编码方式

![](redispic/b24.png)

实际编码

![](redispic/b23.png)

![](redispic/b25.png)

AOF 持久化

![](redispic/b26.png)

![](redispic/b27.png)

![](redispic/b28.png)

![](redispic/b29.png)

![](redispic/b30.png)

![](redispic/b31.png)

AOF 载入和数据还原

![](redispic/b33.png)

AOF 重写
![](redispic/b32.png)

![](redispic/b34.png)

![](redispic/b35.png)

![](redispic/b36.png)

AOF 后台重写

![](redispic/b37.png)

![](redispic/b38.png)

![](redispic/b39.png)

![](redispic/b40.png)

![](redispic/b41.png)

事件驱动

![](redispic/b42.png)

文件事件驱动

![](redispic/b43.png)

![](redispic/b44.png)

![](redispic/b45.png)

IO 多路复用

![](redispic/b46.png)

多路复用：一个线程就完成了n个socket的读写

多路复用是指使用一个线程来检查多个文件描述符（Socket）的就绪状态，比如调用select和poll函数，传入多个文件描述符，如果有一个文件描述符就绪，则返回，否则阻塞直到超时。得到就绪状态后进行真正的操作可以在同一个线程里执行，也可以启动线程执行（比如使用线程池）。


这样在处理1000个连接时，只需要1个线程监控就绪状态，对就绪的每个连接开一个线程处理就可以了，这样需要的线程数大大减少，减少了内存开销和上下文切换的CPU开销。这样在处理1000个连接时，只需要1个线程监控就绪状态，对就绪的每个连接开一个线程处理就可以了，这样需要的线程数大大减少，减少了内存开销和上下文切换的CPU开销。



```
ClientSocket s = accept(ServerSocket);  // 阻塞
  waitting_set.add(s);
  while(1) {
     set_have_data  ds = select(waitting_set); // 多路复用器，
                                               // 多路复用是说多个socket数据通路
                                               // 一个select就可以侦听
                                               // 就好像他们是一个数据通路一样。
     foreach(ds as item) {
          item.read();  // reactor这里不会阻塞，采用异步io，详情查linux aio
          item.send('xxx');
     }
      item.close();
  }
```

![](redispic/b47.png)

![](redispic/b49.png)

文件事件处理器

![](redispic/b48.png)

![](redispic/b50.png)

![](redispic/b52.png)

![](redispic/b51.png)

![](redispic/b53.png)

时间事件

![](redispic/b54.png)

![](redispic/b55.png)

![](redispic/b56.png)

![](redispic/b57.png)
![](redispic/b58.png)

![](redispic/b59.png)

![](redispic/b60.png)

![](redispic/b61.png)

![](redispic/b62.png)

客户端

I/O 多路复用，一对多服务

![](redispic/b63.png)

![](redispic/b64.png)

套接字fid， 名字name， 标志flag记录客户端的角色以及状态,  


![](redispic/b65.png)
![](redispic/b66.png)

输入缓冲区，保存客户端发送的命令请求。


![](redispic/b67.png)

命令与命令参数

![](redispic/b68.png)

命令
![](redispic/b69.png)

![](redispic/b70.png)

输出缓冲区

保存命令回复
![](redispic/b71.png)

![](redispic/b73.png)

身份验证

![](redispic/b74.png)

![](redispic/b75.png)

时间

![](redispic/b76.png)

![](redispic/b77.png)

![](redispic/b78.png)

![](redispic/b79.png)

服务器端

命令请求的执行过程

![](redispic/b80.png)

![](redispic/b81.png)

![](redispic/b82.png)

![](redispic/b83.png)

![](redispic/b84.png)

![](redispic/b85.png)

![](redispic/b86.png)

![](redispic/b87.png)

![](redispic/b88.png)

![](redispic/b89.png)

servcron函数

管理服务器资源，保持服务器自身良好运转。定期函数。

![](redispic/b90.png)

![](redispic/b91.png)

![](redispic/b92.png)

![](redispic/b93.png)

![](redispic/b94.png)

![](redispic/b95.png)

![](redispic/b96.png)

![](redispic/b97.png)

![](redispic/b98.png)

![](redispic/c1.png)
![](redispic/c4.png)
![](redispic/c2.png)

![](redispic/c3.png)

![](redispic/c6.png)

![](redispic/c7.png)

![](redispic/c8.png)

![](redispic/c9.png)

![](redispic/c10.png)

![](redispic/c11.png)

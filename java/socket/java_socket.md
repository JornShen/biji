## Socket

####　TCP

#### UDP

在 IP 协议的基础上添加了端口；

对传输过程中可能产生的数据错误进行了检测，并抛弃已经损坏的数据。

Java 通过 **DatagramPacket** 类和 **DatagramSocket** 类来使用 UDP 套接字，客户端和服务器端都**通过DatagramSocket 的 send()方法和 receive()方法来发送和接收数据**，用 **DatagramPacket** 来包装需要发送或者接收到的数据。发送信息时，Java 创建一个包含待发送信息的 DatagramPacket 实例，并将其作为参数传递给DatagramSocket实例的send()方法；接收信息时，Java 程序首先创建一个 DatagramPacket 实例，该实例预先分配了一些空间，并将接收到的信息存放在该空间中，然后把该实例作为参数传递给 DatagramSocket 实例的 receive()方法。在创建 DatagramPacket 实例时，要注意：**如果该实例用来包装待接收的数据，则不指定数据来源的远程主机和端口，只需指定一个缓存数据的 byte 数组** 即可（在调用 receive()方法接收到数据后，源地址和端口等信息会自动包含在 DatagramPacket 实例中），而如果**该实例用来包装待发送的数据**，则要指定要发送到的目的主机和端口。

* 创建一个 DatagramSocket 实例，可以有选择对本地地址和端口号进行设置，如果设置了端口号，则客户端会在该端口号上监听从服务器端发送来的数据；
* 使用 DatagramSocket 实例的 send()和 receive()方法来发送和接收 DatagramPacket 实例，进行通信；
* 通信完成后，调用 DatagramSocket 实例的 close()方法来关闭该套接字。

我们在客户端使用 DatagramSocket 类的 setSoTimeout()方法来制定 receive()方法的最长阻塞时间，并指定重发数据报的次数，如果每次阻塞都超时，并且重发次数达到了设置的上限，则关闭客户端。

当在 TCP 套接字的输出流上调用 write()方法返回后，所有调用者都知道数据已经被复制到一个传输缓存区中，实际上此时数据可能已经被发送，也有可能还没有被传送，而 UDP 协议没有提供从网络错误中恢复的机制，因此，并不对可能需要重传的数据进行缓存。这就意味着，当send（）方法调用返回时，消息已经被发送到了底层的传输信道中。

UDP 数据报文所能负载的最多数据，亦及一次传送的最大数据为 65507 个字节。

DatagramPacket 的内部消息长度值在接收数据后会发生改变，变为实际接收到的数据的长度值。

一个应用程序使用同一个 DatagramPacket 实例多次调用 receive()方法，每次调用前就必须显式地将其内部消息长度重置为缓存区的实际长度，以免接受的数据发生丢失。

DatagramPacket 的 getData()方法总是返回缓冲区的原始大小，忽略了实际数据的内部偏移量和长度信息。

##### 应用程序协议中消息的成帧与解析

应用程序协议中明确定义了信息的发送者应该如何排列和解释这些位序列，同时还要定义接收者应该如何解析，这样才能使信息的接收者能够抽取出每个字段的意义。

成帧技术就是解决接收端如何定位消息首尾位置问题的，由于协议通常处理的是由一组字段组成的离散的信息，因此应用程序协议必须指定消息的接收者如何确定何时消息已被完整。主要有两种技术使接收者能够准确地找到消息的结束位置：

* 基于定界符：消息的结束由一个唯一的标记指出，即发送者在传输完数据后显式添加的一个特定字节序列，这个特殊标记不能在传输的数据中出现（这也不是绝对的，应用填充技术能够对消息中出现的定界符进行修改，从而使接收者不将其识别为定界符）。该方法通常用在以文本方式编码的消息中。

* 显式长度：在变长字段或消息前附加一个固定大小的字段，用来指示该字段或消息中包含了多少字节。该方法主要用在以二进制字节方式编码的消息中。

##### 构建和解析自定义协议消息

....

##### 基于线程池的服务器

``` java
public class ServerExecutor {  

    public static void main(String[] args) throws IOException{  
        //服务端在20006端口监听客户端请求的TCP连接   
        ServerSocket server = new ServerSocket(20006);  
        Socket client = null;  
        //通过调用Executors类的静态方法，创建一个ExecutorService实例  
        //ExecutorService接口是Executor接口的子接口  
        Executor service = Executors.newCachedThreadPool();  
        boolean f = true;  
        while(f){  
            //等待客户端的连接  
            client = server.accept();  
            System.out.println("与客户端连接成功！");  
            //调用execute()方法时，如果必要，会创建一个新的线程来处理任务，但它首先会尝试使用已有的线程，  
            //如果一个线程空闲60秒以上，则将其移除线程池；  
            //另外，任务是在Executor的内部排队，而不是在网络中排队  
            service.execute(new ServerThread(client));  
        }   
        server.close();  
    }  
}
```

read阻塞

如果在客户端不知道反馈回来的数据的情况下，该如何避免死锁呢？Java 的 Socket 类提供了 shutdownOutput()和 shutdownInput()另个方法，用来分别只关闭 Socket 的输出流和输入流，而不影响其对应的输入流和输出流，那么我们便可以在客户端发送完数据后，调用 shutdownOutput()方法将套接字的输出流关闭，这样，服务端的 read()方法便会返回 -1，继续往下执行，最后关闭服务端的套接字，而后客户端的 read()()方法也会返回 -1，继续往下执行，直到关闭套接字。

由于 read()方法只有在另一端关闭套接字的输出流时，才会返回 -1，而有时候由于我们不知道所要接收数据的大小，因此不得不用 read()方法返回 -1 这一判断条件，那么此时，合理的程序设计应该是先关闭网络输出流（亦即套接字的输出流），再关闭套接字。

#### NIO socket

Buffer：它是包含数据且用于读写的线形表结构。其中还提供了一个特殊类用于内存映射文件的 I/O 操作。

Charset：它提供 Unicode 字符串影射到字节序列以及逆影射的操作。

Channels：包含 socket，file 和 pipe 三种管道，它实际上是双向交流的通道。

Selector：它将多元异步 I/O 操作集中到一个或多个线程中。

非阻塞式网络 IO 的特点

把整个过程切换成小的任务，通过任务间协作完成。

由一个专门的线程来处理所有的 IO 事件，并负责分发。

事件驱动机制：事件到的时候触发，而不是同步的去监视事件。

线程通讯：线程之间通过 wait,notify 等方式通讯。保证每次上下文
切换都是有意义的。减少无谓的进程切换。

IO多路复用

Java NIO 引入了选择器的概念，选择器可以监听多个通道的事件（比如：连接打开，数据到达）。因此，单个的线程可以监听多个数据通道，这也是非阻塞 IO 的核心。而在标准 IO 的 Socket 编程中，单个线程则只能在一个端口监听。

NIO 的 **Selector** 选择器就实现了这样的功能，一个 Selector 实例可以同时检查一组信道的 I/O 状态，它就类似一个观察者，只要我们把需要探知的 SocketChannel 告诉 Selector,我们接着做别的事情，当有事件（比如，连接打开、数据到达等）发生时，它会通知我们，传回一组 SelectionKey,我们读取这些 Key,就会获得我们刚刚注册过的 SocketChannel,然后，我们从这个 Channel 中读取数据，接着我们可以处理这些数据。

客户端

``` java
//创建一个信道，并设为非阻塞模式  
SocketChannel clntChan = SocketChannel.open();  
clntChan.configureBlocking(false);  
//向服务端发起连接  
if (!clntChan.connect(new InetSocketAddress(server, servPort))){  //连接语句
    //不断地轮询连接状态，直到完成连接  
    while (!clntChan.finishConnect()){  
        //在等待连接的时间里，可以执行其他任务，以充分发挥非阻塞IO的异步特性  
        //这里为了演示该方法的使用，只是一直打印"."  
        System.out.print(".");    
    }  
}  
//分别实例化用来读写的缓冲区  
ByteBuffer writeBuf = ByteBuffer.wrap(argument);  
ByteBuffer readBuf = ByteBuffer.allocate(argument.length);  
//接收到的总的字节数  
int totalBytesRcvd = 0;   
//每一次调用read（）方法接收到的字节数  
int bytesRcvd;   
//循环执行，直到接收到的字节数与发送的字符串的字节数相等  
while (totalBytesRcvd < argument.length){  
   //如果用来向通道中写数据的缓冲区中还有剩余的字节，则继续将数据写入信道  
   if (writeBuf.hasRemaining()){  
       clntChan.write(writeBuf);  
   }  
   //如果read（）接收到-1，表明服务端关闭，抛出异常  
   if ((bytesRcvd = clntChan.read(readBuf)) == -1){  
       throw new SocketException("Connection closed prematurely");  
   }  
   //计算接收到的总字节数  
   totalBytesRcvd += bytesRcvd;  
   //在等待通信完成的过程中，程序可以执行其他任务，以体现非阻塞IO的异步特性  
   //这里为了演示该方法的使用，同样只是一直打印"."  
   System.out.print(".");   
}  
//打印出接收到的数据  
System.out.println("Received: " +  new String(readBuf.array(), 0, totalBytesRcvd));  
//关闭信道  
clntChan.close();  
```
服务端

``` java
//创建　selector，绑定端口
Selector selector = Selector.open();  
for (String arg : args){  
    //实例化一个信道  
    ServerSocketChannel listnChannel = ServerSocketChannel.open();  
    //将该信道绑定到指定端口  
    listnChannel.socket().bind(new InetSocketAddress(Integer.parseInt(arg)));  
    //配置信道为非阻塞模式  
    listnChannel.configureBlocking(false);  
    //将选择器注册到各个信道  
    listnChannel.register(selector, SelectionKey.OP_ACCEPT);  
}

TCPProtocol protocol = new EchoSelectorProtocol(BUFSIZE);  
       //不断轮询select方法，获取准备好的信道所关联的Key集  
while (true){  
     //一直等待,直至有信道准备好了I/O操作  
     if (selector.select(TIMEOUT) == 0){  
         //在等待信道准备的同时，也可以异步地执行其他任务，  
         //这里只是简单地打印"."  
         System.out.print(".");  
         continue;  
     }  
     //　获取准备好的信道所关联的Key集合的iterator实例  
     Iterator<SelectionKey> keyIter = selector.selectedKeys().iterator();  
     //　循环取得集合中的每个键值  
     while (keyIter.hasNext()){  
         SelectionKey key = keyIter.next();   
         //如果服务端信道感兴趣的I/O操作为accept  
         if (key.isAcceptable()){  
             protocol.handleAccept(key);  
         }  
         //如果客户端信道感兴趣的I/O操作为read  
         if (key.isReadable()){  
             protocol.handleRead(key);  
         }  
         //如果该键值有效，并且其对应的客户端信道感兴趣的I/O操作为write  
         if (key.isValid() && key.isWritable()) {  
             protocol.handleWrite(key);  
         }  
         //这里需要手动从键集中移除当前的key  
         keyIter.remove();   
     }  
 }

//服务端信道已经准备好了接收新的客户端连接  
public void handleAccept(SelectionKey key) throws IOException {  
    SocketChannel clntChan = ((ServerSocketChannel) key.channel()).accept();  
    clntChan.configureBlocking(false); //　异步加载
    //将选择器注册到连接到的客户端信道，并指定该信道key值的属性为OP_READ，同时为该信道指定关联的附件  
    clntChan.register(key.selector(), SelectionKey.**OP_READ**, ByteBuffer.allocate(bufSize));  
}  

//客户端信道已经准备好了从信道中读取数据到缓冲区  
public void handleRead(SelectionKey key) throws IOException{  
    SocketChannel clntChan = (SocketChannel) key.channel();  
    //获取该信道所关联的附件，这里为缓冲区  
    ByteBuffer buf = (ByteBuffer) key.attachment();  
    long bytesRead = clntChan.read(buf);  
    //如果read（）方法返回-1，说明客户端关闭了连接，那么客户端已经接收到了与自己发送字节数相等的数据，可以安全地关闭  
    if (bytesRead == -1){   
        clntChan.close();  
    }else if(bytesRead > 0){  
    //如果缓冲区总读入了数据，则将该信道感兴趣的操作设置为为可读可写  
    key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);  
    }  
}  

//客户端信道已经准备好了将数据从缓冲区写入信道  
   public void handleWrite(SelectionKey key) throws IOException {  
   //获取与该信道关联的缓冲区，里面有之前读取到的数据  
   ByteBuffer buf = (ByteBuffer) key.attachment();  
   //重置缓冲区，准备将数据写入信道  
   buf.flip();   
   SocketChannel clntChan = (SocketChannel) key.channel();  
   //将数据写入到信道中  
   clntChan.write(buf);  
   if (!buf.hasRemaining()){   
   //如果缓冲区中的数据已经全部写入了信道，则将该信道感兴趣的操作设置为可读  
     key.interestOps(SelectionKey.OP_READ);  
   }  
   //为读入更多的数据腾出空间  
   buf.compact();   
 }  
```

对于 ServerSocketChannel 来说，accept 是唯一的有效操作，而对于 SocketChannel 来说，有效操作包括读、写和连接，另外，对于 DatagramChannle，只有读写操作是有效的。

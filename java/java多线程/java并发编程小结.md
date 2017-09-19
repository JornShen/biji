
###　同步　

线程间的通信主要是通过共享访问字段以及其字段所引用的对象来实现的。

两种错误:线程干扰和内存一致性错误。同步解决这两种问题。

线程干扰

线程间的干扰出现在多个线程对同一个数据进行多个操作的时候，也就是出现了“交错”。

内存一致性错误

发生在不同线程对同一数据产生不同的“看法”。

避免出现内存一致性错误的关键在于理解 happens-before 关系。

这种关系是一种简单的方法，能够确保一条语句对内存的写操作对于其它特定的语句都是可见的。为了理解这点

可以采取多种方式建立这种 happens-before 关系,使用同步就是其中之一。

同步方法（synchronized methods）和同步语句

同步方法

```java
class SynchronizedCounter{

    private int c = 0;

    public synchronized void add(){
        c++;
    }

    public synchronized void dec(){

    }

    public synchronized int getC(){
        return c;
    }
}

```
#### 设置其方法为同步方法将有两个效果：

1. 首先，不可能出现对**同一对象**的**同步方法**的两个调用的“交错”。当一个线程在执行一个对象的同步方式的时候，其他所有的调用该对象的同步方法的**其他的线程**都会**被挂起**，直到第一个线程对该对象操作完毕。(避免干扰)

2. 其次，当一个同步方法退出时，会自动与该对象的同步方法的后续调用建立 happens-before 关系。这就确保了对该对象的修改对其他线程是**可见的**。(避免内存一致性，建立happens-before的可见性)


注意：构造函数不能是 synchronized ——在构造函数前使用 synchronized 关键字将导致语义错误。同步构造函数是没有意义的。这是因为只有创建该对象的线程才能调用其构造函数。


在创建多个线程共享的对象时，要特别小心对该对象的引用不能过早地“泄露”


同步方法是一种简单的可以避免线程相互干扰和内存一致性错误的策略：如果一个对象对多个线程都是可见的，那么所有对该对象的变量的读写都应该是通过同步方法完成的（一个例外就是 **final** 字段，他在对象创建完成后是不能被修改的，因此，在对象创建完毕后，可以通过非同步的方法对其进行安全的读取）。这种策略是有效的，但是可能导致“活跃度（liveness）”问题。


同步是构建在被称为“内部锁（intrinsic lock）”或者是“监视锁（monitor lock）”的内部实体上。
在 API 中通常被称为是“监视器（monitor）”(管程)。）内部锁在两个方面都扮演着重要的角色：保证对对象状态访问的**排他性**(互斥)和建立也对象可见性相关的重要的“ happens-before。

每一个**对象**都有一个与之相关联动的内部锁。按照传统的做法，当一个线程需要对一个对象的字段进行排他性访问并保持访问的一致性时，他必须在访问前先获取该对象的内部锁，然后才能访问之，最后释放该内部锁。在线程获取对象的内部锁到释放对象的内部锁的这段时间，我们说该线程拥有该对象的内部锁。只要有一个线程已经拥有了一个内部锁，其他线程就不能再拥有该锁了。其他线程将会在试图获取该锁的时候被阻塞了。当一个线程释放了一个内部锁，那么就会建立起该动作和后续获取该锁之间的 **happens-before** 关系。

**同步方法中的锁**

当一个线程调用一个同步方法的时候，他就自动地获得了该方法所属对象的内部锁，并在方法返回的时候释放该锁。即使是由于出现了**没有被捕获的异常**而导致方法返回，该锁也会被释放。

当调用一个 **静态的同步方法** 的时候会怎样了，静态方法是和**类**相关的，而不是和对象相关的。在这种情况下，线程获取的是**该类的类对象的内部锁**。这样对于静态字段的方法是通过一个和类的实例的锁相区分的另外的锁来进行的。

同步语句

使用同步语句是必须指明是要使用哪个对象的内部锁.

```java
public void addName(String name) {
    synchronized(this) {
        lastName = name;
        nameCount++;
    }
    nameList.add(name);
}
```
此表达相当于在方法前加入synchronized  

重入同步（Reentrant Synchronization）

线程不能获取已经被别的线程获取的锁。但是线程可以获取自身已经拥有的锁。允许一个线程能重复获得同一个锁就称为**重入同步**（reentrant synchronization）。它是这样的一种情况：在同步代码中直接或者间接地调用了还有同步代码的方法，两个同步代码段中使用的是同一个锁。如果没有重入同步，在编写同步代码时需要额外的小心，以避免线程将自己阻塞。

原子操作：

对几乎所有的原生数据类型变量（除了 long he double）的读写以及引用变量的读写都是原子的。

对所有声明为 volatile 的变量的读写都是原子的，包括 long 和 double 类型。

原子性动作时不用考虑线程间的干扰。然而，这并不意味着可以移除对原子操作的同步。内存一致性错误还是有可能出现的。使用 volatile 变量可以减少内存一致性错误的风险，因为任何对 volatile 变 量的写操作都和后续对该变量的读操作建立了 happens-before 关系。这就意味着对 volatile 类型变量的修改对于别的线程来说是**可见的**。

当一个线程读取一个 volatile 类型的变量时，他看到的不仅仅是对该变量的最后一次修改，还看到了导致这种修改的代码带来的其他影响。

死锁，活锁，饥饿，

Guarded Blocks（类似于管程）
它循环检查一个条件（通常初始值为 true），直到条件发生变化才跳出循环继续执行

wait() , notify,

当一个线程调用 wait 方法时，它释放锁并挂起。然后另一个线程请求并获得这个锁并调用 Object.notifyAll 通知所有等待该锁的线程。受通知的线程从wait()方法处开始执行。

还有另外一个通知方法，notify()，它只会唤醒一个线程。但由于它并不允许指定哪一个线程被唤醒，所以一般只在大规模并发应用（即系统有大量相似任务的线程）中使用。因为对于大规模并发应用，我们其实并不关心哪一个线程被唤醒。

生产者消费者问题

不可变对象

如果一个对象它被构造后其，状态不能改变，则这个对象被认为是不可变的

优点:使用不可变对象降低了垃圾回收所产生的额外开销，也减少了用来确保使用可变对象不出现并发错误的一些额外代码。

定义不可变对象的策略

不要提供 setter 方法。（包括修改字段的方法和修改字段引用对象的方法）

将类的所有字段定义为 final、private 的。

不允许子类重写方法。简单的办法是将类声明为 final，更好的方法是将构造函数声明为私有的，通过工厂方法创建对象。

如果类的字段是对可变对象的引用，不允许修改被引用对象。

不提供修改可变对象的方法。

不共享可变对象的引用。当一个引用被当做参数传递给构造函数，而这个引用指向的是一个外部的可变对象时，一定不要保存这个引用。如果必须要保存，那么创建可变对象的拷贝，然后保存拷贝对象的引用。同样如果需要返回内部的可变对象时，不要返回可变对象本身，而是返回其拷贝。

高级并发对象

Lock 对象作用非常类似同步代码使用的内部锁。如同内部锁，每次只有一个线程可以获得 Lock 对象。通过关联 Condition 对象，Lock 对象也支持 wait/notify 机制。

Lock 对象之于隐式锁最大的优势在于，它们有能力收回获得锁的尝试。如果当前锁对象不可用，或者锁请求超时（如果超时时间已指定），tryLock（非阻塞方法）方法会收回获取锁的请求。如果在锁获取前，另一个线程发送了一个中断，lockInterruptibly 方法也会收回获取锁的请求。

执行器 　方便管理线程，适合大规模应用

Executor，一个运行新任务的简单接口。

Executor 接口只有一个 execute 方法，用来替代通常创建（启动）线程的方法。例如：r 是一个 Runnable 对象，e 是一个 Executor 对象。可以使用

ExecutorService，扩展了 Executor 接口。添加了一些用来管理执行器生命周期和任务生命周期的方法。

ExecutorService 接口在提供了 execute 方法的同时，新加了更加通用的 submit 方法。submit 方法除了和 execute 方法一样可以接受 Runnable 对象作为参数，还可以接受 Callable 对象作为参数。使用 Callable对象可以能使任务返还执行的结果。通过 submit 方法返回的 Future 对象可以读取 Callable 任务的执行结果，或是管理 Callable 任务和 Runnable 任务的状态。 ExecutorService 也提供了批量运行 Callable 任务的方法。最后，ExecutorService 还提供了一些关闭执行器的方法。如果需要支持即时关闭，执行器所执行的任务需要正确处理中断。

ScheduledExecutorService，扩展了 ExecutorService。支持 future 和（或）定期执行任务。

线程池

使用工作线程可以使创建线程的开销最小化

一种最常见的**线程池**是固定大小的线程池。这种线程池始终有一定数量的线程在运行，如果一个线程由于某种原因终止运行了，线程池会自动创建一个新的线程来代替它。需要执行的任务通过一个**内部队列**提交给线程，当没有更多的工作线程可以用来执行任务时，队列保存额外的任务。

使用固定大小的线程池一个很重要的好处是可以实现优雅退化。限制web服务器创建的线程的数量

常用的线程池

newCachedThreadPool 方法创建了一个可扩展的线程池。适合用来启动很多短任务的应用程序。

newSingleThreadExecutor 方法创建了每次执行一个任务的执行器。

还有一些 ScheduledExecutorService 执行器创建的工厂方法。

newFixedThreadPool　创建固定数量的线程

Fork/Join框架

目的在于能够使用所有可用的运算能力来提升你的应用的性能。更好地利用多处理器带来的好处

fork/join 框架会将任务分发给线程池中的工作线程。fork/join 框架的独特之处在与它使用工作窃取(work-stealing)算法。完成自己的工作而处于空闲的工作线程能够从其他仍然处于忙碌(busy)状态的工作线程处窃取等待执行的任务。

fork/join 框架的**核心**是 ForkJoinPool 类，它是对 AbstractExecutorService 类的扩展。ForkJoinPool 实现了**工作窃取算法**，并可以执行 ForkJoinTask 任务。

工作窃取算法：某个线程从其他队列中窃取任务进行执行的过程。将大型任务分割，然后这些小任务会放在不同的队列中，**每一个队列都有一个相应的的工作执行线程来执行**，当一个线程所需要执行的队列中，任务执行完之后，这个线程就会被闲置，为了提高线程的利用率，这些空闲的线程可以从其他的任务队列中窃取一些任务，来避免使自身资源浪费，这种在自己线程闲置，同时窃取其他任务队列中任务执行的算法，就是工作窃取算法

工作代码框架
		if (当前这个任务工作量足够小)// 有一个判断的界限
		    直接完成这个任务
		else
		    将这个任务或这部分工作分解成两个部分
		    分别触发(invoke)这两个子任务的执行，并等待结果(invokeall)

并发集合

**BlockingQueue** 定义了一个先进先出的数据结构，当你尝试往满队列中添加元素，或者从空队列中获取元素时，将会**阻塞或者超时**。

**ConcurrentMap** 是 java.util.Map 的子接口，定义了一些有用的**原子操作**。移除或者替换键值对的操作只有当 key 存在时才能进行，而新增操作只有当 key 不存在时。使这些操作原子化，可以**避免同步**。ConcurrentMap 的标准实现是 **ConcurrentHashMap**，它是 HashMap 的并发模式。

ConcurrentNavigableMap 是 ConcurrentMap 的子接口，支持近似匹配。ConcurrentNavigableMap 的标准实现是 ConcurrentSkipListMap，它是 TreeMap 的并发模式。

所有这些集合，通过在集合里新增对象和访问或移除对象的操作之间，定义一个happens-before 的关系，来帮助程序员避免内存一致性错误。

原子变量

java.util.concurrent.atomic 包定义了对单一变量进行原子操作的类。所有的类都提供了 get 和 set 方法，可以使用它们像读写 volatile 变量一样读写原子类。就是说，同一变量上的一个 set 操作对于任意后续的 get 操作存在 happens-before 关系。

原子变量：AtomicInteger

TheadLocalRandom　并发随机数

ThreadLocalRandom.current().nextInt(4, 77);

fork-join 框架

Fork分解任务，Join收集数据。

子问题中应该避免使用synchronized关键词或其他方式方式的同步。也不应该是一阻塞IO或过多的访问共享变量。在理想情况下，每个子问题的实现中都应该只进行CPU相关的计算，并且只适用每个问题的内部对象。唯一的同步应该只发生在子问题和创建它的父问题之间。

ForkJoinTask实现了Future接口，可以按照Future接口的方式来使用。在ForkJoinTask类中之重要的两个方法fork和join。fork方法用以异步方式启动任务的执行，join方法则等待任务完成并返回指向结果。

RecurisiveTask类表示的任务可以返回结果，而RecurisiveAction类不行。

创建ForkJoinPool实例后，可以调用ForkJoinPool的submit(ForkJoinTask<T> task)或者invoke(ForkJoinTask<T> task)来执行指定任务。

将任务分解，然后在执行


```java
class SumTask extends RecurisiveTask<Integer> {
protected Integer compute() {  
        int sum = 0;  
        // 当end-start的值小于MAX时候，开始打印  
        if ((end - start) < MAX) {  
            for (int i = start; i < end; i++) {  
                sum += arr[i];  
            }  
            return sum;  
        } else {  
            System.err.println("=====任务分解======");  
            // 将大任务分解成两个小任务  
            int middle = (start + end) / 2;  
            SumTask left = new SumTask(arr, start, middle);  
            SumTask right = new SumTask(arr, middle, end);  
            // 并行执行两个小任务  
            left.fork();  //　异步方式启动线程
            right.fork();  
            // 把两个小任务累加的结果合并起来  
            return left.join() + right.join();  //　等待任务完成并返回结果
        }  
    }  
}
ForkJoinPool forkJoinPool = new ForkJoinPool();  
// 提交可分解的PrintTask任务  
Future<Integer> future = forkJoinPool.submit(new SumTask(arr, 0,  
				arr.length));  
System.out.println("计算出来的总和=" + future.get());  
// 关闭线程池  
forkJoinPool.shutdown();  		

```  

Synchronized的作用主要有三个：（1）确保线程互斥的访问同步代码（2）保证共享变量的修改能够及时可见happens-before（3）有效解决重排序问题。从语法上讲，Synchronized总共有三种用法：

（1）修饰普通方法

（2）修饰静态方法

（3）修饰代码块

对静态方法的同步本质上是对类的同步（静态方法本质上是属于类的方法，而不是对象上的方法）

monitorenter

每个对象有一个监视器锁（monitor）。当monitor被占用时就会处于锁定状态，线程执行monitorenter指令时尝试获取monitor的所有权，过程如下：

1、如果monitor的进入数为0，则该线程进入monitor，然后将进入数设置为1，该线程即为monitor的所有者。

2、如果线程已经占有该monitor，只是重新进入，则进入monitor的进入数加1.

3.如果其他线程已经占用了monitor，则该线程进入阻塞状态，直到monitor的进入数为0，再重新尝试获取monitor的所有权。

monitorexit

执行monitorexit的线程必须是objectref所对应的monitor的所有者。

指令执行时，monitor的进入数减1，如果减1后进入数为0，那线程退出monitor，不再是这个monitor的所有者。其他被这个monitor阻塞的线程可以尝试去获取这个 monitor 的所有权。

Synchronized的语义底层是通过一个monitor的对象来完成，其实wait/notify等方法也依赖于monitor对象，这就是为什么只有在同步的块或者方法中才能调用wait/notify等方法，否则会抛出java.lang.IllegalMonitorStateException的异常的原因。

方法的同步并没有通过指令monitorenter和monitorexit来完成（理论上其实也可以通过这两条指令来实现），不过相对于普通方法，其常量池中多了ACC_SYNCHRONIZED标示符。JVM就是根据该标示符来实现方法的同步的：当方法调用时，调用指令将会检查方法的 ACC_SYNCHRONIZED 访问标志是否被设置，如果设置了，执行线程将先获取monitor，获取成功之后才能执行方法体，方法执行完后再释放monitor。在方法执行期间，其他任何线程都无法再获得同一个monitor对象。 其实本质上没有区别，只是方法的同步是一种隐式的方式来实现，无需通过字节码来完成。

[参考文档](http://www.cnblogs.com/paddix/p/5367116.html)

---

**用volatile修饰的变量，线程在每次使用变量的时候，都会读取变量修改后的最新的值。** 

有一个内存区域是jvm虚拟机栈，每一个线程运行时都有一个线程栈，线程栈保存了线程运行时候变量值信息。

当线程访问某一个对象时候值的时候，首先通过对象的引用找到对应在堆内存的变量的值，然后把堆内存变量的具体值load到线程本地内存中，建立一个变量副本，之后线程就不再和对象在堆内存变量值有任何关系，而是直接修改副本变量的值，在修改完之后的某一个时刻（线程退出之前），自动把线程变量副本的值回写到对象在堆中变量

对于volatile修饰的变量，jvm虚拟机只是保证从主内存加载到线程工作内存的值是最新的。

只保证原子性，单次读或写具有原子特性
```java
public class TestVolatile {
    public static void main(String[] args){
        ExecutorService  executorService = Executors.newCachedThreadPool();
        for(int i=0; i < 1000 ; i++){
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    Counter.inc();
                }
            });
        }
        print(Counter.count);
    }
}
class Counter{
    public  static volatile int count = 0;
    public static void inc(){
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        count ++;
    }
　　/* count++;
    一个复合操作，包括三步骤：
   （1）读取i的值。
　　（2）对i加1。
　　（3）将i的值写回内存。
   */
}
```

volatile关键字就是Java中提供的另一种解决可见性和有序性问题的方案。

对volatile变量的单次读/写操作可以保证原子性的，如long和double类型变量，但是并不能保证i++这种操作的原子性，因为本质上i++是读、写两次操作。

应用场景：

防止重排序

多线程环境下就可能将一个未初始化的对象引用暴露出来，从而导致不可预料的结果。因此，为了防止这个过程的重排序，我们需要将变量设置为volatile类型的变量。　

（1）分配内存空间。

（2）初始化对象。

（3）将内存空间的地址赋值给对应的引用。

操作系统可以对这些指令进行重排。
（1）分配内存空间。
（2）将内存空间的地址赋值给对应的引用。
（3）初始化对象。

多线程环境下就可能将一个未初始化的对象引用暴露出来，从而导致不可预料的结果
```java
// volatile 应用于单例类
class Singleton{
    private  static volatile Singleton singleton;
    private Singleton(){}
    public static Singleton getInstance(){
        if(singleton == null){
            synchronized(singleton){
                if(singleton == null){
                    singleton = new Singleton();
                }
            }
        }
        return singleton;
    }
}
```

可见性问题主要指一个线程修改了共享变量值，而另一个线程却看不到。引起可见性问题的主要原因是每个线程拥有自己的一个高速缓存区——线程工作内存.

实现可见性
```java
public class VolatileTest {
      int a = 1;
      int b = 2;
      public void change(){
          a = 3;
          b = a;
     }
     public void print(){
         System.out.println("b="+b+";a="+a);
     }
     public static void main(String[] args) {
         while (true){
             final VolatileTest test = new VolatileTest();
             new Thread(new Runnable() {
                 @Override
                 public void run() {
                     try {
                         Thread.sleep(10);
                       } catch (InterruptedException e) {
                         e.printStackTrace();
                     }
                     test.change();
                 }
             }).start();
             new Thread(new Runnable() {
                 @Override
                 public void run() {
                     try {
                         Thread.sleep(10);
                     } catch (InterruptedException e) {
                         e.printStackTrace();
                     }
                     test.print();
                 }
             }).start();
         }
     }
}

```

volatile只能保证对**单次读/写**的原子性。鼓励大家将共享的long和double变量设置为volatile类型。

底层实现

可见性实现:

线程本身并不直接与主内存进行数据的交互，而是通过线程的**工作内存**来完成相应的操作。这也是导致线程间数据不可见的本质原因

对volatile变量的写操作与普通变量的主要区别有两点：

（1）修改volatile变量后将强制将修改后值刷新到主存中

（2）其他线程将对修改volatile变量后的相关的值失效， 当再次读取的时候将再从主存中读取。

有序性实现：　

**happen-before规则**

同一个线程中的，前面的操作 happen-before 后续的操作。（即单线程内按代码顺序执行。但是，在不影响在单线程环境执行结果的前提下，编译器和处理器可以进行重排序，这是合法的。换句话说，这一是规则无法保证编译重排和指令重排）。

监视器上的解锁操作 happen-before 其后续的加锁操作。（Synchronized 规则）

对volatile变量的写操作 happen-before 后续的读操作。（volatile 规则）

线程的start() 方法 happen-before 该线程所有的后续操作。（线程启动规则）

线程所有的操作 happen-before 其他线程在该线程上调用 join 返回成功后的操作。

如果 a happen-before b，b happen-before c，则a happen-before c（传递性）。

过重排序分为编译器重排序和处理器重排序。为了实现volatile内存语义，JMM会对volatile变量限制这两种类型的重排序。

---

为了实现volatile可见性和happen-before的语义。JVM底层是通过一个叫做“**内存屏障**”的东西来完成。内存屏障，也叫做内存栅栏，是一组处理器指令，用于实现对内存操作的顺序限制.

（1）LoadLoad 屏障
执行顺序：Load1—>Loadload—>Load2
确保Load2及后续Load指令加载数据之前能访问到Load1加载的数据。

（2）StoreStore 屏障
执行顺序：Store1—>StoreStore—>Store2
确保Store2以及后续Store指令执行前，Store1操作的数据对其它处理器可见。

（3）LoadStore 屏障
执行顺序： Load1—>LoadStore—>Store2
确保Store2和后续Store指令执行前，可以访问到Load1加载的数据。

（4）StoreLoad 屏障
执行顺序: Store1—> StoreLoad—>Load2
确保Load2和后续的Load指令读取之前，Store1的数据对其他处理器是可见的。

volatile是并发编程中的一种优化。

在某些场景下可以代替Synchronized。但是，volatile的不能完全取代Synchronized的位置，只有在一些特殊的场景下，才能适用volatile。

必须同时满足下面两个条件才能保证在并发环境的线程安全：

（1）对变量的写操作不依赖于当前值。

（2）该变量没有包含在具有其他变量的不变式中。

 ![](http://www.cnblogs.com/paddix/p/5428507.html)

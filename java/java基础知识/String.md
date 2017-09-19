
区别：

String

不可变，内部实现  private **final** char value[];
线程安全

StringBuilder

可变,内部实现 char[] value;
线程不安全　


StringBuffer
可变，内部实现 char[] value;
线程安全　synchronized


AbstractStringBuilder是StringBuilder与StringBuffer的公共父类，定义了一些字符串的基本操作，如expandCapacity、append、insert、indexOf等公共方法。StringBuilder、StringBuffer的方法都会调用AbstractStringBuilder中的公共方法，如super.append(...)。只是StringBuffer会在方法上加synchronized关键字，进行同步。

抽象类与接口的其中一个区别是：抽象类中可以定义一些子类的公共方法，子类只需要增加新的功能，不需要重复写已经存在的方法；而接口中只是对方法的申明和常量的定义。


AbstractStringBuilder中采用一个char数组来保存需要append的字符串，char数组有一个初始大小，当append的字符串长度超过当前char数组容量时，则对char数组进行**动态扩展**，也即重新申请一段更大的内存空间，然后将当前char数组拷贝到新的位置，因为重新分配内存并拷贝的开销比较大，所以每次重新申请内存空间都是采用申请大于当前需要的内存空间的方式，这里是2倍。


使用策略：　

1. 循环中尽量不要使用＋连接字符，使用　StringBuffer 或者是 StringBuilder 来创建新的字符

2. 如果要操作少量的数据，用String ；单线程操作大量数据，用StringBuilder ；多线程操作大量数据，用StringBuffer。

3. 为了获得更好的性能，在构造 StringBuffer 或 StringBuilder 时应尽可能**指定它们的容量**。当然，如果你操作的字符串长度（length）不超过 16 个字符就不用了，当不指定容量（capacity）时默认构造一个容量为**16**的对象。不指定容量会显著 降低性能。

4. StringBuilder一般使用在方法内部来完成类似"+"功能，因为是线程不安全的，所以用完以后可以丢弃。StringBuffer主要用在**全局变量**中。

5. 除非确定系统的瓶颈是在 StringBuffer 上，并且确定你的模块不会运行在多线程模式下，才可以采用StringBuilder；否则还是用StringBuffer。

关系图

![](pic/11.png)

[from web side](http://blog.csdn.net/zztfj/article/details/7563262)

### List

ArrayList的内部实现为一个**Object数组**，重载的两个构造方法，默认长度为**10**。
增加元素：

ArrayList是怎么实现容量自增.

```java
private void grow(int minCapacity) {
// overflow-conscious code
    int oldCapacity = elementData.length;
    int newCapacity = oldCapacity + (oldCapacity >> 1);
    if (newCapacity - minCapacity < 0)
        newCapacity = minCapacity;
    if (newCapacity - MAX_ARRAY_SIZE > 0)
        newCapacity = hugeCapacity(minCapacity);
    // minCapacity is usually close to size, so this is a win:
    elementData = Arrays.copyOf(elementData, newCapacity);//返回制定大小的数组，并拷贝，语法精简  
}
```

LinkedList： 底层用**双向循环链表**实现的List

Vector: 底层用**数组**实现List接口的另一个类；
特点：重量级，占据更多的系统开销，**线程安全**；
Vector与ArrayList基本上一致，最大的差别于在一些方法上使用了**synchronized**关键字，使其变成线程安全。

#### Set

HashSet：

实现：实际上的HashSet内部的实现为一个 **HashMap**。HashSet添加一个元素时，添加的值为HashMap的Key值，而一个 Object对象 却为一个Value值。HashSet无序不重复的属性则来自于HashMap。Map的key值不能重复。

采用**哈希算法**来实现Set接口；

唯一性保证：重复对象**equals**方法返回为true；

重复对象hashCode方法返回相同的整数，不同对象hashCode尽量保证不同（提高效率）


TreeSet：

在元素添加的同时，进行排序。也要给出排序规则；

唯一性保证：根据排序规则，**compareTo** 方法返回为0，就可以认定两个对象中有一个是重复对象。

如果想把自定义类的对象存入TreeSet进行排序, 那么必须实现Comparable接口

在使用TreeSet存储对象的时候, add()方法内部就会自动调用compareTo()方法进行比较, 根据比较结果使用二叉树形式进行存储

底层结构是**二叉树(TreeMap)**　**红黑树** 实现。按照树节点进行存储和取出

#### Map

元素是键值对：key唯一不可重复，value可重复；

遍历：先迭代遍历key的集合，再根据key得到value；

SortedMap：元素自动对key排序

HashMap:
轻量级，非线程安全，允许key或者value是null；

本质上是其为一个**Entry数组**,Entry以及key值一旦赋值就无法更改(final)

Hashtable：

重量级，**线程安全**，不允许key或者value是null；表级锁；

与HashMap很相识，这是Hashmap为其轻量级的实现。

大量带有实际操作性的方法都为synchronized修饰。

一个对象可以容纳了多个对象（不是引用），这个集合对象主要用来管理维护一系列相似的对象。

问题：

1. Map 没有继承 Collections 接口， 从改接口就有看出不可能集成的。

2. HashMap 是如何存放 null 的？

HashMap是根据key的hashCode来寻找存放位置的，那当key为null时，hash映射到table[0]。for循环遍历，当key为null的时候，用value 值进行替换，返回原来的value值， 当没有找到把这个值添加在table[0]链表的表头。

3. HashMap 和 Hashtable的区别？

HashMap是非synchronized的，线程非安全的，并可以接受null(HashMap可以接受为null的键值(key)和值(value)，而Hashtable则不行()。Hashtable是线程安全的。

另一个区别是HashMap的迭代器(Iterator)是 **fail-fast迭代器** ，而Hashtable的enumerator迭代器 **不是fail-fast的** 。当有其它线程改变了HashMap的结构（增加 set或者移除元素 remove），将会抛出ConcurrentModificationException，但迭代器本身的remove()方法移除元素则不会抛出ConcurrentModificationException异常.

4. ConcurrentHashMap 和 HashMap 的区别key没找到的null还是有key值为null，这在多线程里面是模糊不清的，所以压根就不让put null

HashMap 是 线程不安全的， ConcurrentHashMap 是hashmap的线程安全的版本。hashmap key 可以是null， 而 ConcurrentHashMap 不能value 不能是null, 会导致歧义。因为无法辨别
key没找到的null还是有key值为null，这在多线程里面是模糊不清的，所以压根就不让put null.

5. Hashtable 和 ConcurrentHashMap 的区别 ?

Hashtable 采用表级锁， ConcurrentHashMap 采用 分段锁

[](http://www.cnblogs.com/binyue/p/4545550.html)

为什么重写了equals方法就必须重写hashCode方法呢？

equals是判断 key 方法相同，但是，如果只重写equals方法而不重写hashCode方法的话，即使这两个对象通过equals方法判断是相等的，但是因为没有重写hashCode方法，他们的hashCode是不一样的，这样就会被散列到不同的位置去，变成错误的结果了。

6. ConcurrentHashMap的实现原理

jdk 1.7

[](http://www.cnblogs.com/ITtangtang/p/3948786.html)

jdk 1.8 的变化

[](http://blog.csdn.net/wangxiaotongfan/article/details/52074160)

两点改进：

1. 取消segments字段，直接采用transient volatile HashEntry<K,V>[] table保存数据，采用table数组元素作为锁，从而实现了对每一行数据进行加锁，进一步减少并发冲突的概率。

2. 将原先table数组＋单向链表的数据结构，变更为table数组＋单向链表＋红黑树的结构.

如果还是采用单向列表方式，那么查询某个节点的时间复杂度为O(n)；因此，对于个数超过8(默认值)的列表，jdk1.8中采用了红黑树的结构，那么查询的时间复杂度可以降低到O(logN)，可以改进性能。

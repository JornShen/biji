
#### java 8语言新特性　　　Lambda 接口引入默认方法,静态方法  方法引用

Lambda表达式（也称为闭包）:允许把函数作为一个方法的参数（函数作为参数传递进方法中），或者把代码看成数据

为了引入函数式编程，函数编程语言融合声明式编程特性是大势所趋。


##### 扩展接口

接口的默认方法: 默认方法使接口有点像Traits（Scala中特征(trait)类似于Java中的Interface，但它可以包含实现代码，也就是目前Java8新增的功能），但与传统的接口又有些不一样，它允许在已有的接口中添加新方法，而同时又保持了与旧版本代码的兼容性。

####### default关键字:

```java
private interface Defaulable {
    // Interfaces now allow default methods, the implementer may or
    // may not implement (override) them.
    default String notRequired() {
        return "Default implementation";
    }        
}
```

####### java静态方法:

java 8带来的另一个有趣的特性是接口可以声明（并且可以提供实现）静态方法。

```java

private interface DefaulableFactory {
    // Interfaces now allow static methods
    static Defaulable create( Supplier< Defaulable > supplier ) {
        return supplier.get();
    }
}

```

##### 方法引用

直接引用已有Java类或对象（实例）的方法或构造器

第一种方法引用是构造器引用，它的语法是Class::new，或者更一般的Class< T >::new。请注意构造器没有参数。

第二种方法引用是静态方法引用，它的语法是Class::static_method。请注意这个方法接受一个Car类型的参数。

第三种方法引用是特定类的任意对象的方法引用，它的语法是Class::method。请注意，这个方法没有参数

最后，第四种方法引用是特定对象的方法引用，它的语法是instance::method。请注意，这个方法接受一个Car类型的参数

####　重复注解

相同的注解可以在同一地方声明多次。

#### jdk 1.8

jdk1.8　　常量池从堆中移动出去，放在元空间里面，和堆相对独立。

java8 没有永久代，元空间的本质和永久代类似，都是对JVM规范中方法区的实现。不过元空间与永久代之间最大的区别在于：

元空间并不在虚拟机中，而是使用本地内存。可以使用虚拟内存(交换空间)扩容.

为什么要移除？　永久代溢出的问题, GC效率低的问题.

由于类的元数据分配在 **本地内存** 中，元空间的最大可分配空间就是**系统可用内存空间**。因此，我们就不会遇到**永久代存在时的内存溢出错误**，也不会出现泄漏的数据移到交换区这样的事情。最终用户可以为元空间设置一个可用空间最大值，如果不进行设置，JVM会自动根据类的元数据大小动态增加元空间的容量。

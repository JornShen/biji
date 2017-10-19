#### JVM 其他问题

JVM参数设置、分析

-Xms	初始堆大小

-Xmx  最大堆大小

-Xmn  年轻代大小 整个堆大小=年轻代大小 + 年老代大小 + 持久代大小.
增大年轻代后,将会减小年老代大小.此值对系统性能影响较大,Sun官方推荐配置为整个堆的 3/8

-XX:NewSize 年轻代大小

-XX:MaxNewSize 年轻代最大值

-XX:MaxPermSize 持久代初最大值

-XX:PermSize 持久代初始值

-Xss 每个线程的堆栈大小

-XX:ThreadStackSize 线程stack size

-XX:NewRatio 年轻代和老年代的比值   =4表示年轻代与年老代所占比值为1:4,年轻代占整个堆栈的1/5

-XX:SurvivorRatio Eden区与Survivor区的大小比值  设置为8,则两个Survivor区与一个Eden区的比值为2:8,一个Survivor区占整个年轻代的1/10, 假设这个参数为n, 则 eden : Survivor = n : 2 , 总大小看 -Xmn 设置的大小。

-XX:+DisableExplicitGC 关闭GC

-XX:-XX:MaxTenuringThreshold 垃圾最大的年龄

[参数参考博客](http://www.cnblogs.com/redcreen/archive/2011/05/04/2037057.html)

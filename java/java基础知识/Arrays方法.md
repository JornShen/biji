


1、**sort()**
       默认由小到大排序，不只对于数值型的可以排序，对于字符串等也都可以进行排序

2、binarySearch()
 对已排序（从小到大排序的）的数组进行二元搜索，如果找到指定的值就返回其所在的索引位置，否则返回负值
3、**fill()**
 将数组的元素全部设定为指定的值
4、equals()
 比较两个数组元素中的元素值是否全部相等，如果是返回true，否则返回false，适用于一维数组，多维数组用deepEquals()用法同equals;
5、deepEquals()
   对多维数组进行比较其内容是否一致，不能用于一维数组，会编译不过滴~
6、toString(int[] a)
   返回指定数组内容的字符串表示形式。
7、copyOf(int[] original, intnewLength)
   复制指定的数组，截取或用 0 填充（如有必要），以使副本具有指定的长度。从０开始复制。
8、copyOfRange(int[] original, intfrom, int to)
   将指定数组的指定范围复制到一个新数组。  
9、asList(array)
          利用Arrays.asList(array)将返回一个List，然而这个返回的List并不支持add和
   remove的操作。返回的List进行添加或删除时将会报
  java.lang.UnsupportedOperationException 异常。
 原因：在Arrays.asList中，该方法接受一个**变长参数**，一般可看做数组参数，但是因为基本数据类型，
   如int[] 本身就是一个类型，所以data变量作为参数传递时，编译器认为只传了一个变量，这个变量的
 类型是int数组，所以size为1。        
         因为是arrays.aslist中，看代码可以看到这里返回的ArrayList不是原来的传统意义上的java.util.arraylist了
，而是自己工具类的一个静态私有内部类，并没有提供add方法，要自己实现，所以这里是出错了，因此，
除非确信array.aslist后长度不会增加，否则谨慎使用：List abc=Arrays.asList("a","b","c"),
因为这样的长度是无法再add的了。

底层表示的是数组，不能调整尺寸。

数组复制

System.arraycopy(src,from_src,des,from_des,length)


```java
int[] data = {43,5,33,23,17,8};
int [] c = new int[5];
Arrays.sort(data);//排序

Print.print(Arrays.toString(data));
Print.print(Arrays.toString(Arrays.copyOf(array,2)));
Print.print(Arrays.toString(Arrays.copyOfRange(array,2,4)));
System.arraycopy(data,0,c,0,3);
Arrays.fill(data,0);
Print.print(Arrays.toString(data));
Print.print(Arrays.toString(c));

List<Integer> list =  Arrays.asList(1,3,4,5);
list.set(1,99);//
Print.print(list.toString());// 

```

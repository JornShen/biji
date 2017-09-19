

finally块中的内容会先于try中的return语句执行，如果finall语句块中也有return语句的话，那么直接从finally中返回了,finally中的return 会覆盖try中的值

```java

public class TryTest{  
    public static void main(String[] args){  
        System.out.println(test());  
    }  
    private static int test(){  
        int num = 10;  
        try{  
            System.out.println("try");  
            return num += 80;  //num = num + 80; return num;
        }catch(Exception e){  
            System.out.println("error");  
        }finally{  
            if (num > 20){  
                System.out.println("num>20 : " + num);  
            }  
            System.out.println("finally");  
        }  
        return num;  
    }  
}  
```

finally中的return语句先于try中的return语句执行，因而try中的return被”覆盖“掉了，不再执行。

```java
public class TryTest{  
    public static void main(String[] args){  
        System.out.println(test());  
    }  

    private static int test(){  
        int num = 10;  
        try{  
            System.out.println("try");  
            return num;  
        }catch(Exception e){  
            System.out.println("error");  
        }finally{  
            if (num > 20){  
                System.out.println("num>20 : " + num);  
            }  
            System.out.println("finally");  
            num = 100;  
        }  
        return num;  
    }  
}  

//out put try num>20:90 finally 100
```


try语句在返回前，将其他所有的操作执行完，保留好要返回的值，而后转入执行finally中的语句，而后分为以下三种情况：

情况一：如果finally中有return语句，则会将try中的return语句”覆盖“掉，直接执行finally中的return语句，得到返回值，这样便无法得到try之前保留好的返回值。

情况二：如果finally中没有return语句，也没有改变要返回值，则执行完finally中的语句后，会接着执行try中的return语句，返回之前保留的值。

情况三：如果finally中没有return语句，但是改变了要返回的值，这里有点类似与引用传递和值传递的区别，分以下两种情况，：

1）如果return的数据是**基本数据类型或文本字符串**，则在finally中对该基本数据的改变不起作用，try中的return语句依然会返回进入finally块之前保留的值。

2）如果return的数据是**引用数据类型**，而在finally中对该引用数据类型的属性值的改变起作用，try中的return语句返回的就是在finally中改变后的该属性的值。

实现原理：

返回值和返回引用参数。

CLR在执行中也有栈，但这个栈的用途与传统的本地代码中的栈并不完全相同

**本地代码** 中栈的用处非常大，不但可以用来临时保存寄存器的值，还用来保存局部变量，此外还用来保存部分或全部传给函数的参数，而函数的返回值一般是通过EAX寄存器来传递的，而不是用栈。

在CLR中，局部变量并非显式的用栈来保存，栈只是用来**调用函数时传递参数**，此外，函数的返回值也是用栈来保存的。函数的返回值也是用栈来保存的。

当通过压栈传递参数时，参数的类型不同，压栈的内容也不同。如果是值类型，压栈的就是经过复制的参数值，如果是引用类型，那么进栈的只是一个引用

代码中当我们执行new时，对应的IL是newobj，其结果是创建一个TestClass2类型的对像并返回一个引用放置于栈上，之后的stloc就将这个引用保存为局部变量，于是栈上没有了其他内容。Try块并没有执行太多操作，只是把刚保存的引用再放到栈上，再保存为另一个局部变量，这个局部变量就是稍后要返回的引用，此时我们拥有两个局部变量，但它们是指向同一个对象的两个引用。


Finally块先拿出开始时保存的引用放到栈上，dup语句使得栈顶再增加一个完全一样的引用，之后ldfld语句是从栈顶对象取一个成员放到栈上，所取的成员是value，之后再往栈上压一个1，再执行add，就实现了1+1=2的过程，add从栈上弹出两个值，再向栈压回一个值。此时再调用stfld就把刚刚压栈的2设置给栈上2之下的那个引用所指对象的value属性上。而在finally之后的部分才是真正的return，它试图取出我们所保存的第二个局部变量压栈，将它作为返回值。但对于引用类型来说，它与先前所操作的引用所指的是同一对象，因此finally块中的操作会影响到返回值

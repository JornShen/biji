IOC的思想是：Spring容器来实现这些相互依赖对象的创建、协调工作。

控制的什么被反转了？就是：获得依赖对象的方式反转了(由容器直接给)。

所有的类都会在spring容器中**登记**，告诉spring你是个什么东西，你需要什么东西，然后spring会在系统运行到适当的时候，把你要的东西主动给你，同时也把你交给其他需要你的东西。

说**控制对象生存周期的不再是引用它的对象**，而是spring

IoC的一个重点是在系统运行中，动态的向某个对象提供它所需要的其他对象。这一点是通过DI（Dependency Injection，依赖注入）来实现的。setter方法

spring就是通过反射来实现依赖注入的。

Spring **就开始加载我们的配置文件了，将我们配置的信息保存在一个HashMap
HashMap的key就是Bean的Id，HasMap 的value是这个Bean对象。**

Spring　依赖注入的思想也很简单，它是通过**反射机制**实现的。
在实例化一个类时，它通过反射调用类中set方法将事先保存在HashMap中的类属性注入到类中。
```java
public static Object newInstance(String className) {  
        Class<?> cls = null;  
        Object obj = null;  
        try {  
            cls = Class.forName(className);  
            obj = cls.newInstance();  
        } catch (ClassNotFoundException e) {  
            throw new RuntimeException(e);  
        } catch (InstantiationException e) {  
            throw new RuntimeException(e);  
        } catch (IllegalAccessException e) {  
            throw new RuntimeException(e);  
        }  
        return obj;  
}

```

把这个类依赖注入，源码实现
```java
public static void setProperty(Object obj, String name, String value) {   
        Class<? extends Object> clazz = obj.getClass();//获取类  
        try {   
            String methodName = returnSetMethodName(name);   
            Method[] ms = clazz.getMethods();   
            for (Method m : ms) {   
                if (m.getName().equals(methodName)) {//是否也有set方法   
                    if (m.getParameterTypes().length == 1) {   
                        Class<?> clazzParameterType = m.getParameterTypes()[0];　//获取参数类型   
                        setFieldValue(clazzParameterType.getName(), value, m,   
                                    obj);  //调用set方法，实现注入
                        break;   
                    }   
                }   
            }   
        } catch (SecurityException e) {   
            throw new RuntimeException(e);   
        } catch (IllegalArgumentException e) {   
            throw new RuntimeException(e);   
        } catch (IllegalAccessException e) {   
            throw new RuntimeException(e);   
        } catch (InvocationTargetException e) {   
            throw new RuntimeException(e);   
        }   
}  
```

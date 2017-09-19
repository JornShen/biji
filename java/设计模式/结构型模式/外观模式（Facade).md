
#### 外观模式　

外观模式是为了解决类与类之间的依赖关系的，像spring一样，可以将**类和类之间**的关系配置到配置文件中，
而外观模式就是将**他们的关系放在一个Facade**类中，降低了类类之间的耦合度，该模式中没有涉及到接口



```java
public class Computer {  
    private CPU cpu;  
    private Memory memory;  
    private Disk disk;  

    public Computer(){  
        cpu = new CPU();  
        memory = new Memory();  
        disk = new Disk();  
    }  

    public void startup(){  
        System.out.println("start the computer!");  
        cpu.startup();  
        memory.startup();  
        disk.startup();  
        System.out.println("start computer finished!");  
    }  

    public void shutdown(){  
        System.out.println("begin to close the computer!");  
        cpu.shutdown();  
        memory.shutdown();  
        disk.shutdown();  
        System.out.println("computer closed!");  
    }  
}
```

单个类持有其他类的对象，重新组合。

#### quartz 定时器的原理

Quartz是一个大名鼎鼎的Java版开源定时调度器

1. job
表示一个工作，要执行的具体内容。此接口中只有一个方法
void execute(JobExecutionContext context)

2. JobDetail
JobDetail表示一个具体的可执行的调度程序，Job是这个可执行程调度程序所要执行的内容，另外JobDetail还包含了这个任务调度的方案和策略。

3. Trigger代表一个调度参数的配置，什么时候去调。

4. Scheduler代表一个调度容器，一个调度容器中可以注册多个JobDetail和Trigger。当Trigger与JobDetail组合，就可以被Scheduler容器调度了。

job 和 JobDetail 的关系：

job需要完成什么类型的任务，除此之外，Quartz还需要知道该Job实例所包含的属性；这将由JobDetail类来完成（封装Job的其他参数）。

每次当scheduler执行job时，在调用其execute(…)方法之前会创建该类的一个新的实例；执行完毕，对该实例的引用就被丢弃了，实例会被垃圾回收；这种执行策略带来的一个后果是，job必须有一个无参的构造函数（当使用默认的JobFactory时）





#### 工作原理

通过研读Quartz的源代码，和本实例，终于悟出了Quartz的工作原理。

1、scheduler是一个计划调度器容器（总部），容器里面可以盛放众多的JobDetail和trigger，当容器启动后，里面的每个JobDetail都会根据trigger按部就班自动去执行。

2、JobDetail是一个可执行的工作，它本身可能是有状态的。

3、Trigger代表一个调度参数的配置，什么时候去调。

4、当JobDetail和Trigger在scheduler容器上注册后，形成了装配好的作业（JobDetail和Trigger所组成的一对儿），就可以伴随容器启动而调度执行了。

5、scheduler是个容器，容器中有一个线程池，用来并行调度执行每个作业，这样可以提高容器效率。

核心类是 scheduler，容器兼调度器，JobDetail 封装可执行的任务， trigger 封装调度的参数，当**JobDetail和Trigger在scheduler容器上注册后，形成了装配好的作业**（JobDetail和Trigger所组成的一对儿），就可以伴随容器启动而调度执行了。内部通过调用线程池统一执行。

Quartz的JobDetail、Trigger都可以在运行时重新设置，并且在下次调用时候起作用。这就为动态作业的实现提供了依据。你可以将调度时间策略存放到数据库，然后通过数据库数据来设定Trigger，这样就能产生动态的调度。

内部源码具体实现：

jobStore，表示使用哪种方式存储scheduler相关数据。quartz有两大jobStore：**RAMJobStore和JDBCJobStore**。RAMJobStore把数据存入内存，性能最高，配置也简单，但缺点是系统挂了难以恢复数据。JDBCJobStore保存数据到数据库，保证数据的可恢复性，但性能较差且配置复杂。


由调度线程和工作线程组成。

工作线程以{instanceName}_Worker-{[1-10]}命名。线程数目由quart.properties文件中的org.quartz.threadPool.threadCount配置项指定。**所有工作线程都会放在线程池中**，即所有工作线程都放在SimpleThreadPool实例的一个LinkedList<WorkerThread>成员变量中。WorkerThread是SimpleThreadPool的内部类，这么设计可能是因为不想继承SimpleThreadPool但又想调用其protected方法，或者想隐藏WorkerThread。线程池还拥有两个LinkedList<WorkerThread>：availWorkers和busyWorkers，分别存放空闲和正在执行job的工作线程。

调度器线程以{instanceName}_QuartzSchedulerThread命名。该线程将根据trigger找出要待运行job，然后从threadpool中拿出工作线程来执行。调度器线程主体是QuartzSchedulerThread对象。

##### 调度线程调度

```java

// 调度器线程一旦启动，将一直运行此方法
public void run() {
  ......
  // while()无限循环，每次循环取出时间将到的trigger，触发对应的job，直到调度器线程被关闭
  // halted是一个 AtomicBoolean类变量，有个volatile int变量value，其get()方法仅仅简单的一句return value != 0，get()返回结果表示调度器线程是否开关
  // volatile修饰的变量，存取必须走内存，不能通过cpu缓存，这样一来get总能获得set的最新真实值，因此volatile变量适合用来存放简单的状态信息
  // 顾名思义，AtomicBoolean要解决原子性问题，但volatile并不能保证原子性，详见http://blog.csdn.net/wxwzy738/article/details/43238089
  while (!halted.get()) { //调度线程是否开启
     try {
        // check if we're supposed to pause...
        // sigLock是个Object对象，被用于加锁同步
        // 需要用到wait()，必须加到synchronized块内
        synchronized (sigLock) {
            while (paused && !halted.get()) {
                try {
                    // wait until togglePause(false) is called...
                    // 这里会不断循环等待，直到QuartzScheduler.start()调用了togglePause(false)
                    // 调用wait()，调度器线程进入休眠状态，同时sigLock锁被释放
                    // togglePause(false)获得sigLock锁，将paused置为false，使调度器线程能够退出此循环，同时执行sigLock.notifyAll()唤醒调度器线程
                    sigLock.wait(1000L);
                } catch (InterruptedException ignore) {}
            }
            ......
        }
        ......
        // 如果线程池中的工作线程个数 > 0
        if(availThreadCount > 0) {
            ......
            // 获取马上到时间的trigger
            // 允许取出的trigger个数不能超过一个阀值，这个阀值是线程池个数与org.quartz.scheduler.batchTriggerAcquisitionMaxCount配置值间的最小者
            triggers = qsRsrcs.getJobStore().acquireNextTriggers(
                now + idleWaitTime, Math.min(availThreadCount, qsRsrcs.getMaxBatchSize()), qsRsrcs.getBatchTimeWindow());
            ......
            // 执行 与trigger绑定的job
            // shell是JobRunShell对象，实现了Runnable接口
            // SimpleThreadPool.runInThread(Runnable)从线程池空闲列表中取出一个工作线程
            // 工作线程执行WorkerThread.run(Runnable)，详见下方WorkerThread的讲解
            if (qsRsrcs.getThreadPool().runInThread(shell) == false) { ...... }
        } else {......}
        ......
    } catch(RuntimeException re) {......}
  } // while (!halted)
  ......
}

```

工作线程：

```java

public void run(Runnable newRunnable) {
        synchronized(lock) {
            if(runnable != null) {
                throw new IllegalStateException("Already running a Runnable!");
            }

            runnable = newRunnable;
            lock.notifyAll();
        }
}

// 工作线程一旦启动，将一直运行此方法
@Override
public void run() {
        boolean ran = false;

        // 工作线程一直循环等待job，直到线程被关闭，原理同QuartzSchedulerThread.run()中的halted.get()
        while (run.get()) {
            try {
               // 原理同QuartzSchedulerThread.run()中的 synchronized(sigLock)
               // 锁住lock，不断循环等待job，当job要被执行时，WorkerThread.run(Runnable)被调用，job运行环境被赋值给runnable
                synchronized(lock) {
                    while (runnable == null && run.get()) {
                        lock.wait(500);
                    }
                    // 开始执行job
                    if (runnable != null) {
                        ran = true;

                        // runnable.run()将触发运行job实现类（比如JobImpl.execute()）
                        runnable.run();
                    }
                }
            } catch (InterruptedException unblock) {
             ......
            }
        }
        ......
}

```

[参考博客，写得非常好](http://www.jianshu.com/p/164fbbdd0449)


#### 避免 GC

方案其实简单而实用，就是QuartzScheduler类创建了一个列表ArrayList<Object>(5) holdToPreventGC，如果某对象被add进该列表，则意味着QuartzScheduler实例引用了此对象，那么此对象至少在QuartzScheduler实例存活时不会被GC。

哪些对象要避免GC？通过源码可看到，调度器池和db管理器对象被放入了holdToPreventGC，但实际上两种对象是static的，而static对象属于GC root，应该是不会被GC的，所以即使不放入holdToPreventGC，这两种对象也不会被GC，除非被class unload或jvm生命结束。

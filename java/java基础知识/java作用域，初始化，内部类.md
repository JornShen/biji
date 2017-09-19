
类定义下才可以实现类对象（实例）直接访问类私有成员。

```java
public class testC {
	private int a;
	public void testF(testC a) {
		testC t = new testC();
		this.a = t.a;
		this.a = a.a;
	}
	public testC() {
	}
}
```
private（私有域）定义为本类作用域可见。即testC｛私有域｝中定义的成员，方法在本域是可见的。
类比｛｝中定义的局部变量，只有在｛｝中才是可见的（java）。

同类可以访问相同类的任何成员，不同类之间靠public和protected（继承）关系来访问

内部类　扩充多重继承，是对接口的补充

闭包回调的优点

内部类：普通内部类，嵌套类，匿名内部类

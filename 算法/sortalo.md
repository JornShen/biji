
#### 排序

快排:

qsort(arr, 0, arr.length-1);

void  qsort(int[] arr, int low, int high) {
	if (hight - low < 8) insertSort(arr, low, high);
	while (low < high) {
		int pos = partition(arr, low, high);
		qsort(arr, low, pos - 1);
		qsort(arr, pos + 1, high);
	}

}

int partition(int[] arr, int low, int high) {
	int temp = arr[low];
	while (low < high) {
		while (low < high && arr[high] >= temp) high--;
		arr[low] = arr[high];

		while (low < high && arr[low] < temp) low++;
		arr[high] = arr[low];
	}
	arr[low] = temp;
	return low;
}


// 堆排序

void sort(int[] arr) {

	for (int i = arr.length / 2 - 1; i >= 0; i--) {
		heapSort(arr, i, arr.length);
	}

	for (int i = arr.length - 1; i > 0; i--) {
		int temp = arr[i];
		arr[i] = arr[0];
		arr[0] = temp;
		heapSort(arr, 0, i);
	}

}



int heapSort(int[] arr, int pos, int end) {
	int next = 2 * pos;
	int temp = arr[pos];
	while (next < end) {

		if (next + 1 < end) {
			if (arr[next] < arr[next + 1]) {
				next++; // find max One;
			}
		}

		if (arr[next] < arr[pos]) {
			arr[pos] = arr[next];
			pos = next;
			next = 2 * pos;
		} else {
			break;
		}
	}
	arr[pos] = temp;

}

//  插入排序

void insertedSorted(int[] s, int start, int end) {
	int j, temp;
	for (int i = 1; i < s.length; i++) { // 从左到右进行排序
		int temp = s[i];
		for (j = i - 1; j >= 0; j--) {
			if (s[i] < s[j]) {
				s[j + 1] = s[j];   
			} else {
				break;
			}
		}
		s[j + 1] = temp;
	}
}

// 归并排序

public static int[] merge(int[] a, int[] b, boolean up){
		println("---- merge sort ----");
		int i = 0,j = 0,k = 0;
	    int[] c = new int[a.length + b.length];
		while (i < a.length && j < b.length) {
			if(compare(a[i], b[j], up)){
				c[k++] = b[j++];				
			}else{
				c[k++] = a[i++];
			}
		}
		if (i < a.length){
		   while(i < a.length){
			   c[k++] = a[i++];
		   }
		}

		if (j < b.length) {
			while(j < b.length){
				c[k++]= b[j++];
			}
		}
		for(int item : c){
			print(item +" ");
		}
		println(null);
		return c ;
}


private static void Sort(int[] a, int left, int right) {
        if(left>=right)
            return;

        int mid = (left + right) / 2;
        //二路归并排序里面有两个Sort，多路归并排序里面写多个Sort就可以了
        Sort(a, left, mid);
        Sort(a, mid + 1, right);
        merge(a, left, mid, right);
}

system.arraycopy(src, srcPos, des, desPos, length);

#### 树的遍历


1. 先序遍历

递归
```java

void preTrace(TreeNode root) {
	 if (root == null) return;
	 System.out.println(root.val);
	 preTrace(root.left);
	 preTrace(root.right);
}

```

非递归

```java
public void preTrace(TreeNode root) {
		if (root == null) return;
		Stack<TreeNode> stack = new Stack<>();
		while (root != null || !stack.isEmpty()) {
			 while (root != null) {
				 	System.out.println(root.val); // 遍历处理
					stack.push(root);
					root = root.left;
			 }
			 root = stack.pop();
			 root = root.right;
		}
}
```

2. 中序遍历

递归
```java
void midorderTrace(TreeNode root) {
	 if (root == null) return;
	 preTrace(root.left);
	 System.out.println(root.val);
	 preTrace(root.right);
}
```

```java
public void midorderTrace(TreeNode root) {
		if (root == null) return;
		Stack<TreeNode> stack = new Stack<>();
		while (root != null || !stack.isEmpty()) {
			 while (root != null) {
					stack.push(root);
					root = root.left;
			 }
			 root = stack.pop();
			 System.out.println(root.val); // 遍历处理
			 root = root.right;
		}
}
```

3. 后序遍历

递归
```java
public void postOrderTrace(TreeNode root) {
	 if (root == null) return;
	 preTrace(root.left);
	 preTrace(root.right);
	 System.out.println(root.val);
}
```

非递归

```java
public void postOrderTrace(TreeNode root) {
		if (root == null) return;
		Stack<TreeNode> stack = new Stac11k<>();
		TreeNode lastVisit = root;
		while (root != null || !stack.isEmpty()) {
			// 和先/中序遍历一样的
			 	while (root != null) {
						stack.push(root);
						root = root.left;
				} // 遍历左边的顶点
				root = stack.peek();
				// 没有右边节点，或者右边的节点是之前遍历过的节点
				if (root.right == null || root.right == lastVisit) {
 						// 第二次遍历该节点
						System.out.println(root.val); // 遍历处理
						stack.pop();
						lastVisit = root;
						root = null; //  继续向上进行遍历.
				} else {
						root = root.right;
				}
		}
}
```

![参看自...](http://www.jianshu.com/p/456af5480cee)

##### 单例模式

懒汉模式

```java
双重检查模式
public class Singleton {
		private volatile static Singleton singleton; // 此处需要使用 volatile， 避免重排序，防止使用签前未进行未初始化。

	  private Singleton() {}

	  public static Singleton getInstance() {
				if (Singleton == null) { // 第一重检查
					 	synchronized(Singleton.class) {
							if (singleton == null) { // 双重检查
									singleton = new Singleton();
							}
						}
				}
				return singleton;
		}
}
```

饿汉模式

静态内部类
```java
public class Singleton {
		private static class SingletonHolder {
				public static final Singleton SINGLETON = new Singleton();
		}
		private Singleton() {}
		public static final Singleton getInstance() {
				return SingletonHolder.SINGLETON;
		}
}
```

区别：懒汉模式，借助双重检查模式确保线程安全，延迟加载，占用内存少，但是访问起来比较慢。饿汉模式，提前初始化类，比较简单，访问快。但是占用内存。

枚举可以实现单例类

枚举 Enum

用枚举写单例实在太简单了！这也是它最大的优点。下面这段代码就是声明枚举实例的通常做法。

public enum EasySingleton{
    INSTANCE;
}

我们可以通过EasySingleton.INSTANCE来访问实例，这比调用getInstance()方法简单多了。创建枚举默认就是线程安全的，所以不需要担心double checked locking，而且还能防止反序列化导致重新创建新的对象。但是还是很少看到有人这样写，可能是因为不太熟悉吧。


深度优先遍历(先序遍历)

广度优先（队列实现）


dijistra 算法

```java

// d[] 表示 集合到其他点 的最短距离，p[i] 表离 i 最近的节点

public void shortDIJ(int v0,int d[],int p[]){
    int[] finnal = new int[n];//final 集合表示是否在s集合当中，即是否已经取得了最短距离
    int minum,w;
    for (int i =1; i <= n; i++) {
        d[i] = arc[v0][i];
        if (arc[v0][i] < MAXNUM) {// 可以直接到达
            p[i] = v0;
        } else {
            p[i] = MAXNUM;
        }
        finnal[i]=0;
    }
    d[v0] = 0 ;
    finnal[v0] = 1;
    for (int i = 1; i < n ;i++) {
        // 选择一条最小的路径，将相邻的另一点包进中
         minum = Integer.MAX_VALUE;
         for (int i = 1; i <= n; i++) {
             if (!finnal[i]) { // 从没有遍历的点中选出一条节点值最小的点
                 if (d[i] < minum) {
                     minum = d[i];
                     w = i;
                 }
             }
         }
         //此处考虑， 当有的点不可达的时候，依然可以成立 w 为上一轮的值
         finnal[w] = 1; //包进s的集合里

         //更新 到其他点的距离
         for (int i =1 ;i <= n; i++) {
             if (!finnal[i] && (arc[w][i] + d[w] < d[i])) {
                 d[i] = arc[w][i] + d[w];
                 p[i] = w; // 更新最近的点
             }
         }
    }
}

```

弗洛伊德算法

```java
for(int k = 1; k <= n; k++) {
		 for(int i = 1; i <= n; i++) {
				 for(int j =1; j <= n; j++) {
						 if(a[i][k] + a[k][j] < a[i][j]) {
								 a[i][j]= a[i][k]+ a[k][j];// 核心代码 动态规划
								 path[i][j]=k;//记录路径
						 	}
				 }
		 }
}
```

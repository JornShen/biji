# Redis

[install](https://redis.io/topics/quickstart)

```
wget http://download.redis.io/redis-stable.tar.gz
tar xvzf redis-stable.tar.gz
cd redis-stable
make
```

then enter the src dir and execute make test 


redis-server is the Redis Server itself. 

redis-sentinel is the Redis Sentinel executable (monitoring and failover).

redis-cli is the command line interface utility to talk with Redis.

redis-benchmark is used to check Redis performances.

redis-check-aof and redis-check-dump are useful in the rare event of corrupted data files.

```cmd
sudo cp src/redis-server /usr/local/bin/

sudo cp src/redis-cli /usr/local/bin/

```

Check if Redis is working

redis-cli

ping 
>pong //成功


连接方式

External programs talk to Redis using a TCP socket and a Redis specific protocol. 

##### ------------------   Redis 数据类型  --------------------

Redis is not a plain key-value store 非简单的键值对数据库

not limited to a simple string, but can also hold more complex data structures

* String: Binary-safe

* Lists: collections of **string elements** sorted

* Set: collections of **unique, unsorted** string elements.

* Sorted sets: 根据score进行排行。retrieve(检索) a range of elements 

* Hashes :fields(string) associated with values(string). 

* Bit arrays: 

#### ------------   redis 的命名规范   ----------------

rules about keys

empty string is also a valid key.

Key 值不可太长，不可太断，表明含义

注意 key 的命名

For instance "object-type:id" is a good idea, as in "user:1000". Dots or dashes are often used for multi-word fields, as in "comment:1234:reply.to" or "comment:1234:reply-to".

The maximum allowed key size is **512 MB.**

#### 数据类型 

============================== String =====================================
### String 

相关的操作命令

string -> string 

mapping a string to another string. 

##### set mykey somevalue

##### get mykey

value可以用来存放图片


set mykey somevalue nx 如果存在执行失败

set mykey somevalue xx 如果不存在执行失败

###### incr counter 原子性的

The INCR command parses the string value as an integer, increments it by one, and finally sets the obtained value as the new value. 

类似的命令：INCRBY : incrby counter 5                =>(count + 5)
            DECR: decr count
            DECRBY decrby count 11

the read-increment-set operation is performed while all the other clients are not executing a command at the same time.

###### GETSET（Atomically） 获取历史记录

command sets a key to a new value, returning the old value as the result.

set or retrieve the value of multiple keys

批量set 和 get
###### mset a 1 b 2 c 3

###### mget a b c 
1) "1"
2) "2"
3) "3"

###### EXISTS  returns 1 or 0 to signal if a given key exists or not in the database,

1 存在 
0 不存在

if the same existing key is mentioned in the arguments multiple times, it will be counted multiple times. So if somekey exists, EXISTS somekey somekey will return 2.


统计存在的key值，并且进行了累加

###### exists somekey somekey somekey 

del : deletes a key and associated value, whatever the value is.

Removes the specified keys. A key is ignored if it does not exist. 
返回删除值的个数
###### del key

type:The different types that can be returned are: string, list, set, zset and hash.
	 value if no exists, return none

###### set key1 value

type key1
>"string"

#### Redis expires  设置过期时间
keys with limited time to live

can set a timeout for a key, which is a limited time to live. 

the expire time resolution is always 1 millisecond.执行时间1ms

Information about expires are replicated and persisted on disk, the time virtually passes when your Redis server remains stopped (this means that Redis saves the date at which a key will expire).


#######  expire key 5  设置key的时间是5s

A key with an associated timeout is often said to be **volatile**

失效: cleared by commands that delete or overwrite the contents of the key, including **DEL, SET, GETSET and all the *STORE commands**.

all the operations that conceptually **alter** the value stored at the key without replacing it with a new one will leave the timeout untouched(不变). INCR , LPUSH, HSET

###### PERSIST timeout can also be cleared.

If a key is renamed with **RENAME**, the associated time to live is transferred to the new key name.

call EXPIRE using as argument a key that already has an existing expire set. 
In this case the time to live of a key is updated to the new value.

return value:
1 if the timeout was set

0 if key does not exist.


###### rename 会转移 time 

Note that calling **EXPIRE/PEXPIRE** with a **non-positive** timeout or EXPIREAT/PEXPIREAT with a time in the past will result in the key being deleted rather than expired 非负数直接删除

###### expire d -2  => delete d

PERSIST  remove time out

Remove the existing timeout on key, turning the key from volatile (a key with an expire set) to persistent (a key that will never expire as no timeout is associated)

1 if the timeout was removed.

0 if key does not exist or does not have an associated timeout

---
###### ttl 显示剩余时间

-1 not set timeout and exit 

-2 not exit 

positive left time

RENAME  key newkey 

returns an error when key does not exist

If newkey already exists it is overwritten, when this happens RENAME executes an implicit DEL operation

expire 特性

Keys expiring information is stored as **absolute** Unix timestamps 
迁移文件后，出现时间的不一致，将导致一定的后果。

过期的方式

Redis keys are expired in two ways: a passive way, and an active way.

A key is passively expired simply when some client tries to access it, and the key is found to be timed out.
periodically Redis tests a few keys at random among keys with an expire set. 

This way the expiration process is centralized in the master instance

while the slaves(子节点) connected to a master（主节点） will not expire keys independently (but will wait for the DEL coming from the master), they'll still take the full state of the expires existing in the dataset, so when a slave is elected to a master it will be able to expire the keys independently, fully acting as a master. 


### set 命令参数 

EX seconds -- Set the specified expire time, in seconds.

PX milliseconds -- Set the specified expire time, in milliseconds.

NX -- Only set the key if it does not already exist.

XX -- Only set the key if it already exist.

set and check expires in milliseconds, check the PEXPIRE and the PTTL 

===================================== list ====================================================

##### Redis Lists  队列

Redis lists are implemented via **Linked Lists.**

###### LPUSH key value [value ...]

Insert all the specified values at the head of the list stored at key.
If key does not exist, it is created as empty list before performing the push operations.
When key holds a value that is not a list, an error is returned.

The LPUSH command adds a new element into a list, on the left (at the head), while the RPUSH command adds a new element into a list ,on the right (at the tail)。

return 
Integer reply: the length of the list after the push operations.

多参数输入

lpush mylist a b c
顺序： c b a 

lrange mylist 0 -1 返回list的顺序

遍历顺序比较慢

Redis Lists are implemented with linked lists because for a database system it is crucial to be able to add elements to a very long list in a very fast way. Another strong advantage, as you'll see in a moment, is that Redis Lists can be taken at constant length in constant time.

####### rpush 

Insert all the specified values at the tail of the list stored at key.

###### LRANGE key start stop 

The offsets start and stop are zero-based indexes, with 0 being the first element of the list (the head of the list), 1 being the next element and so on.

-1 is the last element of the list, -2 the penultimate, and so on. 

If start is larger than the end of the list, an empty list is returned. If stop is larger than the actual end of the list, Redis will treat it like the last element of the list.


####### rpop remove from right and  return element

###### lpop  remove from left  and return element

list useage:

Remember the latest updates posted by users into a social network.加速访问

Communication between processes, using a consumer-producer pattern where the producer pushes items into a list, and a consumer (usually a worker) consumes those items and executed actions. Redis has special list commands to make this use case both more reliable and efficient.

use lists to store the latest items


###### LTRIM key start stop  保留指定范围的值，list 剪切

Trim an existing list so that it will contain only the specified range of elements specified。

Out of range indexes will not produce an error: if start is larger than the end of the list, or start > end, the result will be an empty list (which causes key to be removed). If end is larger than the end of the list, Redis will treat it like the last element of the list.

doing a List push operation + a List trim operation together in order to add a new element and discard elements** exceeding a limit**

Lists have a special feature that make them suitable to implement queues.

生产者 / 消费者 模式   lpush and rpop 

To push items into the list, producers call LPUSH.

To extract / process items from the list, consumers call RPOP.

So Redis implements commands called **BRPOP and BLPOP** which are versions of RPOP and LPOP able to block if the list is empty: they'll return to the caller only when a new element is added to the list, or when a user-specified timeout is reached.

BRPOP key [key ...] timeout 

it blocks the connection when there are no elements to pop from any of the given lists.
An element is popped from the tail of the first list that is non-empty, with the given keys being checked in the order that they are given.

A nil multi-bulk when no element could be popped and the timeout expired.
A two-element multi-bulk with the first element being the name of the key where an element was popped and the second element being the value of the popped element.

当值为0的时候，队列为空，将会无限期阻塞

多个key 将从key的从左至右进行弹出，当某一个key弹完，接着弹出下一个，一次仅弹出一个。
返回值是弹出list的关键字， 和值

RPOPLPUSH source destination

Atomically returns and removes the last element (tail) of the list stored at source, and pushes the element at the first element (head) of the list stored at destination.

If source does not exist, the value nil is returned and no operation is performed.

If **source and destination are the same**, the operation is equivalent to removing 
the last element from the list and pushing it as first element of the list, 
so it can be considered as a list rotation command.（循环队列）

BRPOPLPUSH source destination timeout

A timeout of zero can be used to block indefinitely.

three rules:

* When we add an element to an aggregate data type, if the target key does not exist, an empty aggregate data type is created before adding the element.

* When we remove elements from an aggregate data type, if the value remains empty, the key is automatically destroyed.

* Calling a read-only command such as LLEN (which returns the length of the list), or a write command removing elements, with an empty key, always produces the same result as if the key is holding an empty aggregate type of the type the command expects to find. 

LLEN  listkey

return len of list  

==================================== hash ===========================================
### Redis Hashes 数据结构

命令有点像 string 

适合用来存放用户的数据 key: user:1000

###### HMSET key field value [field value ...]

This command overwrites any specified fields already existing in the hash. If key does not exist, a new key holding a hash is created.

###### HGET key field

###### HMGET key field [field ...]

###### HSET key field value

###### HINCRBY key field increment(正数或负数)

If key does not exist, a new key holding a hash is created. If field does not exist the value is set to 0 before the operation is performed.

The range of values supported by HINCRBY is limited to 64 bit signed integers.

[hash 命令](https://redis.io/commands#hash)

================================= Set =============================================

### Redis Sets

unordered collections of strings.

####### SADD

Add the specified members to the set stored at key. Specified members that are already a member of this set are ignored. If key does not exist, a new set is created before adding the specified members.

An error is returned when the value stored at key is not a set.

用途：

It's also possible to do a number of other operations against sets like testing if a given element already exists, performing the intersection, union or difference between multiple sets, and so forth.

####### smembers 
查看set集合所有的元素

Redis is free to return the elements in any order at every call, since there is no contract with the user about element ordering.

####### sismenber key value
Redis has commands to test for membership.

Sets are good for **expressing relations** between objects. 
For instance we can easily use sets in order to implement tags

####### SINTER key [key ...]

Returns the members of the set resulting from the intersection of all the given sets.

key1 = {a,b,c,d}
key2 = {c}
key3 = {a,c,e}
SINTER key1 key2 key3 = {c}

###### SPOP key [count]

Removes and returns one or more random elements from the set value store at key.

this command is not suitable when you need a guaranteed uniform distribution of the returned elements
（无法保证均匀分布）

###### SRANDMEMBER key [count] 

return a random element from the set value stored at key.

return an array of count distinct elements if count is positive. 
If called with a negative count the behavior changes and the command is allowed to return the same element multiple times
（负数每次独立提取，可能会产生重复的数）

####### SUNIONSTORE destination key [key ...]

This command is equal to SUNION, but instead of returning the resulting set, it is stored in destination.

If destination already exists, it is overwritten.

if destinantion exists , it will overwrite

####### SCARD key

Returns the set  (number of elements) of the set stored at key.


================================    ZSet (Sorted Set)   ==========================================
###  Redis Sorted sets (ZSet)

unique, non-repeating

However while elements inside sets are not ordered, every element in a sorted set is associated with a floating point value, called the score (this is why the type is also similar to a hash, since every element is mapped to a value).

order rule:
They are ordered according to the following rule:
If A and B are two elements with a different score, then A > B if A.score is > B.score.
If A and B have exactly the same score, then A > B if the A string is lexicographically greater than the B string. A and B strings can't be equal since sorted sets only have unique elements.

###### ZADD key [NX|XX] [CH] [INCR] score member [score member ...]

If a specified member is already a member of the sorted set, the score is updated and the element reinserted at the right position to ensure the correct ordering.

The score values should be the string representation of a double precision floating point number. +inf and -inf values are valid values as well.

XX: Only update elements that already exist

NX: Don't update already existing elements. Always add new elements.

CH: Modify the return value from the number of new elements added, to the total number of elements changed 

The number of elements added to the sorted sets, not including elements already existing for which the score was updated.

实现：
Sorted sets are implemented via a dual-ported data structure containing both a skip list and a hash table, so every time we add an element Redis performs an O(log(N)) operation. (二分查找)

###### ZREVRANGE key start stop [WITHSCORES]

score  from high to low
Descending lexicographical order is used for elements with equal score.

###### ZRANGE key start stop [WITHSCORES]

ordered from the lowest to the highest score. Lexicographical order is used for elements with equal score.

###### ZRANGEBYSCORE key min max [WITHSCORES] [LIMIT offset count]

Returns all the elements in the sorted set at key with a score between min and max (including elements with score equal to min or max). The elements are considered to be ordered from low to high scores.

min and max can be ** -inf and +inf **, so that you are not required to know the highest or lowest score in the sorted set to get all elements from or up to a certain score.

###### ZRANGEBYSCORE zset (5 (10
Will return all the elements with 5 < score < 10 (5 and 10 excluded).

###### ZRANGEBYSCORE zset (1 5
Will return all elements with 1 < score <= 5 while:

( means no equal 

###### ZREMRANGEBYSCORE key min max

Removes all elements in the sorted set stored at key with a score between min and max (inclusive).

####### ZREVRANK key member

Returns the rank of member in the sorted set stored at key, with the scores ordered from high to low

####### ZRANK key member
	
Returns the rank of member in the sorted set stored at key, with the scores ordered from low to high

Lexicographical scores

####### ZRANGEBYLEX key min max [LIMIT offset count]

The elements are considered to be ordered from lower to higher strings as compared byte-by-byte using the memcmp() C function. Longer strings are considered greater than shorter strings if the common part is identical.

index elements by a 128-bit unsigned integer argument, all you need to do is to add elements into a sorted set with the same score (for example 0) but with an 16 byte prefix consisting of the 128 bit number in big endian.

sorted sets are suitable when there are tons of updates.

### Bitmaps

a set of bit-oriented operations defined on the String type.

Since strings are binary safe blobs and their maximum length is 512 MB, they are suitable to set up to 232 different bits.

One of the biggest advantages of bitmaps is that they often provide extreme space savings when storing information. 


SETBIT key offset value

The bit is either set or cleared depending on value, which can be either 0 or 1. 
When key does not exist, a new string value is created. 
The string is grown to make sure it can hold a bit at offset. 
The offset argument is required to be greater than or equal to 0, 
and smaller than 232 (this limits bitmaps to 512MB). When the string at key is grown, added bits are set to 0

Note that once this first allocation is done, subsequent calls to SETBIT for the same key will not have the allocation overhead.

automatically enlarges the string if the addressed bit is outside the current string length.

GETBIT key offset
	
BITOP operation destkey key [key ...]

Perform a bitwise operation between multiple keys (containing string values) and store the result in the destination key.

four bitwise operations: AND, OR, XOR and NOT.

all the strings shorter than the longest string in the set are treated as if they were zero-padded up to the length of the longest string.

The same holds true for non-existent keys, that are considered as a stream of zero bytes up to the length of the longest string.

Pattern: real time metrics using bitmaps
[](http://blog.getspool.com/2011/11/29/fast-easy-realtime-metrics-using-redis-bitmaps/)

性能：

BITOP is a potentially slow command as it runs in O(N) time. Care should be taken when running it against long input strings.
For **real-time metrics and statistics involving large inputs** a good approach is to **use a slave (with read-only option disabled) where the bit-wise operations** are performed to avoid blocking the master instance.

BITCOUNT key [start end]

Count the number of set bits (population counting) in a string

By default all the bytes contained in the string are examined. 
It is possible to specify the counting operation only in an 
interval passing the additional arguments start（B） and end(B).

bitmap数据非常大的时候:

将一个大的 bitmap 分散到不同的 key 中，作为小的 bitmap 来处理。使用 Lua 脚本可以很方便地完成这一工作。

使用 BITCOUNT 的 start 和 end 参数，每次只对所需的部分位进行计算，将位的累积工作(accumulating)放到客户端进行，并且对结果进行缓存 (caching)。

BITPOS key bit [start] [end] 

Common user cases for bitmaps are:

Real time analytics of all kinds.

Storing space efficient but high performance boolean information associated with object IDs.

#### HyperLogLog 

Counting unique things

A HyperLogLog is a probabilistic data structure used in order to count unique things.

Every time you see a new element, you add it to the count with PFADD.

Every time you want to retrieve the current approximation of the unique elements added with PFADD so far, you use the PFCOUNT.

PFADD key element [element ...]

PFCOUNT key [key ...] 

[](http://antirez.com/news/75)

[](https://redis.io/topics/data-types-intro#bitmaps)


GEOADD key longitude latitude member [longitude latitude member ...]
记录地理位置

Adds the specified geospatial items (latitude, longitude, name) to the specified key

经纬度的范围

Valid longitudes are from -180 to 180 degrees.

Valid latitudes are from -85.05112878 to 85.05112878 degrees.

The command will report an error when the user attempts to index coordinates outside the specified ranges.

use ZREM in order to remove elements. The Geo index structure is just a sorted set.

####### GEODIST key member1 member2 [unit]

Return the distance between two members in the geospatial index represented by the sorted set.


GEORADIUS key longitude latitude radius m|km|ft|mi [WITHCOORD] [WITHDIST] [WITHHASH] [COUNT count] [ASC|DESC] [STORE key] [STOREDIST key]

Return the members of a sorted set populated with geospatial information using GEOADD, which are within the borders of the area specified with the center location and the maximum distance from the center (the radius).


SCAN 

游标遍历
scan  0
返回下一次的游标和本次遍历的数组
当返回第下一次游标为0的时候，表示遍历结束


scan 0 MATCH *11* 
匹配


redis 持久化 

[](http://redisbook.readthedocs.io/en/latest/internal/aof.html)
[](https://redis.io/topics/persistence)

Redis总共支持四个级别：debug、verbose、notice、warning，默认为verbose

[](http://www.runoob.com/redis/redis-conf.html)

####  redis 的相关配置

13. 设置当本机为slave服务时，设置master服务的IP地址及端口，在Redis启动时，它会自动从master进行数据同步
slaveof <masterip> <masterport>

14.当master服务设置了密码保护时，slav服务连接master的密码
masterauth <master-password>


设置Redis连接密码，如果配置了连接密码，客户端在连接Redis时需要通过AUTH <password>命令提供密码，默认关闭
requirepass foobared

maxclients 128 同一时间最大客户端连接数，默认无限制

maxmemory <bytes>  指定Redis最大内存限制，Redis在启动时会把数据加载到内存中，达到最大内存后，Redis会先尝试清除已到期或即将到期的Key，当此方法处理 后，仍然到达最大内存设置，将无法再进行写入操作，但仍然可以进行读取操作。Redis新的vm机制，会把Key存放内存，Value会存放在swap区

指定是否启用虚拟内存机制，默认值为no，简单的介绍一下，VM机制将数据分页存放，由Redis将访问量较少的页即冷数据swap到磁盘上，访问多的页面由磁盘自动换出到内存中（在后面的文章我会仔细分析Redis的VM机制）
vm-enabled no

slave-serve-stale-data 当主机挂了，是否继续相应客户端

SUBSCRIBE redisChat

PUBLISH redisChat "Redis is a great caching technique"

Redis 发布订阅(pub/sub)是一种消息通信模式：发送者(pub)发送消息，订阅者(sub)接收消息。

Redis 客户端可以订阅任意数量的频道。


官方推荐配置：

Install Redis in your Linux box in a proper way using an init script, so that after a restart everything will start again properly

sudo mkdir /etc/redis

sudo mkdir /var/redis 

sudo cp utils/redis_init_script /etc/init.d/redis_6379 

sudo vi /etc/init.d/redis_6379 更改脚本，里面包括所有的相关文件的地址

sudo cp redis.conf /etc/redis/6379.conf

sudo update-rc.d redis_6379 defaults

sudo /etc/init.d/redis_6379 start

spring 单机整合
```xml

<dependency>
    <groupId>org.springframework.data</groupId>
    <artifactId>spring-data-redis</artifactId>
    <version></version>
</dependency>

<dependency>
    <groupId>redis.clients</groupId>
    <artifactId>jedis</artifactId>
    <version></version>
</dependency>

```

```xml

<bean id="redisTemplate" class="org.springframework.data.redis.core.StringRedisTemplate">  
        <property name="connectionFactory"   ref="jedisConnectionFactory" />  
        <!-- 不配置Serializer，存储只能使用String，用User存储，会提示错误User can't cast to String-->
        <property name="keySerializer">
        	<bean class="org.springframework.data.redis.serializer.StringRedisSerializer"/>
        </property>
        <property name="valueSerializer">
        	<bean class="org.springframework.data.redis.serializer.JdkSerializationRedisSerializer" />
        </property>
        <!-- <property name="enableTransactionSupport" value="true" /> -->
</bean> 

```





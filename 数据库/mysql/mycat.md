

mycat mysql 分布式集群管理

数据切分：分散单台设备的负载的效果。

垂直切分（不同表切分到不同的数据库上）   横向切分（同一个表中的数据切分到多台主机数据库上）


核心功能是分表分库

Mycat 是一个强大的数据库中间件，不仅仅可以用作读写分离、以及分表分库、容灾备份，而且可以用于多
租户应用开发、云平台基础设施、让你的架构具备很强的适应性和灵活性，借助于即将发布的 Mycat 智能优化模
块，系统的数据访问瓶颈和热点一目了然，根据这些统计分析数据，你可以自动或手工调整后端存储，将不同的
表映射到不同存储引擎上，而整个应用的代码一行也不用改变。

Mycat 的原理中最重要的一个动词是“拦截”，它拦截了用户发送过来的 SQL 语句，首先对 SQL 语句做了
一些特定的分析：如分片分析、路由分析、读写分离分析、缓存分析等，然后将此 SQL 发往后端的真实数据库，
并将返回的结果做适当的处理，最终再返回给用户。

数据库中间件

相关概念

#### 逻辑库(schema)




#### 逻辑表

既然有逻辑库，那么就会有逻辑表，分布式数据库中，对应用来说，读写数据的表就是逻辑表。逻辑表，可
以是数据切分后，分布在一个或多个分片库中，也可以不做数据切分，不分片，只有一个表构成

##### 分片表
分片表，是指那些原有的很大数据的表，需要切分到多个数据库的表，这样，每个分片都有一部分数据，所
有分片构成了完整的数据。

##### 非分片表
一个数据库中并不是所有的表都很大，某些表是可以不用进行切分的，非分片是相对分片表来说的，就是那
些不需要进行数据切分的表。


##### ER 表

关系型数据库是基于实体关系模型（Entity-Relationship Model)之上，通过其描述了真实世界中事物与关
系，Mycat 中的 ER 表即是来源于此。根据这一思路，提出了基于 E-R 关系的数据分片策略，子表的记录与所关
联的父表记录存放在同一个数据分片上，即子表依赖于父表，通过表分组（Table Group）保证数据 Join 不会跨
库操作。

表分组（Table Group）是解决跨分片数据 join 的一种很好的思路，也是数据切分规划的重要一条规则。

#####全局表

所有的分片都有一份数据的拷贝，所有将字典表或者符合字典表特性的一些表定义为全局表。

数据冗余是解决跨分片数据 join 的一种很好的思路，也是数据切分规划的另外一条重要规则。

#### 分片节点(dataNode)

数据切分后，一个大表被分到不同的分片数据库上面，每个表分片所在的数据库就是分片节点

分片节点

一个或多个分片节点（dataNode）所在的机器就是节点主机（dataHost）,为了规避单节点主机并发数限
制，尽量将读写压力高的分片节点（dataNode）均衡的放在不同的节点主机（dataHost）。

分片规则

一个大表被分成若干个分片表，就需要一定的规则，这样按照某种业务规则把数据分到
某个分片的规则就是分片规则

全局序列号

数据切分后，原有的关系数据库中的主键约束在分布式条件下将无法使用，因此需要引入外部机制保证数据
唯一性标识，这种保证全局性的数据唯一标识的机制就是全局序列号（sequence）

服务安装与配置


/bin
程序目录，存放了 window 版本和 linux 版本，除了提供封装成服务的版本之外，也提供了 nowrap 的
shell 脚本命令，方便大家选择和修改，进入到 bin 目录：
Linux 下运行：./mycat console, 首先要 chmod +x *
注：mycat 支持的命令{ console | start | stop | restart | status | dump }

/conf
目录下存放配置文件
server.xml 是 Mycat 服务器参数调整和用户授权的配置文件
schema.xml 是逻辑库定义和表以及分片定义的配置文件
rule.xml 是分片规则的配置文件，分片规则的具体一些参数信息单独存放为文件，也在这个目录下，

** 配置文件修改，需要重启 Mycat 或者通过 9066 端口 reload. **

服务启动与启动设置

1) vi /etc/profile,在系统环境变量文件中增加 MYCAT_HOME=/usr/local/Mycat

2) 执行 source /etc/profile 命令，使环境变量生效

如果是在多台 Linux 系统中组建的 MyCAT 集群，那需要在 MyCAT Server 所在的服务器上配置对其他 ip 和
主机名的映射，配置方式如下：
vi /etc/hosts
例如：我有 4 台机器，配置如下：
IP 主机名：
192.168.100.2 sam_server_1
192.168.100.3 sam_server_2
192.168.100.4 sam_server_3
192.168.100.5 sam_server_4
编辑完后，保存文件。
经过以上两个步骤的配置，就可以到/usr/local/Mycat/bin 目录下执行：
./mycat start
即可启动 mycat 服务！


Mycat 防火墙配置  

P66

##Mycat 的配置

#### Schema.xml

Schema.xml 作为 MyCat 中重要的配置文件之一，管理着 MyCat 的逻辑库、表、分片规则、DataNode 以
及 DataSource。

###### schema标签

schema 标签用于定义 MyCat 实例中的逻辑库，MyCat 可以有多个逻辑库，每个逻辑库都有自己的相关配
置。可以使用 schema 标签来划分这些不同的逻辑库。
如果不配置 schema 标签，所有的表配置，会属于同一个默认的逻辑库。

可以到server.xml 添加该用户可以访问到的 schema

```xml
<!-- 逻辑数据库 -->
<schema name="TESTDB" checkSQLschema="false" sqlMaxLimit="100">
</schema>

server.xml
<user name="user">
	<property name="password">user</property>
	<property name="schemas">TESTDB</property>
</user>

```

dataNode

该属性用于绑定逻辑库到某个具体的 database上

```xml
<schema name="USERDB" checkSQLschema="false" sqlMaxLimit="100" dataNode="dn2">
<!--配置需要分片的表-->
<table name=“tuser” dataNode=”dn1”/>
</schema>

```

checkSQLschema

当该值设置为 true 时，如果我们执行语句**select * from TESTDB.travelrecord;**则 MyCat 会把语句修改
为**select * from travelrecord;**。即把表示 schema 的字符去掉

sqlMaxLimit

当该值设置为某个数值时。每条执行的 SQL 语句，如果没有加上 limit 语句，MyCat 也会自动的加上所对应
的值。例如设置值为 100，执行**select * from TESTDB.travelrecord;**的效果为和执行**select * from
TESTDB.travelrecord limit 100;**相同。
设置该值的话，MyCat 默认会把查询到的信息全部都展示出来，造成过多的输出。所以，在正常使用中，还
是建议加上一个值，用于减少过多的数据返回。

需要注意的是，如果运行的 schema 为非拆分库的，那么该属性不会生效。需要手动添加 limit 语句。


table 标签

<table name="travelrecord" dataNode="dn1,dn2,dn3" rule="auto-sharding-long" ></table>

Table 标签定义了 MyCat 中的逻辑表，所有需要拆分的表都需要在这个标签中定义。


name : 逻辑表的表名, 同个 schema 标签中定义的名字必须唯一。
dataNode : 定义这个逻辑表所属的 dataNode, 该属性的值需要和 dataNode 标签中 name 属性的值相互对应
rule : 该属性用于指定逻辑表要使用的规则名字，规则名字在 rule.xml 中定义，必须与 tableRule 标签中 name 属
性属性值一一对应。
ruleRequired 属性
primaryKey 属性
type 属性 全局表：global。普通表：不指定该值为 globla 的所有表。
needAddLimit 属性  mycat 就自动的为我们加上 LIMIT 100。当然，如果语句中有 limit，就不会在次添加了。这个属性默认为 true,你也可以设置成 false`禁用掉默认行为。


####  childTable 标签

childTable 标签用于定义 E-R 分片的子表。通过标签上的属性与父表进行关联。


#### dataNode 标签

dataNode 标签定义了 MyCat 中的数据节点，也就是我们通常说所的数据分片。一个 dataNode 标签就是
一个独立的数据分片。

<dataNode name="dn1" dataHost="lch3307" database="db1" ></dataNode>

name : 定义数据节点的名字，这个名字需要是唯一的，我们需要在 table 标签上应用这个名字，来建立表与分片对
应的关系。

dataHost : 该属性用于定义该分片属于哪个数据库实例的，属性值是引用 dataHost 标签上定义的 name 属性

database : 该属性用于定义该分片属性哪个具体数据库实例上的具体库，因为这里使用两个纬度来定义分片，就是：实
例+具体的库。

####  dataHost 标签

直接定义了具体的数据库实例、读写分离配置和心跳语句

maxCon 属性 指定每个读写实例连接池的最大连接。也就是说，标签内嵌套的 writeHost、readHost 标签都会使用这个属
性的值来实例化出连接池的最大连接数。

minCon 属性 指定每个读写实例连接池的最小连接，初始化连接池的大小。

balance 属性 负载均衡类型

writeType 属性 ：负载均衡类型

dbType 属性： 指定后端连接的数据库类型，目前支持二进制的 mysql 协议，还有其他使用 JDBC 连接的数据库

dbDriver 属性：指定连接后端数据库使用的 Driver，目前可选的值有 native 和 JDBC。使用 native 的话，因为这个值执行的
是二进制的 mysql 协议，所以可以使用 mysql 和 maridb。其他类型的数据库则需要使用 JDBC 驱动来支持。

switchType 属性 ;2 基于 MySQL 主从同步的状态决定是否切换心跳语句为 show slave status

####  heartbeat 标签
这个标签内指明用于和后端数据库进行心跳检查的语句。

####  writeHost 标签、readHost 标签

#### writeHost 标签、readHost 标签

这两个标签都指定后端数据库的相关配置给 mycat，用于实例化后端连接池。唯一不同的是，writeHost 指
定写实例、readHost 指定读实例，组着这些读写实例来满足系统的要求。

在一个 dataHost 内可以定义多个 writeHost 和 readHost。但是，如果 writeHost 指定的后端数据库宕机，
那么这个 writeHost 绑定的所有 readHost 都将不可用。另一方面，由于这个 writeHost 宕机系统会自动的检测
到，并切换到备用的 writeHost 上去。

host 属性
用于标识不同实例，一般 writeHost 我们使用*M1，readHost 我们用*S1

url 属性
后端实例连接地址，如果是使用 native 的 dbDriver，则一般为 address:port 这种形式



##### user 标签

privileges 子节点

对用户的 schema 及 下级的 table 进行精细化的 DML 权限控制，privileges 节点中的 check 属性是用
于标识是否开启 DML 权限检查， 默认 false 标识不检查，当然 privileges 节点不配置，等同 check=false,
由于 Mycat 一个用户的 schemas 属性可配置多个 schema ，所以 privileges 的下级节点 schema 节点同样
可配置多个，对多库多表进行细粒度的 DML 权限控制

##### system 标签

这个标签内嵌套的所有 property 标签都与系统配置有关，请注意，下面我会省去标签 property 直接使用这
个标签的 name 属性内的值来介绍这个属性的作用。

##### rule.xml

rule.xml 里面就定义了我们对表进行拆分所涉及到的规则定义。我们可以灵活的对表使用不同的分片算法，
或者对表使用相同的算法但具体的参数不同。

#### tableRule 标签

name 属性指定唯一的名字，用于标识不同的表规则。
内嵌的 rule 标签则指定对物理表中的哪一列进行拆分和使用什么路由算法。
columns 内指定要拆分的列名字。
algorithm 使用 function 标签中的 name 属性。连接表规则和具体路由算法。当然，多个表规则可以连接到
同一个路由算法上。table 标签内使用。让逻辑表使用这个规则进行分片。


##### function 标签
```xml

<function name="hash-int"
class="org.opencloudb.route.function.PartitionByFileMap">
<property name="mapFile">partition-hash-int.txt</property>
</function>

```
name 指定算法的名字。
class 制定路由算法具体的类名字。
property 为具体算法需要用到的一些属性。

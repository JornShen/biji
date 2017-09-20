#### mybatis  


jdbc url 参数

useUnicode	是否使用Unicode字符集，如果参数characterEncoding设置为gb2312或gbk，本参数值必须设置为true

characterEncoding	当useUnicode设置为true时，指定字符编码。比如可设置为gb2312或gbk，utf8

autoReconnect	当数据库连接异常中断时，是否自动重新连接

serverTimezone 覆盖时区的检测/映射。当服务器的时区为映射到Java时区时使用
s
mysql://localhost:3306/test?user=root&amp; password=&amp; useUnicode=true&amp; characterEncoding=gbk
&amp; autoReconnect=true&amp; failOverReadOnly=false


Mapper 相当于dao 操作数据库的接口，只是他会映射到具体的xml中，操作相应的数据

配置

destroy-method="close"的作用是当数据库连接不使用的时候,就把该连接重新放到数据池中,方便下次使用调用.

引入的jar包

```xml

```

数据源

dbcp

spring-jdbc

druid

c3p0

spring-mybatis xml 配置


```xml
<context:property-placeholder location="classpath:jdbc.properties"/>
	<!-- 配置c3p0数据库连接池 -->
	<bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource" >
       <!-- 数据连接信息 -->
	    <property name="jdbcUrl" value="${jdbcUrl}"></property>

```

在classpath中可能需要使用所有相同名字的资源文件，如果用classpath:只会加载第一个，而使用classpath*:前缀则能够加载所有符合类型的文件。然而，使用classpath*:需要遍历所有的classpath，加载速度很慢，因此您应该尽量避免使用classpath*。

Recource Folders表示的都是资源文件目录，这些目录里面的文件会在代码编译运行被直接复制到target->classess文件夹中。可以这么讲，target->classes即为classpath，任何我们需要在classpath前缀中获取的资源都必须在target->classes文件夹中找到，否则将出现java.io.FileNotFoundException的错误信息。

代码的目录结构

用maven构建项目时候**resource**目录就是默认的classpath

File→Project Structure，进入“Project Structure”对话框，点击左侧的**“Modules”**，然后点击中部的“Dependencies”tab，然后再点击右部的“+”号添加你指定的目录，这里要注意了，在弹出的“Attach Files”对话框中，你必须要选择“classes”


cache -  配置给定命名空间的缓存。
resultMap  –  最复杂，也是最有力量的元素，用来描述如何从数据库结果集中来加载你的对象。
insert –  映射插入语句
update –  映射更新语句
delete –  映射删除语句
select –  映射查询语句
动态SQL


#### select 语句

```java

<select id="selectPerson" parameterType="int" resultType="hashmap">
  SELECT * FROM PERSON WHERE ID = #{id}
</select>

```

<select
  id="selectPerson"  // 语句引用的唯一标识
  parameterType="int"  // 传入参数的类型， 通过 TypeHandler 推断出具体传入语句的参数
  resultType="hashmap" // 返回值类型，和 resultMap 二选一，返回的期望类型的类的完全限定名或别名. 集合可以包含的类型，而不能是集合本身
  resultMap="personResultMap" // reference to an external resultMap.
  flushCache="false"  // 清空 缓存 和 二级缓存
  useCache="true"  //  结果二级缓存
  timeout="10000"
  fetchSize="256"  
  statementType="PREPARED"  // STATEMENT，PREPARED 或 CALLABLE 的一。使用 Statement，PreparedStatement 或 CallableStatement
  resultSetType="FORWARD_ONLY"
  databaseId = "" //
  resultSets  = "" // 多结果集
  resultOrdered = "" //
  >

<insert
  id="insertAuthor"
  parameterType="domain.blog.Author"
  flushCache="true"
  statementType="PREPARED"
  keyProperty=""  //  主键名
  keyColumn=""
  useGeneratedKeys=""
  timeout="20">

<update
  id="updateAuthor"
  parameterType="domain.blog.Author"
  flushCache="true"
  statementType="PREPARED"
  timeout="20">

<delete
  id="deleteAuthor"
  parameterType="domain.blog.Author"
  flushCache="true"
  statementType="PREPARED"
  timeout="20">

useGeneratedKeys (自动生成主键)
（仅对 insert 和 update 有用）这会令 MyBatis 使用 JDBC 的 getGeneratedKeys 方法来取出由数据库内部生成的主键（比如：像 MySQL 和 SQL Server 这样的关系数据库管理系统的自动递增字段），默认值：false。

keyProperty	（仅对 insert 和 update 有用）唯一标记一个属性，MyBatis 会通过 getGeneratedKeys 的返回值或者通过 insert 语句的 selectKey 子元素设置它的键值，默认：unset。如果希望得到多个生成的列，也可以是逗号分隔的属性名称列表。

keyColumn	（仅对 insert 和 update 有用）通过生成的键值设置表中的列名，这个设置仅在某些数据库（像 PostgreSQL）是必须的，当主键列不是表中的第一列的时候需要设置。如果希望得到多个生成的列，也可以是逗号分隔的属性名称列表。


主键自增
```xml
<insert id="insertAuthor" useGeneratedKeys="true"
    keyProperty="id">
  insert into Author (username,password,email,bio)
  values (#{username},#{password},#{email},#{bio})
</insert>
```

另一种生成主键的方式
```xml

<insert id="insertAuthor">
  <selectKey keyProperty="id" resultType="int" order="BEFORE">
    select CAST(RANDOM()*1000000 as INTEGER) a from SYSIBM.SYSDUMMY1
  </selectKey>
  insert into Author
    (id, username, password, email,bio, favourite_section)
  values
    (#{id}, #{username}, #{password}, #{email}, #{bio}, #{favouriteSection,jdbcType=VARCHAR})
</insert>

```



多行插入

```xml

<insert id="insertAuthor" useGeneratedKeys="true"
    keyProperty="id">
  insert into Author (username, password, email, bio) values
  <foreach item="item" collection="list" separator=",">
    (#{item.username}, #{item.password}, #{item.email}, #{item.bio})
  </foreach>
</insert>

```

<selectKey
  keyProperty="id"  // 结果应该被设置的目标属性
  resultType="int"
  order="BEFORE"
  statementType="PREPARED">

keyColumn  匹配结果集合的列名称

selectKey 元素将会首先运行

可重用的sql代码段

可以被静态地(在加载参数) 参数化

<sql id="userColumns"> ${alias}.id,${alias}.username,${alias}.password </sql>

<select id="selectUsers" resultType="map">
  select
    <include refid="userColumns"><property name="alias" value="t1"/></include>, // 引用代码段
    <include refid="userColumns"><property name="alias" value="t2"/></include>
  from some_table t1
    cross join some_table t2
</select>

更加复杂的引用

<sql id="sometable">
  ${prefix}Table
</sql>

<sql id="someinclude">
  from
    <include refid="${include_target}"/>
</sql>

<select id="select" resultType="map">
  select
    field1, field2, field3
  <include refid="someinclude">
  	<property name="include_target" value="sometable"/>
    <property name="prefix" value="Some"/>
  </include>
</select>

Parameters

普通单一的参数 命名随意

<select id="selectUsers" resultType="User">
  select id, username, password
  from users
  where id = #{id}
</select>

对象参数  对象的参数会被引入

<insert id="insertUser" parameterType="User">
  insert into users (id, username, password)
  values (#{id}, #{username}, #{password})
</insert>

多个参数传入

@Parm

List<Order> getOrdersByTime(@Param("startTime") Long startTime, @Param("endTime") Long endTime);

<select id="getOrdersByTime" resultMap="BaseResultMap">
        SELECT * FROM `142422_order_info`
        WHERE add_time BETWEEN #{startTime, jdbcType=BIGINT} AND #{endTime, jdbcType=BIGINT}
</select>

本质上会创建一个hashmap 来映射

Dao层的函数方法

采用Map传多参数

Public User selectUser(Map paramMap);

<select id=" selectUser" resultMap="BaseResultMap">
   select  *  from user_user_t where user_name = #{userName，jdbcType=VARCHAR} and user_area=#{userArea, jdbcType=VARCHAR}
</select>




参数也可以指定一个特殊的数据类型。

#{property,javaType=int,jdbcType=NUMERIC}

javaType 通常可以从参数对象中来去确定，前提是只要对象不是一个 HashMap。那么 javaType 应该被确定来保证使用正确类型处理器。

#{age,javaType=int,jdbcType=NUMERIC,typeHandler=MyTypeHandler}

保留两位小数
#{height,javaType=double,jdbcType=NUMERIC,numericScale=2}

#{department, mode=OUT, jdbcType=CURSOR, javaType=ResultSet, resultMap=departmentResultMap}

mode属性：IN, OUT, INOUT

如果 mode 为 OUT（或 INOUT），而且 jdbcType 为 CURSOR(也就是 Oracle 的 REFCURSOR)，你必须指定一个 resultMap 来映射结果集到参数类型。

#{middleInitial, mode=OUT, jdbcType=STRUCT, jdbcTypeName=MY_TYPE, resultMap=departmentResultMap}

为可能为空的列名指定 jdbcType

ORDER BY ${columnName}

不改变相应的值  ORDER BY ${columnName}

MyBatis 不会修改或转义字符串

Result Maps

类型别名


<!-- In mybatis-config.xml file 在配置文件中定义 -->
<typeAlias type="com.someapp.model.User" alias="User"/>

<select id="selectUsers" resultType="User">
  select id, username, hashedPassword
  from some_table
  where id = #{id}
</select>

返回值幕后自动创建一个ResultMap, 基于属性名来映射列到 JavaBean 的属性上。如果列名没有精确匹配,你可以在列名上使用 select 字句的别名(一个 基本的 SQL 特性)来匹配标签

<select id="selectUsers" resultType="User">
  select
    user_id             as "id",
    user_name           as "userName",
    hashed_password     as "hashedPassword"
  from some_table
  where id = #{id}
</select>

解决列名不匹配的另外一种方式

<resultMap id="userResultMap" type="User">
  <id property="id" column="user_id" />
  <result property="username" column="user_name"/>
  <result property="password" column="hashed_password"/>
</resultMap>

constructor

类在实例化时,用来注入结果到构造方法中

<resultMap id="detailedBlogResultMap" type="Blog">
  <constructor> // 构造方法 初始化类
    <idArg column="blog_id" javaType="int"/> // ID 参数;
  </constructor>
  <result property="title" column="blog_title"/> // 注入到字段或 JavaBean 属性的普通结果
  <association property="author" javaType="Author"> // 一个复杂的类型关联;许多结果将包成这种类型
    <id property="id" column="author_id"/>
    <result property="username" column="author_username"/>
    <result property="password" column="author_password"/>
    <result property="email" column="author_email"/>
    <result property="bio" column="author_bio"/>
    <result property="favouriteSection" column="author_favourite_section"/>
  </association>
  <collection property="posts" ofType="Post"> // 复杂类型的集
    <id property="id" column="post_id"/>
    <result property="subject" column="post_subject"/>
    <association property="author" javaType="Author"/>
    <collection property="comments" ofType="Comment">
      <id property="id" column="comment_id"/>
    </collection>
    <collection property="tags" ofType="Tag" >
      <id property="id" column="tag_id"/>
    </collection>
    <discriminator javaType="int" column="draft">
      <case value="1" resultType="DraftPost"/>
    </discriminator>
  </collection>
</resultMap>

property 注入bean的类的属性

column 从select 获得 列的名称




idArg - ID 参数;标记结果作为 ID 可以帮助提高整体效能
arg - 注入到构造方法的一个普通结果


#### resultMap

id 标志该 Map

type 类的全限定名, 或者一个类型别名

autoMapping 如果设置这个属性，MyBatis将会为这个ResultMap开启或者关闭自动映射。

<id property="id" column="post_id"/>
<result property="subject" column="post_subject"/>

####  id  result

映射一个单独列的值到简单数据类型。

id 表示的结果将是当比较对象实例时用到的标识属性

属性

property 映射到列结果的字段或属性

column 从数据库中得到的列名,或者是列名的重命名标签

javaType 一个 Java 类的完全限定名

JDBC 类型是仅 仅需要对插入,更新和删除操作可能为空的列进行处理。这是 JDBC jdbcType 的需要,而不是 MyBatis 的。

typeHandler 类型处理器

jdbcType

BIT	FLOAT	CHAR	TIMESTAMP	OTHER	UNDEFINED
TINYINT	REAL	VARCHAR	BINARY	BLOB	NVARCHAR
SMALLINT	DOUBLE	LONGVARCHAR	VARBINARY	CLOB	NCHAR
INTEGER	NUMERIC	DATE	LONGVARBINARY	BOOLEAN	NCLOB
BIGINT	DECIMAL	TIME	NULL	CURSOR	ARRAY


<constructor>
   <idArg column="id" javaType="int" name="id" />
   <arg column="age" javaType="_int" name="age" />
   <arg column="username" javaType="String" name="username" />
</constructor>

name 指定 构造器的属性

```java

public User(Integer id, String username, int age) {
     //...
}

```

#### association

<association property="author" javaType="Author">
  <id property="id" column="author_id"/>
  <result property="username" column="author_username"/>
</association>

解决关联对象 has one 的关系

关联对象的两种方式

Nested Select: By executing another mapped SQL statement that returns the complex type desired.

Nested Results: By using nested result mappings to deal with repeating subsets of joined results.

属性
property  映射到列结果的字段或属性

javaType 一个 Java 类的完全限定名,或一个类型别名

jdbcType	在这个表格之前的所支持的 JDBC 类型列表中的类型。插入, 更新和删除操作可能为空的列进行处理。

Nested Select for Association

column The column name from the database, or the aliased column label that holds the value that will **be passed to** the nested statement as an input parameter.

column="{prop1=col1,prop2=col2}" 将多个参数对应的传递给select 方法

select select 语句的ID，参数将从column属性传递过去。

fetchType	可选的。有效值为 lazy和eager。 如果使用了，它将取代全局配置参数lazyLoadingEnabled
懒加载

```xml
<resultMap id="blogResult" type="Blog">
  <association property="author" column="author_id" javaType="Author" select="selectAuthor"/>
</resultMap>

<select id="selectBlog" resultMap="blogResult">
  SELECT * FROM BLOG WHERE ID = #{id}
</select>

<select id="selectAuthor" resultType="Author">
  SELECT * FROM AUTHOR WHERE ID = #{id}
</select>

```

N+1 查询问题

执行了一个单独的 SQL 语句来获取结果列表(就是“+1”)。

对返回的每条记录,你执行了一个查询语句来为每个加载细节(就是“N”)。

这个问题会导致成百上千的 SQL 语句被执行。这通常不是期望的。

MyBatis 能延迟加载这样的查询就是一个好处,因此你可以分散这些语句同时运行的消 耗。然而,如果你加载一个列表,之后迅速迭代来访问嵌套的数据,你会调用所有的延迟加 载,这样的行为可能是很糟糕的。

Nested Results for Association

<association property="author" resultMap="authorResult" />

```xml


<resultMap id="blogResult" type="Blog">
  <id property="id" column="blog_id" />
  <result property="title" column="blog_title"/>
  <association property="author" resultMap="authorResult" />
</resultMap>

<resultMap id="authorResult" type="Author">
  <id property="id" column="author_id"/>
  <result property="username" column="author_username"/>
  <result property="password" column="author_password"/>
  <result property="email" column="author_email"/>
  <result property="bio" column="author_bio"/>
</resultMap>

```

resultMap   ID of a ResultMap that can map the nested results of this association into an appropriate object graph.

columnPrefix columnPrefix allows you to map such columns to an external resultMap

notNullColumn 默认情况下，子对象仅在至少一个列映射到其属性非空时才创建。 通过对这个属性指定非空的列将改变默认行为，这样做之后Mybatis将仅在这些列非空时才创建一个子对象。

autoMapping	如果使用了，当映射结果到当前属性时，Mybatis将启用或者禁用自动映射。 该属性覆盖全局的自动映射行为。

```xml

<resultMap id="blogResult" type="Blog">
  <id property="id" column="blog_id" />
  <result property="title" column="blog_title"/>
  <association property="author" column="blog_author_id" javaType="Author" resultMap="authorResult"/>
</resultMap>

<resultMap id="authorResult" type="Author">
  <id property="id" column="author_id"/>
  <result property="username" column="author_username"/>
  <result property="password" column="author_password"/>
  <result property="email" column="author_email"/>
  <result property="bio" column="author_bio"/>
</resultMap>

```

不需要重用它的话,或者你仅仅引用你所有的结果映射合到一个单独描述的结果映射中

```xml
<resultMap id="blogResult" type="Blog">
  <id property="id" column="blog_id" />
  <result property="title" column="blog_title"/>
  <association property="author" javaType="Author">
    <id property="id" column="author_id"/>
    <result property="username" column="author_username"/>
    <result property="password" column="author_password"/>
    <result property="email" column="author_email"/>
    <result property="bio" column="author_bio"/>
  </association>
</resultMap>

```

<select id="selectBlog" resultMap="blogResult">
  select
    B.id            as blog_id,
    B.title         as blog_title,
    A.id            as author_id,
    A.username      as author_username,
    A.password      as author_password,
    A.email         as author_email,
    A.bio           as author_bio,
    CA.id           as co_author_id,
    CA.username     as co_author_username,
    CA.password     as co_author_password,
    CA.email        as co_author_email,
    CA.bio          as co_author_bio
  from Blog B
  left outer join Author A on B.author_id = A.id
  left outer join Author CA on B.co_author_id = CA.id
  where B.id = #{id}
</select>

引用多个相同的类

<resultMap id="authorResult" type="Author">
  <id property="id" column="author_id"/>
  <result property="username" column="author_username"/>
  <result property="password" column="author_password"/>
  <result property="email" column="author_email"/>
  <result property="bio" column="author_bio"/>
</resultMap>


<resultMap id="blogResult" type="Blog">
  <id property="id" column="blog_id" />
  <result property="title" column="blog_title"/>
  <association property="author"
    resultMap="authorResult" />
  <association property="coAuthor"
    resultMap="authorResult"
    columnPrefix="co_" /> // 指定列的前缀
</resultMap>



has many

collection

<collection property="posts" ofType="domain.blog.Post">
  <id property="id" column="post_id"/>
  <result property="subject" column="post_subject"/>
  <result property="body" column="post_body"/>
</collection>

private List<Post> posts;

----------

###### Nested Select for Collection

<resultMap id="blogResult" type="Blog">
  <collection property="posts" javaType="ArrayList" column="id" ofType="Post" select="selectPostsForBlog"/>
</resultMap>

<select id="selectBlog" resultMap="blogResult">
  SELECT * FROM BLOG WHERE ID = #{id}
</select>

<select id="selectPostsForBlog" resultType="Post">
  SELECT * FROM POST WHERE BLOG_ID = #{id}
</select>

javaType 一般课余省略掉

<collection property="posts" column="id" ofType="Post" select="selectPostsForBlog"/>

ofType 类型，list 泛型的类型


###### Nested Results for Collection

<select id="selectBlog" resultMap="blogResult">
  select
  B.id as blog_id,
  B.title as blog_title,
  B.author_id as blog_author_id,
  P.id as post_id,
  P.subject as post_subject,
  P.body as post_body,
  from Blog B
  left outer join Post P on B.id = P.blog_id
  where B.id = #{id}
</select>

<resultMap id="blogResult" type="Blog">
  <id property="id" column="blog_id" />
  <result property="title" column="blog_title"/>
  <collection property="posts" ofType="Post">
    <id property="id" column="post_id"/>
    <result property="subject" column="post_subject"/>
    <result property="body" column="post_body"/>
  </collection>
</resultMap>

重用

<resultMap id="blogResult" type="Blog">
  <id property="id" column="blog_id" />
  <result property="title" column="blog_title"/>
  <collection property="posts" ofType="Post" resultMap="blogPostResult" columnPrefix="post_"/>
</resultMap>

<resultMap id="blogPostResult" type="Post">
  <id property="id" column="id"/>
  <result property="subject" column="subject"/>
  <result property="body" column="body"/>
</resultMap>

discriminator

有时一个单独的数据库查询也许返回很多不同 (但是希望有些关联) 数据类型的结果集。 鉴别器元素就是被设计来处理这个情况的, 还有包括类的继承层次结构。 鉴别器非常容易理 解,因为它的表现很像 Java 语言中的 switch 语句。

条件映射

<resultMap id="vehicleResult" type="Vehicle">
  <id property="id" column="id" />
  <result property="vin" column="vin"/>
  <result property="year" column="year"/>
  <result property="make" column="make"/>
  <result property="model" column="model"/>
  <result property="color" column="color"/>
  <discriminator javaType="int" column="vehicle_type">
    <case value="1" resultMap="carResult"/>
    <case value="2" resultMap="truckResult"/>
    <case value="3" resultMap="vanResult"/>
    <case value="4" resultMap="suvResult"/>
  </discriminator>
</resultMap>

<resultMap id="carResult" type="Car" extends="vehicleResult">
  <result property="doorCount" column="door_count" />
</resultMap>


<resultMap id="vehicleResult" type="Vehicle">
  <id property="id" column="id" />
  <result property="vin" column="vin"/>
  <result property="year" column="year"/>
  <result property="make" column="make"/>
  <result property="model" column="model"/>
  <result property="color" column="color"/>
  <discriminator javaType="int" column="vehicle_type">
    <case value="1" resultType="carResult">
      <result property="doorCount" column="door_count" />
    </case>
    <case value="2" resultType="truckResult">
      <result property="boxSize" column="box_size" />
      <result property="extendedCab" column="extended_cab" />
    </case>
    <case value="3" resultType="vanResult">
      <result property="powerSlidingDoor" column="power_sliding_door" />
    </case>
    <case value="4" resultType="suvResult">
      <result property="allWheelDrive" column="all_wheel_drive" />
    </case>
  </discriminator>
</resultMap>


有三种自动映射等级：

NONE - 禁用自动映射。仅设置手动映射属性。
PARTIAL - 将自动映射结果除了那些有内部定义内嵌结果映射的(joins).（默认）
FULL - 自动映射所有。

cache

查询缓存特性

映射语句文件中的所有 **select** 语句将会被缓存。

映射语句文件中的所有 **insert,update 和 delete 语句会刷新缓存。**

缓存会使用 **Least Recently Used(LRU,最近最少使用的)算法**来收回。

根据时间表(比如 no Flush Interval,没有刷新间隔), 缓存不会以任何时间顺序 来刷新。

缓存会存储列表集合或对象(无论查询方法返回什么)的 1024 个引用。

缓存会被视为是 read/write(可读/可写)的缓存,意味着对象检索不是共享的,而 且可以安全地被调用者修改,而不干扰其他调用者或线程所做的潜在修改。

<cache
  eviction="FIFO"
  flushInterval="60000"
  size="512"
  readOnly="true"/>

eviction

收回策略有:

LRU – 最近最少使用的:移除最长时间不被使用的对象。
FIFO – 先进先出:按对象进入缓存的顺序来移除它们。
SOFT – 软引用:移除基于垃圾回收器状态和软引用规则的对象。
WEAK – 弱引用:更积极地移除基于垃圾收集器状态和弱引用规则的对象。
默认的是 LRU

readOnly(只读)属性可以被设置为 true 或 false。只读的缓存会给所有调用者返回缓 存对象的相同实例。因此这些对象不能被修改。这提供了很重要的性能优势。可读写的缓存 会返回缓存对象的拷贝(通过序列化) 。这会慢一些,但是安全,因此默认是 false。


动态sql

if
choose (when, otherwise)
trim (where, set)
foreach


```xml
<select id="findActiveBlogLike"
     resultType="Blog">
  SELECT * FROM BLOG WHERE state = ‘ACTIVE’
  <if test="title != null">
    AND title like #{title}
  </if>
  <if test="author != null and author.name != null">
    AND author_name like #{author.name}
  </if>
</select>
```

choose, when, otherwise

<choose>
    <when test="title != null">
      AND title like #{title}
    </when>
    <when test="author != null and author.name != null">
      AND author_name like #{author.name}
    </when>
    <otherwise>
      AND featured = 1
    </otherwise>
</choose>

trim, where, set


<select id="findActiveBlogLike"
     resultType="Blog">
  SELECT * FROM BLOG
  <where>
    <if test="state != null">
         state = #{state}
    </if>
    <if test="title != null">
        AND title like #{title}
    </if>
    <if test="author != null and author.name != null">
        AND author_name like #{author.name}
    </if>
  </where>
</select>

where 元素知道只有在一个以上的if条件有值的情况下才去插入“WHERE”子句。
若最后的内容是“AND”或“OR”开头的，where 元素也知道如何将他们去除。

等价表达式

<trim prefix="WHERE" prefixOverrides="AND |OR ">
  ...
</trim>


<update id="updateAuthorIfNecessary">
  update Author
    <set>
      <if test="username != null">username=#{username},</if>
      <if test="password != null">password=#{password},</if>
      <if test="email != null">email=#{email},</if>
      <if test="bio != null">bio=#{bio}</if>
    </set>
  where id=#{id}
</update>


<trim prefix="SET" suffixOverrides=",">
  ...
</trim>


foreach

需要对一个集合进行遍历，通常是在构建 IN 条件语句的时候。

<select id="selectPostIn" resultType="domain.blog.Post">
  SELECT *
  FROM POST P
  WHERE ID in
  <foreach item="item" index="index" collection="list"
      open="(" separator="," close=")">
        #{item}
  </foreach>
</select>

你可以将任何可迭代对象（如列表、集合等）和任何的字典或者数组对象传递给foreach作为集合参数。

index是当前迭代的次数，item的值是本次迭代获取的元素。当使用字典（或者Map.Entry对象的集合）时，index是键，item是值。

bind 元素可以从 OGNL 表达式中创建一个变量并将其绑定到上下文。

_databaseId”变量的 databaseIdProvider 对于动态代码来说是可用的。


mybatis-generator

for maven

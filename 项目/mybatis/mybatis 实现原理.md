##### mybatis 实现原理及其架构设计


SqlSessionFactoryBuilder：每一个Mybatis的应用程序的入口是SqlSessionFactoryBuilder，它的作用是**通过XML配置文件创建Configuration对象（当然也可以在程序中自行创建），然后通过build方法创建SqlSessionFactory对象**。没有必要每次访问Mybatis就创建一次SqlSessionFactoryBuilder，通常的做法是**创建一个全局的对象**就可以了。

SqlSessionFactory：SqlSessionFactory对象由SqlSessionFactoryBuilder创建。它的主要功能是**创建SqlSession对象** (全局变量)通常的做法是创建一个全局的对象就可以了。SqlSessionFactory对象一个必要的属性是Configuration对象,它是保存Mybatis全局配置的一个配置对象，通常由SqlSessionFactoryBuilder从XML配置文件创建。

SqlSession：SqlSession对象的主要功能是 **完成一次数据库的访问和结果的映射**，它类似于数据库的session概念，由于 **不是线程安全** 的，所以SqlSession对象的 **作用域需限制在方法内**。SqlSession的默认实现类是DefaultSqlSession，它有两个必须配置的属性：**Configuration和Executor** 。

Configuration即是我们的mybatis-config.xml文件, SqlSession对数据库的操作都是**通过Executor来完成的。**

SqlSession的getMapper方法是联系应用程序和Mybatis纽带，应用程序访问getMapper时，Mybatis会根据传入的接口类型和对应的XML配置文件生成一个代理对象，这个代理对象就叫Mapper对象。**应用程序获得Mapper对象后，就应该通过这个Mapper对象来访问Mybatis的SqlSession对象**，这样就达到里插入到Mybatis流程的目的。通过动态代理，将处理代码织入来创建。

StatementHandler访问数据库，并将查询结果存入缓存中（如果配置了缓存的话）。

StatementHandler：是 **真正访问数据库的地方**，并调用ResultSetHandler处理查询结果。

---

ORM 框架 对象关系映射 Object Relational Mapping

缓存机制， 事务机制

MyBatis和数据库的交互有两种方式：

a.使用传统的MyBatis提供的API；

b. 使用Mapper接口

根据MyBatis 的配置规范配置好后，通过 ** SqlSession.getMapper(XXXMapper.class) **方法，MyBatis 会根据相应的接口声明的方法信息，通过 **动态代理机制** 生成一个Mapper 实例

MyBatis 会根据这个方法的方法名和参数类型，确定Statement Id，底层还是通过SqlSession.select("statementId",parameterObject);或者SqlSession.update("statementId",parameterObject); 等等来实现对数据库的操作.

数据处理层可以说是MyBatis 的核心:

a. 通过传入参数 **构建动态SQL语句**
b. SQL语句的执行
c. 将查询执行结果封装集成List<E>

参数映射指的是对于java 数据类型和jdbc数据类型之间的转换：这里有包括两个过程：查询阶段，我们要将java类型的数据，转换成jdbc类型的数据，通过 preparedStatement.setXXX() 来设值；另一个就是对resultset查询结果集的jdbcType 数据转换成java 数据类型


MyBatis的主要的核心部件

SqlSession    作为MyBatis工作的主要顶层API，表示和数据库交互的会话，完成必要数据库增删改查功能
    |
		V
Executor     MyBatis执行器，是MyBatis 调度的核心，负责SQL语句的生成和查询缓存的维护
    |
		V
StatementHandler     封装了**JDBC Statement操作**，负责对JDBC statement 的操作，如设置参数、将Statement结果集转换成List集合。

ParameterHandler   负责对用户传递的**参数**转换成JDBC Statement 所需要的参数，

ResultSetHandler   负责将JDBC返回的**ResultSet结果集**对象转换成 **List类型的集合**

TypeHandler        负责java数据类型和jdbc数据类型之间的映射和转换

MappedStatement   MappedStatement维护了**一条<select|update|delete|insert>节点** (mapper中的一个语句的封装)的封装，
				  相当于将 xml 中的单挑语句进行抽象封装。

SqlSource         负责根据用户传递的parameterObject，动态地生成SQL语句，将信息封装到BoundSql对象中，并返回

BoundSql          表示动态生成的SQL语句以及相应的参数信息

非常的重要的一个类  Configuration     MyBatis所有的配置信息都维持在Configuration对象之中。

![mybatis执行架构](../pic/y1.jpg)
![mybatis执行架构](../pic/y2.jpg)

```java
    InputStream inputStream = Resources.getResourceAsStream("mybatisConfig.xml");  
    SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();  
    SqlSessionFactory factory = builder.build(inputStream);  

    //2. 从SqlSession工厂 SqlSessionFactory中创建一个SqlSession，进行数据库操作  
    SqlSession sqlSession = factory.openSession();  

    //3.使用SqlSession查询  
    Map<String,Object> params = new HashMap<String,Object>();

    params.put("min_salary",10000);  
    //a.查询工资低于10000的员工  
    List<Employee> result = sqlSession.selectList("com.louis.mybatis.dao.EmployeesMapper.selectByMinSalary",params);  
    //b.未传最低工资，查所有员工  
    List<Employee> result1 = sqlSession.selectList("com.louis.mybatis.dao.EmployeesMapper.selectByMinSalary");  
    System.out.println("薪资低于10000的员工数："+result.size());  
    //~output :   查询到的数据总数：5    
    System.out.println("所有员工数: "+result1.size());
```

SqlSession 的工作过程

1.开启一个数据库访问会话---创建SqlSession对象

MyBatis封装了对数据库的访问，把对数据库的会话和事务控制放到了SqlSession对象中。

2.为SqlSession传递一个配置的Sql语句 的Statement Id和参数，然后返回结果：

加载到内存中会生成一个对应的MappedStatement对象，然后会以key="com.louis.mybatis.dao.EmployeesMapper.selectByMinSalary"，value为MappedStatement对象的形式维护到Configuration的一个Map中。当以后需要使用的时候，只需要通过Id值来获取就可以了。


sqlSession的作用（委托执行）：
SqlSession根据Statement ID, 在mybatis配置对象Configuration中获取到对应的MappedStatement对象，然后调用mybatis执行器来执行具体的操作。

Executor.query()方法几经转折，最后会创建一个StatementHandler对象，然后将必要的参数传递给StatementHandler，使用StatementHandler来完成对数据库的查询，最终返回List结果集。

Executor的功能和作用是：

(1、根据传递的参数，完成**SQL语句的动态解析，生成BoundSql对象，供StatementHandler使用**；

(2、为查询创建缓存，以提高性能

(3、创建JDBC的Statement连接对象，传递给StatementHandler对象，返回List查询结果。

StatementHandler对象负责设置**Statement对象中的查询参数、处理JDBC返回的resultSet，将(查询语句)resultSet加工为List 集合返回

StatementHandler对象主要完成两个工作：

(1. 对于JDBC的PreparedStatement类型的对象，创建的过程中，我们使用的是SQL语句字符串会包含 若干个? 占位符，我们其后再对占位符进行设值。

StatementHandler通过parameterize(statement)方法对Statement进行设值；       


(2.StatementHandler通过List<E> query(Statement statement, ResultHandler resultHandler)方法来完成执行Statement，和将Statement对象返回的resultSet封装成List；

##### Mybatis初始化机制详解

MyBatis初始化的过程，就是创建 Configuration对象的过程。

![](../pic/y3.jpg)

SqlSessionFactoryBuilder：SqlSessionFactory的构造器，用于创建SqlSessionFactory，采用了Builder设计模式

Configuration ：该对象是mybatis-config.xml文件中所有mybatis配置信息

SqlSessionFactory：SqlSession工厂类，以工厂形式创建SqlSession对象，采用了Factory工厂设计模式

1. 调用SqlSessionFactoryBuilder对象的build(inputStream)方法；

2. SqlSessionFactoryBuilder会根据输入流inputStream等信息创建XMLConfigBuilder对象;

3. SqlSessionFactoryBuilder调用XMLConfigBuilder对象的**parse()**方法；

4. XMLConfigBuilder对象返回 **Configuration对象** .

5. SqlSessionFactoryBuilder根据Configuration对象创建一个 **DefaultSessionFactory** 对象；

6. SqlSessionFactoryBuilder返回 DefaultSessionFactory对象给Client，供Client使用

类的重要属性和方法，以及类的之间的关系

```java
产生 SqlSessionFactory 的工厂类
public class SqlSessionFactoryBuilder {
	  从内部可以看到清一色的build的方法，带上不同的参数，参数有Reader， 和InputStream， 说明配置的xml来源（读取的方式的不同）
	 以下是一个核心的构造类
	 public SqlSessionFactory build(InputStream inputStream, String environment, Properties properties) {
	 try {

		 XMLConfigBuilder parser = new XMLConfigBuilder(inputStream, environment, properties);  XML解析器
		 return build(parser.parse()); 借助parse方法抽象成Configuration
	 } catch (Exception e) {
		 throw ExceptionFactory.wrapException("Error building SqlSession.", e);
	 } finally {
		 ErrorContext.instance().reset();
		 try {
			 inputStream.close();
		 } catch (IOException e) {
			 // Intentionally ignore. Prefer previous error.
		 }
	 }
 }

 我们看到创建了一个DefaultSqlSessionFactory, 传入了配置信息
 public SqlSessionFactory build(Configuration config) {
    return new DefaultSqlSessionFactory(config);
 }

}

public enum ExecutorType {
  SIMPLE, REUSE, BATCH
	执行器的三种类型，对应三个类， SimpleExecutor（默认的类型）, ReuseExecutor, BatchExecutor.
	Executor的几种类型
	BaseExcutor分为4, BatchExecutor专门用于执行 批量sql操作 ，，ClosedExecutor用于关闭Executor, ReuseExecutor会 重用statement执行sql操作 ，SimpleExecutor只是简单执行sql没有什么特别的。
	开启cache的话（默认是开启的并且没有任何理由去关闭它），就会创建CachingExecutor，它以前面创建的Executor作为唯一参数。CachingExecutor在查询数据库前先查找缓存，若没找到的话调用delegate（就是构造时传入的Executor对象）从数据库查询，并将查询结果存入缓存中。
}

Creates an {@link SqlSession} out of a connection or a DataSource

public interface SqlSessionFactory {

  SqlSession openSession(...); 创建sqlsession，数据来自connection和DataSource

  Configuration getConfiguration();

}

创建SqlSession的工厂类
public class DefaultSqlSessionFactory implements SqlSessionFactory {
		 private final Configuration configuration; 存放配置,从这里可以获得数据的来源信息

		 这个类有两个重要的方法， openSessionFromDataSource 和  openSessionFromConnection， 对应不同的数据来源
		private SqlSession openSessionFromDataSource(ExecutorType execType, TransactionIsolationLevel level, boolean autoCommit) {
		    Transaction tx = null;
		    try {
		      final Environment environment = configuration.getEnvironment(); 存放数据源和事务工厂
		      final TransactionFactory transactionFactory = getTransactionFactoryFromEnvironment(environment);
					// final Transaction tx = transactionFactory.newTransaction(connection);  openSessionFromConnection 就差这一句话
		      tx = transactionFactory.newTransaction(environment.getDataSource(), level, autoCommit);
		      final Executor executor = configuration.newExecutor(tx, execType);  创建执行器，根据 execType 创建不同的类
		      return new DefaultSqlSession(configuration, executor, autoCommit);  传入配置，和执行器构造DefaultSqlSession
		    } catch (Exception e) {
		      closeTransaction(tx); // may have fetched a connection so lets call close()
		      throw ExceptionFactory.wrapException("Error opening session.  Cause: " + e, e);
		    } finally {
		      ErrorContext.instance().reset();
		    }
     }
}


SqlSession 的一个实现类，处理一次数据库连接操作
public class DefaultSqlSession implements SqlSession  {
		private Configuration configuration; 配置，存放所有映射相关的语句（statement），
	  private Executor executor;  执行器,所有相关的操作都委托给它执行

		// 构造一定要传入的参数
		public DefaultSqlSession(Configuration configuration, Executor executor, boolean autoCommit) {
	    ...
	  }
	  public DefaultSqlSession(Configuration configuration, Executor executor) {
	  }
		增删改查的代码， 其中查找的接口比较多
		注意到删除是用更新来实现的。
		@Override
	  public int delete(String statement) {
	    return update(statement, null);
	  }

		@Override
	  public int update(String statement, Object parameter) {
	    try {
	      dirty = true;
	      MappedStatement ms = configuration.getMappedStatement(statement); 根据id string 来获得mappedstatement，维护了节点如select等节点
	      return executor.update(ms, wrapCollection(parameter)); 借助executor来执行
	    } catch (Exception e) {
	      throw ExceptionFactory.wrapException("Error updating database.  Cause: " + e, e);
	    } finally {
	      ErrorContext.instance().reset();
	    }
	  }

		 @Override
		 public <E> List<E> selectList(String statement, Object parameter, RowBounds rowBounds) {
			 try {
				 一样的套路，先获取MappedStatemnt，再交给executor执行
				 MappedStatement ms = configuration.getMappedStatement(statement);
				 return executor.query(ms, wrapCollection(parameter), rowBounds, Executor.NO_RESULT_HANDLER);
			 } catch (Exception e) {
				 throw ExceptionFactory.wrapException("Error querying database.  Cause: " + e, e);
			 } finally {
				 ErrorContext.instance().reset();
			 }
		 }
		 @Override
	   public <T> T getMapper(Class<T> type) {
	     return configuration.<T>getMapper(type, this);  动态代理创建Mapper对象
	   }
}

执行器里面一个很重要的抽象类
public abstract class BaseExecutor implements Executor {

	  protected Transaction transaction;
	  protected Executor wrapper;
	  protected ConcurrentLinkedQueue<DeferredLoad> deferredLoads;
	  protected PerpetualCache localCache;
	  protected PerpetualCache localOutputParameterCache;
	  protected Configuration configuration;
	  protected int queryStack;
	  private boolean closed;

	  @Override
	  public int update(MappedStatement ms, Object parameter) throws SQLException {
	    ErrorContext.instance().resource(ms.getResource()).activity("executing an update").object(ms.getId());
	    if (closed) {
	      throw new ExecutorException("Executor was closed.");
	    }
	    clearLocalCache();
	    return doUpdate(ms, parameter); 交给子类来完成的，如下文的SimpleExecutor
	  }

		@Override
	  public <E> List<E> query(MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler) throws SQLException {
	    BoundSql boundSql = ms.getBoundSql(parameter);
	    CacheKey key = createCacheKey(ms, parameter, rowBounds, boundSql);
	    return query(ms, parameter, rowBounds, resultHandler, key, boundSql);
	  }

	  @SuppressWarnings("unchecked")
	  @Override
	  public <E> List<E> query(MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, CacheKey key, BoundSql boundSql) throws SQLException {
	    ErrorContext.instance().resource(ms.getResource()).activity("executing a query").object(ms.getId());
	    if (closed) {
	      throw new ExecutorException("Executor was closed.");
	    }
	    if (queryStack == 0 && ms.isFlushCacheRequired()) {
	      clearLocalCache();
	    }
	    List<E> list;
	    try {
	      queryStack++;
	      list = resultHandler == null ? (List<E>) localCache.getObject(key) : null;
	      if (list != null) {
	        handleLocallyCachedOutputParameters(ms, key, parameter, boundSql);
	      } else {
	        list = queryFromDatabase(ms, parameter, rowBounds, resultHandler, key, boundSql); 调用这个获取结果
	      }
	    } finally {
	      queryStack--;
	    }
	    if (queryStack == 0) {
	      for (DeferredLoad deferredLoad : deferredLoads) {
	        deferredLoad.load();
	      }
	      // issue #601
	      deferredLoads.clear();
	      if (configuration.getLocalCacheScope() == LocalCacheScope.STATEMENT) {
	        // issue #482
	        clearLocalCache();
	      }
	    }
	    return list;
	  }

		private <E> List<E> queryFromDatabase(MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, CacheKey key, BoundSql boundSql) throws SQLException {
					List<E> list;
					localCache.putObject(key, EXECUTION_PLACEHOLDER);
					try {
						list = doQuery(ms, parameter, rowBounds, resultHandler, boundSql); 调用子类的doQuery方法获取结果
					} finally {
						localCache.removeObject(key);
					}
					localCache.putObject(key, list);
					if (ms.getStatementType() == StatementType.CALLABLE) {
						localOutputParameterCache.putObject(key, parameter);
					}
					return list;
     }

}


public class CachingExecutor implements Executor {

  	private Executor delegate;

		@Override
	  public int update(MappedStatement ms, Object parameterObject) throws SQLException {
	    flushCacheIfRequired(ms);
	    return delegate.update(ms, parameterObject); 交给其他的执行器来执行
	  }

		@Override
	  public <E> List<E> query(MappedStatement ms, Object parameterObject, RowBounds rowBounds, ResultHandler resultHandler) throws SQLException {
	    BoundSql boundSql = ms.getBoundSql(parameterObject);
	    CacheKey key = createCacheKey(ms, parameterObject, rowBounds, boundSql);
	    return query(ms, parameterObject, rowBounds, resultHandler, key, boundSql);
	  }

     查询功能，是先从cache中获取，取不到时再执行查询方法，并将结果保存到cache中。
		@Override  
	   public <E> List<E> query(MappedStatement ms, Object parameterObject, RowBounds rowBounds, ResultHandler resultHandler, CacheKey key, BoundSql boundSql)  
	       throws SQLException {  
	     Cache cache = ms.getCache();  
	     if (cache != null) {  
	       flushCacheIfRequired(ms);  
	       if (ms.isUseCache() && resultHandler == null) {  
	         ensureNoOutParams(ms, parameterObject, boundSql);  
	         @SuppressWarnings("unchecked")  
	         List<E> list = (List<E>) tcm.getObject(cache, key);   
	         if (list == null) {  
	           list = delegate.<E> query(ms, parameterObject, rowBounds, resultHandler, key, boundSql);   交给其他的executor来执行
	           tcm.putObject(cache, key, list); // issue #578 and #116  
	         }  
	         return list;  
	       }  
	     }  
	     return delegate.<E> query(ms, parameterObject, rowBounds, resultHandler, key, boundSql);  
	   }  
}

public class SimpleExecutor extends BaseExecutor {
		  @Override
		  public int doUpdate(MappedStatement ms, Object parameter) throws SQLException {
			    Statement stmt = null;
			    try {
			      Configuration configuration = ms.getConfiguration();
			      StatementHandler handler = configuration.newStatementHandler(this, ms, parameter, RowBounds.DEFAULT, null, null); 从configuration创建RoutingStatementHandler
			      stmt = prepareStatement(handler, ms.getStatementLog());
			      return handler.update(stmt); 真正执行是在StatementHandler里面执行的
			    } finally {
			      closeStatement(stmt);
			    }
		  }

			private Statement prepareStatement(StatementHandler handler, Log statementLog) throws SQLException {
					Statement stmt;
					Connection connection = getConnection(statementLog);
					stmt = handler.prepare(connection, transaction.getTimeout());
					handler.parameterize(stmt); 调用了StatementHandler中的parameterHandler （DefaultParameterHandler）	setParameters方法进行操作，
					return stmt;
			}

			查找时调用
			@Override
			public <E> List<E> doQuery(MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) throws SQLException {
						Statement stmt = null;
						try {
							Configuration configuration = ms.getConfiguration();
							StatementHandler handler = configuration.newStatementHandler(wrapper, ms, parameter, rowBounds, resultHandler, boundSql);
							stmt = prepareStatement(handler, ms.getStatementLog());
							return handler.<E>query(stmt, resultHandler);
						} finally {
							closeStatement(stmt);
						}
			}

}


工厂模式 + 适配器模式
public class RoutingStatementHandler implements StatementHandler {
  	private final StatementHandler delegate;    封装内部使用的StatementHandler

		根据不同的StatementType来创建不同的StatementHandler
		public RoutingStatementHandler(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) {
		    switch (ms.getStatementType()) {
		      case STATEMENT:
		        delegate = new SimpleStatementHandler(executor, ms, parameter, rowBounds, resultHandler, boundSql);
		        break;
		      case PREPARED:
		        delegate = new PreparedStatementHandler(executor, ms, parameter, rowBounds, resultHandler, boundSql);
		        break;
		      case CALLABLE:
		        delegate = new CallableStatementHandler(executor, ms, parameter, rowBounds, resultHandler, boundSql);
		        break;
		      default:
		        throw new ExecutorException("Unknown statement type: " + ms.getStatementType());
		    }
				可选的StatementHandler有SimpleStatementHandler、PreparedStatementHandler 和 CallableStatementHandler 三种。选用哪种在mapper配置文件的每个statement里指定，默认的是PreparedStatementHandler。这三个类都是从BaseStatementHandler抽象类继承而来。
		  }

			 @Override
		   public Statement prepare(Connection connection, Integer transactionTimeout) throws SQLException {
		     	return delegate.prepare(connection, transactionTimeout); 代理内部的其他的executor，跟踪发现在BaseStatementHandler中执行
		   }

			 委托给内部的StatementHandler进行执行
			 @Override
				public <E> List<E> query(Statement statement, ResultHandler resultHandler) throws SQLException {
					return delegate.<E>query(statement, resultHandler);
				}

}

public abstract class BaseStatementHandler implements StatementHandler {

	protected final ResultSetHandler resultSetHandler;

  protected final ParameterHandler parameterHandler;

	protected BaseStatementHandler(Executor executor, MappedStatement mappedStatement, Object parameterObject, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) {
			this.configuration = mappedStatement.getConfiguration();
			this.executor = executor;
			this.mappedStatement = mappedStatement;
			this.rowBounds = rowBounds;

			this.typeHandlerRegistry = configuration.getTypeHandlerRegistry();
			this.objectFactory = configuration.getObjectFactory();

			if (boundSql == null) { // issue #435, get the key before calculating the statement
					generateKeys(parameterObject);
					boundSql = mappedStatement.getBoundSql(parameterObject);
			}

			this.boundSql = boundSql;

			从configuration中创建，详细见下面
			this.parameterHandler = configuration.newParameterHandler(mappedStatement, parameterObject, boundSql); 处理参数
			this.resultSetHandler = configuration.newResultSetHandler(executor, mappedStatement, rowBounds, parameterHandler, resultHandler, boundSql);
	}


	@Override
  public Statement prepare(Connection connection, Integer transactionTimeout) throws SQLException {
	    ErrorContext.instance().sql(boundSql.getSql());
	    Statement statement = null;
	    try {
	      statement = instantiateStatement(connection);
	      setStatementTimeout(statement, transactionTimeout);
	      setFetchSize(statement);
	      return statement;
	    } catch (SQLException e) {
	      closeStatement(statement);
	      throw e;
	    } catch (Exception e) {
	      closeStatement(statement);
	      throw new ExecutorException("Error preparing statement.  Cause: " + e, e);
	    }
  }
}
public class PreparedStatementHandler extends	 BaseStatementHandler {  

//...  
	@Override  
  public void parameterize(Statement statement) throws SQLException {  
    parameterHandler.setParameters((PreparedStatement) statement);   调用了BaseStatementHandler中的parameterHandler
  }  

	@Override
  public <E> List<E> query(Statement statement, ResultHandler resultHandler) throws SQLException {
    PreparedStatement ps = (PreparedStatement) statement;
    ps.execute();
    return resultSetHandler.<E> handleResultSets(ps); 查询结果交给了ResultHandler进行封装数据
  }

//...  
}  

public class DefaultParameterHandler implements ParameterHandler {
	private final TypeHandlerRegistry typeHandlerRegistry;
	private final MappedStatement mappedStatement;
	private final Object parameterObject;
	private BoundSql boundSql;
	private Configuration configuration;
	完成参数的设置
	public void setParameters(PreparedStatement ps) {
	     ErrorContext.instance().activity("setting parameters").object(mappedStatement.getParameterMap().getId());
	     List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
	     if (parameterMappings != null) {
	       for (int i = 0; i < parameterMappings.size(); i++) {
	         ParameterMapping parameterMapping = parameterMappings.get(i);
	         if (parameterMapping.getMode() != ParameterMode.OUT) {
	           Object value;
	           String propertyName = parameterMapping.getProperty();
	           if (boundSql.hasAdditionalParameter(propertyName)) { // issue #448 ask first for additional params
	             value = boundSql.getAdditionalParameter(propertyName);
	           } else if (parameterObject == null) {
	             value = null;
	           } else if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
	             value = parameterObject;
	           } else {
	             MetaObject metaObject = configuration.newMetaObject(parameterObject);
	             value = metaObject.getValue(propertyName);
	           }
	           TypeHandler typeHandler = parameterMapping.getTypeHandler();
	           JdbcType jdbcType = parameterMapping.getJdbcType();
	           if (value == null && jdbcType == null) {
	             jdbcType = configuration.getJdbcTypeForNull();
	           }
	           try {
	             typeHandler.setParameter(ps, i + 1, value, jdbcType);  合适的TypeHandler完成参数的设置， 重点
	           } catch (TypeException e) {
	             throw new TypeException("Could not set parameters for mapping: " + parameterMapping + ". Cause: " + e, e);
	           } catch (SQLException e) {
	             throw new TypeException("Could not set parameters for mapping: " + parameterMapping + ". Cause: " + e, e);
	           }
	         }
	       }
	     }
   }
}

public class DefaultResultSetHandler implements ResultSetHandler {  

	public List<Object> handleResultSets(Statement stmt) throws SQLException {
			    ErrorContext.instance().activity("handling results").object(mappedStatement.getId());

			    final List<Object> multipleResults = new ArrayList<Object>();

			    int resultSetCount = 0;
			    ResultSetWrapper rsw = getFirstResultSet(stmt);

			    List<ResultMap> resultMaps = mappedStatement.getResultMaps();
			    int resultMapCount = resultMaps.size();
			    validateResultMapsCount(rsw, resultMapCount);
			    while (rsw != null && resultMapCount > resultSetCount) {
			      ResultMap resultMap = resultMaps.get(resultSetCount);
			      handleResultSet(rsw, resultMap, multipleResults, null);
			      rsw = getNextResultSet(stmt);
			      cleanUpAfterHandlingResultSet();
			      resultSetCount++;
			    }

			    String[] resultSets = mappedStatement.getResultSets();
			    if (resultSets != null) {
			      while (rsw != null && resultSetCount < resultSets.length) {
			        ResultMapping parentMapping = nextResultMaps.get(resultSets[resultSetCount]);
			        if (parentMapping != null) {
			          String nestedResultMapId = parentMapping.getNestedResultMapId();
			          ResultMap resultMap = configuration.getResultMap(nestedResultMapId);
			          handleResultSet(rsw, resultMap, null, parentMapping);
			        }
			        rsw = getNextResultSet(stmt);
			        cleanUpAfterHandlingResultSet();
			        resultSetCount++;
			      }
	       }

	    return collapseSingleResultList(multipleResults);
	  }
}

TypeHandler使用的是结果参数的属性类型。因此我们在定义作为结果的对象的属性时一定要考虑与数据库字段类型的兼容性。


sql类型
public enum SqlCommandType {
  UNKNOWN, INSERT, UPDATE, DELETE, SELECT, FLUSH;
}


public class Configuration {
	protected Environment environment; 存放别人的变量
	protected final Map<String, MappedStatement> mappedStatements = new StrictMap<MappedStatement>("Mapped Statements collection");
	protected final MapperRegistry mapperRegistry = new MapperRegistry(this); 用来存放mapper

  动态代理创建Mapper对象
	public <T> T getMapper(Class<T> type, SqlSession sqlSession) {  
	    return mapperRegistry.getMapper(type, sqlSession);  
	 }  

  根据ExecutorType的类型，生成Executor, 是一个工厂方法
	public Executor newExecutor(Transaction transaction, ExecutorType executorType) {
	    executorType = executorType == null ? defaultExecutorType : executorType;
	    executorType = executorType == null ? ExecutorType.SIMPLE : executorType;
	    Executor executor;
	    if (ExecutorType.BATCH == executorType) {
	      executor = new BatchExecutor(this, transaction);
	    } else if (ExecutorType.REUSE == executorType) {
	      executor = new ReuseExecutor(this, transaction);
	    } else {
	      executor = new SimpleExecutor(this, transaction);
	    }
	    if (cacheEnabled) {
	      executor = new CachingExecutor(executor);
	    }
	    executor = (Executor) interceptorChain.pluginAll(executor); 注意到Executor是可以被插件拦截，这里InterceptorChain，封装了Executor和拦截器
	    return executor;
	}

	可以看到interceptorChain，可以拦截Executor，还可以拦截ParameterHandler,ResultSetHandler,StatementHandler这四种对象。
	public ParameterHandler newParameterHandler(MappedStatement mappedStatement, Object parameterObject, BoundSql boundSql) {
	 ParameterHandler parameterHandler = mappedStatement.getLang().createParameterHandler(mappedStatement, parameterObject, boundSql);
	 parameterHandler = (ParameterHandler) interceptorChain.pluginAll(parameterHandler);
	 return parameterHandler;
  }
	public ResultSetHandler newResultSetHandler(Executor executor, MappedStatement mappedStatement, RowBounds rowBounds, ParameterHandler parameterHandler,  ResultHandler resultHandler, BoundSql boundSql) {
    ResultSetHandler resultSetHandler = new DefaultResultSetHandler(executor, mappedStatement, parameterHandler, resultHandler, boundSql, rowBounds);
    resultSetHandler = (ResultSetHandler) interceptorChain.pluginAll(resultSetHandler);
    return resultSetHandler;
  }
	public StatementHandler newStatementHandler(Executor executor, MappedStatement mappedStatement, Object parameterObject, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) {
		  注意此处创建的是RoutingStatementHandler
	    StatementHandler statementHandler = new RoutingStatementHandler(executor, mappedStatement, parameterObject, rowBounds, resultHandler, boundSql);
	    statementHandler = (StatementHandler) interceptorChain.pluginAll(statementHandler);
	    return statementHandler;
	  }
}

拦截器链，运用动态代理实现
public class InterceptorChain {

  private final List<Interceptor> interceptors = new ArrayList<Interceptor>();

  public Object pluginAll(Object target) { 代理织入代码
    for (Interceptor interceptor : interceptors) {
      target = interceptor.plugin(target); 调用拦截器的plugin动态代理织入代码
    }
    return target;
  }
}

public interface Interceptor {

  Object intercept(Invocation invocation) throws Throwable; 	

  Object plugin(Object target);

  void setProperties(Properties properties);

}

以下是一个分页的拦截器
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class})})
public class PageInterceptor implements Interceptor {
		public Object intercept(Invocation invocation) throws Throwable {
			...  编写拦截处理
		}

   @Override
	 public Object plugin(Object target) {
		   选择性织入代码
			 // 当目标类是StatementHandler类型时，才包装目标类，否者直接返回目标本身,减少目标被代理的次数
			 if (target instanceof StatementHandler) {
						 return Plugin.wrap(target, this); 注意到这里使用了Plugin.wrap  的静态方法进行织入
			 } else {
					 return target;
			 }
	 }
}
动态代理
public class Plugin implements InvocationHandler {
	 private Object target;
	 private Interceptor interceptor;
	 private Map<Class<?>, Set<Method>> signatureMap;

	 private Plugin(Object target, Interceptor interceptor, Map<Class<?>, Set<Method>> signatureMap) {
		 this.target = target;
		 this.interceptor = interceptor;
		 this.signatureMap = signatureMap;
	 }
	动态代理工厂方法
	public static Object wrap(Object target, Interceptor interceptor) {
	    Map<Class<?>, Set<Method>> signatureMap = getSignatureMap(interceptor);
	    Class<?> type = target.getClass();
	    Class<?>[] interfaces = getAllInterfaces(type, signatureMap);
	    if (interfaces.length > 0) {
	      return Proxy.newProxyInstance(
	          type.getClassLoader(),
	          interfaces,
	          new Plugin(target, interceptor, signatureMap));
	    }
	    return target;
	  }
		代理织入方法
	  @Override
	  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
	    try {
	      Set<Method> methods = signatureMap.get(method.getDeclaringClass());
	      if (methods != null && methods.contains(method)) {
	        return interceptor.intercept(new Invocation(target, method, args)); 此处调用了interceptor.intercept 的方法，
					Invocation 在此处有两个作用，一个是封装必要的参数供intercept方法使用，另一个推进动态代理。
	      }
	      return method.invoke(target, args);
	    } catch (Exception e) {
	      throw ExceptionUtil.unwrapThrowable(e);
	    }
	  }
}
public class Invocation {
	 private Object target;
	 private Method method;
	 private Object[] args;
	 ....
  public Object proceed() throws InvocationTargetException, IllegalAccessException {
    return method.invoke(target, args); 推进动态代理方法，所以在intercept的方法最后一定要调用这个方法
  }
}

Mapper代理类注册方法
public class MapperRegistry {
	private final Configuration config;
  private final Map<Class<?>, MapperProxyFactory<?>> knownMappers = new HashMap<Class<?>, MapperProxyFactory<?>>();

		@SuppressWarnings("unchecked")
	public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
	    final MapperProxyFactory<T> mapperProxyFactory = (MapperProxyFactory<T>) knownMappers.get(type);
	    if (mapperProxyFactory == null) {
	      throw new BindingException("Type " + type + " is not known to the MapperRegistry.");
	    }
	    try {
	      return mapperProxyFactory.newInstance(sqlSession); 见下面分析
	    } catch (Exception e) {
	      throw new BindingException("Error getting mapper instance. Cause: " + e, e);
	    }
	  }

		/**
	   * @since 3.2.2
	   */
		添加 Mapper 方法
	  public void addMappers(String packageName, Class<?> superType) {
	    ResolverUtil<Class<?>> resolverUtil = new ResolverUtil<Class<?>>();
	    resolverUtil.find(new ResolverUtil.IsA(superType), packageName);
	    Set<Class<? extends Class<?>>> mapperSet = resolverUtil.getClasses();
	    for (Class<?> mapperClass : mapperSet) {
	      addMapper(mapperClass);
	    }
	  }

		public <T> void addMapper(Class<T> type) {
    if (type.isInterface()) {
      if (hasMapper(type)) {
        throw new BindingException("Type " + type + " is already known to the MapperRegistry.");
      }
      boolean loadCompleted = false;
      try {
        knownMappers.put(type, new MapperProxyFactory<T>(type)); 存放在Map中，注意此处放入的是代理工厂，出入接口来创建代理工厂
        // It's important that the type is added before the parser is run
        // otherwise the binding may automatically be attempted by the
        // mapper parser. If the type is already known, it won't try.
        MapperAnnotationBuilder parser = new MapperAnnotationBuilder(config, type);
        parser.parse();
        loadCompleted = true;
      } finally {
        if (!loadCompleted) {
          knownMappers.remove(type);
        }
      }
    }
  }
}

代理工厂
public class MapperProxyFactory<T> {
	private final Class<T> mapperInterface;
  private final Map<Method, MapperMethod> methodCache = new ConcurrentHashMap<Method, MapperMethod>();

	public MapperProxyFactory(Class<T> mapperInterface) {
     this.mapperInterface = mapperInterface;
  }

	public T newInstance(SqlSession sqlSession) {
		 final MapperProxy<T> mapperProxy = new MapperProxy<T>(sqlSession, mapperInterface, methodCache);
		 return newInstance(mapperProxy);
	}

	@SuppressWarnings("unchecked")
  protected T newInstance(MapperProxy<T> mapperProxy) {
		 动态代理生成对象
     return (T) Proxy.newProxyInstance(mapperInterface.getClassLoader(), new Class[] { mapperInterface }, mapperProxy);
  }
}

public class MapperProxy<T> implements InvocationHandler, Serializable {
   可以看到传入了sqlSession，它是不安全的，所以只能放在方法中进行使用
	 private final SqlSession sqlSession;
	 private final Class<T> mapperInterface;
	 private final Map<Method, MapperMethod> methodCache;

		代理处理方法
	  @Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			try {
				if (Object.class.equals(method.getDeclaringClass())) {
					return method.invoke(this, args);
				} else if (isDefaultMethod(method)) {
					return invokeDefaultMethod(proxy, method, args);
				}
			} catch (Throwable t) {
				throw ExceptionUtil.unwrapThrowable(t);
			}
			final MapperMethod mapperMethod = cachedMapperMethod(method);  
			return mapperMethod.execute(sqlSession, args); 传入 sqlsession , 执行方法
		}

		public class MapperMethod {
		  private final SqlCommand command;
		  private final MethodSignature method;
			根据SqlCommand的SqlCommandType类型进行判断，使用sqlSession来执行相应的方法，详细方法的执行见上文。
		  MapperMethod就像是一个分发者，他根据参数和返回值类型选择不同的sqlsession方法来执行。这样mapper对象与sqlsession就真正的关联起来了。
			public Object execute(SqlSession sqlSession, Object[] args) {
				    Object result;
				    switch (command.getType()) {
				      case INSERT: {
				    	  Object param = method.convertArgsToSqlCommandParam(args);
				        result = rowCountResult(sqlSession.insert(command.getName(), param)); 返回影响的行数
				        break;
				      }
				      case UPDATE: {
				        Object param = method.convertArgsToSqlCommandParam(args);
				        result = rowCountResult(sqlSession.update(command.getName(), param)); 返回影响的行数
				        break;
				      }
				      case DELETE: {
				        Object param = method.convertArgsToSqlCommandParam(args);
				        result = rowCountResult(sqlSession.delete(command.getName(), param));  返回影响的行数
				        break;
				      }		
				      case SELECT:
				        if (method.returnsVoid() && method.hasResultHandler()) {
				          executeWithResultHandler(sqlSession, args);
				          result = null;
				        } else if (method.returnsMany()) {
				          result = executeForMany(sqlSession, args);
				        } else if (method.returnsMap()) {
				          result = executeForMap(sqlSession, args);
				        } else if (method.returnsCursor()) {
				          result = executeForCursor(sqlSession, args);
				        } else {
				          Object param = method.convertArgsToSqlCommandParam(args);
				          result = sqlSession.selectOne(command.getName(), param);
				        }
				        break;
				      case FLUSH:
				        result = sqlSession.flushStatements();
				        break;
				      default:
				        throw new BindingException("Unknown execution method for: " + command.getName());
				    }
				    if (result == null && method.getReturnType().isPrimitive() && !method.returnsVoid()) {
				      throw new BindingException("Mapper method '" + command.getName()
				          + " attempted to return null from a method with a primitive return type (" + method.getReturnType() + ").");
				    }
				    return result;
				  }
		}

}

```


// mybayis 分页拦截器

/**
 * 通过拦截<code>StatementHandler</code>的<code>prepare</code>方法，重写sql语句实现物理分页。
 * 老规矩，签名里要拦截的类型只能是接口。
 * 
 * @author 湖畔微风
 * 
 */
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class})})
public class PageInterceptor implements Interceptor {
    private static final Log logger = LogFactory.getLog(PageInterceptor.class);
    private static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();
    private static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();
    private static final ReflectorFactory DEFAULT_OBJECT_REFLECTOR_FACTORY=new DefaultReflectorFactory();
    private static String defaultDialect = "oracle"; // 数据库类型(默认为mysql)
    private static String defaultPageSqlId = ".*Page$"; // 需要拦截的ID(正则匹配)
    private static String dialect = ""; // 数据库类型(默认为mysql)
    private static String pageSqlId = ""; // 需要拦截的ID(正则匹配)

    @SuppressWarnings("rawtypes")
	@Override
    public Object intercept(Invocation invocation) throws Throwable {
        //获取 StatementHandler获取相应信息
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget(); 
        //操作的对象就由StatementHandler的实例，变为MetaObject的实例
        MetaObject metaStatementHandler = MetaObject.forObject(statementHandler, DEFAULT_OBJECT_FACTORY,
                DEFAULT_OBJECT_WRAPPER_FACTORY, DEFAULT_OBJECT_REFLECTOR_FACTORY);

        //=====================  考虑多个代理的情况下 ===========================
        // 分离代理对象链(由于目标类可能被多个拦截器拦截，从而形成多次代理，通过下面的两次循环可以分离出最原始的的目标类)
        while (metaStatementHandler.hasGetter("h")) {
            Object object = metaStatementHandler.getValue("h");
            metaStatementHandler = MetaObject.forObject(object, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY, DEFAULT_OBJECT_REFLECTOR_FACTORY);
        }
        // 分离最后一个代理对象的目标类
        while (metaStatementHandler.hasGetter("target")) {
            Object object = metaStatementHandler.getValue("target");
            metaStatementHandler = MetaObject.forObject(object, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY, DEFAULT_OBJECT_REFLECTOR_FACTORY);
        }
        // 获取配置参数 
        Configuration configuration = (Configuration) metaStatementHandler.getValue("delegate.configuration");
        // 判断数据库连接的类型
        dialect = configuration.getVariables().getProperty("dialect");
        if (null == dialect || "".equals(dialect)) {
            logger.warn("Property dialect is not setted,use default 'mysql' ");
            dialect = defaultDialect;
        }
        // 获取配置的过滤的正则匹配
        pageSqlId = configuration.getVariables().getProperty("pageSqlId");
        if (null == pageSqlId || "".equals(pageSqlId)) {
            logger.warn("Property pageSqlId is not setted,use default '.*Page$' ");
            pageSqlId = defaultPageSqlId;
        }
        // 获取 mappedStatement
        MappedStatement mappedStatement = (MappedStatement) metaStatementHandler.getValue("delegate.mappedStatement");
        // 只重写需要分页的sql语句。通过MappedStatement的ID匹配，默认重写以Page结尾的MappedStatement的sql
        // 拦截需要拦截的方法
        if (mappedStatement.getId().matches(pageSqlId)) {
            // 获取原始sql语句
            BoundSql boundSql = (BoundSql) metaStatementHandler.getValue("delegate.boundSql");
            Object parameterObject = boundSql.getParameterObject();
            if (parameterObject == null) {
                throw new NullPointerException("parameterObject is null!");
            } else {
                // 获取page参数
                Page page = (Page) metaStatementHandler
                        .getValue("delegate.boundSql.parameterObject.page");
                //Object o=metaStatementHandler.getValue("delegate.boundSql.parameterObject.page.length");
                //System.out.println(o);
                String sql = boundSql.getSql(); // 获取sql语句
                // 重写sql
                String pageSql = buildPageSql(sql, page);
                metaStatementHandler.setValue("delegate.boundSql.sql", pageSql);
                // 采用物理分页后，就不需要mybatis的内存分页了，所以重置下面的两个参数
                metaStatementHandler.setValue("delegate.rowBounds.offset", RowBounds.NO_ROW_OFFSET);
                metaStatementHandler.setValue("delegate.rowBounds.limit", RowBounds.NO_ROW_LIMIT);
                Connection connection = (Connection) invocation.getArgs()[0];
                // 重设分页参数里的总页数等
                setPageParameter(sql, connection, mappedStatement, boundSql, page);
            }
        }
        // 将执行权交给下一个拦截器, 推进责任链向下执行
        return invocation.proceed();
    }

    /**
     * 从数据库里查询总的记录数并计算总页数，回写进分页参数<code>PageParameter</code>,这样调用者就可用通过 分页参数
     * <code>PageParameter</code>获得相关信息。
     * 
     * @param sql
     * @param connection
     * @param mappedStatement
     * @param boundSql
     * @param page
     */
    @SuppressWarnings("rawtypes")
	private void setPageParameter(String sql, Connection connection, MappedStatement mappedStatement,
            BoundSql boundSql, Page page) {
        // 记录总记录数
        String countSql = "select count(0) from (" + sql + ") as total";
        PreparedStatement countStmt = null;
        ResultSet rs = null;
        try {
            countStmt = connection.prepareStatement(countSql); 
            BoundSql countBS = new BoundSql(mappedStatement.getConfiguration(), countSql,
                    boundSql.getParameterMappings(), boundSql.getParameterObject());
            setParameters(countStmt, mappedStatement, countBS, boundSql.getParameterObject());
            rs = countStmt.executeQuery(); // 执行原始的查询操作 返回所有的结果
            int totalCount = 0;
            if (rs.next()) {
                totalCount = rs.getInt(1);
            }
            page.setTotalCount(totalCount); // 设置总数
            int totalPage = totalCount / page.getLength() + ((totalCount % page.getLength() == 0) ? 0 : 1);
            page.setTotalPage(totalPage); // 设置页数

        } catch (SQLException e) {
            logger.error("Ignore this exception", e);
        } finally {
            try {
                rs.close();
            } catch (SQLException e) {
                logger.error("Ignore this exception", e);
            }
            try {
                countStmt.close();
            } catch (SQLException e) {
                logger.error("Ignore this exception", e);
            }
        }

    }

    /**
     * 对SQL参数(?)设值
     * 
     * @param ps
     * @param mappedStatement
     * @param boundSql
     * @param parameterObject
     * @throws SQLException
     */
    private void setParameters(PreparedStatement ps, MappedStatement mappedStatement, BoundSql boundSql,
            Object parameterObject) throws SQLException {
        ParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement, parameterObject, boundSql);
        parameterHandler.setParameters(ps); // 将 PreparedStatement 设置进 ParameterHandler 用于执行sql 语句
    }

    /**
     * 根据数据库类型，生成特定的分页sql
     * 
     * @param sql
     * @param page
     * @return
     */
    @SuppressWarnings("rawtypes")
	private String buildPageSql(String sql, Page page) {
        if (page != null) {
            StringBuilder pageSql = new StringBuilder();
            if ("mysql".equals(dialect)) {
                pageSql = buildPageSqlForMysql(sql, page);
            } else if ("oracle".equals(dialect)) {
                pageSql = buildPageSqlForOracle(sql, page);
            } else {
                return sql;
            }
            return pageSql.toString();
        } else {
            return sql;
        }
    }

    /**
     * mysql的分页语句
     * 
     * @param sql
     * @param page
     * @return String
     */
    @SuppressWarnings("rawtypes")
	public StringBuilder buildPageSqlForMysql(String sql, Page page) {
        StringBuilder pageSql = new StringBuilder(1000);
        String beginrow = String.valueOf(page.getStart()); // int -> String
        pageSql.append(sql);
        // SELECT * FROM table LIMIT [offset,] rows
        // SELECT * FROM table LIMIT 5,10 检索记录行 6-15 
        // SELECT * FROM table LIMIT 95,-1; // 检索记录行 96-last
        // SELECT * FROM table LIMIT 5; //检索前 5 个记录行  0-5
        pageSql.append(" limit " + beginrow + "," + page.getLength());
        return pageSql;
    }

    /**
     * 参考hibernate的实现完成oracle的分页
     * 
     * @param sql
     * @param page
     * @return String
     */
    @SuppressWarnings("rawtypes")
	public StringBuilder buildPageSqlForOracle(String sql, Page page) {
        StringBuilder pageSql = new StringBuilder(1000);
        String beginrow = String.valueOf(page.getStart());
        String endrow = String.valueOf(page.getStart()+page.getLength());
        // 似曾相识 
        pageSql.append("select * from ( select temp.*, rownum row_id from ( ");
        pageSql.append(sql);
        pageSql.append(" ) temp where rownum <= ").append(endrow);
        pageSql.append(") where row_id > ").append(beginrow);
        return pageSql;
    }

    @Override
    public Object plugin(Object target) {
        // 当目标类是StatementHandler类型时，才包装目标类，否者直接返回目标本身,减少目标被代理的次数
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        } else {
            return target;
        }
    }

    @Override
    public void setProperties(Properties properties) {
    }

}




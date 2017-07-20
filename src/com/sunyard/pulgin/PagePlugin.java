package com.sunyard.pulgin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import javax.xml.bind.PropertyException;

import org.apache.ibatis.executor.statement.BaseStatementHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;

import com.sunyard.util.Common;


/**
 * Mybatis的分页查询插件，通过拦截StatementHandler的prepare方法来实现。
 * 只有在参数列表中包括Page类型的参数时才进行分页查询。
 */
@SuppressWarnings("unchecked")
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class})})
public class PagePlugin implements Interceptor {

    public static String dialect = null;//数据库类型

    @SuppressWarnings("rawtypes")
    public Object intercept(Invocation ivk) throws Throwable {
        if (ivk.getTarget() instanceof RoutingStatementHandler) {
            RoutingStatementHandler statementHandler = (RoutingStatementHandler) ivk.getTarget();
            BaseStatementHandler delegate = (BaseStatementHandler) ReflectHelper.getValueByFieldName(statementHandler, "delegate");
            MappedStatement mappedStatement = (MappedStatement) ReflectHelper.getValueByFieldName(delegate, "mappedStatement");
            /**
             * 传入的参数是否有page参数，如果有，则分页，
             */
            BoundSql boundSql = delegate.getBoundSql();
            Object parameterObject = boundSql.getParameterObject();// 分页SQL<select>中parameterType属性对应的实体参数，即Mapper接口中执行分页方法的参数,该参数不得为空
            if (parameterObject == null) {
                return ivk.proceed();
            } else {
                PageView pageView = null;
                if (parameterObject instanceof PageView) { // 参数就是Pages实体
                    pageView = (PageView) parameterObject;
                } else if (parameterObject instanceof Map) {
                    for (Entry entry : (Set<Entry>) ((Map) parameterObject).entrySet()) {
                        if (entry.getValue() instanceof PageView) {
                            pageView = (PageView) entry.getValue();
                            break;
                        }
                    }
                } else { // 参数为某个实体，该实体拥有Pages属性
                    pageView = ReflectHelper.getValueByFieldType(
                            parameterObject, PageView.class);
                    if (pageView == null) {
                        return ivk.proceed();
                    }
                }
                if (pageView == null) {
                    return ivk.proceed();
                }
                String sql = boundSql.getSql();
                Connection connection = (Connection) ivk.getArgs()[0];
                setPageParameter(sql, connection, mappedStatement, boundSql, parameterObject, pageView);
                String pageSql = generatePagesSql(sql, pageView);
                ReflectHelper.setValueByFieldName(boundSql, "sql", pageSql); // 将分页sql语句反射回BoundSql.
            }
        }
        return ivk.proceed();
    }

    /**
     * 从数据库里查询总的记录数并计算总页数，回写进分页参数<code>PageParameter</code>,这样调用者就可用通过 分页参数
     * <code>PageParameter</code>获得相关信息。
     *
     * @param sql
     * @param connection
     * @param mappedStatement
     * @param boundSql
     * @throws SQLException
     */
    private void setPageParameter(String sql, Connection connection, MappedStatement mappedStatement,
                                  BoundSql boundSql, Object parameterObject, PageView pageView) throws SQLException {
        // 记录总记录数
        PreparedStatement countStmt = null;
        ResultSet rs = null;
        try {
            String countSql = "select count(1) from (" + sql + ") tmp_count"; // 记录统计
            countStmt = connection.prepareStatement(countSql);
            ReflectHelper.setValueByFieldName(boundSql, "sql", countSql);
            DefaultParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement, parameterObject, boundSql);
            parameterHandler.setParameters(countStmt);
            rs = countStmt.executeQuery();
            int count = 0;
            if (rs.next()) {
                count = ((Number) rs.getObject(1)).intValue();
            }
            pageView.setTotalCount(count);
        } finally {
            try {
                rs.close();
            } catch (Exception e) {
            }
            try {
                countStmt.close();
            } catch (Exception e) {
            }
        }

    }

    /**
     * 根据数据库方言，生成特定的分页sql
     *
     * @param sql
     * @param page
     * @return
     */
    private String generatePagesSql(String sql, PageView page) {
        if (page != null) {
            if ("mysql".equals(dialect)) {
                return buildPageSqlForMysql(sql, page).toString();
            } else if ("oracle".equals(dialect)) {
                return buildPageSqlForOracle(sql, page).toString();
            } else if ("access".equals(dialect)) {
                return buildPageSqlForAccess(sql, page).toString();
            }
        }
        return sql;
    }

    /**
     * mysql的分页语句
     *
     * @param sql
     * @param page
     * @return String
     */
    public StringBuilder buildPageSqlForMysql(String sql, PageView page) {
        StringBuilder pageSql = new StringBuilder(100);
        String beginrow = String.valueOf(page.getStart());
        pageSql.append(sql);
        pageSql.append(" limit " + beginrow + "," + page.getPageSize());
        return pageSql;
    }

    /**
     * 参考hibernate的实现完成oracle的分页
     *
     * @param sql
     * @param page
     * @return String
     */
    public StringBuilder buildPageSqlForOracle(String sql, PageView page) {
        StringBuilder pageSql = new StringBuilder(100);
        String beginrow = String.valueOf(page.getStart());
        String endrow = String.valueOf(page.getStart() + page.getPageSize());

        pageSql.append("select * from ( select temp.*, rownum row_id from ( ");
        pageSql.append(sql);
        pageSql.append(" ) temp where rownum <= ").append(endrow);
        pageSql.append(") where row_id > ").append(beginrow);
        return pageSql;
    }


    /**
     * 参考hibernate的实现完成access的分页
     *
     * @param sql
     * @param page
     * @return String
     */
    public StringBuilder buildPageSqlForAccess(String sql, PageView page) {
        StringBuilder pageSql = new StringBuilder(100);
        long pageNum = (page.getStart() + page.getPageSize()) / page.getPageSize();
        String andSql = "";
        if(sql.contains("and")){
            andSql = sql.substring(sql.indexOf("and"),sql.indexOf("order"));
        }

        String orderBy = sql.substring(sql.lastIndexOf("order"));
        String tableName = sql.substring(sql.indexOf("from") + 4, sql.indexOf("where")).trim();
        pageSql.append(new StringBuffer(sql).insert(sql.indexOf("select") + 6, " top " + page.getPageSize()));
        if(pageNum > 1){
            pageSql.insert(pageSql.indexOf("1=1") + 3, " and id not in ( ");
            pageSql.insert(pageSql.indexOf("(") + 1, "select top " + page.getPageSize() * (pageNum -1) + " id from " + tableName + " where 1=1 " + andSql + orderBy + " )");
        }
        return pageSql;
    }


    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    public void setProperties(Properties p) {
        dialect = p.getProperty("dialect");
        if (Common.isEmpty(dialect)) {
            try {
                throw new PropertyException("dialectName or dialect property is not found!");
            } catch (PropertyException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getDialect() {
        return dialect;
    }

    public static void setDialect(String dialect) {
        PagePlugin.dialect = dialect;
    }
}

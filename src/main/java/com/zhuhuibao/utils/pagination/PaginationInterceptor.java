package com.zhuhuibao.utils.pagination;

import com.zhuhuibao.utils.pagination.dialect.DatabaseDialectShortName;
import com.zhuhuibao.utils.pagination.dialect.Dialect;
import com.zhuhuibao.utils.pagination.helper.DialectHelper;
import com.zhuhuibao.utils.pagination.helper.SqlHelper;
import com.zhuhuibao.utils.pagination.model.Paging;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.util.Properties;

/**
 * Date Created  2014-2-17
 *
 * @author loafer[zjh527@gmail.com]
 * @version 1.0
 */
@Intercepts({@Signature(
        type = Executor.class,
        method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})
public class PaginationInterceptor implements Interceptor {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final int MAPPED_STATEMENT_INDEX = 0;
    private static final int PARAMETER_INDEX = 1;
    private static final int ROWBOUNDS_INDEX = 2;
    private static final int RESULT_HANDLER_INDEX = 3;


    private Dialect dialect;
    private String mappedStatementIdRegex;


    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        final Object[] queryArgs = invocation.getArgs();
        final MappedStatement ms = (MappedStatement) queryArgs[MAPPED_STATEMENT_INDEX];
        final Object parameter = queryArgs[PARAMETER_INDEX];
        final RowBounds rowBounds = (RowBounds) queryArgs[ROWBOUNDS_INDEX];

        int offset = rowBounds.getOffset();
        int limit = rowBounds.getLimit();

        boolean intercept = ms.getId().matches(mappedStatementIdRegex);//PatternMatchUtils.simpleMatch(mappedStatementIdRegex, ms.getId());

        if (intercept && dialect.supportsLimit() && (offset != RowBounds.NO_ROW_OFFSET || limit != RowBounds.NO_ROW_LIMIT)) {
            final BoundSql boundSql = ms.getBoundSql(parameter);
            String sql = boundSql.getSql().trim();
            logger.info("sql: {}", boundSql.getSql().trim());
            final Executor executor = (Executor) invocation.getTarget();
            Connection connection = executor.getTransaction().getConnection();
            int count = SqlHelper.getCount(ms, connection, parameter, dialect);
            Paging.setPaginationTotal(count);

            String limitSql = dialect.getLimitString(sql, offset, limit);
            MappedStatement newMs = newMappedStatement(ms, boundSql, limitSql);

            queryArgs[ROWBOUNDS_INDEX] = new RowBounds(RowBounds.NO_ROW_OFFSET, RowBounds.NO_ROW_LIMIT);
            queryArgs[MAPPED_STATEMENT_INDEX] = newMs;
        }

        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        String dialectClass = properties.getProperty("dialectClass");
        if (StringUtils.isBlank(dialectClass)) {
            String dialectShortName = properties.getProperty("dbms");
            checkDialect(dialectShortName);
            dialect = DialectHelper.getDialect(DatabaseDialectShortName.valueOf(dialectShortName.toUpperCase()));
        } else {
            try {
                dialect = (Dialect) Class.forName(dialectClass).newInstance();
            } catch (Exception e) {
                throw new RuntimeException("Plug-in [PaginationInterceptor] cannot create dialect instance by dialectClass: " + dialectClass);
            }
        }

        mappedStatementIdRegex = properties.getProperty("sqlRegex", ".*findAll.*");

    }

    private void checkDialect(String dialectShortName) {
        try {
            DatabaseDialectShortName.valueOf(dialectShortName.toUpperCase());
        } catch (Exception e) {
            throw new RuntimeException("Plug-in [PaginationInterceptor] the dialect of the attribute value is invalid!");
        }

    }


    //@see org.apache.ibatis.builder.MapperBuilderAssistant
    private MappedStatement newMappedStatement(final MappedStatement ms,
                                               final BoundSql boundSql,
                                               final String sql) {
        BoundSql newBoundSql = newBoundSql(ms, boundSql, sql);
        RawSqlSource sqlSource = new RawSqlSource(newBoundSql);
        MappedStatement.Builder builder = new MappedStatement.Builder(
                ms.getConfiguration(),
                ms.getId(),
                sqlSource,
                ms.getSqlCommandType()
        );

        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        String[] keyProperties = ms.getKeyProperties();
        builder.keyProperty(keyProperties == null ? null : keyProperties[0]);

        //setStatementTimeout()
        builder.timeout(ms.getTimeout());

        //setStatementResultMap()
        builder.parameterMap(ms.getParameterMap());

        //setStatementResultMap()
        builder.resultMaps(ms.getResultMaps());
        builder.resultSetType(ms.getResultSetType());

        //setStatementCache()
        builder.cache(ms.getCache());
        builder.flushCacheRequired(ms.isFlushCacheRequired());
        builder.useCache(ms.isUseCache());

        return builder.build();
    }

    private BoundSql newBoundSql(final MappedStatement ms,
                                 final BoundSql boundSql,
                                 final String sql) {
        BoundSql newBoundSql = new BoundSql(
                ms.getConfiguration(),
                sql,
                boundSql.getParameterMappings(),
                boundSql.getParameterObject()
        );

        for (ParameterMapping mapping : boundSql.getParameterMappings()) {
            String prop = mapping.getProperty();
            if (boundSql.hasAdditionalParameter(prop)) {
                newBoundSql.setAdditionalParameter(prop, boundSql.getAdditionalParameter(prop));
            }
        }

        return newBoundSql;
    }

    public class RawSqlSource implements SqlSource {
        private BoundSql boundSql;

        public RawSqlSource(BoundSql boundSql) {
            this.boundSql = boundSql;
        }

        @Override
        public BoundSql getBoundSql(Object parameterObject) {
            return boundSql;
        }
    }
}

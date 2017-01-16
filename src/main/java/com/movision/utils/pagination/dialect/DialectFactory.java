package com.movision.utils.pagination.dialect;

/**
 * Date Created  2014-2-18
 *
 * @author loafer[zjh527@gmail.com]
 * @version 1.0
 */
public abstract class DialectFactory {
    public static Dialect buildDialect(DatabaseDialectShortName databaseName) {
        switch (databaseName) {
            case MYSQL:
                return new MySQLDialect();
            case ORACLE:
                return new OracleDialect();
            default:
                throw new UnsupportedOperationException();
        }
    }
}

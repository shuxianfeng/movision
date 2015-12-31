package com.zhuhuibao.utils.pagination.helper;

import java.util.HashMap;
import java.util.Map;

import com.zhuhuibao.utils.pagination.dialect.DatabaseDialectShortName;
import com.zhuhuibao.utils.pagination.dialect.DialectFactory;
import com.zhuhuibao.utils.pagination.dialect.Dialect;

/**
 * Date Created  2014-2-18
 *
 * @author loafer[zjh527@gmail.com]
 * @version 1.0
 */
public abstract class DialectHelper {
    private static Map<DatabaseDialectShortName, Dialect> MAPPERS = new HashMap<DatabaseDialectShortName, Dialect>();

    public static Dialect getDialect(DatabaseDialectShortName dialectShortName) {
        if (MAPPERS.containsKey(dialectShortName)) {
            return MAPPERS.get(dialectShortName);
        } else {
            Dialect dialect = DialectFactory.buildDialect(dialectShortName);
            MAPPERS.put(dialectShortName, dialect);
            return dialect;
        }
    }
}

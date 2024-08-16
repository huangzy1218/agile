package com.agile.datasource.enums;

/**
 * @author Huang Z.Y.
 */

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * JDBC url.
 *
 * @author Huang Z.Y.
 */
@Getter
@AllArgsConstructor
public enum DsJdbcUrlEnum {

    /**
     * MySQL
     */
    MYSQL("mysql",
            "jdbc:mysql://%s:%s/%s?characterEncoding=utf8"
                    + "&zeroDateTimeBehavior=convertToNull&useSSL=false&useJDBCCompliantTimezoneShift=true"
                    + "&useLegacyDatetimeCode=false&allowMultiQueries=true&allowPublicKeyRetrieval=true",
            "select 1", "mysql8 connection"),

    /**
     * pg
     */
    PG("pg", "jdbc:postgresql://%s:%s/%s", "select 1", "postgresql Link"),

    /**
     * SQL SERVER.
     */
    MSSQL("mssql", "jdbc:sqlserver://%s:%s;database=%s;characterEncoding=UTF-8", "select 1", "sqlserver connection"),

    /**
     * Oracle.
     */
    ORACLE("oracle", "jdbc:oracle:thin:@%s:%s:%s", "select 1 from dual", "oracle connection"),

    /**
     * DB2.
     */
    DB2("db2", "jdbc:db2://%s:%s/%s", "select 1 from sysibm.sysdummy1", "DB2 TYPE4 connection"),

    /**
     * Dameng.
     */
    DM("dm", "jdbc:dm://%s:%s/%s", "select 1 from dual", "Dameng connection"),

    /**
     * Highgo.
     */
    HIGHGO("highgo", "jdbc:highgo://%s:%s/%s", "select 1", "highgo connection");

    private final String dbName;

    private final String url;

    private final String validationQuery;

    private final String description;

    public static DsJdbcUrlEnum get(String dsType) {
        return Arrays.stream(DsJdbcUrlEnum.values())
                .filter(dsJdbcUrlEnum -> dsType.equals(dsJdbcUrlEnum.getDbName()))
                .findFirst()
                .get();
    }

}


package com.edavalos.mtx.keystore.db;

import java.sql.Connection;

public class QueryInsertBuilder extends QueryBuilder {
    private final String[] dbKeys;
    private final String[] dbValues;

    public QueryInsertBuilder(Connection connection, String dbTable, String[] dbKeys, String[] dbValues) {
        super(connection, dbTable);
        this.dbKeys = dbKeys;
        this.dbValues = dbValues;
    }
}

package com.edavalos.mtx.keystore.db;

import java.sql.Connection;

public class QueryInsertKvBuilder extends QueryBuilder {
    private final String[] dbKeys;
    private final String[] dbValues;

    public QueryInsertKvBuilder(Connection connection, String dbTable, String[] dbKeys, String[] dbValues) {
        super(connection, dbTable);
        this.dbKeys = dbKeys;
        this.dbValues = dbValues;
    }
}

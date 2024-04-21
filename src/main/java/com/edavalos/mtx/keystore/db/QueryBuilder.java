package com.edavalos.mtx.keystore.db;

import java.sql.Connection;

public abstract class QueryBuilder {
    protected final Connection connection;
    protected final String dbName;
    protected final String table;

    public QueryBuilder(Connection connection, String tableToQuery) {
        this.connection = connection;
        this.dbName = DbConst.SCHEMA_NAME;
        this.table = tableToQuery;
    }
}

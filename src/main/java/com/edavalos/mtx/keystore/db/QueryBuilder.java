package com.edavalos.mtx.keystore.db;

public abstract class QueryBuilder {
    protected final String dbName;
    protected final String table;

    public QueryBuilder(String tableToQuery) {
        this.dbName = DbConst.SCHEMA_NAME;
        this.table = tableToQuery;
    }
}

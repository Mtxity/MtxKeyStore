package com.edavalos.mtx.keystore.db;

public abstract class QueryBuilder {
    protected final String dbName;
    protected final String table;

    public QueryBuilder(String tableToQuery) {
        this.dbName = "MtxKvStore"; // @Todo: get this from a config file
        this.table = tableToQuery;
    }
}

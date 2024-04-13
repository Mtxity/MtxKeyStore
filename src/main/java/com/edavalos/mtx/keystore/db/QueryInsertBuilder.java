package com.edavalos.mtx.keystore.db;

public class QueryInsertBuilder extends QueryBuilder {
    private final String[] dbKeys;
    private final String[] dbValues;

    public QueryInsertBuilder(String dbTable, String[] dbKeys, String[] dbValues) {
        super(dbTable);
        this.dbKeys = dbKeys;
        this.dbValues = dbValues;
    }
}

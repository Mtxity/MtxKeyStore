package com.edavalos.mtx.keystore.db;

import java.sql.Connection;

public class QuerySelectKvBuilder extends QueryBuilder {

    public QuerySelectKvBuilder(Connection connection) {
        super(connection, DbConst.TABLE_KV_NAME);
    }

    @Override
    protected String getQuery() {
        return "SELECT * FROM \"" + super.dbName + "\"." + super.table + ";";
    }
}

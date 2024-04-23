package com.edavalos.mtx.keystore.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class QuerySelectKvBuilder extends QueryBuilder {

    public QuerySelectKvBuilder(Connection connection) {
        super(connection, DbConst.TABLE_KV_NAME);
    }

    protected String getQuery() {
        return "SELECT * FROM \"" + super.dbName + "\"." + super.table + ";";
    }

    public PreparedStatement getPreparedStatement() {
        try {
            return super.connection.prepareStatement(this.getQuery());
        } catch (SQLException e) {
            System.err.println("Failed to create sql query: " + e);
            throw new RuntimeException(e);
        }
    }
}

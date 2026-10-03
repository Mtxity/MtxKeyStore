package com.edavalos.mtx.keystore.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class QueryBuilder {
    protected final Connection connection;
    protected final String dbName;
    protected final String table;

    public QueryBuilder(Connection connection, String tableToQuery) {
        this.connection = connection;
        this.dbName = DbConst.SCHEMA_NAME;
        this.table = tableToQuery;
    }

    protected abstract String getQuery();

    protected void setParameters(PreparedStatement statement) throws SQLException {
        // Queries without parameters do not need any additional setup.
    }

    public PreparedStatement getPreparedStatement() {
        try {
            PreparedStatement statement = this.connection.prepareStatement(this.getQuery());
            this.setParameters(statement);
            return statement;
        } catch (SQLException e) {
            System.err.println("Failed to create sql query: " + e);
            throw new RuntimeException(e);
        }
    }
}

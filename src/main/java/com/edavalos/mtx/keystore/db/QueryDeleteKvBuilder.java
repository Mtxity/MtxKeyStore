package com.edavalos.mtx.keystore.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class QueryDeleteKvBuilder extends QueryBuilder {
    private final String appId;
    private final String key;

    public QueryDeleteKvBuilder(Connection connection, String appId, String key) {
        super(connection, DbConst.TABLE_KV_NAME);
        this.appId = appId;
        this.key = key;
    }

    @Override
    protected String getQuery() {
        return "DELETE FROM \"" + super.dbName + "\"." + super.table +
                " WHERE app_id = ? AND key = ?;";
    }

    @Override
    protected void setParameters(PreparedStatement statement) throws SQLException {
        statement.setString(1, appId);
        statement.setString(2, key);
    }
}

package com.edavalos.mtx.keystore.db;

import com.edavalos.mtx.keystore.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class QueryInsertKvBuilder extends QueryBuilder {
    private final List<KvRow> rows;

    public QueryInsertKvBuilder(Connection connection, List<KvRow> rows) {
        super(connection, DbConst.TABLE_KV_NAME);
        this.rows = rows;
    }

    public QueryInsertKvBuilder(Connection connection, KvRow row) {
        super(connection, DbConst.TABLE_KV_NAME);
        this.rows = List.of(row);
    }

    public QueryInsertKvBuilder(Connection connection,
                                String appId,
                                String key,
                                String value,
                                String timestamp) {
        this(connection, new KvRow(appId, key, value, timestamp));
    }

    // @TODO: Add unit tests for this
    protected String getQuery() {
        if (this.rows == null || this.rows.isEmpty()) {
            return "";
        }

        String[] values = new String[this.rows.size()];
        int i = 0;
        for (KvRow row : this.rows) {
            values[i++] = "('" + Util.getConcat("', '", row.appId(), row.key(), row.val(), row.timestamp()) + "')";
        }

        return "INSERT INTO \"" + super.dbName + "\"." + super.table + " (" +
                "   app_id, key, val, lastSetTimestamp " +
                ") VALUES " +
                Util.getConcat(", ", values) + ";";
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
